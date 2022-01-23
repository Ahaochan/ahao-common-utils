package moe.ahao.java;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

public class JDKProxyTest {
    // 加了RUNTIME才能被method.getAnnotation()获取
    @Retention(RetentionPolicy.RUNTIME)
    @interface AhaoSelect {
        String value() default "";
    }

    interface AhaoInterface {
        @AhaoSelect("select * from t")
        List<Object> findList();
    }

    private static class AhaoInvocationHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getDeclaringClass().equals(Object.class)) {
                method.invoke(this, args);
            }
            AhaoSelect annotation = method.getAnnotation(AhaoSelect.class);
            String sql = annotation.value();
            System.out.println("执行sql语句: " + sql);
            return Arrays.asList("1", "2");
        }
    }

    @Test
    void test() {
        AhaoInterface bean = (AhaoInterface) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{AhaoInterface.class}, new AhaoInvocationHandler());
        List<Object> list = bean.findList();
        Assertions.assertEquals(2, list.size());
    }
}
