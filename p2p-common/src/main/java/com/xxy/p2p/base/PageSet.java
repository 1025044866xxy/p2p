package com.xxy.p2p.base;

import lombok.Data;

import java.util.List;

@Data
public class PageSet<T> extends BaseDO {
    private static final long serialVersionUID = 333333L;
    private Integer start;
    private Integer pageSize;
    private Integer resultCount = 0;
    private Integer currentPageNo;
    public Integer totalPageCount;
    public Integer totallPageCount;
    public Object param;
    private List<T> resultList;

    public PageSet(Integer start, Integer pageSize) {
        this.start = start;
        this.pageSize = pageSize;
    }

    public PageSet() {
    }
}
