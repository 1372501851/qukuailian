package com.saiyun.model;

import java.math.BigDecimal;

public class UserWallet {
    private String userWalletId;

    private String userId;

    private String address;

    private String state;

    private String String;

    private BigDecimal btcBalance;

    private BigDecimal btcUnbalance;

    private String flag;

    private BigDecimal recountbalance;

    private BigDecimal balanceBackup;

    private String payStatus;

    private String walletState;

    private BigDecimal usdtBalance;

    private BigDecimal usdtUnbalance;

    public String getUserWalletId() {
        return userWalletId;
    }

    public void setUserWalletId(String userWalletId) {
        this.userWalletId = userWalletId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getString() {
        return String;
    }

    public void setString(String String) {
        this.String = String;
    }

    public BigDecimal getBtcBalance() {
        return btcBalance;
    }

    public void setBtcBalance(BigDecimal btcBalance) {
        this.btcBalance = btcBalance;
    }

    public BigDecimal getBtcUnbalance() {
        return btcUnbalance;
    }

    public void setBtcUnbalance(BigDecimal btcUnbalance) {
        this.btcUnbalance = btcUnbalance;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag == null ? null : flag.trim();
    }

    public BigDecimal getRecountbalance() {
        return recountbalance;
    }

    public void setRecountbalance(BigDecimal recountbalance) {
        this.recountbalance = recountbalance;
    }

    public BigDecimal getBalanceBackup() {
        return balanceBackup;
    }

    public void setBalanceBackup(BigDecimal balanceBackup) {
        this.balanceBackup = balanceBackup;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getWalletState() {
        return walletState;
    }

    public void setWalletState(String walletState) {
        this.walletState = walletState;
    }

    public BigDecimal getUsdtBalance() {
        return usdtBalance;
    }

    public void setUsdtBalance(BigDecimal usdtBalance) {
        this.usdtBalance = usdtBalance;
    }

    public BigDecimal getUsdtUnbalance() {
        return usdtUnbalance;
    }

    public void setUsdtUnbalance(BigDecimal usdtUnbalance) {
        this.usdtUnbalance = usdtUnbalance;
    }
}