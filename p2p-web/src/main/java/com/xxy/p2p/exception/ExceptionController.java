package com.xxy.p2p.exception;


import com.alibaba.fastjson.JSON;
import com.xxy.p2p.base.ErrorResponse;
import com.xxy.p2p.code.ErrorCodeEnum;
import com.xxy.p2p.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.BindException;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    /**
     * 非法参数验证统一处理
     */
    @ExceptionHandler(value = { BindException.class })
    public ErrorResponse handleValidationException(BindException ex, HttpServletRequest request) throws Exception {
        console(ex, request);
        return getErrorResponse(ErrorCodeEnum.P01);
    }

    /**
     * 参数缺失统一处理
     * @return
     */
    @ExceptionHandler(value = { MissingServletRequestParameterException.class })
    public ErrorResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException ex,
                                                                       HttpServletRequest request) throws Exception {
        console(ex, request);
        return getErrorResponse(ErrorCodeEnum.P01);
    }


    /**
     * Assert 业务逻辑校验异常处理
     * @return
     */
    @ExceptionHandler(value = { IllegalArgumentException.class })
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex,
                                                        HttpServletRequest request) throws Exception {
        console(ex, request);
        //sendEmail(ex, request);

        // 特殊处理的desc
        String[] split = ex.getMessage().split("#");
        boolean flag = split.length > 1;

        String code = split[0];
        String attach = flag ? split[1] : null;

        if (flag) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(code);
            errorResponse.setErrorDesc(attach);
            return errorResponse;
        } else {
            ErrorCodeEnum errorCodeEnum = ErrorCodeEnum.valueOf(code);
            return getErrorResponse(errorCodeEnum);
        }
    }

    /**
     * 异常统一处理
     */
    @ExceptionHandler(value = { Exception.class })
    public ErrorResponse handleException(Exception ex, HttpServletRequest request) throws Exception {
        console(ex, request);
        return getErrorResponse(ErrorCodeEnum.E01);
    }

    private void console(Exception ex, HttpServletRequest request) {
        logger.error("handleValidation,URL:{}, param:{},referer:{}", request.getServletPath(),
                JSON.toJSONString(request.getParameterMap()), request.getHeader("referer"));
        logger.warn("handleValidationExceptionReturn:exception,URL={}", request.getServletPath(), ex);
    }


}
