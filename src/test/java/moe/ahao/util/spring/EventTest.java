package moe.ahao.util.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
    classes = {SpringContextHolder.class, EventTest.class})
class EventTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void test() {
        context.publishEvent("hello world");
    }

    @EventListener
    public void listener(String msg) {
        System.out.println(msg);
    }
}
