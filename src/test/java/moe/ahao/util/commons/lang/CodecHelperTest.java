package moe.ahao.util.commons.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CodecHelperTest {
    @Test
    public void base64() {
        String data = "hello world";

        String base64 = CodecHelper.toBase64(data);
        System.out.println(base64);
        Assertions.assertTrue(CodecHelper.isBase64(base64));
        String parse = CodecHelper.parseBase64(base64);
        Assertions.assertEquals(data, parse);
    }

    @Test
    public void md5() {
        String data = "hello world";

        String md5 = CodecHelper.md5(data);
        System.out.println(md5);
        Assertions.assertEquals(32, md5.length());
    }
}
