package com.xxy.p2p.dao.mapper;

import com.xxy.p2p.base.PageSet;
import com.xxy.p2p.entity.domain.RepaymentDO;
import com.xxy.p2p.entity.example.RepaymentExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RepaymentDAO {
    void insert(RepaymentDO repaymentDO);

    Integer count(@Param("example") RepaymentExample example);

    List<RepaymentDO> listByExample(@Param("example") RepaymentExample example, @Param("pageSet") PageSet pageSet);
}
