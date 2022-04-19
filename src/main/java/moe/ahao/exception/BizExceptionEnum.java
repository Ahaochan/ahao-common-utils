package moe.ahao.exception;

public interface BizExceptionEnum<T extends BizException> {
    int getCode();
    String getMessage();
    T msg(Object... args);

    // default T msg(String... args) {
    //     return new T(getCode(), String.format(getMessage(), args));
    // }
}
