package moe.ahao.java8.file;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Paths;

class FileSystemTest {
    @Test
    void store() throws Exception {
        FileStore store = Files.getFileStore(Paths.get("."));
        Assertions.assertNotNull(store);
        // 判断C盘的总空间，可用空间
        System.out.println("磁盘名称: " + store.name());
        System.out.println("共有空间大小: " + store.getTotalSpace() + "bytes");
        System.out.println("可用空间大小: " + store.getUsableSpace() + "bytes");
        System.out.println("不可用空间大小: " + store.getUnallocatedSpace());
        System.out.println("是否只读: " + store.isReadOnly());
        System.out.println("文件系统类型: " + store.type());
    }
}
