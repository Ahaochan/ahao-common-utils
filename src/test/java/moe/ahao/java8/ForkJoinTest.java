package moe.ahao.java8;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

public class ForkJoinTest {
    @Test
    public void sum() {
        int min = 0, max = 100;
        int sum = IntStream.rangeClosed(min, max).sum();
        System.out.printf("期望的sum为: %d%n", sum);

        // fork/join:
        ForkJoinTask<Integer> task = new SumTask(min, max);
        long startTime = System.currentTimeMillis();
        // invoke 是同步执行, 调用之后需要等待任务完成, 才能执行后面的代码.
        // submit 是异步执行, 只有在 Future 调用 get 的时候会阻塞.
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Integer result = forkJoinPool.invoke(task);
        long endTime = System.currentTimeMillis();
        System.out.printf("Fork/join sum: %d, 耗时: %d ms.%n", result, (endTime - startTime));
    }

    /**
     * 继承 RecursiveTask: 适用于有返回值的场景
     * 继承 RecursiveAction: 适合于没有返回值的场景
     */
    public static class SumTask extends RecursiveTask<Integer> {
        private static final int THRESHOLD = 2;
        private final int min;
        private final int max;

        public SumTask(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        protected Integer compute() {
            if (max - min <= THRESHOLD) {
                // 如果任务足够小,直接计算:
                return IntStream.rangeClosed(min, max).sum();
            } else {
                // 任务太大,一分为二:
                int middle = (max + min) / 2;
                System.out.printf("任务过大, 将任务 %d~%d 拆为两个小任务 %d~%d, %d~%d%n", min, max, min, middle, middle, max);
                SumTask subtask1 = new SumTask(min, middle);
                SumTask subtask2 = new SumTask(middle + 1, max);
                // fork: 让子线程自己去完成任务，父线程监督子线程执行，浪费父线程。
                // invokeAll: 子父线程共同完成任务，可以更好的利用线程池。
                // subtask1.fork();
                // subtask2.fork();
                ForkJoinTask.invokeAll(subtask1, subtask2);
                Integer subResult1 = subtask1.join();
                Integer subResult2 = subtask2.join();
                Integer result = subResult1 + subResult2;
                System.out.printf("任务 %d~%d 结果为: %d+%d==>%d%n", min, max, subResult1, subResult2, result);
                return result;
            }
        }
    }
}
