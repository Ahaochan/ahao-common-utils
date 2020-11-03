package com.ahao.java8;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoinTest {
    @Test
    public void sum() {
        long[] array = new long[2000];
        long sum = 0;
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
            sum += array[i];
        }
        System.out.printf("期望的sum为: %d%n", sum);

        // fork/join:
        ForkJoinTask<Long> task = new SumTask(array, 0, array.length);
        long startTime = System.currentTimeMillis();
        // invoke 是同步执行, 调用之后需要等待任务完成, 才能执行后面的代码.
        // submit 是异步执行, 只有在 Future 调用 get 的时候会阻塞.
        Long result = ForkJoinPool.commonPool().invoke(task);
        long endTime = System.currentTimeMillis();
        System.out.printf("Fork/join sum: %d, 耗时: %d ms.%n", result, (endTime - startTime));
    }

    /**
     * 继承 RecursiveTask: 适用于有返回值的场景
     * 继承 RecursiveAction: 适合于没有返回值的场景
     */
    public static class SumTask extends RecursiveTask<Long> {
        private static final int THRESHOLD = 500;
        private final long[] array;
        private final int start;
        private final int end;

        public SumTask(long[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if (end - start <= THRESHOLD) {
                // 如果任务足够小,直接计算:
                long sum = 0;
                for (int i = start; i < end; i++) {
                    sum += this.array[i];
                }
                return sum;
            } else {
                // 任务太大,一分为二:
                int middle = (end + start) / 2;
                System.out.printf("任务过大, 将任务 %d~%d 拆为两个小任务 %d~%d, %d~%d%n", start, end, start, middle, middle, end);
                SumTask subtask1 = new SumTask(this.array, start, middle);
                SumTask subtask2 = new SumTask(this.array, middle, end);
                // fork: 让子线程自己去完成任务，父线程监督子线程执行，浪费父线程。
                // invokeAll: 子父线程共同完成任务，可以更好的利用线程池。
                ForkJoinTask.invokeAll(subtask1, subtask2);
                Long subResult1 = subtask1.join();
                Long subResult2 = subtask2.join();
                Long result = subResult1 + subResult2;
                System.out.printf("任务 %d~%d 结果为: %d+%d==>%d%n", start, end, subResult1, subResult2, result);
                return result;
            }
        }
    }
}
