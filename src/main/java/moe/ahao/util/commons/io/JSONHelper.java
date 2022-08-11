package moe.ahao.util.commons.io;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import moe.ahao.util.spring.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

public class JSONHelper {
    private static final Logger logger = LoggerFactory.getLogger(JSONHelper.class);

    // ======================================== 依赖 ==================================================
    private volatile static ObjectMapper om;

    public static ObjectMapper getOm() {
        if (om == null) {
            synchronized (JSONHelper.class) {
                if (om == null) {
                    if (SpringContextHolder.enable()) {
                        om = SpringContextHolder.getBean(ObjectMapper.class, JSONHelper.withDefault());
                    } else {
                        om = JSONHelper.withDefault();
                    }
                }
            }
        }
        return om;
    }
    public static ObjectMapper withDefault() {
        ObjectMapper om = new ObjectMapper();
        om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        om.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        om.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);

        // serialization
        om.disable(INDENT_OUTPUT, FAIL_ON_EMPTY_BEANS);

        // deserialization
        om.enable(ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        om.disable(FAIL_ON_UNKNOWN_PROPERTIES);
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
            String value = node.asText();
            if (StringUtils.isEmpty(value)) {
                value = node.toString();
            }
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

    public static String mergeJSON(String... jsonArray) {
        ObjectNode root = getOm().createObjectNode();
        for (String json : jsonArray) {
            try {
                ObjectNode jsonNode = (ObjectNode) getOm().readTree(json);
                root.setAll(jsonNode);
            } catch (JsonProcessingException e) {
                logger.error("json处理合并失败, json:{}", json, e);
            }
        }
        return root.toString();
    }
}
