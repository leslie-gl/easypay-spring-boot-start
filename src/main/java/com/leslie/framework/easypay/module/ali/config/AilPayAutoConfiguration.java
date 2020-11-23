package com.leslie.framework.easypay.module.ali.config;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.leslie.framework.easypay.module.ali.AliPayClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author leslie
 * @date 2020/9/2
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "uni.pay.ali", name = "enable", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(AliPayProperties.class)
public class AilPayAutoConfiguration {

    @Bean
    @ConditionalOnClass(Config.class)
    public Config config(AliPayProperties aliPayProperties) {
        Config config = new Config();

        config.protocol = "https";
        config.gatewayHost = aliPayProperties.getGatewayHost();
        config.appId = aliPayProperties.getAppId();
        config.signType = "RSA2";
        config.alipayPublicKey = aliPayProperties.getAlipayPublicKey();
        config.merchantPrivateKey = aliPayProperties.getMerchantPrivateKey();
        config.merchantCertPath = aliPayProperties.getMerchantCertPath();
        config.alipayCertPath = aliPayProperties.getAlipayCertPath();
        config.alipayRootCertPath = aliPayProperties.getAlipayRootCertPath();
        config.notifyUrl = aliPayProperties.getNotifyUrl();
        config.encryptKey = aliPayProperties.getEncryptKey();
        config.signProvider = aliPayProperties.getSignProvider();

        Factory.setOptions(config);
        return config;
    }

    @Bean
    @ConditionalOnMissingBean
    public AliPayClient aliPayClient() {
        return new AliPayClient();
    }
}
