package cn.itsource.service;

import cn.itsource.dto.ApplyPayDto;

public interface IPayService {
    /**
     * 发起支付申请
     * @param dto
     * @return
     */
    String apply(ApplyPayDto dto);
}
