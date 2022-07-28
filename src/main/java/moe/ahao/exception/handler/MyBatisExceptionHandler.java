package moe.ahao.exception.handler;

import moe.ahao.domain.entity.Result;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.PersistenceException;

/**
 * Mybatis异常拦截器
 */
@RestControllerAdvice("moe.ahao")
@ConditionalOnClass({MyBatisSystemException.class, PersistenceException.class})
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class MyBatisExceptionHandler {
    public static final Logger logger = LoggerFactory.getLogger(MyBatisExceptionHandler.class);

    @ExceptionHandler(PersistenceException.class)
    public Result<Object> persistenceException(PersistenceException ex) {
        logger.error("Mybatis 错误", ex);
        Throwable cause = ex.getCause();
        String errorMsg = cause.getMessage();
        return Result.failure(errorMsg, errorMsg);
    }

    @ExceptionHandler(MyBatisSystemException.class)
    public Result<Object> myBatisSystemException(MyBatisSystemException ex) {
        logger.error("Mybatis 错误", ex);
        Throwable cause = ex.getCause(); // 获取 MyBatisSystemException 的 cause
        if (cause != null && cause.getCause() != null) {
            cause = cause.getCause(); // 获取 PersistenceException 的 cause
        }
        String errorMsg = cause == null ? ex.getMessage() : cause.getMessage();
        return Result.failure(errorMsg, errorMsg);
    }
}
