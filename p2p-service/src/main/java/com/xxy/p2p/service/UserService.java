package com.xxy.p2p.service;

import com.xxy.p2p.entity.domain.UserDO;

public interface UserService {
    UserDO getByAccountNumber(String accountNumber);

    Boolean insert(UserDO userDO);
}
