package moe.ahao.util.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

class CloneHelperTest {

    @Test
    void testDateClone() {
        Date now = new Date();
        Date clone = CloneHelper.clone(now);
        Assertions.assertNotSame(now, clone);
        Assertions.assertEquals(now.getTime(), clone.getTime());
    }

    @Test
    void testNullClone() {
        Object null1 = null;
        Object null2 = CloneHelper.clone(null1);
        Assertions.assertSame(null1, null2);
    }

    @Test
    void testStringClone() {
        String str1 = "hello";
        String str2 = CloneHelper.clone(str1);
        Assertions.assertNotSame(str1, str2);

        boolean eq = true;
        char[] char1 = str1.toCharArray(), char2 = str2.toCharArray();
        for(int i = 0, len = char1.length; i < len && eq; i++) {
            if(char1[i] != char2[i]) {
                eq = false;
            }
        }
        Assertions.assertTrue(eq);
    }


}
