package com.xxy.p2p.dao.mapper;

import com.xxy.p2p.entity.domain.LoginHistoryDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginHistoryDAO {
    void insert(LoginHistoryDO loginHistory);
}
