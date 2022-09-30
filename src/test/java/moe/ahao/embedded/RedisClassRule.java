package moe.ahao.embedded;

import org.junit.rules.ExternalResource;
import redis.embedded.RedisServer;

public class RedisClassRule extends ExternalResource {
    private RedisServer redisServer;

    @Override
    protected void before() throws Throwable {
        redisServer = RedisServer.builder()
            .port(6379)
            .setting("maxmemory 128M")
            .build();
        redisServer.start();
    }

    @Override
    protected void after() {
        redisServer.stop();
    }
}
