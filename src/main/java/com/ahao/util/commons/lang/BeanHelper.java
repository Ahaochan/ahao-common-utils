package com.ahao.util.commons.lang;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class BeanHelper {
    private static final Logger logger = LoggerFactory.getLogger(BeanHelper.class);

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj, Class<T> clazz) {
        // 1. 处理 void
        if(obj == null || clazz == null || clazz == void.class || clazz == Void.class) {
            return null;
        }

        // 2. 从 Redis 中获取数据
        String string = String.valueOf(obj);
        if(clazz == String.class) {
            return (T) string;
        }

        // 3. 处理 char 类型
        if(clazz == char.class || clazz == Character.class) {
            return (T) Character.valueOf(string.charAt(0));
        }

        // 4. 处理数值类型
        if(NumberUtils.isCreatable(string)) {
            if(clazz == byte.class || clazz == Byte.class) {
                return (T) Byte.valueOf(string);
            }
            if(clazz == short.class || clazz == Short.class) {
                return (T) Short.valueOf(string);
            }
            if(clazz == int.class || clazz == Integer.class) {
                return (T) Integer.valueOf(string);
            }
            if(clazz == long.class || clazz == Long.class) {
                return (T) Long.valueOf(string);
            }
            if(clazz == float.class || clazz == Float.class) {
                return (T) Float.valueOf(string);
            }
            if(clazz == double.class || clazz == Double.class) {
                return (T) Double.valueOf(string);
            }
        }
        return (T) obj;
    }

    public static Map toMap(Object obj) {
        return new BeanMap(obj);
    }

    public static <T> T toBean(Map map, Class<T> clazz) {
        if(clazz == null || MapUtils.isEmpty(map)) {
            return null;
        }

        T obj = ReflectHelper.create(clazz);
        try {
            BeanUtils.populate(obj, map);
            return obj;
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("转为Bean失败", e);
        }
        return null;
    }
}
