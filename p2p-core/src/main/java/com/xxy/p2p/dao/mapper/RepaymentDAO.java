package com.xxy.p2p.dao.mapper;

import com.xxy.p2p.entity.domain.RepaymentDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RepaymentDAO {
    void insert(RepaymentDO repaymentDO);
}
