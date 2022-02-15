package moe.ahao.java.lang.thread;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadTest {
    @Test
    void createThread() {
        Thread mainThread = Thread.currentThread();
        ThreadGroup mainThreadGroup = mainThread.getThreadGroup();

        Thread newThread = new Thread();
        ThreadGroup newThreadGroup = new ThreadGroup("新线程组");
        Assertions.assertEquals(mainThreadGroup, newThread.getThreadGroup());
        Assertions.assertEquals(mainThreadGroup, newThreadGroup.getParent());
    }

    @Test
    void interrupt() throws Exception {
        AtomicInteger i = new AtomicInteger(0);
        AtomicBoolean run = new AtomicBoolean(true);
        Thread thread = new Thread() {
            // volatile boolean run = true;
            @Override
            public void run() {
                while (run.get()) {
                    int j = i.getAndIncrement();
                    System.out.println(j);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println("当前线程执行了interrupt()方法, 线程从sleep唤醒并产生异常");
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
        System.out.println("启动打印线程");

        System.out.println("主线程休眠1000ms, 自增10次");
        Thread.sleep(1000);
        System.out.println("主线程休眠完毕, 中断打印线程");
        run.set(false);
        thread.interrupt();

        Assertions.assertEquals(10, i.get());
    }
}
