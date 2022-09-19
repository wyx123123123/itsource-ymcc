package cn.itsource.service.impl;

import cn.itsource.domain.CourseMarket;
import cn.itsource.domain.CourseUserLearn;
import cn.itsource.dto.PayResultDto;
import cn.itsource.mapper.CourseUserLearnMapper;
import cn.itsource.service.ICourseMarketService;
import cn.itsource.service.ICourseUserLearnService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author director
 * @since 2022-09-02
 */
@Service
public class CourseUserLearnServiceImpl extends ServiceImpl<CourseUserLearnMapper, CourseUserLearn> implements ICourseUserLearnService {

    @Autowired
    private ICourseMarketService courseMarketService;
    /**
     *
     * 保存购买记录
     *  一个人loginId 可以购买一个课程多次 courseId
     *  我们需要校验表中是否已经存在过购买记录
     *  。如果存在追加可观看时间，覆盖最近的订单号
     *  。如果没有，直接新增即可
     * @param orderDto
     */
    @Override
    public void payResultHandle(PayResultDto orderDto) {
        String orderNo = orderDto.getOrderNo();
        String extParams = orderDto.getExtParams();
        Map<String,Object> map = JSON.parseObject(extParams, Map.class);
        Long loginId = Long.valueOf(map.get("loginId").toString());
        String courseIds = map.get("courseIds").toString();
        Wrapper<CourseUserLearn> wrapper = new EntityWrapper<>();
        wrapper.eq("login_id",loginId);

        String[] ids = courseIds.split(",");
        for (String courseId : ids) {
            //1.查询是否有过购买记录
            wrapper.eq("course_id",courseId);
            CourseUserLearn userLearn = selectOne(wrapper);
            CourseMarket courseMarket = courseMarketService.selectById(courseId);
            Date now = new Date();
            Date endTime = DateUtils.addDays(now,courseMarket.getValidDays());//追加时间
            if(userLearn != null){
                //2.不为空，追加可观看时间，覆盖订单号
                if(now.before(userLearn.getEndTime())){
                    endTime = DateUtils.addDays(userLearn.getEndTime(), courseMarket.getValidDays());
                }else{
                    userLearn.setStartTime(now);//设置开始时间
                }
                userLearn.setEndTime(endTime);//追加时间
                userLearn.setCourseOrderNo(orderNo);//覆盖订单号
                updateById(userLearn);//修改已有记录
            }else{
                //3.为空，保存数据
                userLearn = new CourseUserLearn();
                userLearn.setLoginId(loginId);
                userLearn.setStartTime(now);
                userLearn.setEndTime(endTime);
                userLearn.setCourseId(Long.valueOf(courseId));
                userLearn.setCourseOrderNo(orderNo);
                userLearn.setCreateTime(now);
                insert(userLearn);//新增课程购买记录
            }
        }
    }
}
