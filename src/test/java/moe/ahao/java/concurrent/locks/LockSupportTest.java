package moe.ahao.java.concurrent.locks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.LockSupport;

class LockSupportTest {
    @Test
    void test() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        Thread thread1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "挂起之前");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "唤醒之后");
            latch.countDown();
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                try {
                    System.out.println(Thread.currentThread().getName() + "等待" + i + "秒");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + "尝试唤醒" + thread1.getName());
            LockSupport.unpark(thread1);
        });

        thread1.start();
        thread2.start();

        latch.await();
        Assertions.assertEquals(0, latch.getCount());
    }

}
