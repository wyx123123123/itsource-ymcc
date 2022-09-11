package cn.itsource.service.impl;

import cn.itsource.domain.Department;
import cn.itsource.mapper.DepartmentMapper;
import cn.itsource.service.IDepartmentService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author director
 * @since 2022-08-25
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

}
