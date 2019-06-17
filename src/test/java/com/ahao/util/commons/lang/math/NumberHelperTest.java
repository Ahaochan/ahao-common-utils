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
    void isNumber(){
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
}
