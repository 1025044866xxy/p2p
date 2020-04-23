package com.xxy.p2p.entity.domain;

import com.xxy.p2p.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class BorrowDO extends BaseDO {

    private Integer id;

    private Integer userId;

    private String startDate;

    private BigDecimal money;

    private BigDecimal repayment;

    private BigDecimal interest;

    private BigDecimal interestMoney;

    private BigDecimal overdueMoney;

    private BigDecimal overdueInterest;

    private BigDecimal overdueInterestMoney;

    private String endDate;

    private Integer state;

    private String finishDate;


}
