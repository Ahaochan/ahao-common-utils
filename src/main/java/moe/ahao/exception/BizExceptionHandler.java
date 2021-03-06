package moe.ahao.exception;

import moe.ahao.domain.entity.AjaxDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@ConditionalOnClass(DispatcherServlet.class)
public class BizExceptionHandler {
    public static final Logger logger = LoggerFactory.getLogger(BizExceptionHandler.class);

    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public AjaxDTO methodNotAllowed(HttpServletRequest request, BizException e) {
        String message = String.format("发生业务异常: %s", e.getMessage());
        logger.error(message);
        return AjaxDTO.failure(message);
    }
}
