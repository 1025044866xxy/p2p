package com.xxy.p2p.entity.domain;

import com.xxy.p2p.base.BaseDO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RepaymentDO extends BaseDO {

    private Integer id;

    private Integer userId;

    private BigDecimal money;

    private String date;
}
