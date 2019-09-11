package com.ahao.util.commons.lang.reflect;

public class ClassHelper {
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
}
