package com.leslie.framework.easypay.module.wx.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.time.Duration;

/**
 * @author leslie
 * @date 2020/9/2
 */
@Getter
@Setter()
@Validated
@ConfigurationProperties("uni.pay.wx")
public class WxPayProperties {
    /**
     * 开启微信支付功能
     */
    private boolean enable = true;
    /**
     * 沙箱
     */
    private boolean enableSandbox = false;
    /**
     * 协议
     */
    private String protocol = "https";
    /**
     * 域名
     */
    @NotBlank
    private String domain = "api.mch.weixin.qq.com";
    /**
     * App ID
     */
    @NotBlank
    private String appId;
    /**
     * 获取 Mch ID
     */
    @NotBlank
    private String mchId;
    /**
     * API 密钥
     */
    @NotBlank
    private String key;
    /**
     * 商户证书路径
     */
    private String certPath;
    /**
     * 异步通知地址
     */
    private String notifyUrl;
    /**
     * 加密方式
     */
    private WxPayConstants.SIGN_TYPE_ENUM signType = WxPayConstants.SIGN_TYPE_ENUM.MD5;
    /**
     * HTTP(S) 连接超时时间，单位毫秒
     */
    private Duration connectTimeout = Duration.ofMillis(6000);
    /**
     * HTTP(S) 读数据超时时间，单位毫秒
     */
    private Duration readTimeout = Duration.ofMillis(8000);
    ///**
    // * 是否自动上报
    // */
    //private boolean enableReport = true;
    ///**
    // * 进行健康上报的线程的数量
    // */
    //private int reportWorkerNum = 6;
    ///**
    // * 健康上报缓存消息的最大数量。会有线程去独立上报
    // * 粗略计算：加入一条消息200B，10000消息占用空间 2000 KB，约为2MB，可以接受
    // */
    //private int reportQueueMaxSize = 10000;
    ///**
    // * 批量上报，一次最多上报多个数据
    // */
    //private int reportBatchSize = 10;

}
