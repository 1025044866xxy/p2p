package com.xxy.p2p.service;

import com.xxy.p2p.entity.domain.UserInfoDO;

public interface TokenHelperService {

    String create(UserInfoDO userInfoDO);

    boolean check(String token);

    Integer get(String token);

    boolean delete(String token);
}
