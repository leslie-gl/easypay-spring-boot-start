package com.leslie.framework.easypay.module.wx.config;


import org.apache.http.client.HttpClient;

/**
 * 微信支付 常量
 *
 * @author leslie
 * @date 2020/9/7
 */
public class WxPayConstants {

    public enum SIGN_TYPE_ENUM {
        /**
         * MD5
         */
        MD5,
        /**
         * HMAC-SHA256
         */
        HMAC_SHA_256;
    }

    /**
     * domain
     */
    public static final String DOMAIN_API    = "api.mch.weixin.qq.com";
    public static final String DOMAIN_API2   = "api2.mch.weixin.qq.com";
    public static final String DOMAIN_API_HK = "apihk.mch.weixin.qq.com";
    public static final String DOMAIN_API_US = "apius.mch.weixin.qq.com";

    /**
     * 与微信网关交付中涉及到的字段
     */
    public static final String SUCCESS         = "SUCCESS";
    public static final String FAIL            = "FAIL";
    public static final String SIGN_FIELD      = "sign";
    public static final String SIGN_TYPE_FIELD = "sign_type";

    /**
     * user-agent
     */
    public static final String SDK_VERSION = "WXPaySDK/3.0.9";
    public static final String USER_AGENT;

    static {
        USER_AGENT = SDK_VERSION +
                " (" +
                System.getProperty("os.arch") +
                " " +
                System.getProperty("os.name") +
                " " +
                System.getProperty("os.version") +
                ") Java/" +
                System.getProperty("java.version") +
                " HttpClient/" +
                HttpClient.class.getPackage().getImplementationVersion();
    }


    /**
     * pay url
     */
    public static final String MICROPAY_URL         = "/pay/micropay";
    public static final String UNIFIEDORDER_URL     = "/pay/unifiedorder";
    public static final String ORDERQUERY_URL       = "/pay/orderquery";
    public static final String REVERSE_URL          = "/secapi/pay/reverse";
    public static final String CLOSEORDER_URL       = "/pay/closeorder";
    public static final String REFUND_URL           = "/secapi/pay/refund";
    public static final String REFUNDQUERY_URL      = "/pay/refundquery";
    public static final String DOWNLOADBILL_URL     = "/pay/downloadbill";
    public static final String REPORT_URL           = "/payitil/report";
    public static final String SHORTURL_URL         = "/tools/shorturl";
    public static final String AUTHCODETOOPENID_URL = "/tools/authcodetoopenid";

    /**
     * sandbox url
     */
    public static final String SANDBOX_MICROPAY_URL         = "/sandboxnew/pay/micropay";
    public static final String SANDBOX_UNIFIEDORDER_URL     = "/sandboxnew/pay/unifiedorder";
    public static final String SANDBOX_ORDERQUERY_URL       = "/sandboxnew/pay/orderquery";
    public static final String SANDBOX_REVERSE_URL          = "/sandboxnew/secapi/pay/reverse";
    public static final String SANDBOX_CLOSEORDER_URL       = "/sandboxnew/pay/closeorder";
    public static final String SANDBOX_REFUND_URL           = "/sandboxnew/secapi/pay/refund";
    public static final String SANDBOX_REFUNDQUERY_URL      = "/sandboxnew/pay/refundquery";
    public static final String SANDBOX_DOWNLOADBILL_URL     = "/sandboxnew/pay/downloadbill";
    public static final String SANDBOX_REPORT_URL           = "/sandboxnew/payitil/report";
    public static final String SANDBOX_SHORTURL_URL         = "/sandboxnew/tools/shorturl";
    public static final String SANDBOX_AUTHCODETOOPENID_URL = "/sandboxnew/tools/authcodetoopenid";
}

