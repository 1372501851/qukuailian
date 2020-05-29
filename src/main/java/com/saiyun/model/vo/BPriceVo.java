package com.saiyun.model.vo;

import java.math.BigDecimal;

public class BPriceVo {
    private BigDecimal btcCny;//1btc转成cny
    private BigDecimal usdtCny;//1usdt转成cny
    private BigDecimal eurCny;//1eur转成cny
    private BigDecimal btcEur;//1btc转成eur
    private BigDecimal usdtEur;//1usdt转成eur
    private BigDecimal btcUsdt;//btc转成usdt

    public BigDecimal getBtcUsdt() {
        return btcUsdt;
    }

    public void setBtcUsdt(BigDecimal btcUsdt) {
        this.btcUsdt = btcUsdt;
    }

    public BigDecimal getBtcCny() {
        return btcCny;
    }

    public void setBtcCny(BigDecimal btcCny) {
        this.btcCny = btcCny;
    }

    public BigDecimal getUsdtCny() {
        return usdtCny;
    }

    public void setUsdtCny(BigDecimal usdtCny) {
        this.usdtCny = usdtCny;
    }

    public BigDecimal getEurCny() {
        return eurCny;
    }

    public void setEurCny(BigDecimal eurCny) {
        this.eurCny = eurCny;
    }

    public BigDecimal getBtcEur() {
        return btcEur;
    }

    public void setBtcEur(BigDecimal btcEur) {
        this.btcEur = btcEur;
    }

    public BigDecimal getUsdtEur() {
        return usdtEur;
    }

    public void setUsdtEur(BigDecimal usdtEur) {
        this.usdtEur = usdtEur;
    }
}
