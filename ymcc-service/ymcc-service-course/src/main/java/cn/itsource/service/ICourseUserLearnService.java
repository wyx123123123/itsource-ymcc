package cn.itsource.service;

import cn.itsource.domain.CourseUserLearn;
import cn.itsource.dto.PayResultDto;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author director
 * @since 2022-09-02
 */
public interface ICourseUserLearnService extends IService<CourseUserLearn> {

    /**
     * 处理支付成功业务逻辑
     * 报错购买记录
     * @param orderDto
     */
    void payResultHandle(PayResultDto orderDto);
}
