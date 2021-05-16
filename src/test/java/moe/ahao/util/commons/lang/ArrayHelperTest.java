package moe.ahao.util.commons.lang;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ArrayHelperTest {

    @Test
    void testToArray() {
        List<String> list = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            list.add("测试:"+i);
        }
        String[] array = ArrayHelper.toArray(list);
        for(int i = 0; i < 100; i++){
            Assertions.assertEquals(("测试:" + i), array[i]);
        }

        String[] subArray = ArrayHelper.toArray(list, 10, 5);
        for(int i = 0; i < 5; i++){
            Assertions.assertEquals(("测试:" + (10 + i)), subArray[i]);
        }

//        Integer[] intArray = ArrayHelper.toArray(0,1,2,3,4,5,6,7,8,9);
//        for(int i = 0; i <= 9; i++){
//            Assert.assertTrue(i == intArray[i]);
//        }
    }

    @Test
    void testLength() {
        Assertions.assertEquals(10, ArrayHelper.length(0,1,2,3,4,5,6,7,8,9));
        Assertions.assertEquals(2, ArrayHelper.length(null, null));
        Assertions.assertEquals(1, ArrayHelper.length(1));
        Assertions.assertEquals(0, ArrayHelper.length(null));
        Assertions.assertEquals(0, ArrayHelper.length());
    }
}
