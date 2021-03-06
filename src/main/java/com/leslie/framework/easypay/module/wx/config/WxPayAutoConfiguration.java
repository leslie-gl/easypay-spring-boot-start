package com.leslie.framework.easypay.module.wx.config;

import com.leslie.framework.easypay.module.wx.WxPayClient;
import com.leslie.framework.easypay.module.wx.core.Client;
import com.leslie.framework.easypay.module.wx.core.Signer;
import org.apache.http.client.HttpClient;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

/**
 * @author leslie
 * @date 2020/9/2
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "uni.pay.wx", name = "enable", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(WxPayProperties.class)
public class WxPayAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WxPayClient wxPayClient(HttpClient httpClient, WxPayProperties wxPayProperties) {
        return new WxPayClient(new Client(httpClient, wxPayProperties), new Signer());
    }


    @Bean
    @ConditionalOnClass(HttpClient.class)
    public HttpClient httpClient(BasicHttpClientConnectionManager basicHttpClientConnectionManager) {
        return HttpClientBuilder.create()
                .setConnectionManager(basicHttpClientConnectionManager)
                .build();
    }

    @Bean
    @ConditionalOnClass(BasicHttpClientConnectionManager.class)
    public BasicHttpClientConnectionManager basicHttpClientConnectionManager(SSLConnectionSocketFactory sslConnectionSocketFactory) {

        return new BasicHttpClientConnectionManager(
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
                        .register("https", sslConnectionSocketFactory)
                        .build(),
                null,
                null,
                null
        );
    }

    @Bean
    @ConditionalOnClass(SSLConnectionSocketFactory.class)
    public SSLConnectionSocketFactory sslConnectionSocketFactory(WxPayProperties wxPayProperties) {
        String certPath = wxPayProperties.getCertPath();
        if (certPath == null) {
            return SSLConnectionSocketFactory.getSocketFactory();
        }

        SSLConnectionSocketFactory sslConnectionSocketFactory = null;

        File file = new File(certPath);
        char[] password = wxPayProperties.getMchId().toCharArray();

        try (FileInputStream certStream = new FileInputStream(file)) {

            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(certStream, password);

            // 实例化密钥库 & 初始化密钥工厂
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, password);

            // 创建 SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());

            sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    sslContext,
                    new String[]{"TLSv1"},
                    null,
                    new DefaultHostnameVerifier());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslConnectionSocketFactory;
    }
}
