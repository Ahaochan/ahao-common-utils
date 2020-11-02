package com.ahao.java8;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalTest {

    @Test
    public void format() {
        BigDecimal b1 = new BigDecimal("100");
        BigDecimal b3 = new BigDecimal("300");
        BigDecimal b0 = b1.divide(b3, 2, RoundingMode.HALF_UP);

        Assertions.assertEquals("100", b1.toString());            // 有必要时使用科学计数法
        Assertions.assertEquals("100", b1.toPlainString());       // 不使用任何指数
        Assertions.assertEquals("100", b1.toEngineeringString()); // 有必要时使用工程计数法
        Assertions.assertEquals("300", b3.toString());
        Assertions.assertEquals("300", b3.toPlainString());
        Assertions.assertEquals("300", b3.toEngineeringString());
        Assertions.assertEquals("0.33", b0.toString());
        Assertions.assertEquals("0.33", b0.toPlainString());
        Assertions.assertEquals("0.33", b0.toEngineeringString());

        Assertions.assertEquals("100.00", b1.setScale(2, RoundingMode.HALF_UP).toString());
    }
}
