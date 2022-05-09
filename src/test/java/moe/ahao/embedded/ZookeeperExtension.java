package moe.ahao.embedded;

import org.apache.curator.test.TestingServer;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ZookeeperExtension implements BeforeAllCallback, AfterAllCallback {
    private static TestingServer zookeeperServer;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        zookeeperServer = new TestingServer(2181);
        System.out.println("连接:" + zookeeperServer.getConnectString());
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        zookeeperServer.stop();
    }

    public String getConnectString() {
        return zookeeperServer.getConnectString();
    }
}
