package com.xxy.p2p.dao.mapper;

import com.xxy.p2p.entity.domain.BorrowDO;
import com.xxy.p2p.entity.example.BorrowExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BorrowDAO {
    int insert(BorrowDO borrowDO);

    BorrowDO getByExample(@Param("example") BorrowExample example);

    int update(@Param("update") BorrowDO update);
}
