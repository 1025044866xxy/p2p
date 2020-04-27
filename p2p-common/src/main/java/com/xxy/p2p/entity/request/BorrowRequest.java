package com.xxy.p2p.entity.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BorrowRequest {

    private Integer id;

    private Integer type;

    private BigDecimal money;

    private BigDecimal interest;

    private Integer userId;

    private Integer rankStatus;

    private Integer state;

}
