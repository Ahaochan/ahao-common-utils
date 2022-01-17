package moe.ahao.spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class SpringRegisterTest {
    private static class A {
        Object obj;
        public A() {
            System.out.println("构造函数A()");
        }
        public A(String s) {
            obj = s;
            System.out.println("构造函数A(" + s + ")");
        }
        public A(int i) {
            obj = i;
            System.out.println("构造函数A(" + i + ")");
        }
        public A(float f) {
            obj = f;
            System.out.println("构造函数A(" + f + ")");
        }
    }
    private static class AhaoFactoryBean {
        public A createNo() {
            return new A();
        }
        public static A createString() {
            return new A("hello");
        }
        public A createInt() {
            return new A(1);
        }
        public A createFloat() {
            return new A(1.0f);
        }
    }

    @Test
    private void beanDefinition() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(A.class);
        context.registerBeanDefinition("a", beanDefinition);

        context.refresh();

        A bean = context.getBean(A.class);
        Assertions.assertNotNull(bean);
        Assertions.assertNull(bean.obj);
    }

    @Test
    void factoryBean() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.register(AhaoFactoryBean.class);

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(A.class);
        beanDefinition.setFactoryBeanName("ahaoFactoryBean");
        beanDefinition.setFactoryMethodName("createInt");
        context.registerBeanDefinition("a", beanDefinition);

        context.refresh();

        A bean = context.getBean(A.class);
        Assertions.assertNotNull(bean);
        Assertions.assertEquals(Integer.class, bean.obj.getClass());
    }

    @Test
    void supplier1() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        AhaoFactoryBean factoryBean = new AhaoFactoryBean();

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(A.class);
        beanDefinition.setInstanceSupplier(factoryBean::createFloat);
        context.registerBeanDefinition("a", beanDefinition);

        context.refresh();

        A bean = context.getBean(A.class);
        Assertions.assertNotNull(bean);
        Assertions.assertEquals(Float.class, bean.obj.getClass());
    }

    @Test
    void supplier2() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(A.class);
        beanDefinition.setInstanceSupplier(AhaoFactoryBean::createString);
        context.registerBeanDefinition("a", beanDefinition);

        context.refresh();

        A bean = context.getBean(A.class);
        Assertions.assertNotNull(bean);
        Assertions.assertEquals(String.class, bean.obj.getClass());
    }
}
