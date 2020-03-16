package com.xxy.p2p.service.impl;

import com.xxy.p2p.BaseService;
import com.xxy.p2p.dao.mapper.UserDAO;
import com.xxy.p2p.entity.domain.UserDO;
import com.xxy.p2p.entity.example.UserExample;
import com.xxy.p2p.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseService implements UserService {

    @Autowired
    UserDAO userDAO;

    @Override
    public UserDO getByAccountNumber(String accountNumber) {
        UserExample userExample = new UserExample();
        userExample.setAccountNumber(accountNumber);
        return userDAO.getByExample(userExample);
    }

    @Override
    public Boolean insert(UserDO userDO) {
        return userDAO.insert(userDO) == 1;
    }
}
