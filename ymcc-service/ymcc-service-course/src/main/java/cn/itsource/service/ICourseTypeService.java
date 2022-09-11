package cn.itsource.service;

import cn.itsource.domain.CourseType;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 课程目录 服务类
 * </p>
 *
 * @author director
 * @since 2022-09-02
 */
public interface ICourseTypeService extends IService<CourseType> {

    List<CourseType> treeData();

    void updateTotalCountById(Long courseTypeId);
}
