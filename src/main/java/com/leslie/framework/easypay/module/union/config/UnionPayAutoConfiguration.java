package com.leslie.framework.easypay.module.union.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author leslie
 * @date 2020/9/15
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "uni.pay.union", name = "enable", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(UnionPayProperties.class)
public class UnionPayAutoConfiguration {
}
