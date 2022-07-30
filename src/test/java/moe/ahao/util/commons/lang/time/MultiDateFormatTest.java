package moe.ahao.util.commons.lang.time;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import moe.ahao.util.commons.juc.ConcurrentTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.Callable;

class MultiDateFormatTest {
    @Test
    void test() throws Exception {
        ObjectMapper om = new ObjectMapper();
        om.setDateFormat(new MultiDateFormat());

        List<Callable<Long>> taskList = new ArrayList<>();
        int threadCount = 59;
        for (int i = 0; i < threadCount; i++) {
            int index = i;
            taskList.add(() -> {
                try {
                    String now = "{\"now\":\"2022-07-30 15:30:"+index+"\"}";
                    AhaoData data = om.readValue(now, AhaoData.class);
                    return data.getNow().getTime();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                return null;
            });
        }
        List<Long> list = ConcurrentTestUtils.concurrentCallable(threadCount, taskList);
        Set<Long> set = new HashSet<>(list);
        System.out.println("执行完毕");
        Assertions.assertEquals(list.size(), set.size());
    }

    static class AhaoData {
        private Date now;
        public Date getNow() {
            return now;
        }
        public void setNow(Date now) {
            this.now = now;
        }
    }
}
