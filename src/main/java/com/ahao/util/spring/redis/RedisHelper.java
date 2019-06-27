package com.ahao.util.spring.redis;

import com.ahao.util.commons.lang.BeanHelper;
import com.ahao.util.spring.SpringContextHolder;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RedisHelper {
    private volatile static RedisTemplate<String, Object> redisTemplate;
    public static RedisTemplate<String, Object> getRedisTemplate() {
        if(redisTemplate == null) {
            synchronized (RedisHelper.class) {
                if(redisTemplate == null) {
                    RedisHelper.redisTemplate = SpringContextHolder.getBean("redisTemplate");
                }
            }
        }
        return redisTemplate;
    }

    public static Boolean del(String key) {
        return getRedisTemplate().delete(key);
    }
    public static Long dels(String... keys) {
        return getRedisTemplate().delete(Arrays.asList(keys));
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key, Class<T> clazz) {
        // 1. 参数校验
        if(StringUtils.isEmpty(key) || clazz == null) {
            return null;
        }

        // 2. 从 Redis 中获取数据
        Object obj = getRedisTemplate().opsForValue().get(key);
        return BeanHelper.cast(obj, clazz);
    }
    public static int getInt(String key) {
        Object value = getRedisTemplate().opsForValue().get(key);
        return value == null ? 0 : Integer.parseInt(value.toString());
    }
    public static long getLong(String key) {
        Object value = getRedisTemplate().opsForValue().get(key);
        return value == null ? 0 : Long.parseLong(value.toString());
    }

    public static void set(String key, Object value) {
        getRedisTemplate().opsForValue().set(key, value);
    }
    public static void setEx(String key, Object value, long expiredSeconds) {
        getRedisTemplate().opsForValue().set(key, value, expiredSeconds, TimeUnit.SECONDS);
    }
    public static Boolean setNx(String key, Object value, long expiredSeconds) {
        return getRedisTemplate().opsForValue().setIfAbsent(key, value, expiredSeconds, TimeUnit.SECONDS);
    }

    public static Long incr(String key) {
        return getRedisTemplate().opsForValue().increment(key);
    }
    public static Long decr(String key) {
        return getRedisTemplate().opsForValue().decrement(key);
    }


    // ======================================== hash ==================================================
    public static <T> T hget(String key, Class<T> clazz) {
        Map entries = getRedisTemplate().boundHashOps(key).entries();
        return BeanHelper.toBean(entries, clazz);
    }
    public static <T> T hget(String key, Object field, Class<T> clazz) {
        Object value = getRedisTemplate().boundHashOps(key).get(field);
        return BeanHelper.cast(value, clazz);
    }

    public static void hset(String key, Object field, Object value) {
        getRedisTemplate().boundHashOps(key).put(field, value);
    }
    public static void hset(String key, Object hash) {
        hset(key, new BeanMap(hash));
    }
    public static void hset(String key, Map hash) {
        getRedisTemplate().opsForHash().putAll(key, hash);
    }

    // ======================================== hash ==================================================
}
