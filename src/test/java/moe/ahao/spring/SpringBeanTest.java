package moe.ahao.spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.Proxy;

class SpringBeanTest {
    interface AhaoInterface {}
    static class AhaoInterfaceImpl implements AhaoInterface {}
    static class AhaoBean {}
    class NoSuchBean {}
    @Test
    void registerBean() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.registerBean(AhaoBean.class);
        context.refresh();
        Assertions.assertNotNull(context.getBean(AhaoBean.class));
    }
    @Test
    void noSuchBean() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.registerBean(NoSuchBean.class);
        // context.refresh();
        Assertions.assertThrows(UnsatisfiedDependencyException.class, context::refresh);
    }
    @Test
    void proxy() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.registerBean(AhaoInterfaceImpl.class);
        context.registerBean(AhaoBeanPostProcessor.class);
        context.refresh();

        AhaoInterface bean = context.getBean(AhaoInterface.class);
        Assertions.assertNotNull(bean);
        System.out.println("替换后的类名:" + bean.getClass().getName());
        Assertions.assertNotEquals(AhaoInterfaceImpl.class, bean.getClass());
        System.out.println(context.getBean(AhaoInterface.class).toString());
    }


    public static class AhaoBeanPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            Class<?> clazz = bean.getClass();
            System.out.println("对已有Bean进行动态代理:" + clazz.getName());
            return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), (p, m, a) -> {
                System.out.print("JDK动态代理:");
                // return method.invoke(proxy, args); // loop
                return m.invoke(bean, a);
            });
        }
    }
}
