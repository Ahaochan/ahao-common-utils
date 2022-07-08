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
import java.math.BigDecimal;
import java.util.*;
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
    public static Long dbSize() {
        return getStringRedisTemplate().execute(RedisServerCommands::dbSize);
    }
    public static Set<String> keys(String pattern) {
        Set<String> keys = new HashSet<>();
        scan(pattern, keys::add);
        return keys;
    }

    // ======================================== string ==================================================
    public static boolean del(String key) {
        Boolean success = getRedisTemplate().delete(key);
        return success == null ? false : success;
    }
    public static long dels(String... keys) {
        Long count = getRedisTemplate().delete(Arrays.asList(keys));
        return count == null ? 0 : count;
    }
    public static Boolean expire(String key, long timeout, TimeUnit unit) {
        return getStringRedisTemplate().expire(key, timeout, unit);
    }

    public static Boolean getBoolean(String key) {
        String value = getString(key);
        return value == null ? null : Boolean.valueOf(value);
    }
    public static Integer getInteger(String key) {
        String value = getString(key);
        return value == null ? null : Integer.valueOf(value);
    }
    public static Long getLong(String key) {
        String value = getString(key);
        return value == null ? null : Long.valueOf(value);
    }
    public static BigDecimal getBigDecimal(String key) {
        String value = getString(key);
        return value == null ? null : new BigDecimal(value);
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
    public static <T extends Number> Boolean setNx(String key, T value) {
        return setNx(key, value == null ? null : String.valueOf(value));
    }
    public static Boolean setNx(String key, Boolean value) {
        return setNx(key, value == null ? null : String.valueOf(value));
    }
    public static Boolean setNx(String key, String value) {
        return getStringRedisTemplate().opsForValue().setIfAbsent(key, value);
    }
    public static Boolean setNx(String key, Object value) {
        return getRedisTemplate().opsForValue().setIfAbsent(key, value);
    }
    public static <T extends Number> Boolean setNxEx(String key, T value, long timeout, TimeUnit unit) {
        return setNxEx(key, value == null ? null : String.valueOf(value), timeout, unit);
    }
    public static Boolean setNxEx(String key, Boolean value, long timeout, TimeUnit unit) {
        return setNxEx(key, value == null ? null : String.valueOf(value), timeout, unit);
    }
    public static Boolean setNxEx(String key, String value, long timeout, TimeUnit unit) {
        return getStringRedisTemplate().opsForValue().setIfAbsent(key, value, timeout, unit);
    }
    public static Boolean setNxEx(String key, Object value, long timeout, TimeUnit unit) {
        return getRedisTemplate().opsForValue().setIfAbsent(key, value, timeout, unit);
    }
    @Deprecated
    public static <T extends Number> Boolean setNxExOld(String key, T value, long timeout, TimeUnit unit) {
        return setNxExOld(key, value == null ? null : String.valueOf(value), timeout, unit);
    }
    @Deprecated
    public static Boolean setNxExOld(String key, Boolean value, long timeout, TimeUnit unit) {
        return setNxExOld(key, value == null ? null : String.valueOf(value), timeout, unit);
    }
    @Deprecated
    public static Boolean setNxExOld(String key, String value, long timeout, TimeUnit unit) {
        StringRedisTemplate redisTemplate = getStringRedisTemplate();
        byte[] rawKey = ((RedisSerializer) redisTemplate.getKeySerializer()).serialize(key);
        byte[] rawValue = ((RedisSerializer) redisTemplate.getValueSerializer()).serialize(value);
        Expiration expiration = Expiration.from(timeout, unit);
        return redisTemplate.execute((connection) ->
            connection.set(rawKey, rawValue, expiration, RedisStringCommands.SetOption.ifAbsent()), true);
    }
    @Deprecated
    public static Boolean setNxExOld(String key, Object value, long timeout, TimeUnit unit) {
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
    public static long hdel(String key, String... keys) {
        Long count = getRedisTemplate().opsForHash().delete(key, keys);
        return count == null ? 0 : count;
    }
    public static boolean hexists(String hash, String key) {
        Boolean exist = getRedisTemplate().opsForHash().hasKey(hash, key);
        return exist == null ? false : exist;
    }

    public static Boolean hgetBoolean(String hash, String key) {
        String value = hgetString(hash, key);
        return value == null ? null : Boolean.valueOf(value);
    }
    public static Byte hgetByte(String hash, String key) {
        String value = hgetString(hash, key);
        return value == null ? null : Byte.valueOf(value);
    }
    public static Short hgetShort(String hash, String key) {
        String value = hgetString(hash, key);
        return value == null ? null : Short.valueOf(value);
    }
    public static Integer hgetInteger(String hash, String key) {
        String value = hgetString(hash, key);
        return value == null ? null : Integer.valueOf(value);
    }
    public static Long hgetLong(String hash, String key) {
        String value = hgetString(hash, key);
        return value == null ? null : Long.valueOf(value);
    }
    public static Float hgetFloat(String hash, String key) {
        String value = hgetString(hash, key);
        return value == null ? null : Float.valueOf(value);
    }
    public static Double hgetDouble(String hash, String field) {
        String value = hgetString(hash, field);
        return value == null ? null : Double.valueOf(value);
    }
    public static String hgetString(String hash, String field) {
        Object value = getStringRedisTemplate().opsForHash().get(hash, field);
        return value == null ? null : String.valueOf(value);
    }
    public static <T> T hgetObject(String hash, String field, TypeReference<T> typeReference) {
        Object obj = getRedisTemplate().opsForHash().get(hash, field);
        return JSONHelper.parse(JSONHelper.toString(obj), typeReference);
    }
    public static Map<String, String> hmget(String hash) {
        Map map = getStringRedisTemplate().opsForHash().entries(hash);
        return map;
    }

    public static <T extends Number> void hset(String hash, String key, T value) {
        hset(hash, key, value == null ? null : String.valueOf(value));
    }
    public static void hset(String hash, String key, Boolean value) {
        hset(hash, key, value == null ? null : String.valueOf(value));
    }
    public static void hset(String hash, String key, String value) {
        getStringRedisTemplate().opsForHash().put(hash, key, value);
    }
    public static void hset(String hash, String key, Object value) {
        getRedisTemplate().opsForHash().put(hash, key, value);
    }
    public static void hmset1(String hash, Map<String, String> map) {
        getStringRedisTemplate().opsForHash().putAll(hash, map);
    }
    public static void hmset2(String hash, Map<String, Object> map) {
        getRedisTemplate().opsForHash().putAll(hash, map);
    }
    @Deprecated
    public static <T extends Number> void hsetEx(String hash, String key, T value, long timeout, TimeUnit unit) {
        throw new UnsupportedOperationException("Redis不支持");
    }
    @Deprecated
    public static void hsetEx(String hash, String key, Boolean value, long timeout, TimeUnit unit) {
        throw new UnsupportedOperationException("Redis不支持");
    }
    @Deprecated
    public static void hsetEx(String hash, String key, String value, long timeout, TimeUnit unit) {
        throw new UnsupportedOperationException("Redis不支持");
    }
    @Deprecated
    public static void hsetEx(String hash, String key, Object value, long timeout, TimeUnit unit) {
        throw new UnsupportedOperationException("Redis不支持");
    }
    public static <T extends Number> Boolean hsetNx(String hash, String key, T value) {
        return hsetNx(hash, key, value == null ? null : String.valueOf(value));
    }
    public static Boolean hsetNx(String hash, String key, Boolean value) {
        return hsetNx(hash, key, value == null ? null : String.valueOf(value));
    }
    public static Boolean hsetNx(String hash, String key, String value) {
        return getStringRedisTemplate().opsForHash().putIfAbsent(hash, key, value);
    }
    public static Boolean hsetNx(String hash, String key, Object value) {
        return getRedisTemplate().opsForHash().putIfAbsent(hash, key, value);
    }

    public static Long hincrBy(String hash, String key, long value) {
        return getStringRedisTemplate().opsForHash().increment(hash, key, value);
    }
    public static Double hincrBy(String hash, String key, double value) {
        return getStringRedisTemplate().opsForHash().increment(hash, key, value);
    }
    public static Long hdecrBy(String hash, String key, long value) {
        return getStringRedisTemplate().opsForHash().increment(hash, key, value * -1);
    }
    public static Double hdecrBy(String hash, String key, double value) {
        return getStringRedisTemplate().opsForHash().increment(hash, key, value * -1);
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
