package moe.ahao.spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

class SpringOrderTest {
    private abstract static class Z {
        public Z() {
            System.out.println("init:" + this.getClass().getSimpleName() + ":" + this.getClass().getAnnotation(Order.class).value());
        }
        public void say() {
            System.out.println("method:" + this.getClass().getSimpleName() + ":" + this.getClass().getAnnotation(Order.class).value());
        }
    }
    @Order(5)
    @Component("1")
    private static class Z1 extends Z {
        static {
            System.out.println("static:" + Z1.class.getSimpleName());
        }
    }
    @Order(4)
    @Component("2")
    private static class Z2 extends Z {
        static {
            System.out.println("static:" + Z2.class.getSimpleName());
        }
    }
    @Order(6)
    @Component("3")
    private static class Z3 extends Z {
        static {
            System.out.println("static:" + Z3.class.getSimpleName());
        }
    }
    @Component
    private static class Container {
        @Autowired
        List<Z> list;
    }

    @Test
    void test() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan(SpringOrderTest.class.getPackage().getName());
        context.refresh();

        Container bean = context.getBean(Container.class);
        Assertions.assertEquals(Z2.class, bean.list.get(0).getClass());
        Assertions.assertEquals(Z1.class, bean.list.get(1).getClass());
        Assertions.assertEquals(Z3.class, bean.list.get(2).getClass());
        bean.list.forEach(Z::say);
    }

}
