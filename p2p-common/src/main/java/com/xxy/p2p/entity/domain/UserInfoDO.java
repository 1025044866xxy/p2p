package com.xxy.p2p.entity.domain;

import com.xxy.p2p.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserInfoDO extends BaseDO {
    private Integer id;

    private String accountNumber;

    private String password;

    private String userName;

    private Integer age;

    private Integer gender;

    private String phone;

    private String mail;
}
