package cn.itsource.service.impl;

import cn.itsource.domain.MessageSms;
import cn.itsource.mapper.MessageSmsMapper;
import cn.itsource.service.IMessageSmsService;
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
public class MessageSmsServiceImpl extends ServiceImpl<MessageSmsMapper, MessageSms> implements IMessageSmsService {

}
