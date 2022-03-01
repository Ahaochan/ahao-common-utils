package moe.ahao.java8.file;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class PathTest {
    @Test
    void constructor() throws Exception {
        // 以当前路径来创建Path对象
        Path path = Paths.get(".");
        System.out.println(path);
        System.out.println("path的根路径：" + path.getRoot());
        System.out.println("path的路径深度：" + path.getNameCount());
        // 获取path对应的绝对路径。
        Path absolutePath = path.toAbsolutePath();
        System.out.println(absolutePath);
        System.out.println("absolutePath的根路径：" + absolutePath.getRoot());
        System.out.println("absolutePath的路径深度：" + absolutePath.getNameCount());
        System.out.println("absolutePath的第3层级：" + absolutePath.getName(3));
        // 以多个String来构建Path对象
        Path path2 = Paths.get("D:", "publish", "codes");
        System.out.println(path2);
    }

    @Test
    void writeRead() throws Exception {
        // 写入
        List<String> data = Arrays.asList("1", "2", "3");
        Path path1 = Paths.get("ahao.txt");
        Files.write(path1, data, StandardCharsets.UTF_8);

        // API
        boolean hidden = Files.isHidden(path1);
        System.out.println("是否为隐藏文件：" + hidden);
        System.out.println("文件大小为：" + Files.size(path1));
        Assertions.assertFalse(hidden);

        // 复制文件
        Path path2 = Paths.get("ahao.bak");
        Files.copy(path1, path2, StandardCopyOption.REPLACE_EXISTING);

        // 读取
        List<String> lines1 = Files.readAllLines(path1, StandardCharsets.UTF_8);
        Assertions.assertIterableEquals(data, lines1);
        List<String> lines2 = Files.lines(path2, StandardCharsets.UTF_8).collect(Collectors.toList());
        Assertions.assertIterableEquals(data, lines2);

        // 删除
        Files.delete(path1);
        Files.delete(path2);
    }

    @Test
    void convert() {
        File file = new File(".");
        Path path = Paths.get(".");

        Assertions.assertEquals(file, path.toFile());
        Assertions.assertEquals(path, file.toPath());
    }
}
