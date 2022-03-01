package moe.ahao.java8.time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

class TimeTest {
    @Test
    void api() {
        // 日期API
        LocalDate date = LocalDate.of(2022, 2, 28);
        Assertions.assertEquals(2022, date.getYear());
        Assertions.assertEquals(2, date.getMonthValue());
        Assertions.assertEquals(28, date.getDayOfMonth());
        int year = date.getYear();
        Month month = date.getMonth();
        int dayOfMonth = date.getDayOfMonth();
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        int length = date.lengthOfMonth();
        boolean leapYear = date.isLeapYear();
        System.out.println(year + "年" + month.getValue() + "月" + dayOfMonth + "日");
        System.out.println(month.getValue() + "月有" + length + "天");
        System.out.println("是一周的周几?" + dayOfWeek);
        System.out.println(year + "年是否闰年: " + leapYear);

        // 时间API
        LocalTime time = LocalTime.of(12, 23, 34);
        int hour = time.getHour();
        int minute = time.getMinute();
        int second = time.getSecond();
        System.out.println(hour + "时" + minute + "分" + second + "秒");

        // 日期时间API
        LocalDateTime dateTime = date.atTime(time);
        Assertions.assertEquals(date, dateTime.toLocalDate());
        Assertions.assertEquals(time, dateTime.toLocalTime());

        // 时间戳
        Instant instant = Instant.ofEpochSecond(120, 100000);
    }

    @Test
    void adjust() {
        LocalDate date = LocalDate.of(2022, 2, 28);
        Assertions.assertEquals(2022, date.getYear());
        Assertions.assertEquals(2, date.getMonthValue());
        Assertions.assertEquals(28, date.getDayOfMonth());

        // 修改时间
        Assertions.assertEquals(2023, date.withYear(2023).getYear());
        Assertions.assertEquals(3, date.withMonth(3).getMonthValue());
        Assertions.assertEquals(2, date.withDayOfMonth(2).getMonthValue());

        // 加减日期
        Assertions.assertEquals(2023, date.plusYears(1).getYear());
        Assertions.assertEquals(1, date.minusMonths(1).getMonthValue());
        Assertions.assertEquals(1, date.plus(1, ChronoUnit.DAYS).getDayOfMonth());

        // 返回本年的第一天
        Assertions.assertEquals(1, date.with(TemporalAdjusters.firstDayOfYear()).getDayOfMonth());
        // 返回本年的最后一天
        Assertions.assertEquals(31, date.with(TemporalAdjusters.lastDayOfYear()).getDayOfMonth());
        // 返回下一年的第一天
        Assertions.assertEquals(1, date.with(TemporalAdjusters.firstDayOfNextYear()).getDayOfMonth());
        // 返回下一年的最后一天
        // Assertions.assertEquals(31, date.with(TemporalAdjusters.lastDayOfNextYear()).getDayOfMonth());
        // 返回当月的第一天
        Assertions.assertEquals(1, date.with(TemporalAdjusters.firstDayOfMonth()).getDayOfMonth());
        // 返回当月的最后一天
        Assertions.assertEquals(28, date.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth());
        // 返回下月的第一天
        Assertions.assertEquals(1, date.with(TemporalAdjusters.firstDayOfNextMonth()).getDayOfMonth());
        // 返回下月的最后一天
        // Assertions.assertEquals(31, date.with(TemporalAdjusters.lastDayOfNextMonth()).getDayOfMonth());
        // 返回同一个月中第一个星期几
        Assertions.assertEquals(DayOfWeek.TUESDAY, date.with(TemporalAdjusters.firstInMonth(DayOfWeek.TUESDAY)).getDayOfWeek());
        // 返回同一个月中最后一个星期几
        Assertions.assertEquals(DayOfWeek.TUESDAY, date.with(TemporalAdjusters.lastInMonth(DayOfWeek.TUESDAY)).getDayOfWeek());
        // 返回当前月的第几周的星期几
        Assertions.assertEquals(DayOfWeek.TUESDAY, date.with(TemporalAdjusters.dayOfWeekInMonth(1, DayOfWeek.TUESDAY)).getDayOfWeek());
        // 返回后一个/前一个星期几
        Assertions.assertEquals(DayOfWeek.TUESDAY, date.with(TemporalAdjusters.next(DayOfWeek.TUESDAY)).getDayOfWeek());
        Assertions.assertEquals(DayOfWeek.TUESDAY, date.with(TemporalAdjusters.previous(DayOfWeek.TUESDAY)).getDayOfWeek());
        // 返回后一个/前一个星期几, 如果今天是星期几就返回今天
        Assertions.assertEquals(DayOfWeek.TUESDAY, date.with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY)).getDayOfWeek());
        Assertions.assertEquals(DayOfWeek.TUESDAY, date.with(TemporalAdjusters.previousOrSame(DayOfWeek.TUESDAY)).getDayOfWeek());
    }

    @Test
    void format() {
        LocalDateTime dateTime = LocalDateTime.of(2022, 2, 28, 12, 23, 34);
        Assertions.assertEquals("20220228", dateTime.format(DateTimeFormatter.BASIC_ISO_DATE));
        Assertions.assertEquals("2022-02-28", dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE));
        Assertions.assertEquals("12:23:34", dateTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        Assertions.assertEquals("2022-02-28", dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Assertions.assertEquals("今天是：2022年 二月 28日 星期一", dateTime.format(DateTimeFormatter.ofPattern("今天是：yyyy年 MMMM dd日 E", Locale.CHINESE)));

        Assertions.assertEquals(dateTime.toLocalDate(), LocalDate.parse("2022-02-28", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Assertions.assertEquals(dateTime, LocalDateTime.parse("2022-02-28 12:23:34", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @Test
    void zone() {
        ZoneId shanghaiZoneId = ZoneId.of("Asia/Shanghai");
        ZoneId systemZoneId = ZoneId.systemDefault();

        Set<String> zoneIds = ZoneId.getAvailableZoneIds();
        zoneIds.forEach(System.out::println);

        Assertions.assertTrue(zoneIds.contains(shanghaiZoneId.getId()));
        Assertions.assertTrue(zoneIds.contains(systemZoneId.getId()));
    }

    @Test
    void convert() {
        Date now = new Date();
        Instant instant = now.toInstant();
        Assertions.assertEquals(instant.toEpochMilli(), now.getTime());

        LocalDateTime datetime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime time = instant.atZone(ZoneId.systemDefault()).toLocalTime();
        Assertions.assertEquals(date, datetime.toLocalDate());
        Assertions.assertEquals(time, datetime.toLocalTime());

        Date now1 = Date.from(datetime.atZone(ZoneId.systemDefault()).toInstant());
        Assertions.assertEquals(now1, now);
    }

    @Test
    void duration() {
        // 日期格式的时间段
        Period period = Period.between(LocalDate.of(2022, 2, 20), LocalDate.of(2022, 2, 28));
        Assertions.assertEquals(8, period.getDays());

        // 时间格式的时间段
        Duration duration = Duration.between(LocalTime.of(17, 2, 20), LocalTime.of(17, 2, 28));
        Assertions.assertEquals(8, duration.getSeconds());
    }
}
