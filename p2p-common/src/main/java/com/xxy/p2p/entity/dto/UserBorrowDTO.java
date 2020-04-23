package com.xxy.p2p.entity.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserBorrowDTO {

    private Integer id;

    private String startDate;

    private BigDecimal money;

    private BigDecimal repayment;

    private BigDecimal interestMoney;

    private BigDecimal overdueMoney;

    private BigDecimal totalMoney;

    private BigDecimal overdueInterestMoney;

    private String endDate;

    private Integer state;

    private String finishDate;
}
