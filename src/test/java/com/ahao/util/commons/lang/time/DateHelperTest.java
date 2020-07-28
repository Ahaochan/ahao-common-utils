package com.ahao.util.commons.lang.time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateHelperTest {
    @Test
    public void now() {
        String date = DateHelper.getNow("yyyy-MM-dd");
        int year = DateHelper.getNowYear();
        int month = DateHelper.getNowMonth();
        int day = DateHelper.getNowDay();
        Assertions.assertEquals(date,  year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day));


    }

    @Test
    public void addTime() {
        Date now = new Date();

        Date oneDayLater = DateHelper.addTime(now, 1, TimeUnit.DAYS);
        Assertions.assertEquals(DateHelper.DAY_TIME, oneDayLater.getTime() - now.getTime());

        Date twoDayBefore = DateHelper.addTime(now, -2, TimeUnit.DAYS);
        Assertions.assertEquals(DateHelper.DAY_TIME * -2, twoDayBefore.getTime() - now.getTime());
    }

    @Test
    public void convert() throws Exception {
        String now = "2019-10-24 10:24:10.240";
        String format = "yyyy-MM-dd HH:mm:ss.SSS";

        Date except1 = new SimpleDateFormat(format).parse(now);
        LocalDateTime localDateTime = LocalDateTime.parse(now, DateTimeFormatter.ofPattern(format));
        Date actual1 = DateHelper.toDate(localDateTime);
        System.out.println(DateHelper.getString(actual1, format));
        Assertions.assertEquals(except1.getTime(), actual1.getTime());

        Date except2 = new SimpleDateFormat("yyyy-MM-dd").parse(now);
        LocalDate localDate = LocalDate.parse(now, DateTimeFormatter.ofPattern(format));
        Date actual2 = DateHelper.toDate(localDate);
        System.out.println(DateHelper.getString(actual2, format));
        Assertions.assertEquals(except2.getTime(), actual2.getTime());

        Date except3 = new Date();
        Instant instant = Instant.now();
        Date actual3 = DateHelper.toDate(instant);
        System.out.println(DateHelper.getString(actual3, format));
        Assertions.assertTrue(Math.abs(except3.getTime() - actual3.getTime()) < 10);
    }

    @Test
    public void multiDateFormat() throws Exception {
        String now1 = "2019-10-24";
        String now2 = "2019-10-24 10:24:10";

        MultiDateFormat dateFormat = new MultiDateFormat();
        Date parse1 = dateFormat.parse(now1);
        Date parse2 = dateFormat.parse(now2);
        System.out.println(parse1);
        System.out.println(parse2);
        Assertions.assertNotNull(parse1);
        Assertions.assertNotNull(parse2);

        String format1 = dateFormat.format(parse1);
        String format2 = dateFormat.format(parse2);
        System.out.println(format1);
        System.out.println(format2);
        Assertions.assertEquals(now1 + " 00:00:00", format1);
        Assertions.assertEquals(now2, format2);
    }
}
