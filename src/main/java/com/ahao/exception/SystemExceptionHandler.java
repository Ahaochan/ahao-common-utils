package com.ahao.exception;

import com.ahao.domain.entity.AjaxDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;

/**
 * 系统异常拦截器
 */
@RestControllerAdvice("com.ahao")
@Order(Ordered.LOWEST_PRECEDENCE)
@ConditionalOnClass(Servlet.class)
public class SystemExceptionHandler {
    public static final Logger logger = LoggerFactory.getLogger(SystemExceptionHandler.class);

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public AjaxDTO methodNotAllowed(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        String message = String.format("该请求不支持%s方法, 请使用%s方法", e.getMethod(), e.getSupportedHttpMethods());
        logger.debug(message);
        return AjaxDTO.failure(message);
    }

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
