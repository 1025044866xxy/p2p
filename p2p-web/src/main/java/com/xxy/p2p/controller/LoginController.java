package com.xxy.p2p.controller;

import com.xxy.p2p.base.SuccessResponse;
import com.xxy.p2p.code.ErrorCodeEnum;
import com.xxy.p2p.config.NoneLogin;
import com.xxy.p2p.constant.TokenConstant;
import com.xxy.p2p.entity.domain.UserInfoDO;
import com.xxy.p2p.service.TokenHelperService;
import com.xxy.p2p.service.UserService;
import com.xxy.p2p.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@RestController
@RequestMapping("/token")
public class LoginController extends BaseController {

    @Resource
    UserService userService;

    @Autowired
    TokenHelperService tokenHelperService;

    @NoneLogin
    @GetMapping("/login")
    public SuccessResponse<String> login(@NotBlank String accountNumber, @NotBlank String password) {
        UserInfoDO userInfoDO = userService.getByAccountNumber(accountNumber);
        Assert.isTrue(userInfoDO != null, ErrorCodeEnum.X01.getCode());
        password = MD5Util.MD5(password);
        Assert.isTrue(Objects.equals(password, userInfoDO.getPassword()), ErrorCodeEnum.X02.getCode());
        String token = tokenHelperService.create(userInfoDO);
        return getSuccessResponse(token);
    }

    @NoneLogin
    @GetMapping("/register")
    public SuccessResponse<Boolean> register(@NotBlank String accountNumber, @NotBlank String password) {
        UserInfoDO userInfoDO = userService.getByAccountNumber(accountNumber);
        Assert.isTrue(userInfoDO == null, ErrorCodeEnum.X03.getCode());
        password = MD5Util.MD5(password);
        userInfoDO = new UserInfoDO();
        userInfoDO.setAccountNumber(accountNumber);
        userInfoDO.setPassword(password);
        return getSuccessResponse(userService.insert(userInfoDO));
    }

    @GetMapping("/cancel")
    public SuccessResponse<Boolean> cancel(HttpServletRequest request) {
        String token = request.getHeader("token");
        UserInfoDO userInfoDO = getUserInfo(request);
        tokenHelperService.delete(TokenConstant.tokenKeyPrefix + userInfoDO.getId());
        tokenHelperService.delete(token);
        return getSuccessResponse(true);
    }
}
