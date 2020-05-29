package com.saiyun.model.vo;

import java.math.BigDecimal;
import java.util.List;

public class HomeVo {
    private String btcValue;

    private String cny;

    private List<BVo> bLists;

    public List<BVo> getbLists() {
        return bLists;
    }

    public void setbLists(List<BVo> bLists) {
        this.bLists = bLists;
    }

    public String getBtcValue() {
        return btcValue;
    }

    public void setBtcValue(String btcValue) {
        this.btcValue = btcValue;
    }

    public String getCny() {
        return cny;
    }

    public void setCny(String cny) {
        this.cny = cny;
    }


}
