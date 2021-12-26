package moe.ahao.spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.Resource;

public class SpringRejectTest {
    static class AhaoBean {
        String name;
        public AhaoBean(String name) {
            this.name = name;
        }
    }
    static class AutowireBean {
        @Autowired
        public AhaoBean ahaoBean;
    }
    static class ResourceBean {
        @Resource
        public AhaoBean a;
        @Resource
        public AhaoBean b;
    }

    @Test
    public void autowire() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.registerBean(AhaoBean.class.getSimpleName(), AhaoBean.class, "default");
        context.registerBean(AutowireBean.class);
        context.refresh();

        AutowireBean bean = context.getBean(AutowireBean.class);
        Assertions.assertNotNull(bean.ahaoBean);
    }

    @Test
    public void resource() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.registerBean("a", AhaoBean.class, "a");
        context.registerBean("b", AhaoBean.class, "b");
        context.registerBean(ResourceBean.class);
        context.refresh();

        ResourceBean bean = context.getBean(ResourceBean.class);
        Assertions.assertEquals("a", bean.a.name);
        Assertions.assertEquals("b", bean.b.name);
    }

}
