package cn.itsource.service;

import cn.itsource.domain.KillCourse;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author director
 * @since 2022-09-21
 */
public interface IKillCourseService extends IService<KillCourse> {

    /**
     * 为活动添加秒杀商品
     * @param killCourse
     */
    void addKillCourse(KillCourse killCourse);
}
