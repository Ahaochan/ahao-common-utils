package com.ahao.util.commons.lang.reflect;

import com.ahao.util.commons.lang.ObjectHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ClassHelper {
    private static final String CGLIB_CLASS_SEPARATOR = "$$";

    public static List<Class<?>> getAllInterfaces(Class<?> cls) {
        return ClassUtils.getAllInterfaces(cls);
    }
    public static List<Class<?>> getInterfaces(Class<?> cls, Class... interfaces) {
        return ClassUtils.getAllInterfaces(cls).stream()
            .filter(e -> ArrayUtils.contains(interfaces, e))
            .collect(Collectors.toList());
    }

    /**
     * 判断 obj 所属类是否属于 superclasses 的子类或接口
     * @param obj 要判断的类
     * @param superclasses 父类或接口
     */
    public static boolean isSubClass(Object obj, Class<?>... superclasses) {
        if(obj == null) {
            return false;
        }
        for (Class<?> superclass : superclasses) {
            if(superclass.isAssignableFrom(obj.getClass())) {
                return true;
            }
        }
        return false;
    }

    public static Class<?> unwrapCglib(Class<?> clazz) {
        if (clazz != null && StringUtils.contains(clazz.getName(), CGLIB_CLASS_SEPARATOR)) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && ObjectHelper.notEquals(clazz, superClass)) {
                return superClass;
            }
        }
        return clazz;
    }
}
