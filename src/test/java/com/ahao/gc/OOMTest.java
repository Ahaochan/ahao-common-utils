package com.ahao.gc;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OOMTest {
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
}
