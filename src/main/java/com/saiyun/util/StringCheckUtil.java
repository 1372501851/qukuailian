package com.saiyun.util;

import org.apache.commons.lang3.StringUtils;

public class StringCheckUtil {
    /**
     * 如果有一个字符串为空就返回true
     * @param str
     * @return
     */
    public static boolean isEmpty(String... str){
        for (String s:str){
            if(StringUtils.isEmpty(s)){
                return true;
            }
        }
        return false;
    }

//    public static void main(String[] args) {
//        String s1 = "2";
//        String s2 = "2";
//        String s3 = "5";
//        if (StringCheckUtil.isEmpty(s1,s2,s3)){
//            System.out.printf("空");
//        }else{
//            System.out.println("不为空");
//        }
//
//    }
}
