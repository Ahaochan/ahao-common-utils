package moe.ahao.util.commons.lang.reflect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class NativeTest {
    public interface InterfaceA {
        String methodA(String str);
    }

    public interface InterfaceB1 extends InterfaceA {
        String methodB(String str);
    }

    public interface InterfaceB2 extends InterfaceA {
        String methodA(String str);
        String methodB(String str);
    }

    @Test
    public void getMethods() {
        Method[] aMethods = InterfaceA.class.getMethods();
        Assertions.assertEquals(1, aMethods.length);
        Assertions.assertEquals("methodA", aMethods[0].getName());

        Method[] b1Methods = InterfaceB1.class.getMethods();
        Assertions.assertEquals(2, b1Methods.length);
        Assertions.assertEquals("methodB", b1Methods[0].getName());
        Assertions.assertEquals("methodA", b1Methods[1].getName());

        Method[] b2Methods = InterfaceB1.class.getMethods();
        Assertions.assertEquals(2, b2Methods.length);
        Assertions.assertEquals("methodB", b2Methods[0].getName());
        Assertions.assertEquals("methodA", b2Methods[1].getName());
    }

    @Test
    public void method() throws Exception {
        InterfaceA obj = str -> str + "methodA";
        Class<?> clazz = obj.getClass();
        Method[] methods = clazz.getMethods();
        System.out.println("方法（包括父类）数量: " + methods.length);
        Method[] declaredMethods = clazz.getDeclaredMethods();
        System.out.println("方法（不包括父类）数量: " + declaredMethods.length);

        Method methodA = clazz.getDeclaredMethod("methodA", String.class);
        Assertions.assertNotNull(methodA);
        System.out.println("方法: " + methodA.getName());

        Class<?> declaringClass = methodA.getDeclaringClass();
        System.out.println("在某个类下:" + declaringClass.getName());

        String string = methodA.toString();
        String genericString = methodA.toGenericString();
        System.out.println(string);
        System.out.println(genericString);
    }
}
