package moe.ahao.util.commons.lang;

import moe.ahao.domain.entity.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.bind.annotation.*;
import java.util.Map;

class BeanHelperTest {
    @Test
    void cast() {
        Assertions.assertEquals("1", BeanHelper.cast(1, String.class));
        Assertions.assertEquals(1, BeanHelper.cast("1", Integer.class).intValue());
    }

    @Test
    void obj2map() {
        Result<Object> dto1 = Result.success("123");
        Map<String, Object> map1 = BeanHelper.obj2map(dto1);

        Assertions.assertEquals(Result.SUCCESS, map1.get("code"));
        Assertions.assertEquals("123", map1.get("msg"));
        Assertions.assertEquals(null, map1.get("obj"));

        Result<Object> dto2 = BeanHelper.map2obj(map1, Result.class);
        Assertions.assertEquals(dto1.getCode(), dto2.getCode());
        Assertions.assertEquals(dto1.getMsg(), dto2.getMsg());
        Assertions.assertEquals(dto1.getObj(), dto2.getObj());
    }


    @Test
    void obj2xml() {
        Data data1 = new Data(1, "name1", 14);
        String xml = BeanHelper.obj2xml(data1);

        System.out.println(xml);

        Data data2 = BeanHelper.xml2obj(xml, Data.class);
        Assertions.assertNotNull(data2);
        Assertions.assertEquals(data1.id  , data1.id  );
        Assertions.assertEquals(data1.name, data1.name);
        Assertions.assertEquals(data1.age , data1.age );
    }

    @XmlRootElement(name = "rootName", namespace = "rootNamespace")
    @XmlAccessorType(XmlAccessType.FIELD)
    static class Data { // 如果是内部类则必须是静态内部类
        @XmlAttribute(name = "stuId")
        int id;
        @XmlElement(name = "stuName")
        String name;
        @XmlElement(name = "stuAge")
        int age;
        public Data() {}
        public Data(int id, String name, int age) { this.id = id; this.name = name; this.age = age; }
    }

}
