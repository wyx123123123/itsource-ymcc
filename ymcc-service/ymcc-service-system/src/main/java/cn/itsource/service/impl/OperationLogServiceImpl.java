package cn.itsource.service.impl;

import cn.itsource.domain.OperationLog;
import cn.itsource.mapper.OperationLogMapper;
import cn.itsource.service.IOperationLogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志记录 服务实现类
 * </p>
 *
 * @author director
 * @since 2022-08-25
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements IOperationLogService {

}
