package com.xxy.p2p.config;

import com.xxy.p2p.controller.BaseController;
import com.xxy.p2p.dao.mapper.LoginHistoryDAO;
import com.xxy.p2p.entity.domain.LoginHistoryDO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


@Component
@Aspect
public class LoginHistoryAOP extends BaseController {
    @Autowired
    private LoginHistoryDAO loginHistoryDAO;

    @Pointcut("execution(* com.xxy.p2p.controller.LoginController.login*(..))")
    public void controller() {
    }

    @Around("controller()")
    private Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object o = null;
        try {
             o = joinPoint.proceed();
        }finally {
            ServletRequestAttributes sra = (ServletRequestAttributes)
                    (RequestContextHolder.getRequestAttributes());
            HttpServletRequest request = sra.getRequest();
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            //获取切入方法的对象
            Method method = signature.getMethod();
            NoneLogin noneLogin = method.getAnnotation(NoneLogin.class);
            // 获取用户
            Integer userId = noneLogin.userId();
            String userName = noneLogin.userName();
            // 获取IP
            String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");
            // 获取UA完整信息
            LoginHistoryDO loginHistory = new LoginHistoryDO();
            loginHistory.setUserId(userId);
            loginHistory.setIp(ip);
            loginHistory.setUserName(userName);
            if(userId != 0){
                loginHistoryDAO.insert(loginHistory);
            }
            return o;
        }
    }

}
