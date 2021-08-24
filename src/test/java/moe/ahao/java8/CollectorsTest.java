package moe.ahao.java8;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class CollectorsTest {
    private int size = 100000;
    private List<Integer> list;

    @BeforeEach
    public void beforeEach() {
        list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
    }


    @Test
    public void toMap() {
        TreeMap<Integer, String> map1 = list.stream()
            .collect(Collectors.toMap(i -> i, i -> "name" + i,
                // (u, v) -> u.compareTo(v) > 0 ? u : v,
                BinaryOperator.maxBy(String::compareTo),
                TreeMap::new));
        TreeMap<Integer, String> map2 = list.parallelStream()
            .collect(Collectors.toMap(i -> i, i -> "name" + i,
                // (u, v) -> u.compareTo(v) > 0 ? u : v,
                BinaryOperator.maxBy(String::compareTo),
                TreeMap::new));

        Assertions.assertEquals(size, map1.size());
        Assertions.assertEquals(size, map2.size());
        System.out.println(map1);
    }

    @Test
    public void toMapBug() {
        // https://bugs.openjdk.java.net/browse/JDK-8148463
        Map<String, String> m = new HashMap<>();
        m.put("A", "a");
        m.put("B", "b");
        m.put("C", "c");
        Map<String, String> map1 = m.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Assertions.assertEquals(m.size(), map1.size());

        // 1. toMap的value不能为null
        m.put("D", null);
        Assertions.assertThrows(NullPointerException.class, () ->
                m.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
            , "toMap的value不能为null");
        m.remove("D");

        // 2. toMap的key可以为null
        m.put(null, "e");
        Map<String, String> map2 = m.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Assertions.assertEquals("e", map2.get(null));
    }

    @Test
    public void counting() {
        // Long count = list.stream().map(i -> "name" + i).collect(Collectors.counting());
        Long count1 = list.stream().map(i -> "name" + i).count();
        Long count2 = list.parallelStream().map(i -> "name" + i).count();
        Assertions.assertEquals(size, count1);
        Assertions.assertEquals(size, count2);
        System.out.println(count1);
    }

    @Test
    public void groupingBy() {
        int num = 3;
        TreeMap<Integer, List<Integer>> map1 = list.stream().collect(Collectors.groupingBy(i -> i % num, TreeMap::new, Collectors.toList()));
        TreeMap<Integer, List<Integer>> map2 = list.parallelStream().collect(Collectors.groupingBy(i -> i % num, TreeMap::new, Collectors.toList()));

        Assertions.assertEquals(num, map1.size());
        Assertions.assertEquals(num, map2.size());
        System.out.println(map1);
    }

    @Test
    public void groupingByBug() {
        int num = 3;
        Assertions.assertThrows(NullPointerException.class, () ->
                list.stream().collect(Collectors.groupingBy(i -> i % num == 0 ? null : i % num))
            , "groupingBy的key不能为null");
    }

    @Test
    public void partitioningBy() {
        Map<Boolean, List<Integer>> map1 = list.stream().collect(Collectors.partitioningBy(i -> i % 2 == 0, Collectors.toList()));
        Map<Boolean, List<Integer>> map2 = list.parallelStream().collect(Collectors.partitioningBy(i -> i % 2 == 0, Collectors.toList()));

        Assertions.assertEquals(2, map1.size());
        Assertions.assertEquals(2, map2.size());
        System.out.println(map1);
    }

    @Test
    public void collectingAndThen() {
        int num = 3;
        Integer num1 = list.stream().collect(Collectors.collectingAndThen(Collectors.groupingBy(i -> i % num), Map::size));
        Integer num2 = list.parallelStream().collect(Collectors.collectingAndThen(Collectors.groupingBy(i -> i % num), Map::size));

        Assertions.assertEquals(num, num1);
        Assertions.assertEquals(num, num2);
        System.out.println(num1);
    }

    @Test
    public void mapping() {
        // List<String> list1 = list.stream().map(i -> "name" + i).collect(Collectors.toList());
        List<String> list1 = list.stream().collect(Collectors.mapping(i -> "name" + i, Collectors.toList()));
        List<String> list2 = list.parallelStream().collect(Collectors.mapping(i -> "name" + i, Collectors.toList()));

        Assertions.assertEquals(list1.size(), list2.size());
        System.out.println(list1);
    }

    @Test
    public void reduce() {
        Integer num1 = list.stream().reduce(0, (sum, i) -> sum + i, (i, j) -> i + j);
        Integer num2 = list.parallelStream().reduce(0, (sum, i) -> sum + i, (i, j) -> i + j);
        Assertions.assertEquals(num1, num2);
        System.out.println(num1);
    }
}
