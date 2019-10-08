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

    @Test
    void urlMatchTest() {
        Assertions.assertTrue(urlMatch("com/t?st.jsp", "com/test.jsp"));
        Assertions.assertTrue(urlMatch("com/t?st.jsp", "com/tast.jsp"));
        Assertions.assertTrue(urlMatch("com/t?st.jsp", "com/txst.jsp"));
        Assertions.assertFalse(urlMatch("com/t?st.jsp", "com/tst.jsp"));
        Assertions.assertFalse(urlMatch("com/t?st.jsp", "com/test1.jsp"));
        Assertions.assertFalse(urlMatch("com/t?st.jsp", "com/test"));

        Assertions.assertTrue(urlMatch("com/*.jsp", "com/123.jsp"));
        Assertions.assertTrue(urlMatch("com/*.jsp", "com/abc.jsp"));
        Assertions.assertTrue(urlMatch("com/*.jsp", "com/.jsp"));

        Assertions.assertTrue(urlMatch("com/**/test.jsp", "com/a/test.jsp"));
        Assertions.assertTrue(urlMatch("com/**/test.jsp", "com/a/b/test.jsp"));
        Assertions.assertTrue(urlMatch("com/**/test.jsp", "com/test.jsp"));
        Assertions.assertTrue(urlMatch("com/**/*.jsp", "com/a/test.jsp"));
        Assertions.assertTrue(urlMatch("com/**/*.jsp", "com/a/abc.jsp"));
        Assertions.assertTrue(urlMatch("com/**/*.jsp", "com/a/b/test.jsp"));
        Assertions.assertTrue(urlMatch("com/**/*.jsp", "com/a/b/abc.jsp"));
        Assertions.assertTrue(urlMatch("com/**/*.jsp", "com/test.jsp"));
        Assertions.assertTrue(urlMatch("com/**/*.jsp", "com/abc.jsp"));
    }

    @Test
    public void replace() {
        Assertions.assertNull(StringHelper.replace(null, 3, 10, '*'));
        Assertions.assertEquals("", StringHelper.replace("", 3, 10, '*'));
        Assertions.assertEquals("123******456", StringHelper.replace("123abcdef456", 3, 9, '*'));
        Assertions.assertEquals("123******456", StringHelper.replace("123abcdef456", 3, -3, '*'));
        Assertions.assertEquals("你好**世界", StringHelper.replace("你好我的世界", 2, -2, '*'));
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            StringHelper.replace("123abcdef456", 3, -10, '*');
        });
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
