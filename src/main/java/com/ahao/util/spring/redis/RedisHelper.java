package com.ahao.util.spring.redis;

import com.ahao.util.commons.lang.BeanHelper;
import com.ahao.util.spring.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class RedisHelper {
    private static final Logger logger = LoggerFactory.getLogger(RedisHelper.class);
    // ======================================== 依赖 ==================================================
    private volatile static RedisTemplate<String, Object> redisTemplate;
    private volatile static StringRedisTemplate stringRedisTemplate;
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
    public static StringRedisTemplate getStringRedisTemplate() {
        if(stringRedisTemplate == null) {
            synchronized (RedisHelper.class) {
                if(stringRedisTemplate == null) {
                    RedisHelper.stringRedisTemplate = SpringContextHolder.getBean("stringRedisTemplate");
                }
            }
        }
        return stringRedisTemplate;
    }

    // ======================================== 依赖 ==================================================


    public static Boolean del(String key) {
        return getRedisTemplate().delete(key);
    }
    public static Long dels(String... keys) {
        return getRedisTemplate().delete(Arrays.asList(keys));
    }
    public static Boolean expire(String key, long timeout, TimeUnit unit) {
        return getStringRedisTemplate().expire(key, timeout, unit);
    }

    public static Long dbSize() {
        return getStringRedisTemplate().execute(RedisServerCommands::dbSize);
    }
    public static Set<String> keys(String pattern) {
        Set<String> keys = new HashSet<>();
        scan(pattern, keys::add);
        return keys;
    }

    // ======================================== string ==================================================
    public static Boolean getBoolean(String key) {
        String value = getString(key);
        return value == null ? null : Boolean.valueOf(value);
    }
    public static Byte getByte(String key) {
        String value = getString(key);
        return value == null ? null : Byte.valueOf(value);
    }
    public static Short getShort(String key) {
        String value = getString(key);
        return value == null ? null : Short.valueOf(value);
    }
    public static Integer getInteger(String key) {
        String value = getString(key);
        return value == null ? null : Integer.valueOf(value);
    }
    public static Long getLong(String key) {
        String value = getString(key);
        return value == null ? null : Long.valueOf(value);
    }
    public static Float getFloat(String key) {
        String value = getString(key);
        return value == null ? null : Float.valueOf(value);
    }
    public static Double getDouble(String key) {
        String value = getString(key);
        return value == null ? null : Double.valueOf(value);
    }
    public static String getString(String key) {
        return getStringRedisTemplate().opsForValue().get(key);
    }
    public static <T> T getObject(String key, Class<T> clazz) {
        // 1. 参数校验, 处理 void
        if(StringUtils.isEmpty(key) || clazz == null || clazz == void.class || clazz == Void.class) {
            return null;
        }

        // 2. 处理 基本数据 类型
        if(clazz == boolean.class || clazz == Boolean.class) {
            return (T) getBoolean(key);
        }
        if(clazz == byte.class || clazz == Byte.class) {
            return (T) getByte(key);
        }
        if(clazz == short.class || clazz == Short.class) {
            return (T) getShort(key);
        }
        if(clazz == char.class || clazz == Character.class) {
            String value = getString(key);
            return (T) Character.valueOf(value.charAt(0));
        }
        if(clazz == int.class || clazz == Integer.class) {
            return (T) getInteger(key);
        }
        if(clazz == long.class || clazz == Long.class) {
            return (T) getLong(key);
        }
        if(clazz == float.class || clazz == Float.class) {
            return (T) getFloat(key);
        }
        if(clazz == double.class || clazz == Double.class) {
            return (T) getDouble(key);
        }
        if(clazz == String.class) {
            return (T) getString(key);
        }
        // 3. 处理 复杂 数据类型
        Object obj = getRedisTemplate().opsForValue().get(key);
        return BeanHelper.cast(obj, clazz);
    }


    public static <T extends Number> void set(String key, T value) {
        set(key, value == null ? null : String.valueOf(value));
    }
    public static void set(String key, Boolean value) {
        set(key, value == null ? null : String.valueOf(value));
    }
    public static void set(String key, String value) {
        getStringRedisTemplate().opsForValue().set(key, value);
    }
    public static void set(String key, Object value) {
        getRedisTemplate().opsForValue().set(key, value);
    }
    public static <T extends Number> void setEx(String key, T value, long expiredSeconds) {
        setEx(key, value == null ? null : String.valueOf(value), expiredSeconds);
    }
    public static void setEx(String key, Boolean value, long expiredSeconds) {
        setEx(key, value == null ? null : String.valueOf(value), expiredSeconds);
    }
    public static void setEx(String key, String value, long expiredSeconds) {
        getStringRedisTemplate().opsForValue().set(key, value, expiredSeconds, TimeUnit.SECONDS);
    }
    public static void setEx(String key, Object value, long expiredSeconds) {
        getRedisTemplate().opsForValue().set(key, value, expiredSeconds, TimeUnit.SECONDS);
    }
    public static <T extends Number> Boolean setNx(String key, T value, long expiredSeconds) {
        return setNx(key, value == null ? null : String.valueOf(value), expiredSeconds);
    }
    public static Boolean setNx(String key, Boolean value, long expiredSeconds) {
        return setNx(key, value == null ? null : String.valueOf(value), expiredSeconds);
    }
    public static Boolean setNx(String key, String value, long expiredSeconds) {
        return getStringRedisTemplate().opsForValue().setIfAbsent(key, value, expiredSeconds, TimeUnit.SECONDS);
    }
    public static Boolean setNx(String key, Object value, long expiredSeconds) {
        return getRedisTemplate().opsForValue().setIfAbsent(key, value, expiredSeconds, TimeUnit.SECONDS);
    }


    public static Long incr(String key) {
        return getStringRedisTemplate().opsForValue().increment(key);
    }
    public static Long incr(String key, long value) {
        return getStringRedisTemplate().opsForValue().increment(key, value);
    }
    public static Long incrEx(String key, long expire, TimeUnit timeUnit) {
        String script = "local v = redis.call('INCR', KEYS[1]) if v == 1 then redis.call('EXPIRE', KEYS[1], ARGV[1]) end return v";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);

        String expireTime = String.valueOf(TimeUnit.SECONDS.convert(expire, timeUnit));
        Long incr = getStringRedisTemplate().execute(redisScript, Collections.singletonList(key), expireTime);
        return incr;
    }

    public static Long decr(String key) {
        return getStringRedisTemplate().opsForValue().decrement(key);
    }
    public static Long decr(String key, long value) {
        return getStringRedisTemplate().opsForValue().decrement(key, value);
    }

    public static void scan(String pattern, Consumer<String> consumer) {
        RedisSerializer<String> keySerializer = (RedisSerializer<String>) getStringRedisTemplate().getKeySerializer();

        ScanOptions options = ScanOptions.scanOptions().match(pattern).count(10).build();
        try(Cursor<String> cursor = getStringRedisTemplate().executeWithStickyConnection((RedisCallback<Cursor<String>>) connection ->
            new ConvertingCursor<>(connection.scan(options), keySerializer::deserialize))) {

            if(cursor == null) {
                return;
            }
            while (cursor.hasNext()) {
                String key = cursor.next();
                consumer.accept(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // ======================================== string ==================================================

    // ======================================== hash ==================================================
    public static long hdel(String key, Object... fields) {
        Long count = getRedisTemplate().opsForHash().delete(key, fields);
        return count == null ? 0 : count;
    }

    public static boolean hexists(String key, Object field) {
        Boolean exist = getRedisTemplate().opsForHash().hasKey(key, field);
        return exist == null ? false : exist;
    }

    public static Boolean hgetBoolean(String key, Object field) {
        String value = hgetString(key, field);
        return value == null ? null : Boolean.valueOf(value);
    }
    public static Byte hgetByte(String key, Object field) {
        String value = hgetString(key, field);
        return value == null ? null : Byte.valueOf(value);
    }
    public static Short hgetShort(String key, Object field) {
        String value = hgetString(key, field);
        return value == null ? null : Short.valueOf(value);
    }
    public static Integer hgetInteger(String key, Object field) {
        String value = hgetString(key, field);
        return value == null ? null : Integer.valueOf(value);
    }
    public static Long hgetLong(String key, Object field) {
        String value = hgetString(key, field);
        return value == null ? null : Long.valueOf(value);
    }
    public static Float hgetFloat(String key, Object field) {
        String value = hgetString(key, field);
        return value == null ? null : Float.valueOf(value);
    }
    public static Double hgetDouble(String key, Object field) {
        String value = hgetString(key, field);
        return value == null ? null : Double.valueOf(value);
    }
    public static String hgetString(String key, Object field) {
        Object value = getStringRedisTemplate().opsForHash().get(key, field);
        return value == null ? null : String.valueOf(value);
    }
    public static <T> T hgetObject(String key, String field, Class<T> clazz) {
        // 1. 参数校验, 处理 void
        if(StringUtils.isEmpty(key) || clazz == null || clazz == void.class || clazz == Void.class) {
            return null;
        }

        // 2. 处理 基本数据 类型
        if(clazz == boolean.class || clazz == Boolean.class) {
            return (T) hgetBoolean(key, field);
        }
        if(clazz == byte.class || clazz == Byte.class) {
            return (T) hgetByte(key, field);
        }
        if(clazz == short.class || clazz == Short.class) {
            return (T) hgetShort(key, field);
        }
        if(clazz == char.class || clazz == Character.class) {
            String value = hgetString(key, field);
            return (T) Character.valueOf(value.charAt(0));
        }
        if(clazz == int.class || clazz == Integer.class) {
            return (T) hgetInteger(key, field);
        }
        if(clazz == long.class || clazz == Long.class) {
            return (T) hgetLong(key, field);
        }
        if(clazz == float.class || clazz == Float.class) {
            return (T) hgetFloat(key, field);
        }
        if(clazz == double.class || clazz == Double.class) {
            return (T) hgetDouble(key, field);
        }
        if(clazz == String.class) {
            return (T) hgetString(key, field);
        }
        // 3. 处理 复杂 数据类型
        Object obj = getRedisTemplate().opsForHash().get(key, field);
        return BeanHelper.cast(obj, clazz);
    }

    public static Long hincrBy(String key, Object field, long value) {
        return getStringRedisTemplate().opsForHash().increment(key, field, value);
    }
    public static Double hincrBy(String key, Object field, double value) {
        return getStringRedisTemplate().opsForHash().increment(key, field, value);
    }
    public static Long hdecrBy(String key, Object field, long value) {
        return getStringRedisTemplate().opsForHash().increment(key, field, value * -1);
    }
    public static Double hdecrBy(String key, Object field, double value) {
        return getStringRedisTemplate().opsForHash().increment(key, field, value * -1);
    }
    // ======================================== hash ==================================================

    // ======================================== 分布式锁 ==================================================
    public static boolean lock(String key, String unionId) {
        return lock(key, unionId, 60);
    }
    public static boolean lock(String key, String unionId, int expireSeconds) {
        Boolean success = getStringRedisTemplate().opsForValue().setIfAbsent(key, unionId, expireSeconds, TimeUnit.SECONDS);
        logger.debug("获取 Redis 锁, key:{}, unionId:{}, expireSeconds:{}, 结果:{}", key, unionId, expireSeconds, success);
        return success != null && success;
    }
    public static boolean lockBlock(String key, String unionId) {
        return lockBlock(key, unionId, Long.MAX_VALUE);
    }
    public static boolean lockBlock(String key, String unionId, long blockTime) {
        while (blockTime > 0) {
            if(lock(key, unionId)) {
                return true;
            }
            try {
                logger.debug("获取 Redis 锁失败, 继续阻塞{}毫秒", blockTime);
                blockTime -= 1000;
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("线程中断", e);
            }
        }
        logger.debug("获取 Redis 锁失败");
        return false;
    }
    public static boolean unlock(String key, String unionId){
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Long count = getStringRedisTemplate().execute(redisScript, Collections.singletonList(key), unionId);
        logger.debug("解锁 Redis 锁, key:{}, unionId:{}, 结果:{}", key, unionId, count);
        return count != null && count > 0;
    }
    // ======================================== 分布式锁 ==================================================
}
