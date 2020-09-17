package com.leslie.framework.easypay.module.wx;

import com.leslie.framework.easypay.common.util.StrUtils;
import com.leslie.framework.easypay.common.util.XmlUtils;
import com.leslie.framework.easypay.module.wx.config.WxPayProperties;
import com.leslie.framework.easypay.module.wx.constant.WxPayConstants;
import com.leslie.framework.easypay.module.wx.core.Client;
import com.leslie.framework.easypay.module.wx.core.Signer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * @author leslie
 * @date 2020/9/17
 */
@Slf4j
public class WxPay {

    private final Client client;

    private final Signer signer;

    public WxPay(Client client, Signer signer) {
        this.client = client;
        this.signer = signer;
    }

    /**
     * 统一下单
     * 场景：公共号支付、扫码支付、APP支付
     *
     * @param params post的请求数据
     * @return API返回数据
     */
    @Nullable
    public String basicPay(Map<String, String> params) {
        WxPayProperties properties = properties();
        String result = null;

        String url = properties.isEnableSandbox() ? WxPayConstants.SANDBOX_UNIFIEDORDER_URL_SUFFIX : WxPayConstants.UNIFIEDORDER_URL_SUFFIX;
        String domain = properties.getDomain();

        try {
            String xmlParams = XmlUtils.map2Xml(doParams(params));
            result = client.doRequest(url, xmlParams);
        } catch (Exception e) {
            log.error("统一下单异常, domain={} url={} reason={}", domain, url, e.getMessage());
        }
        return result;
    }

    /**
     * 向 Map 中添加必要参数
     * 该函数适用于商户适用于统一下单等接口，不适用于红包、代金券接口
     *
     * @param params post的请求数据
     * @return 完整的请求数据
     */
    private Map<String, String> doParams(Map<String, String> params) {
        WxPayProperties properties = properties();

        boolean exist = !params.containsKey("notify_url") && StringUtils.isNotBlank(properties.getNotifyUrl());
        if (exist) {
            params.put("notify_url", properties.getNotifyUrl());
        }
        params.put("appid", properties.getAppId());
        params.put("mch_id", properties.getMchId());
        params.put("sign_type", properties.getSignType().name());
        params.put("nonce_str", StrUtils.getRandomStr(32));

        String content = signer.signContent(params);
        params.put("sign", signer.sign(content, properties.getKey(), properties.getSignType()));
        return params;
    }

    /**
     * 对参数集合进行验签
     * 必须包含sign字段，否则返回false
     *
     * @param params 参数集合
     * @return true：验证成功；false：验证失败
     */
    public boolean verify(Map<String, String> params) {
        WxPayProperties properties = properties();
        return signer.verify(params, properties.getKey(), properties.getSignType());
    }

    /**
     * 对XML进行验签
     * 必须包含sign字段，否则返回false
     *
     * @param xmlContent XML格式数据
     * @return 签名是否正确
     */
    public boolean verify(String xmlContent) {
        return verify(XmlUtils.xml2Map(xmlContent));
    }

    public WxPayProperties properties() {
        return client.getWxPayProperties();
    }

}
