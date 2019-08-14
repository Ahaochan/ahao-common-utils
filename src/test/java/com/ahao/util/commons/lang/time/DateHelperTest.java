package com.ahao.util.commons.lang.time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateHelperTest {
    @Test
    public void addTime() {
        Date now = new Date();

        Date oneDayLater = DateHelper.addTime(now, 1, TimeUnit.DAYS);
        Assertions.assertEquals(DateHelper.DAY_TIME, oneDayLater.getTime() - now.getTime());

        Date twoDayBefore = DateHelper.addTime(now, -2, TimeUnit.DAYS);
        Assertions.assertEquals(DateHelper.DAY_TIME * -2, twoDayBefore.getTime() - now.getTime());
    }
}
