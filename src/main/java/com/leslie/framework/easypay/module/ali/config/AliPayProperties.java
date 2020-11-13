package com.leslie.framework.easypay.module.ali.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @author leslie
 * @date 2020/9/2
 */
@Getter
@Setter
@Validated
@ConfigurationProperties("uni.pay.ali")
public class AliPayProperties {
    /**
     * 开启支付宝支付功能
     */
    private boolean enable = true;
    /**
     * 网关域名
     * 线上为：openapi.alipay.com
     * 沙箱为：openapi.alipaydev.com
     */
    @NotBlank
    private String gatewayHost = "openapi.alipay.com";
    /**
     * AppId
     */
    @NotBlank
    private String appId;
    /**
     * 支付宝公钥
     */
    @NotBlank
    private String alipayPublicKey;
    /**
     * 应用私钥
     */
    private String merchantPrivateKey;
    /**
     * 应用公钥证书文件路径
     */
    private String merchantCertPath;
    /**
     * 支付宝公钥证书文件路径
     */
    private String alipayCertPath;
    /**
     * 支付宝根证书文件路径
     */
    private String alipayRootCertPath;
    /**
     * 异步通知回调地址（可选）
     */
    private String notifyUrl;
    /**
     * AES密钥（可选）
     */
    private String encryptKey;
    /**
     * 签名提供方的名称(可选)
     */
    private String signProvider;
}
