package com.saiyun.model.vo;

public class AccountVo {
    private String accountType;
    private String name;
    private String account;
    private String imgUrl;
    private String bankname;

    public String getType() {
        return accountType;
    }

    public void setType(String type) {
        this.accountType = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }
}
