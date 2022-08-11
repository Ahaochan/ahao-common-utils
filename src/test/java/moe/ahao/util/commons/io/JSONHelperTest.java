package moe.ahao.util.commons.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.Getter;
import lombok.Setter;
import moe.ahao.domain.entity.BaseDO;
import moe.ahao.domain.entity.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class JSONHelperTest {
    @Test
    public void parse() {
        Result<List<Integer>> entity1 = Result.get(1, "hello", Arrays.asList(1, 2, 3));

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

        Result<List<Integer>> entity2 = JSONHelper.parse(json1, new TypeReference<Result<List<Integer>>>() {});
        Assertions.assertNotNull(entity2);
        Assertions.assertEquals(entity1.getCode(), entity2.getCode());
        Assertions.assertEquals(entity1.getMsg(), entity2.getMsg());
        Assertions.assertEquals(entity1.getObj(), entity2.getObj());
    }

    @Test
    public void parseList() {
        List<BaseDO> list1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            BaseDO entity = new BaseDO();
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            list1.add(entity);
        }

        String json = JSONHelper.toString(list1);
        System.out.println(json);
        Assertions.assertNotNull(json);
        Assertions.assertNotEquals("", json);

        List<BaseDO> list2 = JSONHelper.parse(json, new TypeReference<List<BaseDO>>() {});
        Assertions.assertNotNull(list2);
        Assertions.assertEquals(list1.size(), list2.size());
    }

    @Test
    public void parseMap() {
        HashMap<Integer, BaseDO> map1 = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            BaseDO entity = new BaseDO();
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            map1.put(i, entity);
        }

        String json = JSONHelper.toString(map1);
        System.out.println(json);
        Assertions.assertNotNull(json);
        Assertions.assertNotEquals("", json);

        Map<Integer, BaseDO> map2 = JSONHelper.parse(json, new TypeReference<Map<Integer, BaseDO>>() {});
        Assertions.assertNotNull(map2);
        Assertions.assertEquals(map1.size(), map2.size());
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

    @Test
    void noDefaultConstructor() throws Exception {
        // 1. 没有无参构造函数的对象的序列化
        ObjectMapper om1 = new ObjectMapper();
        NoDefaultConstructor data1 = new NoDefaultConstructor("hello");
        String json = om1.writeValueAsString(data1);
        System.out.println(json);
        Assertions.assertNotNull(json);

        // 2. 没有无参构造函数的对象的反序列化, 报错cannot deserialize from Object value (no delegate- or property-based Creator)
        Assertions.assertThrows(MismatchedInputException.class, () -> om1.readValue(json, NoDefaultConstructor.class)).printStackTrace();

        // 3. 没有无参构造函数的对象的反序列化, 使用MinIn解决
        ObjectMapper om2 = new ObjectMapper();
        om2.addMixIn(NoDefaultConstructor.class, NoDefaultConstructorCrator.class);
        NoDefaultConstructor data2 = om2.readValue(json, NoDefaultConstructor.class);
        System.out.println(data2);
        Assertions.assertEquals(data1.getData(), data2.getData());
    }

    private static abstract class NoDefaultConstructorCrator {
        @JsonCreator
        public NoDefaultConstructorCrator(@JsonProperty("data") String data) {
        }
    }

    private static class NoDefaultConstructor {
        @Getter @Setter
        private String data;
        public NoDefaultConstructor(String data) {
            this.data = data;
        }
    }

    @Test
    void mergeJSON() {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("key1", 1);
        map1.put("key2", "hhh");

        Map<String, Object> map2 = new HashMap<>();
        map1.put("key3", 1);
        map1.put("key4", "hhh");

        String mergeJSON = JSONHelper.mergeJSON(JSONHelper.toString(map1), JSONHelper.toString(map2));
        System.out.println(mergeJSON);
        Map<String, Object> map3 = JSONHelper.parse(mergeJSON, new TypeReference<Map<String, Object>>() {});

        for (Map.Entry<String, Object> entry : map1.entrySet()) {
            Assertions.assertEquals(entry.getValue(), map3.get(entry.getKey()));
        }
        for (Map.Entry<String, Object> entry : map2.entrySet()) {
            Assertions.assertEquals(entry.getValue(), map3.get(entry.getKey()));
        }
    }
}
