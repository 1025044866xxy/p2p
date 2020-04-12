package com.xxy.p2p.service.impl;

import com.xxy.p2p.BaseService;
import com.xxy.p2p.dao.mapper.UserDAO;
import com.xxy.p2p.entity.domain.UserInfoDO;
import com.xxy.p2p.entity.example.UserExample;
import com.xxy.p2p.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends BaseService implements UserService {

    @Autowired
    UserDAO userDAO;

    @Override
    public UserInfoDO getByAccountNumber(String accountNumber) {
        UserExample userExample = new UserExample();
        userExample.setAccountNumber(accountNumber);
        return userDAO.getByExample(userExample);
    }

    @Override
    public Boolean insert(UserInfoDO userInfoDO) {
        return userDAO.insert(userInfoDO) == 1;
    }

    @Override
    public Boolean update(Integer id, UserInfoDO update) {
        return userDAO.update(id, update) == 1;
    }

    @Override
    public List<UserInfoDO> getByIdList(List<Integer> idList) {
        UserExample example = new UserExample();
        example.setIdList(idList);
        return userDAO.listByExample(example);
    }
}
