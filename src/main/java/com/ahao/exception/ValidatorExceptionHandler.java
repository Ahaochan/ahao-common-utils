package com.ahao.exception;

import com.ahao.domain.entity.AjaxDTO;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.DispatcherServlet;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Spring校验异常拦截器
 */
@RestControllerAdvice("com.ahao")
@Order(0)
@ConditionalOnClass(DispatcherServlet.class)
public class ValidatorExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AjaxDTO methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        // 1. 找到 BindingResult 参数
        BindingResult bindingResult = ex.getBindingResult();
        List<ObjectError> errors = bindingResult.getAllErrors();

        // 2. 生成错误信息
        Map<String, String> fieldErrors = errors.stream()
            .filter(e -> e instanceof FieldError)
            .map(e -> (FieldError) e)
            .collect(Collectors.toMap(FieldError::getField, e -> ObjectUtils.defaultIfNull(e.getDefaultMessage(), "")));

        String firstErrorMsg = errors.get(0).getDefaultMessage();
        return AjaxDTO.failure(firstErrorMsg, fieldErrors);
    }

    @ExceptionHandler(BindException.class)
    public AjaxDTO bindException(BindException ex) {
        // 1. 找到 BindingResult 参数
        BindingResult bindingResult = ex.getBindingResult();
        List<ObjectError> errors = bindingResult.getAllErrors();

        // 2. 生成错误信息
        Map<String, String> fieldErrors = errors.stream()
            .filter(e -> e instanceof FieldError)
            .map(e -> (FieldError) e)
            .collect(Collectors.toMap(FieldError::getField, e -> ObjectUtils.defaultIfNull(e.getDefaultMessage(), "")));

        String firstErrorMsg = errors.get(0).getDefaultMessage();
        return AjaxDTO.failure(firstErrorMsg, fieldErrors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public AjaxDTO constraintViolationException(ConstraintViolationException ex) {
        // 1. 生成错误信息
        Map<String, String> fieldErrors = new HashMap<>();
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            String path = String.valueOf(constraintViolation.getPropertyPath());
            String field = path.substring(Math.max(path.indexOf('.') + 1, 0)); // 排除第一个节点
            String message = constraintViolation.getMessage();
            fieldErrors.put(field, message);
        }

        String firstErrorMsg = fieldErrors.values().iterator().next();
        return AjaxDTO.failure(firstErrorMsg, fieldErrors);
    }
}
