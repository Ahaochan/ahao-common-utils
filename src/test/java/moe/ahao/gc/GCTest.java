package moe.ahao.gc;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class GCTest {
    /**
     * -XX:NewSize=100m -XX:MaxNewSize=100m -XX:InitialHeapSize=200m -XX:MaxHeapSize=200m
     * -XX:SurvivorRatio=8 -XX:+UseParNewGC -XX:+UseConcMarkSweepGC
     * -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc.log
     */
    @Test
    @Disabled("配置JVM参数后测试, youngGc示例")
    public void youngGc() {
        byte[] array1 = new byte[20 * 1024 * 1024];
        array1 = new byte[20 * 1024 * 1024];
        array1 = new byte[20 * 1024 * 1024];
        array1 = null;
        // 新生代: 60M/100M, E区: 60M/80M

        // 第1次YoungGC
        // 新生代: 0M/50M, E区: 0M/40M
        byte[] array2 = new byte[20 * 1024 * 1024];
        // 新生代: 20M/100M, E区: 20M/80M

        // 0.100: [GC (Allocation Failure) 0.100: [ParNew: 64716K->656K(92160K), 0.0006605 secs] 64716K->656K(194560K), 0.0008128 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]

        // 0.100: [GC (Allocation Failure) 0.100: [ParNew: 64716K->656K(92160K), 0.0006605 secs] 64716K->656K(194560K), 0.0008128 secs]
        // 系统运行 0.100 秒后, 因为 Allocation Failure 发生 GC
        // 系统运行 0.100 秒后, 使用 ParNew 进行 GC, 新生代总内存 92160K, 已用 64716K, 回收后还占用 656K, 耗时 0.0006605 秒.
        // 堆总内存 194560K, 已用 64716K, 回收后还占用 656K, 耗时 0.0008128 秒.
        // [Times: user=0.00 sys=0.00, real=0.00 secs]
        // 本次 GC 耗时 0.00 秒

        // JVM 退出时打印以下日志
        // par new generation   total 92160K, used 21956K [0x00000000f3800000, 0x00000000f9c00000, 0x00000000f9c00000)
        // eden space 81920K,  26% used [0x00000000f3800000, 0x00000000f4ccce50, 0x00000000f8800000)
        // from space 10240K,   6% used [0x00000000f9200000, 0x00000000f92a4370, 0x00000000f9c00000)
        // to   space 10240K,   0% used [0x00000000f8800000, 0x00000000f8800000, 0x00000000f9200000)
        // concurrent mark-sweep generation total 102400K, used 0K [0x00000000f9c00000, 0x0000000100000000, 0x0000000100000000)
        // Metaspace       used 2575K, capacity 4486K, committed 4864K, reserved 1056768K
        // class space    used 280K, capacity 386K, committed 512K, reserved 1048576K
    }

    /**
     * -XX:NewSize=100m -XX:MaxNewSize=100m -XX:InitialHeapSize=200m -XX:MaxHeapSize=200m
     * -XX:SurvivorRatio=8 -XX:+UseParNewGC -XX:+UseConcMarkSweepGC
     * -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc.log
     */
    @Test
    @Disabled("配置JVM参数后测试, 动态年龄进入老年代示例")
    public void dynamicAge() {
        byte[] array1 = new byte[20 * 1024 * 1024];
        array1 = new byte[20 * 1024 * 1024];
        array1 = new byte[20 * 1024 * 1024];
        array1 = null;
        // 新生代: 60M/100M, E区: 60M/80M

        byte[] array2 = new byte[6 * 1024 * 1024];
        // 新生代: 60M+6M/100M, E区: 60M+6M/80M

        // 第1次YoungGC
        // 新生代: 6M/100M, E区: 0/80M, S1区: 6M/10M, 超过S区一半
        byte[] array3 = new byte[20 * 1024 * 1024];
        array3 = new byte[20 * 1024 * 1024];
        array3 = new byte[20 * 1024 * 1024];
        // 新生代: 60M+6M/10M, E区: 60M/80M, S1区: 6M/10M
        array3 = new byte[2 * 1024 * 1024];
        array3 = null;
        // 新生代: 60M+6M+2M/10M, E区: 60M+2M/80M, S1区: 6M/10M

        // 第2次YoungGC
        // 新生代: 6M/100M, E区: 0M/80M, S2区: 6M/10M, 超过S区一半, 晋升老年代
        byte[] array4 = new byte[20 * 1024 * 1024];
        // 新生代: 20M/100M, E区: 20M/80M; 老年代: 6M/100M
    }

    /**
     * -XX:NewSize=100m -XX:MaxNewSize=100m -XX:InitialHeapSize=200m -XX:MaxHeapSize=200m
     * -XX:SurvivorRatio=8 -XX:+UseParNewGC -XX:+UseConcMarkSweepGC
     * -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc.log
     * -XX:MaxTenuringThreshold=2
     */
    @Test
    @Disabled("配置JVM参数后测试, 超龄进入老年代示例")
    public void maxTenuringThreshold() {
        byte[] array1 = new byte[40 * 1024 * 1024];
        array1 = null;
        // 新生代: 40M/100M, E区: 40M/80M

        byte[] alive = new byte[2 * 1024 * 1024];
        // 新生代: 40M+2M/100M, E区: 40M+2M/80M

        // 第1次YoungGC
        // 新生代: 2M/100M, E区: 0/80M, S1区: 2M/10M
        byte[] array3 = new byte[40 * 1024 * 1024];
        // 新生代: 40M+2M/100M, E区: 40M/80M, S1区: 2M/10M
        array3 = new byte[2 * 1024 * 1024];
        array3 = null;
        // 新生代: 40M+2M+2M/100M, E区: 40M+2M/80M, S1区: 2M/10M

        // 第2次YoungGC
        // 新生代: 2M/100M, E区: 0/80M, S2区: 2M/10M
        byte[] array4 = new byte[40 * 1024 * 1024];
        // 新生代: 40M+2M/100M, E区: 40M/80M, S2区: 2M/10M
        array4 = new byte[2 * 1024 * 1024];
        array4 = null;
        // 新生代: 40M+2M+2M/100M, E区: 40M+2M/80M, S2区: 2M/10M

        // 第3次YoungGC, 将超龄对象放到老年代
        // 新生代: 0M/100M; 老年代: 2M/100M
        byte[] array5 = new byte[40 * 1024 * 1024];
        // 新生代: 40M/100M, E区: 40M/80M; 老年代: 2M/100M
    }

    /**
     * -XX:NewSize=100m -XX:MaxNewSize=100m -XX:InitialHeapSize=200m -XX:MaxHeapSize=200m
     * -XX:SurvivorRatio=8 -XX:+UseParNewGC -XX:+UseConcMarkSweepGC
     * -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc.log
     */
    @Test
    @Disabled("配置JVM参数后测试, S区装不下进入老年代示例")
    public void biggerThanSurvivor() {
        byte[] array1 = new byte[20 * 1024 * 1024];
        array1 = new byte[20 * 1024 * 1024];
        array1 = new byte[20 * 1024 * 1024];
        // 新生代: 60M/100M, E区: 60M/80M

        byte[] array2 = new byte[10 * 1024 * 1024];
        array2 = new byte[4 * 1024 * 1024];
        // 新生代: 60M+10M+4M/100M, E区: 60M+10M+4M/80M

        // 第1次YoungGC, S区装不下20M的对象, 部分对象晋升老年代, !!!注意是部分!!!!
        // 新生代: 4M/100M, E区: 0/80M, S1区: 4M/10M; 老年代: 20M/100M
        byte[] array3 = new byte[20 * 1024 * 1024];
        // 新生代: 20M+4M/100M, E区: 20M/80M, S1区: 4M/10M; 老年代: 20M/100M
    }

    /**
     * -XX:NewSize=100m -XX:MaxNewSize=100m -XX:InitialHeapSize=200m -XX:MaxHeapSize=200m
     * -XX:SurvivorRatio=8 -XX:+UseParNewGC -XX:+UseConcMarkSweepGC
     * -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc.log
     * -XX:PretenureSizeThreshold=3M
     */
    @Test
    @Disabled("配置JVM参数后测试, 超大对象进入老年代示例")
    public void pretenureSizeThreshold() {
        byte[] array1 = new byte[4 * 1024 * 1024];
        array1 = new byte[2 * 1024 * 1024];
        array1 = new byte[2 * 1024 * 1024];
        // 新生代: 2M+2M/100M, E区: 2M+2M/80M, 老年代4M/100M
    }

    /**
     * -XX:NewSize=10m -XX:MaxNewSize=10m -XX:InitialHeapSize=20m -XX:MaxHeapSize=20m
     * -XX:SurvivorRatio=8 -XX:+UseParNewGC -XX:+UseConcMarkSweepGC
     * -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc.log
     */
    public void fullGc() {
        byte[] array1 = new byte[4 * 1024 * 1024];
        array1 = null;
        // 新生代: 512K/10M, E区: 512K/8M; 老年代: 4M/10M
        byte[] array2 = new byte[2 * 1024 * 1024];
        byte[] array3 = new byte[2 * 1024 * 1024];
        byte[] array4 = new byte[2 * 1024 * 1024];
        byte[] array5 = new byte[128 * 1024];
        // 新生代: 6M+512K+128K/10M, E区: 6M+512K+128K/8M; 老年代: 4M/10M
        // YoungGC回收不掉, S区也装不下这堆对象, 于是晋升老年代
        // 新生代: 2M+512K+128K/10M, E区: 2M+512K+128K/8M; 老年代: 4M+2M+2M/10M
        // 这个时候, 老年代也装不下了, 执行FullGC
        // 新生代: 2M+512K+128K/10M, E区: 2M+512K+128K/8M; 老年代: 2M+2M/10M
        // 老年代空出来了, 继续晋升老年代
        // 新生代: 0M/10M, E区: 0M/8M; 老年代: 6M+512K+128K/10M

        byte[] array6 = new byte[2 * 1024 * 1024];
        // 新生代: 2M/10M, E区: 2M/8M; 老年代: 6M+512K+128K/10M
    }
}
