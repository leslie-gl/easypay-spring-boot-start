package com.leslie.framework.easypay.common.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author leslie
 * @date 2020/9/17
 */
public class SignUtils {

    private SignUtils() {
    }

    /**
     * MD5 加密
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    public static String md5(String data) throws NoSuchAlgorithmException {
        StringBuilder content = new StringBuilder();

        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] array = digest.digest(data.getBytes(StandardCharsets.UTF_8));

        for (byte item : array) {
            content.append(Integer.toHexString((item & 0xFF) | 0x100), 1, 3);
        }

        return content.toString();
    }

    /**
     * HMAC-SHA256 加密
     *
     * @param data       待处理数据
     * @param privateKey 密钥
     * @return 加密结果
     */
    public static String hmacSha256(String data, String privateKey) throws NoSuchAlgorithmException, InvalidKeyException {
        StringBuilder content = new StringBuilder();

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(privateKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));

        byte[] array = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        for (byte item : array) {
            content.append(Integer.toHexString((item & 0xFF) | 0x100), 1, 3);
        }

        return content.toString();
    }

}
