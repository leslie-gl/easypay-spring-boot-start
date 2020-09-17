package com.leslie.framework.easypay.module.union.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

/**
 * @author leslie
 * @date 2020/9/15
 */
@Getter
@Setter
@Validated
@ConfigurationProperties("uni.pay.union")
public class UnionPayProperties {
    /**
     * 开启银联支付功能
     */
    private boolean enable = true;

    private Client client;

    @Getter
    @Setter
    public static class Client {

        private Duration connectTimeout = Duration.ofMillis(15000);

        private Duration readTimeout = Duration.ofMillis(15000);

        private int maxIdleConnections = 5;

        private boolean sslIgnore = false;

    }
}
