package com.ahao.util.commons.lang;

import java.util.Objects;

public class ObjectHelper {

    public static boolean equals(Object a, Object b) {
        return Objects.equals(a, b);
    }

    public static boolean notEquals(Object a, Object b) {
        return !equals(a, b);
    }

    public static boolean equalsAny(Object a, Object... array) {
        if(array == null) {
            return equals(a, null);
        }
        for (Object item : array) {
            if(equals(a, item)) {
                return true;
            }
        }
        return false;
    }

    public static boolean notEqualsAny(Object a, Object... array) {
        return !equalsAny(a, array);
    }
}
