package moe.ahao.util.spring.redis;

import com.fasterxml.jackson.core.type.TypeReference;
import moe.ahao.util.commons.io.JSONHelper;
import moe.ahao.util.spring.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;
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
    public static <T> T getObject(String key, TypeReference<T> typeReference) {
        Object obj = getRedisTemplate().opsForValue().get(key);
        return JSONHelper.parse(JSONHelper.toString(obj), typeReference);
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
    public static <T extends Number> void setEx(String key, T value, long timeout, TimeUnit unit) {
        setEx(key, value == null ? null : String.valueOf(value), timeout, unit);
    }
    public static void setEx(String key, Boolean value, long timeout, TimeUnit unit) {
        setEx(key, value == null ? null : String.valueOf(value), timeout, unit);
    }
    public static void setEx(String key, String value, long timeout, TimeUnit unit) {
        getStringRedisTemplate().opsForValue().set(key, value, timeout, unit);
    }
    public static void setEx(String key, Object value, long timeout, TimeUnit unit) {
        getRedisTemplate().opsForValue().set(key, value, timeout, unit);
    }
    public static <T extends Number> Boolean setNx(String key, T value, long timeout, TimeUnit unit) {
        return setNx(key, value == null ? null : String.valueOf(value), timeout, unit);
    }
    public static Boolean setNx(String key, Boolean value, long timeout, TimeUnit unit) {
        return setNx(key, value == null ? null : String.valueOf(value), timeout, unit);
    }
    public static Boolean setNx(String key, String value, long timeout, TimeUnit unit) {
        return getStringRedisTemplate().opsForValue().setIfAbsent(key, value, timeout, unit);
    }
    public static Boolean setNx(String key, Object value, long timeout, TimeUnit unit) {
        return getRedisTemplate().opsForValue().setIfAbsent(key, value, timeout, unit);
    }
    @Deprecated
    public static <T extends Number> Boolean setNxOld(String key, T value, long timeout, TimeUnit unit) {
        return setNxOld(key, value == null ? null : String.valueOf(value), timeout, unit);
    }
    @Deprecated
    public static Boolean setNxOld(String key, Boolean value, long timeout, TimeUnit unit) {
        return setNxOld(key, value == null ? null : String.valueOf(value), timeout, unit);
    }
    @Deprecated
    public static Boolean setNxOld(String key, String value, long timeout, TimeUnit unit) {
        StringRedisTemplate redisTemplate = getStringRedisTemplate();
        byte[] rawKey = ((RedisSerializer) redisTemplate.getKeySerializer()).serialize(key);
        byte[] rawValue = ((RedisSerializer) redisTemplate.getValueSerializer()).serialize(value);
        Expiration expiration = Expiration.from(timeout, unit);
        return redisTemplate.execute((connection) ->
            connection.set(rawKey, rawValue, expiration, RedisStringCommands.SetOption.ifAbsent()), true);
    }
    @Deprecated
    public static Boolean setNxOld(String key, Object value, long timeout, TimeUnit unit) {
        RedisTemplate<String, Object> redisTemplate = getRedisTemplate();
        byte[] rawKey = ((RedisSerializer) redisTemplate.getKeySerializer()).serialize(key);
        byte[] rawValue = ((RedisSerializer) redisTemplate.getValueSerializer()).serialize(value);
        Expiration expiration = Expiration.from(timeout, unit);
        return redisTemplate.execute((connection) ->
            connection.set(rawKey, rawValue, expiration, RedisStringCommands.SetOption.ifAbsent()), true);
    }


    public static Long incr(String key) {
        return getStringRedisTemplate().opsForValue().increment(key);
    }
    public static Long incr(String key, long value) {
        return getStringRedisTemplate().opsForValue().increment(key, value);
    }
    public static Long incrEx(String key, long timeout, TimeUnit timeUnit) {
        String script = "local v = redis.call('INCR', KEYS[1]) if v == 1 then redis.call('EXPIRE', KEYS[1], ARGV[1]) end return v";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);

        String expireTime = String.valueOf(TimeUnit.SECONDS.convert(timeout, timeUnit));
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
            logger.error("scan Redis 失败, pattern:{}", pattern, e);
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
    public static <T> T hgetObject(String key, String field, TypeReference<T> typeReference) {
        Object obj = getRedisTemplate().opsForHash().get(key, field);
        return JSONHelper.parse(JSONHelper.toString(obj), typeReference);
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
        return lock(key, unionId, 60, TimeUnit.SECONDS);
    }
    public static boolean lock(String key, String unionId, long timeout, TimeUnit unit) {
        Boolean success = getStringRedisTemplate().opsForValue().setIfAbsent(key, unionId, timeout, unit);
        logger.debug("获取 Redis 锁, key:{}, unionId:{}, timeout:{}, unit:{}, 结果:{}", key, unionId, timeout, unit, success);
        return success != null && success;
    }
    public static boolean lockBlock(String key, String unionId) {
        return lockBlock(key, unionId, Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }
    public static boolean lockBlock(String key, String unionId, long timeout, TimeUnit unit) {
        long blockTime = unit.toMillis(timeout);
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
