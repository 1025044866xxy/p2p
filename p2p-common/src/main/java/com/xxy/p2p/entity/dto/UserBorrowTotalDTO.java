package com.xxy.p2p.entity.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserBorrowTotalDTO {

    private Integer totalCount;

    private BigDecimal totalMoney;

    private BigDecimal totalRepayment;

    private BigDecimal totalBorrowMoney;

    private Integer successTotal;

    private Integer failTotal;
}
