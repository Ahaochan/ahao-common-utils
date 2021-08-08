package moe.ahao.java8;

import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadTest {
    public class AhaoNumber {
        Lock lock = new ReentrantLock();
        Condition[] conditions;

        volatile int number = 0;
        int max;

        public AhaoNumber(int threadCount, int max) {
            this.max = max;
            this.conditions = new Condition[threadCount];
            for (int i = 0; i < threadCount; i++) {
                this.conditions[i] = this.lock.newCondition();
            }
        }

        public void print(int i) {
            while (number < max) {
                lock.lock();
                try {
                    int threadCount = conditions.length;
                    int index = i % threadCount;
                    int nextIndex = (i + 1) % threadCount;

                    while (number % threadCount != i) {//注意这里是不等于0，也就是说没轮到该线程执行，之前一直等待状态
                        conditions[index].await(); //该线程将会释放lock锁，构造成节点加入等待队列并进入等待状态
                    }
                    if(number >= max) {
                        break;
                    }
                    number++;
                    System.out.println(Thread.currentThread().getName() + "打印" + number);

                    conditions[nextIndex].signal(); // 执行完唤醒下一个线程
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    @Test
    public void printNumTwoThread() {
        int threadCount = 3;

        AhaoNumber number = new AhaoNumber(threadCount, 100);

        for (int i = 0; i < threadCount; i++) {
            final int finalI = i;
            new Thread(() -> number.print(finalI), "线程" + (char) ('A' + finalI)).start();
        }

        while (number.number < 100) {
        }
    }
}
