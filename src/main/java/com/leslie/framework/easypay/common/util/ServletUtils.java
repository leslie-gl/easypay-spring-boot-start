package com.leslie.framework.easypay.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet 工具类
 *
 * @author leslie
 * @date 2020/9/4
 */
public class ServletUtils {

    public static final String HTTP_CONTENT_HTML = "text/html;charset=UTF-8";

    private ServletUtils() {
    }

    /**
     * 获取响应Map,并以指定分隔分隔符拼接value[]对象
     *
     * @param request HttpServletRequest
     * @param split   分隔符
     * @return Map<String, String>
     */
    public static Map<String, String> getRequestMap(HttpServletRequest request, String split) {
        Map<String, String> params = new HashMap<>(16);
        Map<String, String[]> requestParams = request.getParameterMap();
        requestParams.forEach((k, v) -> params.put(k, StrUtils.convert2Utf8(StringUtils.join(v, split))));
        return params;
    }

    /**
     * 将指定内容输出
     *
     * @param response HttpServletResponse
     * @param out      输出
     * @throws IOException I/O
     */
    public static void responseWrite(HttpServletResponse response, String out) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.write(out);
        writer.flush();
        writer.close();
    }

}
