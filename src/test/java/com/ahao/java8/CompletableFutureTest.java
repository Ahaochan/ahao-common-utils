package com.ahao.java8;

import com.alibaba.ttl.threadpool.TtlExecutors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class CompletableFutureTest {
    private ExecutorService executor;

    @BeforeEach
    public void beforeEach() {
        executor = TtlExecutors.getTtlExecutorService(Executors.newCachedThreadPool());
    }

    @AfterEach
    public void afterEach() {
        executor.shutdownNow();
    }

    @Test
    public void test1() throws Exception {
        String msg = "hello world";
        String defaultValue = null;

        // CompletableFuture.completedFuture(msg);
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> msg, executor)
            .thenApplyAsync(String::toUpperCase, executor);
        Assertions.assertNull(cf.getNow(defaultValue));
        String value = cf.get();
        System.out.println("CompletableFuture 处理完毕: " + value);
        Assertions.assertEquals(msg.toUpperCase(), value);
    }

    @Test
    public void whenComplete() throws Exception {
        CompletableFuture<String>[] completableFutures = Stream.of(0, 1, 2, 3, 4, 5, 6, 7)
            .map(s -> "msg" + s)
            .map(s -> CompletableFuture.supplyAsync(() -> s).thenApplyAsync(String::toUpperCase))
            .toArray(value -> (CompletableFuture<String>[]) new CompletableFuture[8]);

        CompletableFuture.allOf(completableFutures)
            .whenComplete((unused, throwable) -> {
                for (int i = 0; i < completableFutures.length; i++) {
                    CompletableFuture<String> f = completableFutures[i];
                    String value = f.getNow(null);
                    System.out.println(value);
                    Assertions.assertEquals("MSG" + i, value);
                }
            });
    }
}
