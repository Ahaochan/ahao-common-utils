package com.ahao.util.commons.lang;

import com.alibaba.fastjson.JSON;

public class JSONHelper {
    public static String toString(Object obj) {
        return JSON.toJSONString(obj);
    }

    public static byte[] toByteArray(Object obj) {
        return JSON.toJSONBytes(obj);
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }
}
