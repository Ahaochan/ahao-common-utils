package com.ahao.gc;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OOMTest {
    static class MyClass {
        public String toUpperCase(String str) {
            return str.toUpperCase();
        }
    }

    /**
     * -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
     */
    @Test
    @Disabled("配置JVM参数后测试")
    public void heapOOM() {
        System.out.println("dump文件位于" + new File("").getAbsolutePath() + "目录下");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            list.add("测试" + i);
        }
    }

    /**
     * -Xss128k
     */
    @Test
    @Disabled("配置JVM参数后测试")
    public void stackOOM() {
        stackOOM();
    }

    /**
     * -XX:PermSize=10m -XX:MaxPermSize=10m -XX:+HeapDumpOnOutOfMemoryError
     */
    @Test
    @Disabled("配置JVM参数后测试")
    public void constantOOM() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            list.add(("测试" + i).intern());
        }
    }

    /**
     * -XX:MetaspaceSize=100m -XX:MaxMetaspaceSize=100m
     */
    @Test
    @Disabled("配置JVM参数后测试")
    public void metadataOOM() {
        int count = 0;
        while (count++ < 100000) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(MyClass.class);
            enhancer.setUseCache(false);
            enhancer.setCallback((MethodInterceptor) (obj, m, a, proxy) -> proxy.invokeSuper(obj, a));
            MyClass proxy = (MyClass) enhancer.create();

            System.out.println("创建" + count + "个" + proxy.getClass().getSimpleName() + "类");
        }
    }
}
