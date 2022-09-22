package cn.itsource.service;

import cn.itsource.domain.Course;
import cn.itsource.dto.CourseDto;
import cn.itsource.vo.CourseInfoVo;
import cn.itsource.vo.OrderInfoVo;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author director
 * @since 2022-09-02
 */
public interface ICourseService extends IService<Course> {

    void save(CourseDto courseDto);

    /**
     * 发布课程
     * @param courseId
     */
    void onLineCourse(Long courseId);

    /**
     * 根据课程Id查询课程详情相关信息
     * @param courseId  课程ID
     * @return
     */
    CourseInfoVo courseInfo(Long courseId);

    /**
     * 检查当前登录人是否拥有观看视屏的权限
     * 如果有返回视频地址
     * @param mediaId
     * @return
     */
    String getForUser(Long mediaId);

    /**
     * 查询订单结算页的数据
     * @param courseIds
     * @return
     */
    OrderInfoVo orderInfo(String courseIds);

    /**
     * 查询秒杀订单结算页的数据
     * @param orderNo
     * @return
     */
    OrderInfoVo oneByOrder(String orderNo);
}
