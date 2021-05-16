package moe.ahao.util.commons.lang;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class CollectionHelperTest {
    @Test
    void add() {
        Assertions.assertFalse(CollectionHelper.add(null, "1", "2"));
        Assertions.assertTrue(CollectionHelper.add(Stream.of("array").collect(Collectors.toList()), "1", "2"));
        Assertions.assertFalse(CollectionHelper.add(Collections.singletonList("array")));
    }

    @Test
    void addToLinkedHashMap() {
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < 10; i++) {
            map.put(i, i + 1000);
        }
        CollectionHelper.add(map, 2, 888, 8888);
        CollectionHelper.add(map, -2, 999, 999);
        int i = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getKey() == 888) {
                Assertions.assertEquals(2, i);
            }
            if (entry.getKey() == 999) {
                Assertions.assertEquals(map.size() - 2, i);
            }
            i++;
        }
    }

    @Test
    void subList() {
        List<String> list = CollectionHelper.toList("1", "2", "3", "4");

        Assertions.assertEquals(0, CollectionHelper.subList(null, 0, 999).size());
        Assertions.assertEquals(4, CollectionHelper.subList(list, -1, 999).size());
        Assertions.assertEquals(2, CollectionHelper.subList(list, 0, 2).size());
        Assertions.assertEquals(2, CollectionHelper.subList(list, -1, 2).size());
        Assertions.assertEquals(4, CollectionHelper.subList(list, 0, 999).size());
    }


    @Test
    void contains() {
        List<String> list = CollectionHelper.toList("1", "2", "3", "4");

        Assertions.assertFalse(CollectionHelper.contains(null, "1"));
        Assertions.assertFalse(CollectionHelper.contains(list, "5"));
        Assertions.assertTrue (CollectionHelper.contains(list, "1"));

        Assertions.assertFalse(CollectionHelper.containAny(null, "1", null));
        Assertions.assertFalse(CollectionHelper.containAny(list, "5", "6"));
        Assertions.assertTrue (CollectionHelper.containAny(list, "1", "5"));
    }


    @Test
    void retain() {
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            final int j = i;
            list.add(Stream.of("1", "2", "3").collect(Collectors.toMap(s -> "key" + s, s -> "value" + s + j)));
        }
        Assertions.assertEquals(0, CollectionHelper.retain(null).size());

        List<Map<String, String>> retain = CollectionHelper.retain(list, "key1", "key3");
        for (Map<String, String> item : retain) {
            Assertions.assertEquals(2, item.keySet().size());
            Assertions.assertTrue(item.keySet().contains("key1"));
            Assertions.assertFalse(item.keySet().contains("key2"));
            Assertions.assertTrue(item.keySet().contains("key3"));
        }
    }

    @Test
    void getFirst() {
        List<String> list = CollectionHelper.toList("1", "2", "3", "4");

        Assertions.assertNull(CollectionHelper.getFirst(null));
        Assertions.assertEquals("1", CollectionHelper.getFirst(list));
    }
}
