package com.xxy.p2p.controller;

import com.xxy.p2p.base.SuccessResponse;
import com.xxy.p2p.code.ErrorCodeEnum;
import com.xxy.p2p.config.NoneLogin;
import com.xxy.p2p.constant.TokenConstant;
import com.xxy.p2p.entity.domain.UserDO;
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
        UserDO userDO = userService.getByAccountNumber(accountNumber);
        Assert.isTrue(userDO != null, ErrorCodeEnum.X01.getCode());
        password = MD5Util.MD5(password);
        Assert.isTrue(Objects.equals(password, userDO.getPassword()), ErrorCodeEnum.X02.getCode());
        String token = tokenHelperService.create(userDO);
        return getSuccessResponse(token);
    }

    @NoneLogin
    @GetMapping("/register")
    public SuccessResponse<Boolean> register(@NotBlank String accountNumber, @NotBlank String password) {
        UserDO userDO = userService.getByAccountNumber(accountNumber);
        Assert.isTrue(userDO == null, ErrorCodeEnum.X03.getCode());
        password = MD5Util.MD5(password);
        userDO = new UserDO();
        userDO.setAccountNumber(accountNumber);
        userDO.setPassword(password);
        return getSuccessResponse(userService.insert(userDO));
    }

    @GetMapping("/cancel")
    public SuccessResponse<Boolean> cancel(HttpServletRequest request) {
        String token = request.getHeader("token");
        UserDO userDO = getUserInfo(request);
        tokenHelperService.delete(TokenConstant.tokenKeyPrefix + userDO.getId());
        tokenHelperService.delete(token);
        return getSuccessResponse(true);
    }
}
