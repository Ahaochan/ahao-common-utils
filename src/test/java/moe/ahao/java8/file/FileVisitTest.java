package moe.ahao.java8.file;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

class FileVisitTest {
    @Test
    void list() throws Exception{
        File file = new File(".");
        Path path = Paths.get(".");

        File[] files1 = file.listFiles();
        File[] files2 = Files.list(path).map(Path::toFile).toArray(File[]::new);
        File[] files3 = Files.walk(path, 1).filter(p -> !path.equals(p)).map(Path::toFile).toArray(File[]::new);
        Assertions.assertArrayEquals(files1, files2);
        Assertions.assertArrayEquals(files1, files3);
    }

    @Test
    void walk() throws Exception{
        Files.walk(Paths.get("."))
            .forEach(System.out::println);
    }

    @Test
    void walkFileTree() throws Exception{
        Files.walkFileTree(Paths.get("."), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                StringBuilder sb = new StringBuilder("|");
                for (int i = 0; i < dir.getNameCount(); i++) {
                    sb.append("——");
                }
                sb.append(dir.getFileName().toString());
                System.out.println("访问子目录之前触发该方法: \t\t" + sb);
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                StringBuilder sb = new StringBuilder("|");
                for (int i = 0; i < file.getNameCount(); i++) {
                    sb.append("——");
                }
                sb.append(file.getFileName().toString());
                System.out.println("访问 file 文件时触发该方法: \t" + sb);
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                StringBuilder sb = new StringBuilder("|");
                for (int i = 0; i < file.getNameCount(); i++) {
                    sb.append("——");
                }
                sb.append(file.getFileName().toString());
                System.out.println("访问 file 文件失败时触发该方法: \t" + sb);
                return super.visitFileFailed(file, exc);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                StringBuilder sb = new StringBuilder("|");
                for (int i = 0; i < dir.getNameCount(); i++) {
                    sb.append("——");
                }
                sb.append(dir.getFileName().toString());
                // System.out.println("访问子目录之后触发该方法: \t\t" + sb);
                return super.postVisitDirectory(dir, exc);
            }
        });
    }
}
