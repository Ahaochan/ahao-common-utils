package moe.ahao.spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringBeanTest {
    static class AhaoBean {}
    class NoSuchBean {}
    @Test
    public void registerBean() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.registerBean(AhaoBean.class);
        context.refresh();
        Assertions.assertNotNull(context.getBean(AhaoBean.class));
    }
    @Test
    public void noSuchBean() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.registerBean(NoSuchBean.class);
        // context.refresh();
        Assertions.assertThrows(UnsatisfiedDependencyException.class, context::refresh);
    }
}
