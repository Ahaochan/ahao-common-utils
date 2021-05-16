package moe.ahao.convert;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class YamlPropertiesTest {
    @Test
    public void yaml2Prop() throws Exception {
        List<Resource> resources = Arrays.asList(
            new ClassPathResource("yml/test1.yml"),
            new ClassPathResource("yml/test2.yml")
        );

        Map<String, String> data = new LinkedHashMap<>();
        YAMLMapper readOM = new YAMLMapper();
        JavaPropsMapper writeOM = new JavaPropsMapper();
        for (Resource resource : resources) {
            Object obj = readOM.readValue(resource.getFile(), Object.class);
            writeOM.writeValue(data, obj);
        }

        data.forEach((k, v) -> System.out.println(k + " = " + v));
        Assertions.assertEquals("test2", data.get("spring.profiles.active"));
        Assertions.assertEquals("ahao-common-utils", data.get("spring.application.name"));
        Assertions.assertEquals("yyyy-MM-dd HH:mm:ss:SSS", data.get("spring.jackson.date-format"));
        Assertions.assertEquals("GMT+8", data.get("spring.jackson.time-zone"));
        Assertions.assertEquals("zh_CN", data.get("spring.jackson.locale"));
    }

    @Test
    public void prop2yaml() throws Exception {
        List<Resource> resources = Arrays.asList(
            new ClassPathResource("properties/test1.properties"),
            new ClassPathResource("properties/test2.properties")
        );

        Map<String, Map> data = new LinkedHashMap<>();

        JavaPropsMapper readOM = new JavaPropsMapper();
        YAMLMapper writeOM = new YAMLMapper();

        for (Resource resource : resources) {
            JavaType type = readOM.getTypeFactory().constructParametricType(Map.class, String.class, Map.class);
            Map<String, Map> map = readOM.readValue(resource.getFile(), type);
            data.putAll(map);
        }

        String yml = writeOM.writeValueAsString(data);
        System.out.println(yml);
        Assertions.assertTrue(StringUtils.isNotBlank(yml));
    }
}
