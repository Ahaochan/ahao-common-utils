package moe.ahao.spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

class SpringDependsOnTest {
    @Component(C.BEAN_NAME)
    private static class C {
        public static final String BEAN_NAME = "c";
        static {
            System.out.println("C: static");
        }
        @PostConstruct
        public void init() {
            System.out.println("C: PostConstruct");
        }
    }
    @Component(A.BEAN_NAME)
    @DependsOn(C.BEAN_NAME) // 依赖C, 优先加载C
    private static class A {
        public static final String BEAN_NAME = "a";
        static {
            System.out.println("A: static");
        }
        @PostConstruct
        public void init() {
            System.out.println("A: PostConstruct");
        }
    }
    @Component(B.BEAN_NAME)
    private static class B {
        public static final String BEAN_NAME = "b";
        static {
            System.out.println("B: static");
        }
        @PostConstruct
        public void init() {
            System.out.println("B: PostConstruct");
        }
    }


    @Test
    void dependsOn() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan(this.getClass().getPackage().getName());
        context.refresh();
        Assertions.assertTrue(true);
    }
}
