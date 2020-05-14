package com.saiyun.model;


public class UserIdentity {
    private String userIdentityId;

    private String userid;

    private String nationality;

    private String realname;

    private String certificateType;

    private String certificateNumber;

    private String certificateFrontUrl;

    private String certificateBackUrl;

    private String oneCreateString;

    private String oneUpStringString;

    private String twoCreateString;

    private String twoUpStringString;

    public String getUserIdentityId() {
        return userIdentityId;
    }

    public void setUserIdentityId(String userIdentityId) {
        this.userIdentityId = userIdentityId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality == null ? null : nationality.trim();
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname == null ? null : realname.trim();
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType == null ? null : certificateType.trim();
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber == null ? null : certificateNumber.trim();
    }

    public String getCertificateFrontUrl() {
        return certificateFrontUrl;
    }

    public void setCertificateFrontUrl(String certificateFrontUrl) {
        this.certificateFrontUrl = certificateFrontUrl == null ? null : certificateFrontUrl.trim();
    }

    public String getCertificateBackUrl() {
        return certificateBackUrl;
    }

    public void setCertificateBackUrl(String certificateBackUrl) {
        this.certificateBackUrl = certificateBackUrl == null ? null : certificateBackUrl.trim();
    }

    public String getOneCreateString() {
        return oneCreateString;
    }

    public void setOneCreateString(String oneCreateString) {
        this.oneCreateString = oneCreateString;
    }

    public String getOneUpStringString() {
        return oneUpStringString;
    }

    public void setOneUpStringString(String oneUpStringString) {
        this.oneUpStringString = oneUpStringString;
    }

    public String getTwoCreateString() {
        return twoCreateString;
    }

    public void setTwoCreateString(String twoCreateString) {
        this.twoCreateString = twoCreateString;
    }

    public String getTwoUpStringString() {
        return twoUpStringString;
    }

    public void setTwoUpStringString(String twoUpStringString) {
        this.twoUpStringString = twoUpStringString;
    }
}