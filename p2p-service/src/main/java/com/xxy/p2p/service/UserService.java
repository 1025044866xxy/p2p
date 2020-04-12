package com.xxy.p2p.service;

import com.xxy.p2p.entity.domain.UserInfoDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService {
    UserInfoDO getByAccountNumber(String accountNumber);

    Boolean insert(UserInfoDO userInfoDO);

    Boolean update(@Param("id") Integer id, @Param("update") UserInfoDO update);

    List<UserInfoDO> getByIdList(List<Integer> idList);
}
