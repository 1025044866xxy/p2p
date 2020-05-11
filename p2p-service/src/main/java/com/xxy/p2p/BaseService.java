package com.xxy.p2p;

import com.xxy.p2p.base.PageSet;
import com.xxy.p2p.entity.domain.UserInfoDO;
import com.xxy.p2p.service.UserService;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BaseService {
    @Resource
    UserService userService;
    protected PageSet getEmptyPageSet(PageSet pageSet) {
        pageSet.setResultList(Collections.emptyList());
        pageSet.setResultCount(0);
        return pageSet;
    }
    protected UserInfoDO getUserById(Integer id){
        List<UserInfoDO> userInfoDOS = userService.getByIdList(Arrays.asList(id));
        if(!CollectionUtils.isEmpty(userInfoDOS)){
            return userInfoDOS.get(0);
        }
        return null;
    }
}
