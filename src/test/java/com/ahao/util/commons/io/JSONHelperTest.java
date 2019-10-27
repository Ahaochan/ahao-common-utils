package com.ahao.util.commons.io;

import com.ahao.domain.entity.AjaxDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class JSONHelperTest {
    @Test
    public void test() {
        AjaxDTO entity1 = AjaxDTO.get(1, "hello", Arrays.asList(1, 2, 3));

        String json1 = JSONHelper.toString(entity1);
        System.out.println(json1);
        Assertions.assertNotNull(json1);
        Assertions.assertNotEquals("", json1);

        byte[] bytes = JSONHelper.toBytes(entity1);
        String json2 = new String(bytes, StandardCharsets.UTF_8);
        System.out.println(json2);
        Assertions.assertNotNull(json2);
        Assertions.assertNotEquals("", json2);
        Assertions.assertEquals(json1, json2);

        AjaxDTO entity2 = JSONHelper.parse(json1, AjaxDTO.class);
        Assertions.assertNotNull(entity2);
        Assertions.assertEquals(entity1.getResult(), entity2.getResult());
        Assertions.assertEquals(entity1.getMsg(), entity2.getMsg());
        Assertions.assertEquals(entity1.getObj(), entity2.getObj());
    }
}
