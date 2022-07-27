package moe.ahao.java.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CyclicBarrier;

class CyclicBarrierTest {
    @Test
    void test() {
        int parties = 5;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(parties, new Runnable() {
            @Override
            public void run() {
                System.out.println("全部抵达栅栏");
            }
        });

        for (int i = 0; i < parties; i++) {
            int index = i;
            new Thread(() -> {
                try {
                    System.out.println("到达栅栏" + index);
                    cyclicBarrier.await();
                    System.out.println("冲破栅栏" + index);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
