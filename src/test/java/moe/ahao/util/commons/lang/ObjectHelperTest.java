package moe.ahao.util.commons.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ObjectHelperTest {
    @Test
    public void equalsAny() {
        Assertions.assertTrue(ObjectHelper.equalsAny("a", "b", "a", "c"));
        Assertions.assertFalse(ObjectHelper.equalsAny("a", "b", "b", "c"));

        Assertions.assertFalse(ObjectHelper.notEqualsAny("a", "b", "a", "c"));
        Assertions.assertTrue(ObjectHelper.notEqualsAny("a", "b", "b", "c"));
    }
}
