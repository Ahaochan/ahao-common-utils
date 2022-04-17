package moe.ahao.util.commons.lang.reflect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class NativeTest {
    public interface InterfaceA {
        String methodA();
    }

    public interface InterfaceB1 extends InterfaceA {
        String methodB();
    }

    public interface InterfaceB2 extends InterfaceA {
        String methodA();
        String methodB();
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
}
