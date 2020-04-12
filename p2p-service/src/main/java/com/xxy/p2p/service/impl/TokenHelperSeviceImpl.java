package com.xxy.p2p.service.impl;

import com.xxy.p2p.constant.TokenConstant;
import com.xxy.p2p.dao.config.RedisClient;
import com.xxy.p2p.entity.domain.UserInfoDO;
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
    public String create(UserInfoDO userInfoDO) {
        String key = TokenConstant.tokenKeyPrefix + userInfoDO.getId();
        String token = UUID.randomUUID().toString();
        String existToken = redisClient.get(key);
        if(StringUtils.isNotBlank(existToken)){
            redisClient.remove(existToken);
        }
        redisClient.set(key, token);
        redisClient.set(token, userInfoDO.getId().toString());
        return token;
    }

    @Override
    public boolean check(String token) {
        if(StringUtils.isBlank(token)){
            return false;
        }
        String value = redisClient.get(token);
        return StringUtils.isNotBlank(value);
    }

    @Override
    public Integer get(String token) {
        if(StringUtils.isBlank(token)){
            return null;
        }
        String value = redisClient.get(token);
        if(StringUtils.isBlank(value)){
            return null;
        }
        return Integer.parseInt(value);
    }

    @Override
    public boolean delete(String token) {
        return redisClient.remove(token);
    }
}
