package com.ahao.util.commons.lang;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class LambdaHelper {
    /**
     * 处理 lambda 表达式内的异常
     * @param object   上游的参数
     * @param supplier 转换处理器
     * @param consumer 异常处理器
     * @param <O>      上游参数的类型
     * @param <T>      转换后的类型
     * @return 转换后的参数, 如果发生了异常, 返回 null
     */
    public static <O, T> T catchException(O object, Supplier<T> supplier, BiConsumer<O, Exception> consumer) {
        try {
            return supplier.get();
        } catch (Exception e) {
            if(consumer != null) {
                consumer.accept(object, e);
            }
            return null;
        }
    }
}
