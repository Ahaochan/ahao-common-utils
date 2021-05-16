package moe.ahao.util.spring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {SpringContextHolder.class, EventTest.class})
public class EventTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void test() {
        context.publishEvent("hello world");
    }

    @EventListener
    public void listener(String msg) {
        System.out.println(msg);
    }
}
