package moe.ahao.exception;

public enum CommonBizExceptionEnum implements BizExceptionEnum<BizException> {
    SERVER_ILLEGAL_ARGUMENT_ERROR(100001, "业务方法参数检查不通过"),
    ;

    private int code;
    private String message;
    CommonBizExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public BizException msg(Object... args) {
        return new BizException(code, String.format(message, args));
    }
}
