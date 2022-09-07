package moe.ahao.exception.handler;

import moe.ahao.domain.entity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 系统异常拦截器
 */
@RestControllerAdvice("moe.ahao")
@Order(Ordered.LOWEST_PRECEDENCE)
public class SystemExceptionHandler {
    public static final Logger logger = LoggerFactory.getLogger(SystemExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> runtimeException(RuntimeException e) {
        logger.error("运行时异常:", e);
        return Result.failure("服务器异常! 请稍候重试!");
    }

    @ExceptionHandler(Exception.class)
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> runtimeException(Exception e) {
        logger.error("异常:", e);
        return Result.failure("服务器异常! 请稍候重试!");
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result<Object> illegalArgumentException(IllegalArgumentException e) {
        logger.error("业务方法参数检查不通过", e);
        return Result.failure("业务方法参数检查不通过!");
    }
}
