package moe.ahao.embedded;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import redis.embedded.RedisServer;

public class EmbeddedRedisTest {
    private static RedisServer redisServer;

    @BeforeAll
    public static void beforeAll() throws Exception {
        redisServer = RedisServer.builder()
            .port(6379)
            .setting("maxmemory 128M")
            .build();
        redisServer.start();
    }

    @AfterAll
    public static void afterAll() throws Exception {
        redisServer.stop();
    }
}
