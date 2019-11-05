package com.ahao.util.commons.lang.reflect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class ClassHelperTest {

    @Test
    public void getInterface() {
        Class clazz = Integer.class;

        List<Class<?>> actual = ClassHelper.getAllInterfaces(clazz);
        List<Class<?>> expect = Arrays.asList(Comparable.class, Serializable.class);
        Assertions.assertEquals(expect, actual);
    }

    @Test
    public void isSubClass() {
        Assertions.assertTrue(ClassHelper.isSubClass(123, Integer.class));
        Assertions.assertTrue(ClassHelper.isSubClass(123, Number.class));
        Assertions.assertFalse(ClassHelper.isSubClass(123, String.class));
    }

    @Test
    public void forName() {
        Assertions.assertEquals(Integer.class, ClassHelper.forName(Integer.class.getName()));
        Assertions.assertNull(ClassHelper.forName("123"));
    }
}
