package moe.ahao.exception.handler;

import moe.ahao.domain.entity.Result;
import moe.ahao.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
@ConditionalOnClass(DispatcherServlet.class)
@Order(Ordered.LOWEST_PRECEDENCE - 2)
public class BizExceptionHandler {
    public static final Logger logger = LoggerFactory.getLogger(BizExceptionHandler.class);

    @ExceptionHandler(BizException.class)
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> bizException(HttpServletRequest request, HttpServletResponse response, BizException e) {
        logger.warn("发生业务异常: {}", e.getMessage());
        return Result.failure(e.getMessage());
    }
}
