package moe.ahao.util.spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = SpringContextHolder.class)
public class AppUtilsTest {

    @Test
    public void test() {
        Environment env = AppUtils.getEnv();
        Assertions.assertNotNull(env);

        Assertions.assertFalse(AppUtils.isDev());
        Assertions.assertFalse(AppUtils.isProd());
        Assertions.assertFalse(AppUtils.isTest());
    }
}
