package com.xxy.p2p.config;

import java.lang.annotation.*;

/**
 * @author xxy
 * 不需要验证登录
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface NoneLogin {
}
