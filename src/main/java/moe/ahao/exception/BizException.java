package moe.ahao.exception;

/**
 * fillInStackTrace 方法是经过 synchronized 关键字修饰的.
 * 将 writableStackTrace 置为 false, 不打印异常堆栈.
 * @see Throwable#fillInStackTrace()
 */
public class BizException extends RuntimeException {
    // 使得addSuppressed和getSuppressed方法失效
    public static final boolean ENABLE_SUPPRESSION = true;
    // 保证不执行fillInStackTrace()方法
    public static final boolean WRITEABLE_STACK_TRACE = false;

    private Integer code;
    private String message;

    public BizException(int code, String message) {
        this(code, message, null);
    }
    public BizException(int code, String message, Throwable cause) {
        super(message, cause, ENABLE_SUPPRESSION, WRITEABLE_STACK_TRACE);
        this.message = message;
    }

    public static BizException create(int code, String message) {
        BizException exception = new BizException(code, message);
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
