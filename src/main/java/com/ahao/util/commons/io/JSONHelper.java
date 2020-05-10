package com.ahao.util.commons.io;


import com.ahao.util.spring.SpringContextHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JSONHelper {
    private static final Logger logger = LoggerFactory.getLogger(JSONHelper.class);

    // ======================================== 依赖 ==================================================
    private volatile static ObjectMapper om;
    public static ObjectMapper getOm() {
        if(om == null) {
            synchronized (JSONHelper.class) {
                if(om == null) {
                    if (SpringContextHolder.enable()) {
                        om = SpringContextHolder.getBean(ObjectMapper.class, new ObjectMapper());
                    } else {
                        om = new ObjectMapper();
                    }
                }
            }
        }
        return om;
    }
    // ======================================== 依赖 ==================================================

    static {

    }

    public static String toString(Object obj) {
        try {
            String json = getOm().writeValueAsString(obj);
            return json;
        } catch (JsonProcessingException e) {
            logger.error(obj.toString() + " 转为 json 错误.", e);
        }
        return "";
    }

    public static byte[] toBytes(Object obj) {
        try {
            byte[] json = getOm().writeValueAsBytes(obj);
            return json;
        } catch (JsonProcessingException e) {
            logger.error(obj.toString() + " 转为 json 错误.", e);
        }
        return new byte[0];
    }

    public static <T> T parse(String json, Class<T> clazz) {
        try {
            T obj = getOm().readValue(json, clazz);
            return obj;
        } catch (IOException e) {
            logger.error(json + " 转为 " + clazz.getName() + " 错误.", e);
        }
        return null;
    }

    public static <T> T parse(String json, TypeReference<T> typeReference) {
        try {
            T obj = getOm().readValue(json, typeReference);
            return obj;
        } catch (IOException e) {
            logger.error(json + " 转为 " + typeReference.getType() + " 错误.", e);
        }
        return null;
    }

    public static String getString(String json, String key) {
        if (StringUtils.isAnyBlank(json, key)) {
            return "";
        }
        try {
            JsonNode node = getOm().readTree(json);
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
            String value = node.toString();
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
