package cn.itsource.service.impl;

import cn.itsource.domain.KillActivity;
import cn.itsource.domain.KillCourse;
import cn.itsource.mapper.KillCourseMapper;
import cn.itsource.service.IKillActivityService;
import cn.itsource.service.IKillCourseService;
import cn.itsource.util.AssertUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author director
 * @since 2022-09-21
 */
@Service
public class KillCourseServiceImpl extends ServiceImpl<KillCourseMapper, KillCourse> implements IKillCourseService {
    @Autowired
    private IKillActivityService killActivityService;

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
}
