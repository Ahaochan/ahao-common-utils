package com.ahao.util.commons.io;


import com.ahao.util.spring.SpringContextHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JSONHelper {
    private static final Logger logger = LoggerFactory.getLogger(JSONHelper.class);

    private static ObjectMapper om;

    static {
        if (SpringContextHolder.enable()) {
            om = SpringContextHolder.getBean(ObjectMapper.class, null);
        }
        if (om == null) {
            om = new ObjectMapper();
            JacksonProperties jacksonProperties = new JacksonProperties();
        }
    }

    public static String toString(Object obj) {
        try {
            String json = om.writeValueAsString(obj);
            return json;
        } catch (JsonProcessingException e) {
            logger.error(obj.toString() + " 转为 json 错误.", e);
        }
        return "";
    }

    public static byte[] toBytes(Object obj) {
        try {
            byte[] json = om.writeValueAsBytes(obj);
            return json;
        } catch (JsonProcessingException e) {
            logger.error(obj.toString() + " 转为 json 错误.", e);
        }
        return new byte[0];
    }

    public static <T> T parse(String json, Class<T> clazz) {
        try {
            T obj = om.readValue(json, clazz);
            return obj;
        } catch (IOException e) {
            logger.error(json + " 转为 " + clazz.getName() + " 错误.", e);
        }
        return null;
    }

    public static <T> List<T> parseList(String json, Class<T> clazz) {
        JavaType type = om.getTypeFactory().constructParametricType(List.class, clazz);
        try {
            List<T> list = om.readValue(json, type);
            return list;
        } catch (IOException e) {
            logger.error(json + " 转为 List<" + clazz.getName() + ">错误.", e);
        }
        return Collections.emptyList();
    }

    public static <K, V> Map<K, V> parseMap(String json, Class<K> keyClass, Class<V> valueClass) {
        JavaType type = om.getTypeFactory().constructParametricType(Map.class, keyClass, valueClass);
        try {
            Map<K, V> map = om.readValue(json, type);
            return map;
        } catch (IOException e) {
            logger.error(json + " 转为 Map<" + keyClass.getName() + ", " + valueClass.getName() + ">错误.", e);
        }
        return Collections.emptyMap();
    }

    public static String getString(String json, String key) {
        if (StringUtils.isAnyBlank(json, key)) {
            return "";
        }
        try {
            JsonNode node = om.readTree(json);
            String[] searchNodes = StringUtils.split(key, '.');

            for (String searchNode : searchNodes) {
                if (node instanceof ArrayNode) {
                    node = node.get(Integer.parseInt(searchNode));
                } else {
                    node = node.get(searchNode);
                }
                if (node == null) {
                    return "";
                }
            }
            String value = node.asText();
            return value;
        } catch (IOException e) {
            logger.error(json + " 读取 " + key + " 错误.", e);
        }
        return "";
    }

    public static int getInt(String json, String key) {
        String value = getString(json, key);
        return StringUtils.isNumeric(value) ? Integer.parseInt(value) : 0;
    }

    public static boolean getBoolean(String json, String key) {
        String value = getString(json, key);
        return Boolean.parseBoolean(value);
    }
}
