package moe.ahao.embedded;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import redis.embedded.RedisServer;

public class RedisExtension implements BeforeAllCallback, AfterAllCallback {
    private static RedisServer redisServer;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        redisServer = RedisServer.builder()
            .port(6379)
            .setting("maxmemory 128M")
            .build();
        redisServer.start();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        redisServer.stop();
    }
}
