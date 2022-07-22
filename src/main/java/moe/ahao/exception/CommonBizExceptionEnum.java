package moe.ahao.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonBizExceptionEnum implements BizExceptionEnum<BizException> {
    SYSTEM_UNKNOWN_ERROR(-1, "系统未知错误"),
    /**
     * 客户端HTTP请求方法错误
     * org.springframework.web.HttpRequestMethodNotSupportedException
     */
    CLIENT_HTTP_METHOD_ERROR(1001, "客户端HTTP请求方法错误"),
    /**
     * 客户端request body参数错误, 主要是未能通过Hibernate Validator校验的异常处理
     * org.springframework.web.bind.MethodArgumentNotValidException
     */
    CLIENT_REQUEST_BODY_CHECK_ERROR(1002, "客户端请求体参数校验不通过"),
    /**
     * 客户端@RequestBody请求体JSON格式错误或字段类型错误
     * org.springframework.http.converter.HttpMessageNotReadableException
     * 1、参数类型不对:{"test":"abc"}，本身类型是Long
     * 2、{"test":}  test属性没有给值
     */
    CLIENT_REQUEST_BODY_FORMAT_ERROR(1003, "客户端请求体JSON格式错误或字段类型不匹配"),
    /**
     * 客户端@PathVariable参数错误, 一般是类型不匹配，比如本来是Long类型，客户端却给了一个无法转换成Long字符串
     * org.springframework.validation.BindException
     */
    CLIENT_PATH_VARIABLE_ERROR(1004, "客户端URL中的参数类型错误"),
    /**
     * 客户端@RequestParam参数校验不通过, 主要是未能通过Hibernate Validator校验的异常处理
     * javax.validation.ConstraintViolationException
     */
    CLIENT_REQUEST_PARAM_CHECK_ERROR(1005, "客户端请求参数校验不通过"),

    /**
     * 客户端@RequestParam参数必填, 入参中的@RequestParam注解设置了必填，但是客户端没有给值
     * javax.validation.ConstraintViolationException
     */
    CLIENT_REQUEST_PARAM_REQUIRED_ERROR(1006, "客户端请求缺少必填的参数"),
    /**
     * 通用的业务方法入参检查错误
     * java.lang.IllegalArgumentException
     */
    SERVER_ILLEGAL_ARGUMENT_ERROR(2001, "业务方法参数检查不通过"),
    ;
    private final int code;
    private final String message;

    public BizException msg(Object... args) {
        return new BizException(code, String.format(message, args));
    }
}
