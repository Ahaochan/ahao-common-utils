package moe.ahao.exception;

import org.springframework.http.HttpStatus;

/**
 * fillInStackTrace 方法是经过 synchronized 关键字修饰的.
 * 将 writableStackTrace 置为 false, 不打印异常堆栈.
 * @see Throwable#fillInStackTrace()
 */
public class BizException extends RuntimeException {
    public static final boolean writableStackTrace = false;

    private Integer code;

    private String message;

    public BizException() {
        super(null, null, true, writableStackTrace);
    }

    public BizException(String message) {
        super(message, null, true, writableStackTrace);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause, true, writableStackTrace);
    }

    public BizException(Throwable cause) {
        super(null, cause, true, writableStackTrace);
    }

    public BizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static BizException create(HttpStatus status, String message) {
        return BizException.create(status.value(), message);
    }

    public static BizException create(int code, String message) {
        BizException exception = new BizException(message);
        exception.code = code;
        exception.message = message;
        return exception;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
