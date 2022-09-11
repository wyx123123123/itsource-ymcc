package cn.itsource.service.impl;

import cn.itsource.domain.Teacher;
import cn.itsource.mapper.TeacherMapper;
import cn.itsource.service.ITeacherService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 老师表 服务实现类
 * </p>
 *
 * @author director
 * @since 2022-09-02
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {

}
