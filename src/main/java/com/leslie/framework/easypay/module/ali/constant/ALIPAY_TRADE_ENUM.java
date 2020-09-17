package com.leslie.framework.easypay.module.ali.constant;

/**
 * 支付宝回调的支付通知类型
 *
 * @author leslie
 * @date 2020/9/3
 */
public enum ALIPAY_TRADE_ENUM {
    /**
     * 交易关闭
     */
    TRADE_CLOSED("交易关闭"),
    /**
     * 交易完结
     */
    TRADE_FINISHED("交易完结"),
    /**
     * 支付成功
     */
    TRADE_SUCCESS("支付成功"),
    /**
     * 交易创建
     */
    WAIT_BUYER_PAY("交易创建");

    private final String description;

    public String getDescription() {
        return description;
    }

    ALIPAY_TRADE_ENUM(String description) {
        this.description = description;
    }

}
