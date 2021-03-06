package moe.ahao.util.commons.lang;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.AntPathMatcher;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * 字符串工具类
 *
 * @author Ahaochan
 */
public class StringHelper {
    private static final String IGNORE_SEPARATOR_REGEX = "[\\u0000-\\u002F\\u003A-\\u0040\\u005B-\\u0060\\u007B-\\u007F\\u0080-\\u00FF\\u2000-\\u206F\\u3000-\\u3020\\uFF00-\\uFFEF]{0,20}";
    /* 空字符串 */
    public static final String EMPTY_STRING = StringUtils.EMPTY;
    /* 点 */
    public static final char DOT = '.';
    /* 下划线 */
    public static final char UNDERSCORE = '_';
    /* 逗点及空格 */
    public static final String COMMA_SPACE = ", ";
    /* 逗点 */
    public static final String COMMA = ",";
    /* 开始括号 */
    public static final String OPEN_PAREN = "(";
    /* 结束括号 */
    public static final String CLOSE_PAREN = ")";
    /* 单引号 */
    public static final char SINGLE_QUOTE = '\'';

    public static final String CRLF = "\r\n";


    /**
     * 以 searchStrings 中任意一个元素结尾的字符串返回true, 忽略大小写
     *
     * @param string        字符串
     * @param searchStrings 待匹配字符串
     * @return 以 searchStrings 中任意一个元素结尾的字符串返回true
     */
    public static boolean endsWithAnyIgnoreCase(final CharSequence string, final CharSequence... searchStrings) {
        for (CharSequence search : searchStrings) {
            if (StringUtils.endsWithIgnoreCase(string, search)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 去除字符串首尾空格
     * 32 为 普通空格
     * 160 为 html的空格 &nbsp;
     * 12288 为 一个汉字宽度的空格
     *
     * @param str 待处理的字符串
     * @return 去除空格的字符串
     */
    public static String trim(String str) {
        if (str == null) {
            return null;
        }
        char[] val = str.toCharArray();
        int len = val.length;
        int st = 0;

        while ((st < len) &&
                StringUtils.equalsAny(val[st] + "",
                        (char) (32) + "",
                        (char) (160) + "",
                        (char) (12288) + "")) {
            st++;
        }
        while ((st < len) &&
                StringUtils.equalsAny(val[len - 1] + "",
                        (char) (32) + "",
                        (char) (160) + "",
                        (char) (12288) + "")) {
            len--;
        }
        return ((st > 0) || (len < val.length)) ? str.substring(st, len) : str;
    }

    public static String urlEncode(char ch, Charset encoding) {
        return urlEncode(String.valueOf(ch), encoding);
    }
    public static String urlEncode(String string, Charset encoding) {
        try {
            return URLEncoder.encode(string, encoding.name());
        } catch (UnsupportedEncodingException ignored) { // 不可能捕获此异常, 此方法就是为了不捕获此异常所写
        }
        return null;
    }
    public static boolean urlMatch(String pattern, String path) {
        return new AntPathMatcher().match(pattern, path);
    }
    public static String null2Empty(String str) {
        return str == null ? "" : str;
    }
    public static String null2Empty(Object obj) {
        return obj == null ? "" : String.valueOf(obj);
    }

    /**
     * 替换下标在 [start, end) 的字符串为 ch
     * @param string 需要替换的字符串
     * @param start  起始下标
     * @param end    终止下标, 负数则为倒数
     * @param ch     替换为目标字符
     */
    public static String replace(String string, int start, int end, char ch) {
        if (StringUtils.isEmpty(string)) {
            return string;
        }
        if (end < 0) {
            end = string.length() + end;
        }
        if (start > end) {
            throw new IllegalArgumentException("start(" + start + ") must less than end(" + end + ")");
        }
        start = Math.max(0, start);
        end = Math.min(end, string.length());
        StringBuilder sb = new StringBuilder(string);
        for (int i = start; i < end; i++) {
            sb.setCharAt(i, ch);
        }
        return sb.toString();
    }

    /**
     * 字符串转驼峰
     * <pre>
     * StringUtil.toCamelcase(null, *)              = null
     * StringUtil.toCamelcase("", *)                = ""
     * StringUtil.toCamelcase("_a", '_')            = "A"
     * StringUtil.toCamelcase("_A", '_')            = "A"
     * StringUtil.toCamelcase("my_order_id", '_')   = "MyOrderId"
     * </pre>
     * @param source 源字符串
     * @param split 分隔符
     * @return 驼峰式字符串
     */
    public static String toCamelcase(String source, char split) {
        if (source == null || source.length() <= 1) {
            return StringUtils.remove(source, split);
        }
        int len = source.length();
        StringBuilder sb = new StringBuilder(len);
        int gap = 32;
        for (int i = 1; i < len; i++) {
            char ch1 = source.charAt(i - 1);
            char ch2 = source.charAt(i);

            if (ch1 != '_' || ch2 != '_') {
                if (ch1 == '_') {
                    sb.append(ch2 >= 'a' && ch2 <= 'z' ? (char) (ch2 - gap) : ch2);
                    i++;
                } else {
                    sb.append(ch1);
                }
            }
        }

        char lastChar = source.charAt(len - 1);
        if (lastChar != '_' && source.charAt(len - 2) != '_') {
            sb.append(lastChar);
        }
        return sb.toString();
    }

    // ====================================== 汉字处理相关 ==================================================
    public static boolean containChinese(CharSequence charSequence) {
        for (int i = 0, len = charSequence.length(); i < len; i++) {
            char ch = charSequence.charAt(i);
            if(isChinese(ch)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        Character.UnicodeBlock[] chinese = {
                // CJK的意思是 Chinese、Japanese、Korea 的简写 ，实际上就是指中日韩三国的象形文字的 Unicode 编码
                Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS,              // 4E00-9FBF  : CJK 统一表意符号
                Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS,        // F900-FAFF  : CJK 兼容象形文字
                Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A,  // 3400-4DBF  : CJK 统一表意符号扩展 A
                Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B,  // 20000-2A6DF: CJK 统一表意符号扩展 B
                Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C,  // 2A700–2B73F: CJK 统一表意符号扩展 C
                Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D,  // 2B740–2B81F: CJK 统一表意符号扩展 D
                Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION,         // 3000-303F  : CJK 符号和标点
                Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS,       // FF00-FFEF  : 半角及全角形式
                Character.UnicodeBlock.GENERAL_PUNCTUATION,                 // 2000-206F  : 常用标点
        };
        return ArrayUtils.contains(chinese, ub);

    }
    // ====================================== 汉字处理相关 ==================================================
}
