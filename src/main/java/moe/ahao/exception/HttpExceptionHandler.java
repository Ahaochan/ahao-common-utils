package moe.ahao.exception;

import moe.ahao.domain.entity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@ConditionalOnClass(DispatcherServlet.class)
public class HttpExceptionHandler {
    public static final Logger logger = LoggerFactory.getLogger(HttpExceptionHandler.class);

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<Object> methodNotAllowed(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        String message = String.format("请求不支持%s方法, 请使用%s方法", e.getMethod(), e.getSupportedHttpMethods());
        logger.error(message, e);
        return Result.failure(message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> methodNotAllowed(HttpServletRequest request, MissingServletRequestParameterException e) {
        String message = String.format("请求缺少%s类型参数%s", e.getParameterType(), e.getParameterName());
        logger.error(message, e);
        return Result.failure(message);
    }
}
