package cn.itsource.service;

import cn.itsource.dto.AlipayNotifyDto;
import cn.itsource.dto.ApplyPayDto;

public interface IPayService {
    /**
     * 发起支付申请
     * @param dto
     * @return
     */
    String apply(ApplyPayDto dto);

    /**
     * 支付宝支付异步通知结果处理
     * @param dto
     * @return
     */
    String notify(AlipayNotifyDto dto);
}
