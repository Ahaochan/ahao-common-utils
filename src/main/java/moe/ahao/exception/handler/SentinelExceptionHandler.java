package moe.ahao.exception.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import moe.ahao.domain.entity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ConditionalOnClass({BlockException.class})
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class SentinelExceptionHandler {
    public static final Logger logger = LoggerFactory.getLogger(SentinelExceptionHandler.class);

    @ExceptionHandler(value = FlowException.class)
    public Result<Object> handle(FlowException e) {
        logger.error("限流控制", e);
        return Result.failure("限流控制");
    }

    @ExceptionHandler(value = DegradeException.class)
    public Result<Object> handle(DegradeException e) {
        logger.error("降级控制", e);
        return Result.failure("降级控制");
    }

    @ExceptionHandler(value = AuthorityException.class)
    public Result<Object> handle(AuthorityException e) {
        logger.error("授权控制", e);
        return Result.failure("授权控制");
    }

    @ExceptionHandler(value = BlockException.class)
    public Result<Object> handle(BlockException e) {
        logger.error("流控异常", e);
        return Result.failure("流控异常");
    }
}
