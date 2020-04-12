package com.xxy.p2p.dao.mapper;

import com.xxy.p2p.entity.domain.UserInfoDO;
import com.xxy.p2p.entity.example.UserExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDAO {
    UserInfoDO getByExample(@Param("example") UserExample userExample);

    Integer insert(UserInfoDO userInfoDO);

    int update(Integer id, UserInfoDO update);

    List<UserInfoDO> listByExample(@Param("example") UserExample userExample);
}
