package moe.ahao.util.commons.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class ConcurrentTestUtils {
    public static void concurrentRunnable(int threadCount, List<Runnable> taskList) throws Exception {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            int index = i;
            new Thread(() -> {
                try {
                    cyclicBarrier.await();
                    taskList.get(index).run();
                    countDownLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        countDownLatch.await();
    }
    public static <T> List<T> concurrentCallable(int threadCount, List<Callable<T>> taskList) throws Exception {
        List<T> result = new ArrayList<>();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            int index = i;
            new Thread(() -> {
                try {
                    cyclicBarrier.await();
                    result.add(taskList.get(index).call());
                    countDownLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        countDownLatch.await();
        return result;
    }
}
