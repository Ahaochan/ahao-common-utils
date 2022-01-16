package moe.ahao.spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

class SpringLookupTest {
    @Component
    @Scope("prototype")
    static class PrototypeBean {
    }
    @Component
    static class AutowiredInjectBean {
        @Autowired
        PrototypeBean prototypeBean;
        public void print() {
            PrototypeBean prototypeBean1 = prototypeBean;
            System.out.println("Autowired获取PrototypeBean: " + prototypeBean1);
            PrototypeBean prototypeBean2 = prototypeBean;
            System.out.println("Autowired获取PrototypeBean: " + prototypeBean2);
            Assertions.assertEquals(prototypeBean1, prototypeBean2);
            System.out.println("直接Autowired无法注入两个不同的PrototypeBean");
        }
    }
    @Component
    static class ApplicationContextInjectBean implements ApplicationContextAware {
        private ApplicationContext context;
        @Override
        public void setApplicationContext(ApplicationContext context) throws BeansException {
            this.context = context;
        }
        public void print() {
            PrototypeBean prototypeBean1 = context.getBean(PrototypeBean.class);
            System.out.println("ApplicationContext获取PrototypeBean: " + prototypeBean1);
            PrototypeBean prototypeBean2 = context.getBean(PrototypeBean.class);
            System.out.println("ApplicationContext获取PrototypeBean: " + prototypeBean2);
            Assertions.assertNotEquals(prototypeBean1, prototypeBean2);
            System.out.println("通过ApplicationContext可以注入两个不同的PrototypeBean");
        }
    }
    @Component
    static abstract class LookupInjectBean {
        @Lookup
        public abstract PrototypeBean create();
        public void print() {
            PrototypeBean prototypeBean1 = create();
            System.out.println("@Lookup获取PrototypeBean: " + prototypeBean1);
            PrototypeBean prototypeBean2 = create();
            System.out.println("@Lookup获取PrototypeBean: " + prototypeBean2);
            Assertions.assertNotEquals(prototypeBean1, prototypeBean2);
            System.out.println("通过@Lookup和抽象方法可以注入两个不同的PrototypeBean");
        }
    }

    @Test
    void autowired() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan(this.getClass().getPackage().getName());
        context.refresh();

        AutowiredInjectBean bean = context.getBean(AutowiredInjectBean.class);
        Assertions.assertNotNull(bean);
        bean.print();
    }

    @Test
    void applicationContext() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan(this.getClass().getPackage().getName());
        context.refresh();

        ApplicationContextInjectBean bean = context.getBean(ApplicationContextInjectBean.class);
        Assertions.assertNotNull(bean);
        bean.print();
    }

    @Test
    void lookup() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan(this.getClass().getPackage().getName());
        context.refresh();

        LookupInjectBean bean = context.getBean(LookupInjectBean.class);
        Assertions.assertNotNull(bean);
        bean.print();
    }
}
