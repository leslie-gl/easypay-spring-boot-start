package com.leslie.framework.easypay.module.union.config;

import com.leslie.framework.easypay.module.union.UnionPayClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author leslie
 * @date 2020/9/15
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "uni.pay.union", name = "enable", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(UnionPayProperties.class)
public class UnionPayAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public UnionPayClient unionPayClient() {
        return new UnionPayClient();
    }
}
