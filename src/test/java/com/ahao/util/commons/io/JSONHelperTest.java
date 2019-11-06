package com.ahao.util.commons.io;

import com.ahao.domain.entity.AjaxDTO;
import com.ahao.domain.entity.MybatisPlusBaseDO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class JSONHelperTest {
    @Test
    public void parse() {
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

    @Test
    public void parseList() {
        List<MybatisPlusBaseDO> list1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MybatisPlusBaseDO entity = new MybatisPlusBaseDO();
            entity.setId((long) i);
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            list1.add(entity);
        }

        String json = JSONHelper.toString(list1);
        System.out.println(json);
        Assertions.assertNotNull(json);
        Assertions.assertNotEquals("", json);

        List<MybatisPlusBaseDO> list2 = JSONHelper.parseList(json, MybatisPlusBaseDO.class);
        Assertions.assertNotNull(list2);
        Assertions.assertFalse(list2.isEmpty());
        Assertions.assertEquals(list1, list2);
    }

    @Test
    public void parseMap() {
        HashMap<Integer, MybatisPlusBaseDO> map1 = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            MybatisPlusBaseDO entity = new MybatisPlusBaseDO();
            entity.setId((long) i);
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            map1.put(i, entity);
        }

        String json = JSONHelper.toString(map1);
        System.out.println(json);
        Assertions.assertNotNull(json);
        Assertions.assertNotEquals("", json);

        Map<Integer, MybatisPlusBaseDO> map2 = JSONHelper.parseMap(json, Integer.class, MybatisPlusBaseDO.class);
        Assertions.assertNotNull(map2);
        Assertions.assertFalse(map2.isEmpty());
        Assertions.assertEquals(map1, map2);
    }

    @Test
    public void getNode() {
        // {"status1":1,"message1":"message1","deep1":{"status2":2,"message2":"message2","deep2":[1,2]}}
        String json = "{\"status1\":1,\"message1\":\"message1\",\"deep1\":{\"status2\":2,\"message2\":\"message2\",\"deep2\":[1,2]}}";
        System.out.println(json);

        String message1 = JSONHelper.getString(json, "message1");
        String message2 = JSONHelper.getString(json, "deep1.message2");
        String message3 = JSONHelper.getString(json, "deep1.message3");
        Assertions.assertEquals("message1", message1);
        Assertions.assertEquals("message2", message2);
        Assertions.assertEquals("", message3);

        String data1 = JSONHelper.getString(json, "deep1.deep2.0");
        String data2 = JSONHelper.getString(json, "deep1.deep2.1");
        Assertions.assertEquals("1", data1);
        Assertions.assertEquals("2", data2);
        int int1 = JSONHelper.getInt(json, "deep1.deep2.0");
        int int2 = JSONHelper.getInt(json, "deep1.deep2.1");
        Assertions.assertEquals(1, int1);
        Assertions.assertEquals(2, int2);
    }
}
