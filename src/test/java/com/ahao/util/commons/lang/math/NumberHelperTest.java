package com.ahao.util.commons.lang.math;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

class NumberHelperTest {

    @Test
    void isBetween() {
        Assertions.assertFalse(NumberHelper.isBetween(0, 1, 3));
        Assertions.assertTrue (NumberHelper.isBetween(1, 1, 3));
        Assertions.assertTrue (NumberHelper.isBetween(2, 1, 3));
        Assertions.assertTrue (NumberHelper.isBetween(3, 1, 3));
        Assertions.assertFalse(NumberHelper.isBetween(4, 1, 3));
    }

    @Test
    void between() {
        Assertions.assertEquals(NumberHelper.between(0, 1, 3), 1);
        Assertions.assertEquals(NumberHelper.between(1, 1, 3), 1);
        Assertions.assertEquals(NumberHelper.between(2, 1, 3), 2);
        Assertions.assertEquals(NumberHelper.between(3, 1, 3), 3);
        Assertions.assertEquals(NumberHelper.between(4, 1, 3), 3);
    }

    @Test
    void parseInt() {
        Assertions.assertEquals(NumberHelper.parseInt(null), 0);
        Assertions.assertEquals(NumberHelper.parseInt(false), 0);
        Assertions.assertEquals(NumberHelper.parseInt(Boolean.FALSE), 0);
        Assertions.assertEquals(NumberHelper.parseInt(true), 1);
        Assertions.assertEquals(NumberHelper.parseInt(Boolean.TRUE), 1);
        Assertions.assertEquals(NumberHelper.parseInt(""), 0);
        Assertions.assertEquals(NumberHelper.parseInt("123"), 123);
        Assertions.assertEquals(NumberHelper.parseInt("123.5"), 123);
    }

    @Test
    void isNumber() {
        Assertions.assertTrue(NumberHelper.isNumber(1));
        Assertions.assertTrue(NumberHelper.isNumber(0x21));
        Assertions.assertTrue(NumberHelper.isNumber(1L));
        Assertions.assertTrue(NumberHelper.isNumber(1.2));
        Assertions.assertTrue(NumberHelper.isNumber(1.2f));
        Assertions.assertTrue(NumberHelper.isNumber(1.2d));

        Assertions.assertTrue(NumberHelper.isNumber(Byte.parseByte("1")));
        Assertions.assertTrue(NumberHelper.isNumber(Byte.valueOf("1")));
        Assertions.assertTrue(NumberHelper.isNumber(Short.parseShort("2")));
        Assertions.assertTrue(NumberHelper.isNumber(Short.valueOf("2")));
        Assertions.assertTrue(NumberHelper.isNumber(Integer.parseInt("3")));
        Assertions.assertTrue(NumberHelper.isNumber(Integer.valueOf("3")));
        Assertions.assertTrue(NumberHelper.isNumber(Long.parseLong("4")));
        Assertions.assertTrue(NumberHelper.isNumber(Long.valueOf("4")));
        Assertions.assertTrue(NumberHelper.isNumber(Float.parseFloat("4")));
        Assertions.assertTrue(NumberHelper.isNumber(Float.valueOf("4")));
        Assertions.assertTrue(NumberHelper.isNumber(Double.parseDouble("4")));
        Assertions.assertTrue(NumberHelper.isNumber(Double.valueOf("4")));

        Assertions.assertTrue(NumberHelper.isNumber("1"));
        Assertions.assertTrue(NumberHelper.isNumber("1.2"));
        Assertions.assertTrue(NumberHelper.isNumber("012"));
        Assertions.assertTrue(NumberHelper.isNumber(BigDecimal.TEN));
        Assertions.assertTrue(NumberHelper.isNumber(BigInteger.TEN));

        Assertions.assertFalse(NumberHelper.isNumber(null));
        Assertions.assertFalse(NumberHelper.isNumber(""));
        Assertions.assertFalse(NumberHelper.isNumber("a12"));
        Assertions.assertFalse(NumberHelper.isNumber("1.2.3"));
        Assertions.assertFalse(NumberHelper.isNumber(new Date()));
        Assertions.assertFalse(NumberHelper.isNumber(new Object()));
    }

