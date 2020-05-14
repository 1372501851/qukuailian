package com.saiyun.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * 密码生成工具类
 * @author saiyun
 */
public class PasswordUtil {

    public static String createAdminPwd(String password, String salt){
        return new SimpleHash("md5",password,ByteSource.Util.bytes(salt),2).toHex();
    }

    public static String createCustomPwd(String password, String salt){
        return new SimpleHash("md5",password,ByteSource.Util.bytes(salt),1).toHex();
    }
}
