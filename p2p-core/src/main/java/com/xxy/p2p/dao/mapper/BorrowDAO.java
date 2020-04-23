package com.xxy.p2p.dao.mapper;

        import com.xxy.p2p.base.PageSet;
        import com.xxy.p2p.entity.domain.BorrowDO;
        import com.xxy.p2p.entity.example.BorrowExample;
        import org.apache.ibatis.annotations.Mapper;
        import org.apache.ibatis.annotations.Param;

        import java.util.List;

@Mapper
public interface BorrowDAO {
    int insert(BorrowDO borrowDO);

    BorrowDO getByExample(@Param("example") BorrowExample example);

    int update(@Param("update") BorrowDO update);

    List<BorrowDO> listByExample(@Param("example") BorrowExample example, @Param("pageSet") PageSet pageSet);

    Integer count(@Param("example") BorrowExample example);
}
