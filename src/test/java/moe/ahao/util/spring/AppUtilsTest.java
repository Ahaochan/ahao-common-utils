package moe.ahao.util.spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = SpringContextHolder.class)
class AppUtilsTest {
    @Test
    void test() {
        Environment env = AppUtils.getEnv();
        Assertions.assertNotNull(env);

        Assertions.assertFalse(AppUtils.isDev());
        Assertions.assertFalse(AppUtils.isProd());
        Assertions.assertFalse(AppUtils.isTest());
    }
}
