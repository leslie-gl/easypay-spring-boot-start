package com.leslie.framework.easypay.module.ali;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import com.alipay.easysdk.payment.common.models.AlipayTradeRefundResponse;
import com.alipay.easysdk.payment.page.Client;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.alipay.easysdk.payment.wap.models.AlipayTradeWapPayResponse;
import com.leslie.framework.easypay.common.util.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author leslie
 * @date 2020/9/17
 */
@Slf4j
public class AliPayClient {

    /**
     * PC场景下单并支付,直接输出Form表单
     * 设置异步回调地址,此处设置将在本调用中覆盖Config中的全局配置
     * 批量设置API入参中没有的其他可选业务请求参数(biz_content下的字段)
     *
     * @param response     HttpServletResponse
     * @param subject      订单标题
     * @param outTradeNo   商户订单号,64个字符以内、可包含字母、数字、下划线
     * @param totalAmount  订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
     * @param returnUrl    同步回调地址,以HTTP/HTTPS开头
     * @param notifyUrl    异步回调地址,以HTTP/HTTPS开头 (可选参数)
     * @param optionalArgs 可选参数集合，每个参数由key和value组成 (可选参数)
     *                     key     业务请求参数名称（biz_content下的字段名，比如timeout_express）
     *                     value   业务请求参数的值，一个可以序列化成JSON的对象
     *                     如果该字段是一个字符串类型（String、Price、Date在SDK中都是字符串），请使用String储存
     *                     如果该字段是一个数值型类型（比如：Number），请使用Long储存
     *                     如果该字段是一个复杂类型，请使用嵌套的Map指定各下级字段的值
     *                     如果该字段是一个数组，请使用List储存各个值
     *                     对于更复杂的情况，也支持Map和List的各种组合嵌套，比如参数是值是个List，List中的每种类型是一个复杂对象
     */
    public void pagePay(HttpServletResponse response,
                        String subject,
                        String outTradeNo,
                        String totalAmount,
                        String returnUrl,
                        String notifyUrl,
                        Map<String, Object> optionalArgs) {
        try {

            Client client = Factory.Payment.Page();

            if (StringUtils.isNotBlank(notifyUrl)) {
                client = client.asyncNotify(notifyUrl);
            }

            if (optionalArgs != null && !optionalArgs.isEmpty()) {
                client = client.batchOptional(optionalArgs);
            }

            AlipayTradePagePayResponse payResponse = client.pay(subject, outTradeNo, totalAmount, returnUrl);

            response.setContentType(ServletUtils.HTTP_CONTENT_HTML);
            ServletUtils.write(response, payResponse.body);

        } catch (Exception e) {
            log.error("pc支付异常, method=alipay.trade.page.pay outTradeNo={} reason={}", e.getMessage(), e);
        }
    }

    /**
     * 手机网站下单并支付,直接输出Form表单
     * 设置异步回调地址,此处设置将在本调用中覆盖Config中的全局配置
     * 批量设置API入参中没有的其他可选业务请求参数(biz_content下的字段)
     *
     * @param response     HttpServletResponse
     * @param subject      订单标题
     * @param outTradeNo   商户订单号,64个字符以内、可包含字母、数字、下划线
     * @param totalAmount  订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
     * @param quitUrl      用户付款中途退出返回商户网站的地址
     * @param returnUrl    同步回调地址,以HTTP/HTTPS开头
     * @param notifyUrl    异步回调地址,以HTTP/HTTPS开头 (可选参数)
     * @param optionalArgs 可选参数集合，每个参数由key和value组成 (可选参数)
     */
    public void wayPay(HttpServletResponse response,
                       String subject,
                       String outTradeNo,
                       String totalAmount,
                       String quitUrl,
                       String returnUrl,
                       String notifyUrl,
                       Map<String, Object> optionalArgs) {
        try {

            com.alipay.easysdk.payment.wap.Client client = Factory.Payment.Wap();

            if (StringUtils.isNotBlank(notifyUrl)) {
                client = client.asyncNotify(notifyUrl);
            }

            if (optionalArgs != null && !optionalArgs.isEmpty()) {
                client = client.batchOptional(optionalArgs);
            }

            AlipayTradeWapPayResponse payResponse = client.pay(subject, outTradeNo, totalAmount, quitUrl, returnUrl);

            response.setContentType(ServletUtils.HTTP_CONTENT_HTML);
            ServletUtils.write(response, payResponse.body);

        } catch (Exception e) {
            log.error("手机网站支付异常, method=alipay.trade.way.pay outTradeNo={} reason={}", outTradeNo, e.getMessage());
        }
    }

    /**
     * 交易超过约定时间（签约时设置的可退款时间）的订单无法进行退款。
     * 支付宝退款支持单笔交易分多次退款，多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。
     * 一笔退款失败后重新提交，要采用原来的退款单号。
     * 总退款金额不能超过用户实际支付金额。
     *
     * @param outTradeNo   商户订单号,64个字符以内、可包含字母、数字、下划线
     * @param refundAmount 需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
     * @param refundReason 退款的原因说明 (可选参数)
     * @param outRequestNo 标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传 (可选参数)
     * @return AlipayTradeRefundResponse 响应对象
     */
    @Nullable
    public AlipayTradeRefundResponse refund(String outTradeNo, String refundAmount, String refundReason, String outRequestNo) {
        try {

            com.alipay.easysdk.payment.common.Client client = Factory.Payment.Common();

            if (StringUtils.isNotBlank(refundReason)) {
                client = client.optional("refund_amount", refundAmount);
            }

            if (StringUtils.isNotBlank(outRequestNo)) {
                client = client.optional("out_request_no", outRequestNo);
            }

            return client.refund(outTradeNo, refundAmount);

        } catch (Exception e) {
            log.error("统一退款异常, method=alipay.trade.refund outTradeNo={} reason={}", outTradeNo, e.getMessage());
        }
        return null;
    }

    /**
     * 支付宝退款
     *
     * @param outTradeNo   商户订单号,64个字符以内、可包含字母、数字、下划线
     * @param refundAmount 需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
     * @param optionalArgs 同pagePay参数说明 (可选参数)
     * @return AlipayTradeRefundResponse 响应对象
     */
    @Nullable
    public AlipayTradeRefundResponse refund(String outTradeNo, String refundAmount, Map<String, Object> optionalArgs) {
        try {

            return Factory.Payment.Common()
                    .batchOptional(optionalArgs)
                    .refund(outTradeNo, refundAmount);

        } catch (Exception e) {
            log.error("统一退款异常, method=alipay.trade.refund outTradeNo={} reason={}", outTradeNo, e.getMessage());
        }
        return null;
    }

    /**
     * 查询指定订单状态
     *
     * @param outTradeNo 商户订单号,64个字符以内、可包含字母、数字、下划线
     * @return AlipayTradeQueryResponse 响应对象
     */
    @Nullable
    public AlipayTradeQueryResponse query(String outTradeNo) {
        try {

            return Factory.Payment.Common().query(outTradeNo);

        } catch (Exception e) {
            log.error("统一收单线下交易查询异常, method=alipay.trade.query outTradeNo={} reason={}", outTradeNo, e.getMessage());
        }
        return null;
    }

    /**
     * 验签
     *
     * @param parameters 响应Map
     * @return 验签结果
     */
    public boolean verifyNotify(Map<String, String> parameters) {
        try {

            return Factory.Payment.Common().verifyNotify(parameters);

        } catch (Exception e) {
            log.error("验签异常, reason={}", e.getMessage());
        }
        return false;
    }
}
