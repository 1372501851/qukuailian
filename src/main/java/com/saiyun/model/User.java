package com.saiyun.model;

import javax.persistence.*;
import java.io.Serializable;

public class User  implements Serializable {
    /**
     * 用户id
     */
    @Id
    @Column(name = "user_id")
    private String userId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像地址
     */
    @Column(name = "icon_url")
    private String iconUrl;

    /**
     * 二维码
     */
    @Column(name = "qr_code_url")
    private String qrCodeUrl;



    private String password;
    /**
     * 一级认证 0,未认证, 1，已认证
     */
    private String oneAuth;

    private String twoAuth;

    private String createdate;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取用户id
     *
     * @return user_id - 用户id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     *
     * @param userId 用户id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取用户昵称
     *
     * @return nickname - 用户昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置用户昵称
     *
     * @param nickname 用户昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取手机号
     *
     * @return phone - 手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机号
     *
     * @param phone 手机号
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取头像地址
     *
     * @return icon_url - 头像地址
     */
    public String getIconUrl() {
        return iconUrl;
    }

    /**
     * 设置头像地址
     *
     * @param iconUrl 头像地址
     */
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    /**
     * 获取二维码
     *
     * @return qr_code_url - 二维码
     */
    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    /**
     * 设置二维码
     *
     * @param qrCodeUrl 二维码
     */
    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public String getOneAuth() {
        return oneAuth;
    }

    public void setOneAuth(String oneAuth) {
        this.oneAuth = oneAuth == null ? null : oneAuth.trim();
    }

    public String getTwoAuth() {
        return twoAuth;
    }

    public void setTwoAuth(String twoAuth) {
        this.twoAuth = twoAuth;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }
}