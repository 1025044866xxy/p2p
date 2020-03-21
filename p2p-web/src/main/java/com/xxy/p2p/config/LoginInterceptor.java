package com.xxy.p2p.config;

import com.xxy.p2p.service.TokenHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    TokenHelperService tokenHelperService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        //如果被@NoneAuth注解代表不需要登录验证，直接通过
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if(method.getAnnotation(NoneLogin.class) != null) {
            return true;
        }
        //token验证
        String authStr = request.getHeader("token");
        return tokenHelperService.check(authStr);
    }
}
