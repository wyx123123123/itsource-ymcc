package cn.itsource.service.impl;

import cn.itsource.domain.MessageEmail;
import cn.itsource.mapper.MessageEmailMapper;
import cn.itsource.service.IMessageEmailService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author director
 * @since 2022-08-26
 */
@Service
public class MessageEmailServiceImpl extends ServiceImpl<MessageEmailMapper, MessageEmail> implements IMessageEmailService {

}
