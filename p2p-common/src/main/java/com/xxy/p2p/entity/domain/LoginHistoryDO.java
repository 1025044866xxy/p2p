package com.xxy.p2p.entity.domain;

import com.xxy.p2p.base.BaseDO;
import lombok.Data;

@Data
public class LoginHistoryDO extends BaseDO {

    private Integer id;

    private Integer userId;

    private String userName;

    private String ip;
}
