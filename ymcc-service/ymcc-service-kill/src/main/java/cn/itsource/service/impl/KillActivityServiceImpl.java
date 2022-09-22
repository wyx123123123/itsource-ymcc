package cn.itsource.service.impl;

import cn.itsource.domain.KillActivity;
import cn.itsource.domain.KillCourse;
import cn.itsource.mapper.KillActivityMapper;
import cn.itsource.service.IKillActivityService;
import cn.itsource.service.IKillCourseService;
import cn.itsource.util.AssertUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import jdk.nashorn.internal.ir.ContinueNode;
import net.bytebuddy.asm.Advice;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author director
 * @since 2022-09-21
 */
@Service
public class KillActivityServiceImpl extends ServiceImpl<KillActivityMapper, KillActivity> implements IKillActivityService {
    @Autowired
    private IKillCourseService killCourseService;
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    /**
     *
     * 1.参数校验
     * 2.业务校验
     *   活动必须存在
     *   活动状态要合法
     *   活动的时间要合法
     *
     * 3.修改活动状态+时间
     * 4.活动下的产品存储到redis  怎么存？？？
     * 5.每个商品对应的库存也要使用信号量存储到redis  信号量怎么使用？？
     *
     * @param activityId
     */
    @Override
    public void publish(Long activityId) {
        //1.参数校验
        //2.业务校验
        KillActivity killActivity = selectById(activityId);
        //  活动必须存在
        AssertUtil.isNotNull(killActivity,"活动不存在！！");
        //  活动状态要合法
        //  活动的时间要合法  当前时间小于活动的结束时间
        Date now = new Date();
        boolean before = now.before(killActivity.getEndTime());
        AssertUtil.isTrue(before,"活动时间不合法！！");

        Wrapper<KillCourse> wrapper = new EntityWrapper<>();
        wrapper.eq("activity_id",killActivity.getId());
        List<KillCourse> killCourses = killCourseService.selectList(wrapper);
        AssertUtil.isNotNull(killCourses,"没有秒杀课程，无法上架！！");

        //3.修改活动状态+时间
        killActivity.setPublishStatus(KillActivity.STATE_PUBLISH);
        killActivity.setPublishTime(now);
        updateById(killActivity);
        //4.活动下的产品存储到redis  怎么存？？？ String  list  set  zset  hash
        for (KillCourse killCourse : killCourses) {
            //5.每个商品对应的库存也要使用信号量存储到redis  信号量怎么使用？？
            String xiaoKey = killCourse.getId().toString();
            RSemaphore semaphore = redissonClient.getSemaphore(xiaoKey);
            boolean trySetPermits = semaphore.trySetPermits(killCourse.getKillCount());
            if(!trySetPermits){
                //兜底
                continue;
            }
            //库存设置成功，我们才把秒杀课程存到Redis
            String daKey = "avtivity:"+killActivity.getId();
            //使用hash存储秒杀活动下的商品
            redisTemplate.opsForHash().put(daKey,killCourse.getId().toString(),killCourse);
        }
    }
}
