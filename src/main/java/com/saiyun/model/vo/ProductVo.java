package com.saiyun.model.vo;

import com.saiyun.model.Product;

import java.math.BigDecimal;
import java.util.List;

public class ProductVo {
    private String iconUrl;
    private String nickname;
    private String deal;//交易次数
    private String dealFail;//交易失败次数
    private String succRate;//成功率
    private String productId;//商品id
    private String userId;//用户id
    private BigDecimal amount;//商品数量
    private BigDecimal maxAmount;//最多购买
    private BigDecimal minAmount;//最少购买
    private String wechat;
    private String alipay;
    private String bankcard;
    private String astrict;

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public String getBankcard() {
        return bankcard;
    }

    public void setBankcard(String bankcard) {
        this.bankcard = bankcard;
    }

    public String getAstrict() {
        return astrict;
    }

    public void setAstrict(String astrict) {
        this.astrict = astrict;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSuccRate() {
        return succRate;
    }

    public void setSuccRate(String succRate) {
        this.succRate = succRate;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public String getDeal() {
        return deal;
    }

    public void setDeal(String deal) {
        this.deal = deal;
    }

    public String getDealFail() {
        return dealFail;
    }

    public void setDealFail(String dealFail) {
        this.dealFail = dealFail;
    }

}
