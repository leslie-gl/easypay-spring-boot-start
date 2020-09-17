package com.leslie.framework.easypay.module.okhttp;

import com.aliyun.tea.utils.TrueHostnameVerifier;
import com.aliyun.tea.utils.X509TrustManagerImp;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author leslie
 * @date 2020/9/15
 */
public class OkHttpClientBuilder {

    private static final String PROTOCOL_TLS = "TLS";

    private final OkHttpClient.Builder builder;

    public OkHttpClientBuilder() {
        builder = new OkHttpClient().newBuilder();
    }

    public OkHttpClientBuilder connectTimeout(Duration connectTimeout) {
        this.builder.connectTimeout(connectTimeout);
        return this;
    }

    public OkHttpClientBuilder readTimeout(Duration readTimeout) {
        this.builder.readTimeout(readTimeout);
        return this;
    }

    public OkHttpClientBuilder connectionPool(int maxIdleConnections) {
        ConnectionPool connectionPool = new ConnectionPool(maxIdleConnections, 10000L, TimeUnit.MILLISECONDS);
        this.builder.connectionPool(connectionPool);
        return this;
    }

    public OkHttpClientBuilder certificate(Cert cert, boolean sslIgnore)
            throws GeneralSecurityException, IOException {

        KeyStore keyStore = KeyStore.getInstance(cert.getStoreType());
        keyStore.load(cert.getCertStream(), cert.getStorePass());

        SSLContext sslContext = SSLContext.getInstance(PROTOCOL_TLS);
        if (sslIgnore) {
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, cert.getKeyPass());

            X509TrustManagerImp compositeX509TrustManager = new X509TrustManagerImp();
            sslContext.init(kmf.getKeyManagers(), new TrustManager[]{compositeX509TrustManager}, new SecureRandom());
            this.builder.sslSocketFactory(sslContext.getSocketFactory(), compositeX509TrustManager)
                    .hostnameVerifier(new TrueHostnameVerifier());

        } else {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            TrustManager[] trustManagers = tmf.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
            this.builder.sslSocketFactory(sslContext.getSocketFactory(), trustManager);
        }
        return this;
    }

    public OkHttpClient build() {
        return this.builder.build();
    }
}
