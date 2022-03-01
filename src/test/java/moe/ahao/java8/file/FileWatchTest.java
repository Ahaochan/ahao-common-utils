package moe.ahao.java8.file;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class FileWatchTest {
    @Test
    void watch() throws Exception {
        // 注册监听事件
        Path dir = Paths.get("./");
        Path file = Paths.get("./ahao.txt");

        List<WatchEvent.Kind<?>> except = Arrays.asList(
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_MODIFY,
            StandardWatchEventKinds.ENTRY_MODIFY, // 重复回调问题: https://stackoverflow.com/questions/16777869/
            StandardWatchEventKinds.ENTRY_DELETE);

        List<WatchEvent.Kind<?>> actual = new ArrayList<>();

        Thread thread = new Thread(() -> {
            try {
                // 获取文件系统的WatchService对象
                WatchService watchService = FileSystems.getDefault().newWatchService();

                dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

                while (true) {
                    // 获取下一个文件改动事件
                    // WatchKey key = watchService.poll();
                    // WatchKey key = watchService.poll(1, TimeUnit.SECONDS);
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        System.out.println(event.context() + " 文件发生了 " + event.kind() + "事件！" + event.count() + ", " + event.context());
                        actual.add(event.kind());
                    }
                    // 重设WatchKey
                    boolean valid = key.reset();
                    // 如果重设失败，退出监听
                    if (!valid) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Assertions.fail();
            }
        });
        thread.setDaemon(true);
        thread.start();

        Thread.sleep(1000);

        Files.createFile(file);
        Files.write(file, "ahao".getBytes(StandardCharsets.UTF_8));
        Files.delete(file);

        Assertions.assertIterableEquals(except, actual);
    }
}
