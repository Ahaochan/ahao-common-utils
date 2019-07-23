package com.ahao.util.commons.lang;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanHelper {
    private static final Logger logger = LoggerFactory.getLogger(BeanHelper.class);

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj, Class<T> clazz) {
        // 1. 处理 void
        if(obj == null || clazz == null || clazz == void.class || clazz == Void.class) {
            return null;
        }

        // 2. 转为 String
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

    public static Map<String, Object> obj2map(Object obj) {
        Map<String, Object> map = new HashMap<>();

        List<Field> fields = ReflectHelper.getAllField(obj);
        for (Field field : fields) {
            map.put(field.getName(), ReflectHelper.getValue(obj, field));
        }

        return map;
    }

    public static <T> T map2obj(Map<String, Object> map, Class<T> clazz) {
        if(clazz == null || MapUtils.isEmpty(map)) {
            return null;
        }

        T obj = ReflectHelper.create(clazz);
        for (Map.Entry<String, Object> field : map.entrySet()) {
            ReflectHelper.setValue(obj, field.getKey(), field.getValue());
        }
        return obj;
    }
}
