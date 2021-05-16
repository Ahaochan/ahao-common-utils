package moe.ahao.transmit.util;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TransmitContextHolder {
    public static final Logger logger = LoggerFactory.getLogger(TransmitContextHolder.class);
    public static final String HEADER_TOKEN = "token";

    private static ThreadLocal<Map<String, String>> holder = new TransmittableThreadLocal<Map<String, String>>() {
        @Override
        protected Map<String, String> initialValue() {
            return new ConcurrentHashMap<>();
        }
    };

    public static void set(String key, String value) {
        holder.get().put(key, value);
    }

    public static String get(String key) {
        return holder.get().get(key);
    }

    public static String getOrDefault(String key, String defaultValue) {
        return holder.get().getOrDefault(key, defaultValue);
    }

    public static String remove(String key) {
        return holder.get().remove(key);
    }
}
