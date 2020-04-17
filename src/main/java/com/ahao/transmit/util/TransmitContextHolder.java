package com.ahao.transmit.util;

import com.alibaba.ttl.TransmittableThreadLocal;

public class TransmitContextHolder {
    public static final String HEADER_TOKEN = "token";

    private static ThreadLocal<String> token = new TransmittableThreadLocal<>();
    private static ThreadLocal<String> tenant = new TransmittableThreadLocal<>();

    public static void set(String key, String value) {
        switch (key) {
            case HEADER_TOKEN: token.set(value); break;
            default: throw new UnsupportedOperationException("不支持透传header:"+key);
        }
    }

    public static String get(String key) {
        switch (key) {
            case HEADER_TOKEN: return token.get();
            default: throw new UnsupportedOperationException("不支持透传header:"+key);
        }
    }

    public static void remove(String key) {
        switch (key) {
            case HEADER_TOKEN: token.remove(); break;
            default: throw new UnsupportedOperationException("不支持透传header:"+key);
        }
    }
}
