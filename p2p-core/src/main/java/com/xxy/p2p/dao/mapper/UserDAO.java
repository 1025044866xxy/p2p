package com.xxy.p2p.dao.mapper;

import com.xxy.p2p.entity.domain.UserDO;
import com.xxy.p2p.entity.example.UserExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDAO {
    UserDO getByExample(@Param("example") UserExample userExample);

    Integer insert(UserDO userDO);

    int update(Integer id, UserDO update);

    List<UserDO> listByExample(@Param("example") UserExample userExample);
}
