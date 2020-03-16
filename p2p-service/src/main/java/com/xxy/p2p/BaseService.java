package com.xxy.p2p;

import com.xxy.p2p.base.PageSet;

import java.util.Collections;

public class BaseService {
    protected PageSet getEmptyPageSet(PageSet pageSet) {
        pageSet.setResultList(Collections.emptyList());
        pageSet.setResultCount(0);
        return pageSet;
    }
}
