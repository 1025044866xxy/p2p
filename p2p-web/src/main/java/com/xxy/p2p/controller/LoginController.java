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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/token")
public class LoginController extends BaseController {

    @Resource
    UserService userService;

    @Autowired
    TokenHelperService tokenHelperService;

    @NoneLogin(userId = 0, userName = "")
    @PostMapping("/login")
    public SuccessResponse<String> login(@NotBlank String accountNumber, @NotBlank String password) throws Exception {
        UserInfoDO userInfoDO = userService.getByAccountNumber(accountNumber);
        if(userInfoDO == null){
            return getErrorResponse(ErrorCodeEnum.X01);
        }
        password = MD5Util.MD5(password);
        if(!Objects.equals(password, userInfoDO.getPassword())){
            return getErrorResponse(ErrorCodeEnum.X02);
        }
        String token = tokenHelperService.create(userInfoDO);
        Method[] methods = this.getClass().getDeclaredMethods();
        for(Method method : methods){
            //是否使用MyAnno注解
            boolean methodHasAnno = method.isAnnotationPresent(NoneLogin.class);
            if(methodHasAnno){
                //得到注解
                NoneLogin methodAnno = method.getAnnotation(NoneLogin.class);
                //输出注解属性
                InvocationHandler invocationHandler = Proxy.getInvocationHandler(methodAnno);
                Field f = invocationHandler.getClass().getDeclaredField("memberValues");
                f.setAccessible(true);
                Map<String, Object> memberValues = (Map<String, Object>) f.get(invocationHandler);
                memberValues.put("userName", userInfoDO.getUserName());
                memberValues.put("userId", userInfoDO.getId());
            }
        }
        return getSuccessResponse(token);
    }

    @NoneLogin(userId = 0, userName = "")
    @PostMapping("/login-by-token")
    public SuccessResponse<Boolean> login(HttpServletRequest request) throws Exception{
        if(tokenHelperService.check(request.getHeader("token"))){
            UserInfoDO userInfoDO = getUserInfo(request);
            Method[] methods = this.getClass().getDeclaredMethods();
            for(Method method : methods){
                //是否使用MyAnno注解
                boolean methodHasAnno = method.isAnnotationPresent(NoneLogin.class);
                if(methodHasAnno){
                    //得到注解
                    NoneLogin methodAnno = method.getAnnotation(NoneLogin.class);
                    //输出注解属性
                    InvocationHandler invocationHandler = Proxy.getInvocationHandler(methodAnno);
                    Field f = invocationHandler.getClass().getDeclaredField("memberValues");
                    f.setAccessible(true);
                    Map<String, Object> memberValues = (Map<String, Object>) f.get(invocationHandler);
                    memberValues.put("userName", userInfoDO.getUserName());
                    memberValues.put("userId", userInfoDO.getId());
                }
            }
            return getSuccessResponse(true);
        }
        return getSuccessResponse(false);
    }

    @NoneLogin
    @PostMapping("/register")
    public SuccessResponse<Boolean> register(UserInfoDO userInfoDO) {
        UserInfoDO getDO = userService.getByAccountNumber(userInfoDO.getAccountNumber());
        Assert.isTrue(getDO == null, ErrorCodeEnum.X03.getCode());
        userInfoDO.setPassword(MD5Util.MD5(userInfoDO.getPassword()));
        return getSuccessResponse(userService.insert(userInfoDO));
    }

    @PostMapping("/cancel")
    public SuccessResponse<Boolean> cancel(HttpServletRequest request) {
        String token = request.getHeader("token");
        UserInfoDO userInfoDO = getUserInfo(request);
        tokenHelperService.delete(TokenConstant.tokenKeyPrefix + userInfoDO.getId());
        tokenHelperService.delete(token);
        return getSuccessResponse(true);
    }
}
