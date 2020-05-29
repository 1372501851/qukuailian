package com.saiyun.model;

import java.math.BigDecimal;
import java.util.Date;

public class Entrust  extends  BaseEntity{
    private String entrustId;

    private String entrustNo;

    private String userId;

    private String moneyType;

    private String coinNo;

    private String entrustType;

    private BigDecimal entrustPrice;

    private BigDecimal dayLimit;

    private BigDecimal entrustMinPrice;

    private BigDecimal entrustMaxPrice;

    private BigDecimal entrustNum;

    private BigDecimal remainNum;

    private BigDecimal dealNum;

    private BigDecimal matchNum;

    private BigDecimal cancelNum;

    private BigDecimal poundage;

    private BigDecimal poundageScale;

    private String state;

    private String remark;

    private String creatDate;

    private String wechat;

    private String alipay;

    private String bankcard;

    //数据库筛选条件
    private String moneyCondit;

    private String numCondit;

    private BigDecimal minPriceCondit;

    //前端需要的字段
    private String iconUrl;

    private String nickname;

    private String authMark;

    private String tradeNum;

    private String succRate;

    public BigDecimal getMinPriceCondit() {
        return minPriceCondit;
    }

    public void setMinPriceCondit(BigDecimal minPriceCondit) {
        this.minPriceCondit = minPriceCondit;
    }

    public String getMoneyCondit() {
        return moneyCondit;
    }

    public void setMoneyCondit(String moneyCondit) {
        this.moneyCondit = moneyCondit;
    }

    public String getNumCondit() {
        return numCondit;
    }

    public void setNumCondit(String numCondit) {
        this.numCondit = numCondit;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getAuthMark() {
        return authMark;
    }

    public void setAuthMark(String authMark) {
        this.authMark = authMark;
    }

    public String getTradeNum() {
        return tradeNum;
    }

    public void setTradeNum(String tradeNum) {
        this.tradeNum = tradeNum;
    }

    public String getSuccRate() {
        return succRate;
    }

    public void setSuccRate(String succRate) {
        this.succRate = succRate;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEntrustId() {
        return entrustId;
    }

    public void setEntrustId(String entrustId) {
        this.entrustId = entrustId;
    }

    public String getEntrustNo() {
        return entrustNo;
    }

    public void setEntrustNo(String entrustNo) {
        this.entrustNo = entrustNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType == null ? null : moneyType.trim();
    }

    public String getCoinNo() {
        return coinNo;
    }

    public void setCoinNo(String coinNo) {
        this.coinNo = coinNo;
    }

    public String getEntrustType() {
        return entrustType;
    }

    public void setEntrustType(String entrustType) {
        this.entrustType = entrustType;
    }

    public BigDecimal getEntrustPrice() {
        return entrustPrice;
    }

    public void setEntrustPrice(BigDecimal entrustPrice) {
        this.entrustPrice = entrustPrice;
    }

    public BigDecimal getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(BigDecimal dayLimit) {
        this.dayLimit = dayLimit;
    }

    public BigDecimal getEntrustMinPrice() {
        return entrustMinPrice;
    }

    public void setEntrustMinPrice(BigDecimal entrustMinPrice) {
        this.entrustMinPrice = entrustMinPrice;
    }

    public BigDecimal getEntrustMaxPrice() {
        return entrustMaxPrice;
    }

    public void setEntrustMaxPrice(BigDecimal entrustMaxPrice) {
        this.entrustMaxPrice = entrustMaxPrice;
    }

    public BigDecimal getEntrustNum() {
        return entrustNum;
    }

    public void setEntrustNum(BigDecimal entrustNum) {
        this.entrustNum = entrustNum;
    }

    public BigDecimal getRemainNum() {
        return remainNum;
    }

    public void setRemainNum(BigDecimal remainNum) {
        this.remainNum = remainNum;
    }

    public BigDecimal getDealNum() {
        return dealNum;
    }

    public void setDealNum(BigDecimal dealNum) {
        this.dealNum = dealNum;
    }

    public BigDecimal getMatchNum() {
        return matchNum;
    }

    public void setMatchNum(BigDecimal matchNum) {
        this.matchNum = matchNum;
    }

    public BigDecimal getCancelNum() {
        return cancelNum;
    }

    public void setCancelNum(BigDecimal cancelNum) {
        this.cancelNum = cancelNum;
    }

    public BigDecimal getPoundage() {
        return poundage;
    }

    public void setPoundage(BigDecimal poundage) {
        this.poundage = poundage;
    }

    public BigDecimal getPoundageScale() {
        return poundageScale;
    }

    public void setPoundageScale(BigDecimal poundageScale) {
        this.poundageScale = poundageScale;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getCreatDate() {
        return creatDate;
    }

    public void setCreatDate(String creatDate) {
        this.creatDate = creatDate;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat == null ? null : wechat.trim();
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay == null ? null : alipay.trim();
    }

    public String getBankcard() {
        return bankcard;
    }

    public void setBankcard(String bankcard) {
        this.bankcard = bankcard == null ? null : bankcard.trim();
    }
}