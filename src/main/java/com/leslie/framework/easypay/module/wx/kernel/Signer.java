package com.leslie.framework.easypay.module.wx.kernel;

import com.leslie.framework.easypay.module.wx.constant.SIGN_TYPE_ENUM;
import com.leslie.framework.easypay.module.wx.constant.WxPayConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 微信支付签名构造器
 *
 * @author leslie
 * @date 2020/9/8
 */
@Slf4j
public class Signer {

    /**
     * 构建待签名内容
     *
     * @param params 参数集合
     * @return 待签名内容
     */
    public String signContent(Map<String, String> params) {
        if (params == null) {
            return null;
        }

        StringBuilder content = new StringBuilder();
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            content.append(i == 0 ? "" : "&").append(key).append("=").append(value);
        }
        return content.toString();
    }

    /**
     * 计算签名
     *
     * @param content  待签名的内容
     * @param apiKey   私钥
     * @param signType 加密方式
     * @return 签名串
     */
    public String sign(String content, String apiKey, SIGN_TYPE_ENUM signType) {
        content = content + "key=" + apiKey;

        try {

            if (SIGN_TYPE_ENUM.MD5 == signType) {
                return md5(content);
            } else {
                return hmacSha256(content, apiKey);
            }

        } catch (Exception e) {
            String errorMessage = "签名遭遇异常，content=" + content + " privateKeySize=" + apiKey.length() + " reason=" + e.getMessage();
            log.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    /**
     * 对参数集合进行验签
     * 必须包含sign字段，否则返回false
     *
     * @param params   参数集合
     * @param apiKey   api密钥
     * @param signType 加密方式
     * @return true：验证成功；false：验证失败
     */
    public boolean verify(Map<String, String> params, String apiKey, SIGN_TYPE_ENUM signType) {
        if (params.containsKey(WxPayConstants.FIELD_SIGN)) {
            String sign = params.get(WxPayConstants.FIELD_SIGN);
            String content = signContent(params);
            return StringUtils.equals(sign, sign(content, apiKey, signType));
        }
        return false;
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

        return content.toString().toUpperCase();
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

        return content.toString().toUpperCase();
    }
}
