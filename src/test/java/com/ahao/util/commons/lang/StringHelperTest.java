package com.ahao.util.commons.lang;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static com.ahao.util.commons.lang.StringHelper.*;

class StringHelperTest {

    @Test
    void urlEncodeTest() {
        Assertions.assertEquals("hello+world", urlEncode("hello world", StandardCharsets.UTF_8));
        Assertions.assertEquals("%E4%BD%A0%E5%A5%BD", urlEncode("你好", StandardCharsets.UTF_8));
        Assertions.assertEquals("%E4%B8%96%E7%95%8C", urlEncode("世界", StandardCharsets.UTF_8));
        Assertions.assertEquals("%3C%2Fxml%3E", urlEncode("</xml>", StandardCharsets.UTF_8));
        Assertions.assertEquals("%7B%22abc%22%3A%22123%22%7D", urlEncode("{\"abc\":\"123\"}", StandardCharsets.UTF_8));
    }


    // ====================================== 汉字处理相关 ==================================================
    @Test
    void containChineseTest() {
        Assertions.assertTrue(containChinese("你好, world"));
        Assertions.assertTrue(containChinese("你好, 世界"));
        Assertions.assertTrue(containChinese("hello world！"));
        Assertions.assertTrue(containChinese("！￥……（）——：；“”‘’《》，。？、"));


        Assertions.assertFalse(containChinese("やめて"));
        Assertions.assertFalse(containChinese("한글"));
        Assertions.assertFalse(containChinese("!@#$%^&*()_+{}[]|\"'?/:;<>,."));
        Assertions.assertFalse(containChinese("www.micmiu.com"));
        Assertions.assertFalse(containChinese("hello world!"));
        Assertions.assertFalse(containChinese("hello world"));
    }

    @Test
    void isChineseTest() {
        Assertions.assertTrue(isChinese('我'));
        Assertions.assertTrue(isChinese('錒'));
        Assertions.assertTrue(isChinese('櫂'));
        Assertions.assertTrue(isChinese('呢'));
        Assertions.assertTrue(isChinese('。'));
        Assertions.assertTrue(isChinese('，'));
        Assertions.assertTrue(isChinese('　'));
        Assertions.assertTrue(isChinese('！'));
        Assertions.assertTrue(isChinese('？'));

        Assertions.assertFalse(isChinese('a'));
        Assertions.assertFalse(isChinese('j'));
        Assertions.assertFalse(isChinese('&'));
        Assertions.assertFalse(isChinese('-'));
        Assertions.assertFalse(isChinese('='));
        Assertions.assertFalse(isChinese('/'));
        Assertions.assertFalse(isChinese('`'));
        Assertions.assertFalse(isChinese('~'));
        Assertions.assertFalse(isChinese('+'));
        Assertions.assertFalse(isChinese(' '));
    }
    // ====================================== 汉字处理相关 ==================================================

}
