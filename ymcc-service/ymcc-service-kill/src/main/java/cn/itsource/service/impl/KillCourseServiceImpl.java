package cn.itsource.service.impl;

import cn.itsource.PreOrderDto;
import cn.itsource.domain.KillActivity;
import cn.itsource.domain.KillCourse;
import cn.itsource.dto.KillParamDto;
import cn.itsource.mapper.KillCourseMapper;
import cn.itsource.service.IKillActivityService;
import cn.itsource.service.IKillCourseService;
import cn.itsource.util.AssertUtil;
import cn.itsource.util.CodeGenerateUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author director
 * @since 2022-09-21
 */
@Service
@Slf4j
public class KillCourseServiceImpl extends ServiceImpl<KillCourseMapper, KillCourse> implements IKillCourseService {
    @Autowired
    private IKillActivityService killActivityService;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void addKillCourse(KillCourse killCourse) {
        Wrapper<KillCourse> wrapper = new EntityWrapper<>();
        wrapper.eq("course_id",killCourse.getCourseId());
        wrapper.eq("activity_id",killCourse.getActivityId());
        KillCourse tmp = selectOne(wrapper);
        AssertUtil.isNull(tmp,"已经存在秒杀课程，请勿重复添加");
        KillActivity killActivity = killActivityService.selectById(killCourse.getActivityId());
        killCourse.setCreateTime(new Date());
        killCourse.setKillLimit(1);
        killCourse.setStartTime(killActivity.getStartTime());
        killCourse.setEndTime(killActivity.getEndTime());
        killCourse.setPublishStatus(killActivity.getPublishStatus());
        killCourse.setTimeStr(killActivity.getTimeStr());
        insert(killCourse);
    }

    @Override
    public List<KillCourse> onlineAll() {
        //拿到所有以avtivity: 开头的key们
        Set<Object> keys = redisTemplate.keys("avtivity:*");
        AssertUtil.isNotNull(keys,"不存在活动！！");
        List<KillCourse> courseArrayList= new ArrayList<>();
        // 根据每个大kay,获取大key下的所有秒杀商品
        for (Object key : keys) {
            List values = redisTemplate.opsForHash().values(key);
            // 把每一个活动的商品都追加到集合中
            courseArrayList.addAll(values);
        }
        return courseArrayList;
    }

    @Override
    public KillCourse onlineOne(Long activityId, Long killId) {
        String daKey = "avtivity:"+activityId;
        return (KillCourse) redisTemplate.opsForHash().get(daKey, killId.toString());
    }

    /**
     * 执行秒杀
     * === 校验是当前登录人是否已经秒杀过
     * 1.根据活动ID和秒杀ID，查询出秒杀课程是否存在
     * 2.执行秒杀
     * 3.尝试去扣减库存
     *   。扣减不成功，手速太慢，下次再来
     *   。扣减成功，生成预创订单，存储redis
     *      key：orderNo
     *      value：PreOrder{}
     *   。为了保证一个人只能购买一个课程一次 单独存储redis
     *     key：loginId:killId  只要这个可以存在就不可以在抢了
     *     value:xxoo
     * @param dto
     * @return
     */
    @Override
    public String kill(KillParamDto dto) {
        Long startTime = System.currentTimeMillis();
        Long loginId = 3L;//假的当前登录人
        //* 执行秒杀
        String repeatKey = loginId+":"+dto.getKillCourseId();
        //* === 校验是当前登录人是否已经秒杀过
        Object repeat = redisTemplate.opsForValue().get(repeatKey);
        //AssertUtil.isNull(repeat,"你已经抢购过该课程，请参与其他课程秒杀！！");

        //* 1.根据活动ID和秒杀ID，查询出秒杀课程是否存在
        KillCourse killCourse = onlineOne(dto.getActivityId(), dto.getKillCourseId());
        AssertUtil.isNotNull(killCourse,"秒杀非法！！！");
        AssertUtil.isTrue(killCourse.isKilling(),"秒杀非法！！");
        //* 2.执行秒杀
        //* 3.尝试去扣减库存
        String xiaoKey = killCourse.getId().toString();
        RSemaphore semaphore = redissonClient.getSemaphore(xiaoKey);
        int killCount = 1;
        boolean tryAcquire = semaphore.tryAcquire(killCount);
        //*   。扣减不成功，手速太慢，下次再来
        AssertUtil.isTrue(tryAcquire,"骚年，手速太慢，下次再来！！");
        //*   。扣减成功，生成预创订单，存储redis
        String orderNo = CodeGenerateUtils.generateOrderSn(loginId);
        PreOrderDto preOrderDto = new PreOrderDto(
                orderNo,
                killCourse.getKillPrice(),
                1,
                loginId,
                killCourse.getCourseId()
        );
        //*      key：orderNo
        //*      value：PreOrder{}
        redisTemplate.opsForValue().set(orderNo,preOrderDto);
        //*   。为了保证一个人只能购买一个课程一次 单独存储redis
        //*     key：loginId:killId  只要这个可以存在就不可以在抢了
        //*     value:xxoo
        redisTemplate.opsForValue().set(repeatKey,orderNo);
        log.info("执行时间：{}", System.currentTimeMillis() - startTime);
        return orderNo;
    }
}
