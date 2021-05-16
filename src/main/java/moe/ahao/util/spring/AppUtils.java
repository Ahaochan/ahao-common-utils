package moe.ahao.util.spring;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.env.Environment;

public class AppUtils {

    public static Environment getEnv() {
        return SpringContextHolder.getBean(Environment.class);
    }

    public static boolean isDev() {
        String[] activeProfiles = getEnv().getActiveProfiles();
        return ArrayUtils.contains(activeProfiles, "dev");
    }

    public static boolean isProd() {
        String[] activeProfiles = getEnv().getActiveProfiles();
        return ArrayUtils.contains(activeProfiles, "prod");
    }

    public static boolean isTest() {
        String[] activeProfiles = getEnv().getActiveProfiles();
        return ArrayUtils.contains(activeProfiles, "test");
    }
}
