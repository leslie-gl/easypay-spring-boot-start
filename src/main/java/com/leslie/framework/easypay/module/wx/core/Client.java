package com.leslie.framework.easypay.module.wx.core;

import com.leslie.framework.easypay.common.util.ServletUtils;
import com.leslie.framework.easypay.module.wx.config.WxPayConstants;
import com.leslie.framework.easypay.module.wx.config.WxPayProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * HttpClient
 *
 * @author leslie
 * @date 2020/9/8
 */
@Slf4j
public class Client {

    private final HttpClient httpClient;

    private final WxPayProperties wxPayProperties;

    public Client(HttpClient httpClient, WxPayProperties wxPayProperties) {
        this.httpClient = httpClient;
        this.wxPayProperties = wxPayProperties;
    }

    public WxPayProperties getWxPayProperties() {
        return wxPayProperties;
    }

    /**
     * 对指定路径发起请求
     *
     * @param url    相对请求地址
     * @param params 请求数据
     * @return 响应 HttpEntity
     */
    public String doRequest(String url, String params) throws IOException {
        String uri = generateUri(wxPayProperties.getProtocol(), wxPayProperties.getDomain(), url);
        HttpResponse httpResponse = httpClient.execute(createHttpPost(uri, params));
        HttpEntity httpEntity = httpResponse.getEntity();
        return EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
    }

    private HttpPost createHttpPost(String uri, String params) {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setConfig(requestConfig());
        httpPost.addHeader("Content-Type", ServletUtils.HTTP_CONTENT_HTML);
        httpPost.addHeader("User-Agent", WxPayConstants.USER_AGENT + " " + wxPayProperties.getMchId());
        httpPost.setEntity(new StringEntity(params, StandardCharsets.UTF_8));
        return httpPost;
    }

    private RequestConfig requestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout((int) wxPayProperties.getConnectTimeout().toMillis())
                .setSocketTimeout((int) wxPayProperties.getReadTimeout().toMillis())
                .build();
    }

    private String generateUri(String protocol, String domain, String urlSuffix) {
        return protocol + "://" + domain + urlSuffix;
    }
}
