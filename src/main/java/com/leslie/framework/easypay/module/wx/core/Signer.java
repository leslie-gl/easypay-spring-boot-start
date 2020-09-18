package com.leslie.framework.easypay.module.wx.core;

import com.leslie.framework.easypay.common.util.SignUtils;
import com.leslie.framework.easypay.module.wx.config.WxPayConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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
    public String sign(String content, String apiKey, WxPayConstants.SIGN_TYPE_ENUM signType) {
        content = content + "key=" + apiKey;

        try {

            if (WxPayConstants.SIGN_TYPE_ENUM.MD5 == signType) {
                return SignUtils.md5(content).toUpperCase();
            } else {
                return SignUtils.hmacSha256(content, apiKey).toUpperCase();
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
    public boolean verify(Map<String, String> params, String apiKey, WxPayConstants.SIGN_TYPE_ENUM signType) {
        if (params.containsKey(WxPayConstants.SIGN_FIELD)) {
            String sign = params.get(WxPayConstants.SIGN_FIELD);
            String content = signContent(params);
            return StringUtils.equals(sign, sign(content, apiKey, signType));
        }
        return false;
    }

}
