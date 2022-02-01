package moe.ahao.java;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Objects;

class HashMapTest {
    static class SameHash {
        String key;
        public SameHash(String key) {
            this.key = key;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SameHash sameHash = (SameHash) o;
            return Objects.equals(key, sameHash.key);
        }
        @Override
        public int hashCode() {
            return 1;
        }
    }

    @Test
    void test() {
        HashMap<SameHash, Integer> map = new HashMap<>(16);
        for (int i = 0; i < 10; i++) {
            map.put(new SameHash("key" + i), i);
        }
        Assertions.assertEquals(10, map.size());
    }
}
