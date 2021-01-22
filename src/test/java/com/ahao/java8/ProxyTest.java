package com.ahao.java8;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ProxyTest {

    interface MyInterface {
        String toUpperCase(String str);
    }
    static class MyInterfaceImpl implements MyInterface {
        @Override
        public String toUpperCase(String str) {
            return str.toUpperCase();
        }
    }

    @Test
    public void cglibInterface() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(MyInterface.class);
        enhancer.setCallback((MethodInterceptor) (obj, m, a, proxy) -> {
            System.out.printf("CgLib动态代理接口:%s#%s(%s)%n", obj.getClass().getSimpleName(), m.getName(),
                Arrays.stream(a).map(o -> o.getClass().getSimpleName()).collect(Collectors.joining(",")));
            return m.invoke(new MyInterfaceImpl(), a);
        });

        MyInterface proxy = (MyInterface) enhancer.create();
        String msg = "hello";
        Assertions.assertEquals(new MyInterfaceImpl().toUpperCase(msg), proxy.toUpperCase(msg));
    }

    @Test
    public void cglibClass() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(MyInterfaceImpl.class);
        enhancer.setCallback((MethodInterceptor) (obj, m, a, proxy) -> {
            System.out.printf("CgLib动态代理类:%s#%s(%s)%n", obj.getClass().getSimpleName(), m.getName(),
                Arrays.stream(a).map(o -> o.getClass().getSimpleName()).collect(Collectors.joining(",")));
            return proxy.invokeSuper(obj, a);
        });

        MyInterface proxy = (MyInterface) enhancer.create();
        String msg = "hello";
        Assertions.assertEquals(new MyInterfaceImpl().toUpperCase(msg), proxy.toUpperCase(msg));
    }

    @Test
    public void jdkProxyInterface() {
        MyInterface target = String::toUpperCase;
        Class<?> clazz = target.getClass();
        MyInterface proxy = (MyInterface) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), (p, m, a) -> {
            System.out.printf("JDK动态代理:%s#%s(%s)%n", p.getClass().getSimpleName(), m.getName(),
                Arrays.stream(a).map(o -> o.getClass().getSimpleName()).collect(Collectors.joining(",")));
            // return method.invoke(proxy, args); // loop
            return m.invoke(target, a);
        });

        String msg = "hello";
        Assertions.assertEquals(target.toUpperCase(msg), proxy.toUpperCase(msg));
    }
}
