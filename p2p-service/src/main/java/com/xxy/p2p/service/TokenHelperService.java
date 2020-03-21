package com.xxy.p2p.service;

import com.xxy.p2p.entity.domain.UserDO;

public interface TokenHelperService {

    String create(UserDO userDO);

    boolean check(String token);

    Integer get(String token);

    boolean delete(String token);
}
