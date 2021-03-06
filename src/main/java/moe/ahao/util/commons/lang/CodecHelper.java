package moe.ahao.util.commons.lang;

import moe.ahao.util.commons.io.IOHelper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by Ahaochan on 2018/1/2.
 *
 * 加解密工具类
 */
public class CodecHelper {

    public static String toBase64(String data){
        return Base64.encodeBase64String(data.getBytes(StandardCharsets.UTF_8));
    }

    public static String toBase64(InputStream inputStream){
        return Base64.encodeBase64String(IOHelper.toByte(inputStream));
    }

    public static String parseBase64(String base64){
        byte[] data = Base64.decodeBase64(base64);
        return new String(data, StandardCharsets.UTF_8);
    }

    public static boolean isBase64(String base64){
        return Base64.isBase64(base64);
    }

    public static String md5(String data){
        return DigestUtils.md5Hex(data.getBytes(StandardCharsets.UTF_8));
    }

    public static String hmacSha256(String key, String data) throws Exception {
        String algorithm = "HmacSHA256";
        Charset charset = StandardCharsets.UTF_8;

        Mac HmacSHA256 = Mac.getInstance(algorithm);
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(charset), algorithm);
        HmacSHA256.init(secretKey);
        String str = Hex.encodeHexString(HmacSHA256.doFinal(data.getBytes(charset)));
        return str.toUpperCase();
    }
}
