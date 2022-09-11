package cn.itsource.mapper;

import cn.itsource.domain.CourseType;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 课程目录 Mapper 接口
 * </p>
 *
 * @author director
 * @since 2022-09-02
 */
public interface CourseTypeMapper extends BaseMapper<CourseType> {

    void updateTotalCountById(Long courseTypeId);
}
