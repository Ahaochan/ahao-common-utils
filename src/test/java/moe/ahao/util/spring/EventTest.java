package moe.ahao.util.spring;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

import java.util.Date;

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
    @Order(1)
    public void listener1(String msg1) throws Exception {
        System.out.println("msg1: " + msg1 + "现在时间: " + DateFormatUtils.format(new Date(), "HH:mm:ss.SSS"));
        Thread.sleep(1000);
    }

    @EventListener
    @Order(2)
    public void listener2(String msg2) throws Exception {
        System.out.println("msg2: " + msg2 + "现在时间: " + DateFormatUtils.format(new Date(), "HH:mm:ss.SSS"));
        Thread.sleep(1000);
    }

    @EventListener
    @Order(3)
    public void listener3(String msg3) throws Exception {
        System.out.println("msg3: " + msg3 + "现在时间: " + DateFormatUtils.format(new Date(), "HH:mm:ss.SSS"));
        Thread.sleep(1000);
    }
}
