package cn.itsource.service;

import cn.itsource.domain.KillCourse;
import cn.itsource.dto.KillParamDto;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

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

    /**
     * 从redis查询所有秒杀商品
     * @return
     */
    List<KillCourse> onlineAll();

    /**
     * 从redis查询单个秒杀课程
     * @param activityId
     * @param killId
     * @return
     */
    KillCourse onlineOne(Long activityId, Long killId);

    /**
     * 执行秒杀
     * @param dto
     * @return
     */
    String kill(KillParamDto dto);
}
