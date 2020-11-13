package com.leslie.framework.easypay.common.util;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;

/**
 * 字符串工具类
 *
 * @author leslie
 * @date 2020/9/7
 */
public class StrUtils {

    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final Random RANDOM = new SecureRandom();

    private StrUtils() {
    }

    /**
     * 转换成UTF-8字符串
     *
     * @param str 字符串
     * @return UTF-8字符串
     */
    public static String convert2Utf8(String str) {
        return new String(str.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    /**
     * 生成随机位数字符串
     *
     * @return String 随机字符串
     */
    public static String generateRandomStr(int var) {
        char[] chars = new char[var];
        for (int index = 0; index < chars.length; ++index) {
            chars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(chars);
    }
}