    @Test
    public void equals() {
        Assertions.assertAll("相同数据类型比较",
            () -> Assertions.assertTrue(NumberHelper.equals((byte) 1, (byte) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals((short) 2, (short) 2)),
            () -> Assertions.assertTrue(NumberHelper.equals(3, 3)),
            () -> Assertions.assertTrue(NumberHelper.equals(4L, 4L)),
            () -> Assertions.assertTrue(NumberHelper.equals(5F, 5F)),
            () -> Assertions.assertTrue(NumberHelper.equals(6D, 6D))
        );

        Assertions.assertAll("byte <==> short",
            () -> Assertions.assertTrue(NumberHelper.equals((byte) 1, (short) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals((short) 1, (byte) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals(Byte.MAX_VALUE, (short) Byte.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((short) Byte.MAX_VALUE, Byte.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals(Byte.MIN_VALUE, (short) Byte.MIN_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((short) Byte.MIN_VALUE, Byte.MIN_VALUE))
        );

        Assertions.assertAll("byte <==> int",
            () -> Assertions.assertTrue(NumberHelper.equals((byte) 1, (int) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals((int) 1, (byte) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals(Byte.MAX_VALUE, (int) Byte.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((int) Byte.MAX_VALUE, Byte.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals(Byte.MIN_VALUE, (int) Byte.MIN_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((int) Byte.MIN_VALUE, Byte.MIN_VALUE))
        );

        Assertions.assertAll("byte <==> long",
            () -> Assertions.assertTrue(NumberHelper.equals((byte) 1, (long) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals((long) 1, (byte) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals(Byte.MAX_VALUE, (long) Byte.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((long) Byte.MAX_VALUE, Byte.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals(Byte.MIN_VALUE, (long) Byte.MIN_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((long) Byte.MIN_VALUE, Byte.MIN_VALUE))
        );

        Assertions.assertAll("byte <==> float",
            () -> Assertions.assertTrue(NumberHelper.equals((byte) 1, (float) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals((float) 1, (byte) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals(Byte.MAX_VALUE, (float) Byte.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((float) Byte.MAX_VALUE, Byte.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals(Byte.MIN_VALUE, (float) Byte.MIN_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((float) Byte.MIN_VALUE, Byte.MIN_VALUE))
        );

        Assertions.assertAll("byte <==> double",
            () -> Assertions.assertTrue(NumberHelper.equals((byte) 1, (double) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals((double) 1, (byte) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals(Byte.MAX_VALUE, (double) Byte.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((double) Byte.MAX_VALUE, Byte.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals(Byte.MIN_VALUE, (double) Byte.MIN_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((double) Byte.MIN_VALUE, Byte.MIN_VALUE))
        );

        Assertions.assertAll("short <==> int",
            () -> Assertions.assertTrue(NumberHelper.equals((short) 1, (int) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals((int) 1, (short) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals(Short.MAX_VALUE, (int) Short.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((int) Short.MAX_VALUE, Short.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals(Short.MIN_VALUE, (int) Short.MIN_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((int) Short.MIN_VALUE, Short.MIN_VALUE))
        );

        Assertions.assertAll("short <==> long",
            () -> Assertions.assertTrue(NumberHelper.equals((short) 1, (long) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals((long) 1, (short) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals(Short.MAX_VALUE, (long) Short.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((long) Short.MAX_VALUE, Short.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals(Short.MIN_VALUE, (long) Short.MIN_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((long) Short.MIN_VALUE, Short.MIN_VALUE))
        );

        Assertions.assertAll("short <==> float",
            () -> Assertions.assertTrue(NumberHelper.equals((short) 1, (float) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals((float) 1, (short) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals(Short.MAX_VALUE, (float) Short.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((float) Short.MAX_VALUE, Short.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals(Short.MIN_VALUE, (float) Short.MIN_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((float) Short.MIN_VALUE, Short.MIN_VALUE))
        );

        Assertions.assertAll("short <==> double",
            () -> Assertions.assertTrue(NumberHelper.equals((short) 1, (double) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals((double) 1, (short) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals(Short.MAX_VALUE, (double) Short.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((double) Short.MAX_VALUE, Short.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals(Short.MIN_VALUE, (double) Short.MIN_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((double) Short.MIN_VALUE, Short.MIN_VALUE))
        );

        Assertions.assertAll("int <==> long",
            () -> Assertions.assertTrue(NumberHelper.equals((int) 1, (long) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals((long) 1, (int) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals(Integer.MAX_VALUE, (long) Integer.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((long) Integer.MAX_VALUE, Integer.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals(Integer.MIN_VALUE, (long) Integer.MIN_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((long) Integer.MIN_VALUE, Integer.MIN_VALUE))
        );

        Assertions.assertAll("int <==> float",
            () -> Assertions.assertTrue(NumberHelper.equals((int) 1, (float) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals((float) 1, (int) 1)),
            () -> Assertions.assertFalse(NumberHelper.equals(Integer.MAX_VALUE,  (float) Integer.MAX_VALUE)), // TODO 比较错误
            () -> Assertions.assertFalse(NumberHelper.equals((float) Integer.MAX_VALUE, Integer.MAX_VALUE)),  // TODO 比较错误
            () -> Assertions.assertTrue(NumberHelper.equals((Integer.MAX_VALUE / 50),  (float) (Integer.MAX_VALUE / 50))),
            () -> Assertions.assertTrue(NumberHelper.equals((float) (Integer.MAX_VALUE / 50), (Integer.MAX_VALUE / 50))),
            () -> Assertions.assertTrue(NumberHelper.equals(Integer.MIN_VALUE, (float) Integer.MIN_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((float) Integer.MIN_VALUE, Integer.MIN_VALUE))
        );

        Assertions.assertAll("int <==> double",
            () -> Assertions.assertTrue(NumberHelper.equals((int) 1, (double) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals((double) 1, (int) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals(Integer.MAX_VALUE, (double) Integer.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((double) Integer.MAX_VALUE, Integer.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals(Integer.MIN_VALUE, (double) Integer.MIN_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((double) Integer.MIN_VALUE, Integer.MIN_VALUE))
        );

        Assertions.assertAll("long <==> float",
            () -> Assertions.assertTrue(NumberHelper.equals((long) 1, (float) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals((float) 1, (long) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals(Long.MAX_VALUE, (float) Long.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((float) Long.MAX_VALUE, Long.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals(Long.MIN_VALUE, (float) Long.MIN_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((float) Long.MIN_VALUE, Long.MIN_VALUE))
        );

        Assertions.assertAll("long <==> double",
            () -> Assertions.assertTrue(NumberHelper.equals((long) 1, (double) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals((double) 1, (long) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals(Long.MAX_VALUE, (double) Long.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((double) Long.MAX_VALUE, Long.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals(Long.MIN_VALUE, (double) Long.MIN_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((double) Long.MIN_VALUE, Long.MIN_VALUE))
        );

        Assertions.assertAll("float <==> double",
            () -> Assertions.assertTrue(NumberHelper.equals((float) 1, (double) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals((double) 1, (float) 1)),
            () -> Assertions.assertTrue(NumberHelper.equals(Float.MAX_VALUE, (double) Float.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((double) Float.MAX_VALUE, Float.MAX_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals(Float.MIN_VALUE, (double) Float.MIN_VALUE)),
            () -> Assertions.assertTrue(NumberHelper.equals((double) Float.MIN_VALUE, Float.MIN_VALUE))
        );
    }
}
