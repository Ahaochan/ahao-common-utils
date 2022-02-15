package moe.ahao.java.lang.thread;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class ThreadConfitionTest {
    @Test
    void test() throws Exception {
        int len = 20;
        List<String> actual = new ArrayList<>();

        AhaoQueue queue = new AhaoQueue();
        new Thread(() -> {
            for (int i = 0; i < len; i++) {
                queue.offer("element" + i);
            }
        }).start();
        new Thread(() -> {
            for (int i = 0; i < len; i++) {
                String element = queue.take();
                actual.add(element);
            }
        }).start();

        Thread.sleep(1000);
        for (int i = 0; i < len; i++) {
            Assertions.assertEquals("element" + i, actual.get(i));
        }
    }

    static class AhaoQueue {
        ReentrantLock lock = new ReentrantLock(true);
        Condition condition = lock.newCondition();
        LinkedList<String> queue = new LinkedList<>();

        public void offer(String element) {
            lock.lock();

            try {
                if (queue.size() >= 10) {
                    System.out.println(Thread.currentThread().getName() + "队列长度满了, 暂停入队, 阻塞当前线程并释放锁");
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "线程唤醒, 继续入队");
                }
                queue.addLast(element);
                System.out.println(Thread.currentThread().getName() + "入队: " + element);
                condition.signal();
            } finally {
                lock.unlock();
            }
        }

        public String take() {
            lock.lock();

            try {
                if (queue.size() <= 0) {
                    System.out.println(Thread.currentThread().getName() + "队列长度为空, 暂停出队, 阻塞当前线程并释放锁");
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "线程唤醒, 继续出队");
                }
                String element = queue.removeFirst();
                System.out.println(Thread.currentThread().getName() + "出队: " + element);
                condition.signal();
                return element;
            } finally {
                lock.unlock();
            }
        }
    }
}
