package com.ahao.exception;

import com.ahao.domain.entity.AjaxDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 系统异常拦截器
 */
@RestControllerAdvice("com.ahao")
@Order(Ordered.LOWEST_PRECEDENCE)
public class SystemExceptionHandler {
    public static final Logger logger = LoggerFactory.getLogger(SystemExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxDTO runtimeException(RuntimeException e) {
        logger.error("运行时异常:", e);
        return AjaxDTO.failure("服务器异常! 请稍候重试!");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxDTO runtimeException(Exception e) {
        logger.error("异常:", e);
        return AjaxDTO.failure("服务器异常! 请稍候重试!");
    }
}
