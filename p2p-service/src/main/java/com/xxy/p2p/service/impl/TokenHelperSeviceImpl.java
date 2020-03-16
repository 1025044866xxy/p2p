package com.xxy.p2p.service.impl;

import com.alibaba.fastjson.JSON;
import com.xxy.p2p.constant.TokenConstant;
import com.xxy.p2p.dao.config.RedisClient;
import com.xxy.p2p.entity.domain.UserDO;
import com.xxy.p2p.service.TokenHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenHelperSeviceImpl  implements TokenHelperService{

    @Autowired
    RedisClient redisClient;

    @Override
    public String create(UserDO userDO) {
        String key = TokenConstant.tokenKeyPrefix + userDO.getId();
        String token = UUID.randomUUID().toString();
        redisClient.set(token, JSON.toJSONString(userDO));
        return token;
    }

    @Override
    public boolean check(String token) {
        String value = redisClient.get(token);
        return StringUtils.isBlank(value);
    }

    @Override
    public UserDO get(String token) {
        String value = redisClient.get(token);
        if(StringUtils.isBlank(value)){
            return null;
        }
        return (UserDO)JSON.parse(value);
    }

    @Override
    public boolean delete(String token) {
        return redisClient.remove(token);
    }
}
