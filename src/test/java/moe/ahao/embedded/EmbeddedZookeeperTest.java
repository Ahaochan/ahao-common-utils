package moe.ahao.embedded;

import org.apache.curator.test.TestingServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class EmbeddedZookeeperTest {
    private static TestingServer zookeeperServer;

    @BeforeAll
    public static void beforeAll() throws Exception {
        zookeeperServer = new TestingServer(2181);
        System.out.println("连接:" + zookeeperServer.getConnectString());
    }

    @AfterAll
    public static void afterAll() throws Exception {
        zookeeperServer.stop();
    }
}
