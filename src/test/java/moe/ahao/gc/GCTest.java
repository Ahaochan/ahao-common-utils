package moe.ahao.gc;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class GCTest {
    /**
     * -XX:NewSize=5m -XX:MaxNewSize=5m -XX:InitialHeapSize=10m -XX:MaxHeapSize=10m
     * -XX:SurvivorRatio=8 -XX:+UseParNewGC -XX:+UseConcMarkSweepGC
     * -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc.log
     */
    @Test
    @Disabled("配置JVM参数后测试, youngGc示例")
    public void youngGc() {
        // 新生代: 3M/5M
        byte[] array1 = new byte[1024 * 1024];
        array1 = new byte[1024 * 1024];
        array1 = new byte[1024 * 1024];
        array1 = null;

        // 新生代: 6M/5M 触发 YoungGC
        byte[] array2 = new byte[2 * 1024 * 1024];

        // 0.268: [GC (Allocation Failure) 0.269: [ParNew: 4030K->512K(4608K), 0.0015734 secs] 4030K->574K(9728K), 0.0017518 secs][Times: user=0.00 sys=0.00, real=0.00 secs]

        // 0.268: [GC (Allocation Failure) 0.269: [ParNew: 4030K->512K(4608K), 0.0015734 secs] 4030K->574K(9728K), 0.0017518 secs]
        // 系统运行 0.268 秒后, 因为 Allocation Failure 发生 GC
        // 系统运行 0.269 秒后, 使用 ParNew 进行 GC, 新生代总内存 4608K, 已用 4030K, 回收后还占用 512 K, 耗时 0.0015734 秒.
        // 堆总内存 9728K, 已用 4030K, 回收后还占用 574K, 耗时 0.0017518 秒.
        // [Times: user=0.00 sys=0.00, real=0.00 secs]
        // 本次 GC 耗时 0.00 秒

        // JVM 退出时打印以下日志
        // par new generation               total 4608K, used 2601K [0x00000000ff600000, 0x00000000ffb00000, 0x00000000ffb00000)
        // eden space 4096K,  51% used [0x00000000ff600000, 0x00000000ff80a558, 0x00000000ffa00000)
        // from space 512K,  100% used [0x00000000ffa80000, 0x00000000ffb00000, 0x00000000ffb00000)
        // to   space 512K,    0% used [0x00000000ffa00000, 0x00000000ffa00000, 0x00000000ffa80000)
        // concurrent mark-sweep generation total 5120K, used   62K [0x00000000ffb00000, 0x0000000100000000,0x0000000100000000)
        // Metaspace        used 2782K, capacity 4486K, committed 4864K, reserved 1056768K
        // class space      used  300K, capacity  386K, committed  512K, reserved 1048576K
    }

    /**
     * -XX:NewSize=10m -XX:MaxNewSize=10m -XX:InitialHeapSize=20m -XX:MaxHeapSize=20m
     * -XX:SurvivorRatio=8 -XX:+UseParNewGC -XX:+UseConcMarkSweepGC
     * -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc.log
     */
    @Test
    @Disabled("配置JVM参数后测试, 动态年龄进入老年代示例")
    public void dynamicAge() {
        byte[] array1 = new byte[2 * 1024 * 1024];
        array1 = new byte[2 * 1024 * 1024];
        array1 = new byte[2 * 1024 * 1024];
        array1 = null;
        // 新生代: 6M+512K/10M, E区: 6M+512K/8M

        byte[] array2 = new byte[128 * 1024];
        // 新生代: 6M+512K+128K/10M, E区: 6M+512K+128K/8M
        // YoungGC
        // 新生代: 512K+128K/10M, E区: 0/8M, S1区: 512K+128K/1M, 超过S区一半
        byte[] array3 = new byte[2 * 1024 * 1024];
        array3 = new byte[2 * 1024 * 1024];
        array3 = new byte[2 * 1024 * 1024];
        // 新生代: 6M+512K+128K/10M, E区: 6M/8M, S1区: 512K+128K/1M
        array3 = new byte[128 * 1024];
        array3 = null;
        // 新生代: 6M+512K+256K/10M, E区: 6M+128K/8M, S1区: 512K+128K/1M
        // YoungGC
        // 新生代: 512K+128K/10M, E区: 0/8M, S2区: 512K+128K/1M, 超过S区一半, 晋升老年代
        byte[] array4 = new byte[2 * 1024 * 1024];
        // 新生代: 2M/10M, E区: 2M/8M; 老年代: 512K+128K/10M
    }

    /**
     * -XX:NewSize=10m -XX:MaxNewSize=10m -XX:InitialHeapSize=20m -XX:MaxHeapSize=20m
     * -XX:SurvivorRatio=6 -XX:+UseParNewGC -XX:+UseConcMarkSweepGC
     * -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc.log
     * -XX:MaxTenuringThreshold=2
     */
    @Test
    @Disabled("配置JVM参数后测试, 超龄进入老年代示例")
    public void maxTenuringThreshold() {
        byte[] array1 = new byte[4 * 1024 * 1024];
        array1 = null;
        // 新生代: 4M+512K/10M, E区: 4M+512K/6M

        byte[] alive = new byte[128 * 1024];
        // 新生代: 4M+512K+128K/10M, E区: 4M+512K+128K/6M
        // 第1次YoungGC
        // 新生代: 512K+128K/10M, E区: 0/6M, S1区: 512K+128K/2M

        byte[] array3 = new byte[4 * 1024 * 1024];
        // 新生代: 4M+512K+128K/10M, E区: 4M/6M, S1区: 512K+128K/2M
        array3 = new byte[128 * 1024];
        array3 = null;
        // 新生代: 4M+512K+256K/10M, E区: 4M+128K/6M, S1区: 512K+128K/2M
        // 第2次YoungGC
        // 新生代: 512K+128K/10M, E区: 0/6M, S2区: 512K+128K/2M

        byte[] array4 = new byte[4 * 1024 * 1024];
        // 新生代: 4M+512K+128K/10M, E区: 4M/6M, S2区: 512K+128K/2M
        array4 = new byte[128 * 1024];
        array4 = null;
        // 新生代: 4M+512K+256K/10M, E区: 4M+128K/6M, S2区: 512K+128K/2M
        // 第3次YoungGC, 将超龄对象放到老年代
        // 新生代: 0M/10M; 老年代: 512K+128K/10M
        byte[] array5 = new byte[4 * 1024 * 1024];
        // 新生代: 4M/10M, E区: 4M/6M; 老年代: 512K+128K/10M
    }

    /**
     * -XX:NewSize=10m -XX:MaxNewSize=10m -XX:InitialHeapSize=20m -XX:MaxHeapSize=20m
     * -XX:SurvivorRatio=8 -XX:+UseParNewGC -XX:+UseConcMarkSweepGC
     * -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc.log
     * -XX:MaxTenuringThreshold=15
     */
    @Test
    @Disabled("配置JVM参数后测试, S区装不下进入老年代示例")
    public void biggerThanSurvivor() {
        byte[] array1 = new byte[2 * 1024 * 1024];
        array1 = new byte[2 * 1024 * 1024];
        array1 = new byte[2 * 1024 * 1024];
        // 新生代: 6M+512K/10M, E区: 6M+512K/8M

        byte[] array2 = new byte[128 * 1024];
        array2 = null;
        // 新生代: 6M+512K+128K/10M, E区: 6M+512K+128K/8M
        // YoungGC, S区装不下2M的对象, 部分对象晋升老年代, !!!注意是部分!!!!
        // 新生代: 512K/10M, E区: 0/8M, S1区: 512K/1M; 老年代: 2M/10M
        byte[] array3 = new byte[2 * 1024 * 1024];
        // 新生代: 2M+512K/10M, E区: 2M/8M, S1区: 512K/1M; 老年代: 2M/10M
    }

    /**
     * -XX:NewSize=10m -XX:MaxNewSize=10m -XX:InitialHeapSize=20m -XX:MaxHeapSize=20m
     * -XX:SurvivorRatio=8 -XX:+UseParNewGC -XX:+UseConcMarkSweepGC
     * -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc.log
     * -XX:PretenureSizeThreshold=1M
     */
    @Test
    @Disabled("配置JVM参数后测试, 超大对象进入老年代示例")
    public void PretenureSizeThreshold() {
        byte[] array1 = new byte[2 * 1024 * 1024];
        array1 = new byte[512 * 1024];
        array1 = new byte[512 * 1024];
        // 新生代: 1M+512K/10M, E区: 1M+512K/8M, 老年代2M/10M
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
