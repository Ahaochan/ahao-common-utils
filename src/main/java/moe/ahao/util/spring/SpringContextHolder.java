package moe.ahao.util.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 使用 {@code @DependsOn(SpringContextHolder.BEAN_NAME)} 解决 applicationContext 为 null 的依赖问题
 */
@Service(SpringContextHolder.BEAN_NAME)
@Lazy(false)
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {
    public static final String BEAN_NAME = "SpringContextHolder";
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext; // 违反 FindBugs 规范
    }

    public static ApplicationContext getApplicationContext() {
        assertApplicationContext();
        return applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        return (T) getApplicationContext().getBean(beanName);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return getApplicationContext().getBean(requiredType);
    }

    public static <T> T getBean(Class<T> requiredType, T defaultValue) {
        ObjectProvider<T> beanProvider = getApplicationContext().getBeanProvider(requiredType);
        T bean = beanProvider.getIfAvailable(() -> defaultValue);
        return bean;
    }

    public static String getValue(String key) {
        return getApplicationContext().getEnvironment().getProperty(key);
    }

    public static boolean getBoolean(String key) {
        String value = getValue(key);
        return Boolean.valueOf(value);
    }

    public static void clearHolder() {
        applicationContext = null;
    }


    @Override
    public void destroy() throws Exception {
        SpringContextHolder.clearHolder();
    }

    public static boolean enable() {
        return SpringContextHolder.applicationContext != null;
    }
    private static void assertApplicationContext() {
        if (SpringContextHolder.applicationContext == null) {
            throw new IllegalStateException("applicationContext属性为null,请检查是否注入了SpringContextHolder!");
        }
    }
}

