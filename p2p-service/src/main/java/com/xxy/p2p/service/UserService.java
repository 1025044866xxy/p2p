package com.xxy.p2p.service;

import com.xxy.p2p.entity.domain.UserDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService {
    UserDO getByAccountNumber(String accountNumber);

    Boolean insert(UserDO userDO);

    Boolean update(@Param("id") Integer id, @Param("update") UserDO update);

    List<UserDO> getByIdList(List<Integer> idList);
}
