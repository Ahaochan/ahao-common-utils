package moe.ahao.java;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class VolatileTest {
    @Test
    public void test1() throws Exception{
        WriteThread1 write = new WriteThread1();
        ReadThread1 read = new ReadThread1();
        write.start();
        read.start();
        System.out.println("启动1、2线程");

        System.out.println("主线程休眠2000ms");
        Thread.sleep(2000);
        System.out.println("主线程休眠完毕, 中断打印线程");
        write.interrupt();
        read.interrupt();

        System.out.println("read线程不能和write线程的值达成一致:" + read.local + "," + write.local);
        Assertions.assertEquals(Var.var1, write.local);
        Assertions.assertNotEquals(Var.var1, read.local);

    }
    @Test
    public void test2() throws Exception{
        WriteThread2 write = new WriteThread2();
        ReadThread2 read = new ReadThread2();
        write.start();
        read.start();
        System.out.println("启动1、2线程");

        System.out.println("主线程休眠2000ms");
        Thread.sleep(2000);
        System.out.println("主线程休眠完毕, 中断打印线程");
        write.interrupt();
        read.interrupt();

        System.out.println("read线程和write线程的值达成一致:" + read.local + "," + write.local);
        Assertions.assertEquals(Var.var2, write.local);
        Assertions.assertEquals(Var.var2, read.local);

    }

    static class Var {
        public static int var1 = 0;
        public volatile static int var2 = 0;
    }
    static class ReadThread1 extends Thread {
        int local = 0;
        @Override
        public void run() {
            local = Var.var1;
            // 这里不能用volatile变量跳出循环, 也不能用Atomic原子类型, 也不能用Thread.interrupted()
            // 否则会因为cache line, 把要验证的变量一起刷新了
            while(true) {
                if(local != Var.var1) {
                    System.out.println("读取到了修改后的标志位：" + Var.var1);
                    local = Var.var1;
                }
            }
        }
    }
    static class WriteThread1 extends Thread {
        int local = 0;
        @Override
        public void run() {
            local = Var.var1;
            // 这里不能用volatile变量跳出循环, 也不能用Atomic原子类型, 也不能用Thread.interrupted()
            // 否则会因为cache line, 把要验证的变量一起刷新了
            while(Var.var1 < 10) {
                ++local;
                Var.var1 = local;
                System.out.println("标志位被修改为了：" + local);
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class ReadThread2 extends Thread {
        int local = 0;
        @Override
        public void run() {
            local = Var.var2;
            // 这里不能用volatile变量跳出循环, 也不能用Atomic原子类型, 也不能用Thread.interrupted()
            // 否则会因为cache line, 把要验证的变量一起刷新了
            while(true) {
                if(local != Var.var2) {
                    System.out.println("读取到了修改后的标志位：" + Var.var2);
                    local = Var.var2;
                }
            }
        }
    }
    static class WriteThread2 extends Thread {
        int local = 0;
        @Override
        public void run() {
            local = Var.var2;
            // 这里不能用volatile变量跳出循环, 也不能用Atomic原子类型, 也不能用Thread.interrupted()
            // 否则会因为cache line, 把要验证的变量一起刷新了
            while(Var.var2 < 10) {
                ++local;
                Var.var2 = local;
                System.out.println("标志位被修改为了：" + local);
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
