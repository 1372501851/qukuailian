package com.saiyun.util;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.shiro.crypto.hash.Md5Hash;


/**

* Copyright (C),2017, Guangzhou ChangLing info. Co., Ltd.

* FileName: CommonUtils.java

* 工具方法

* @author jie
    * @Date    2017年8月14日 上午11:45:53

* @version 2.00

*/
public class CommonUtils {
   
	
    /**
    * 判断字符串是否为空
    * @param str 需要判断是否为空的字符串参数
    * @return 为空或者null返回true
    * @author jie
    * @Date    2017年8月14日上午11:46:14
    * @version 1.00
    */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || str.equals("");
    }
    
    
    /**
    * 对参数param进行lang类型的转换
    * @param param 一个可以装换为long类型的数字
    * @return 如果没有出现异常会返回一个long类型数字，如果出现异常会抛出NumberFormatException
    * @author jie
    * @Date    2017年9月1日下午4:35:17
    * @version 1.00
    */
    public static Long convertLong(String param){
    	try {
    		return Long.valueOf(param);
		} catch (NumberFormatException e) {
			throw new RuntimeException(e);
		}
    }

 
    
    /**
    * 判断集合是否为空
    * @param collection 需要判断的集合
    * @return 为空或者null返回true
    * @author jie
    * @Date    2017年8月14日上午11:47:27
    * @version 1.00
    */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.size() < 1;
    }

    /**
    * 判断Map是否为空的方法
    * @param map 需要判断的map
    * @return 为空或者null返回true
    * @author jie
    * @Date    2017年8月14日上午11:48:02
    * @version 1.00
    */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Map map) {
        return map == null || map.size() < 1;
    }

    /**
    * 判断字符串是否满足指定值
    * @param str 需要判断的字符串
    * @param length 需要满足的值
    * @return 字符串不为null且满足指定长度返回true
    * @author jie
    * @Date    2017年8月14日上午11:48:40
    * @version 1.00
    */
    public static boolean isLengthEnough(String str, int length) {
        if (str == null) {
            return false;
        }
        return str.length() >= length;
    }

    /**
    * 计算一个字符串的MD5值
    * @param s 
    * @return 返回MD5值
    * @author jie
    * @Date    2017年8月14日上午11:52:24
    * @version 1.00
    */
    public final static String calculateMD5(String s) {
//    	String md5 = new Md5Hash(s,"Yeager",674).toString();
//    	return md5;
    	MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		char[] charArray = s.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
    }
    
    /**
     * 后台MD5加密方式
     * @param s 要加密的字符串
     * @return 返回加密后的值
     */
     public final static String adminCalculateMD5(String s) {
     	String md5 = new Md5Hash(s,"Yeager",674).toString();
     	return md5;
     }

    /**
    * 用一个随机数加UUID码，生成一个RC4对称密钥
    * @param param 随机数
    * @param UUID
    * @return 返回一个RC4对称密钥
    * @author jie
    * @Date    2017年8月21日下午5:00:11
    * @version 1.00
    */
    public static String HloveyRC4(String param, String UUID) {
        int[] iS = new int[256];
        byte[] iK = new byte[256];
        for (int i = 0; i < 256; i++)
            iS[i] = i;
        int j = 1;
        for (short i = 0; i < 256; i++) {
            iK[i] = (byte) UUID.charAt((i % UUID.length()));
        }
        j = 0;
        for (int i = 0; i < 255; i++) {
            j = (j + iS[i] + iK[i]) % 256;
            int temp = iS[i];
            iS[i] = iS[j];
            iS[j] = temp;
        }
        int i = 0;
        j = 0;
        char[] iInputChar = param.toCharArray();
        char[] iOutputChar = new char[iInputChar.length];
        for (short x = 0; x < iInputChar.length; x++) {
            i = (i + 1) % 256;
            j = (j + iS[i]) % 256;
            int temp = iS[i];
            iS[i] = iS[j];
            iS[j] = temp;
            int t = (iS[i] + (iS[j] % 256)) % 256;
            int iY = iS[t];
            char iCY = (char) iY;
            iOutputChar[x] = (char) (iInputChar[x] ^ iCY);
        }
        return new String(iOutputChar);
    }
    
    /**
    * 将字符串转化为16进制
    * @param param   
    * @return 返回16进制字符串
    * @author jie
    * @Date    2017年8月21日下午4:49:16
    * @version 1.00
    */
    public static String toHexString(String param) {  
       String str = "";  
       for (int i = 0; i < param.length(); i++) {  
        int ch = (int) param.charAt(i);  
        String s4 = Integer.toHexString(ch);  
        str = str + s4;  
       }  
       return str;  
    }  
    
    /**
    * 获取一个UUID字符串
    * @return 返回一个去掉“-”，以及全部字母都为大写的UUID字符串
    * @author jie
    * @Date    2017年8月21日下午5:03:39
    * @version 1.00
    */
    public static String getUUID(int num){
    	return UUID.randomUUID().toString().replaceAll("-","").substring(0, num);
    }
    
	/**
	* 校验ip地址是否正确
	* @param ip 需要校验的ip地址
	* @return 正确返回true
	* @author jie
	* @Date    2017年10月12日上午10:03:00
	* @version 1.00
	*/
	public static boolean isIpCorrent(String ip){
		String str="\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b";
		return ip.matches(str);
	}
	
    /**
    * 生成一个14位邀请码
    * @return 返回一个14位字符串邀请码
    * @author Yeager
    * @Date    2017年8月21日下午5:10:58
    * @version 1.00
    */
    public static String getInviteCode() {
        String UUID = CommonUtils.getUUID(14);
        String random = new Random().nextInt(9000000) + 1000000 + "";
        String rc4 = CommonUtils.HloveyRC4(random, UUID);
        return CommonUtils.toHexString(rc4).toUpperCase();
    }
    
    
    /**
     * 生成一个20位订单号
     * @return 返回一个20订单号
     * @author Yeager
     * @Date    2017年8月21日下午5:10:58
     * @version 1.00
     */
    public static String generateOrderId(){
        String keyup_prefix=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String keyup_append=String.valueOf(new Random().nextInt(899999)+100000);
        String pay_orderid=keyup_prefix+keyup_append;//订单号
        return pay_orderid;
    }
    
    
    /**
     * 日期格式化
     * @return 日期格式化
     * @author Yeager
     * @Date    2017年8月21日下午5:10:58
     * @version 1.00
     */
    public static String SimpleDateFormatTest(Date date){
    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	 return sdf.format(date);
    }
    
    /**
     * 获取N天后的日期
     * @return 日期获取
     * @author Yeager
     * @Date    2017年8月21日下午5:10:58
     * @version 1.00
     */
    public static Date getEndTime(Integer day){
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.DAY_OF_MONTH, day);
    	Date  date = calendar.getTime();
    	return date;
    }

    /**
     * 计算2个日期相差天数
     * @param smdate
     * @param bdate
     * @return
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate)
    {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(between_days));
        }
        catch (ParseException ex){
            return 0;
        }
    }

    /**
     * 智能合约收益计算
     * @return 收益计算
     * @author Yeager
     * @Date    2017年8月21日下午5:10:58
     * @version 1.00
     */
    public static BigDecimal getContractRevenue(Integer contractDays,BigDecimal incomeRation,BigDecimal initializePrice){
    	
    	Double incomeRationDou  = incomeRation.doubleValue();
    	BigDecimal contractRevenue = new BigDecimal(incomeRationDou/contractDays*0.01).multiply(initializePrice).setScale(2,BigDecimal.ROUND_HALF_UP);
    	return contractRevenue;
    	
    }
    
    /**
     * Doge币计算
     * @return 收益计算
     * @author Yeager
     * @Date    2017年8月21日下午5:10:58
     * @version 1.00
     */
    public static BigDecimal getDogeMoney(Integer dogeMoney,BigDecimal contractRevenue){
    	
    	BigDecimal bigDogeMoney = new BigDecimal(dogeMoney*0.01).multiply(contractRevenue).setScale(2,BigDecimal.ROUND_HALF_UP);
    	return bigDogeMoney;
    	
    }
    
    
    /**
     * Doge币计算
     * @return 收益计算
     * @author Yeager
     * @Date    2017年8月21日下午5:10:58
     * @version 1.00
     */
    public static Boolean isToday(Date endTime){
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	String endTimeStr = sdf.format(endTime);
    	String date = sdf.format(new Date());
    	if(endTimeStr.equals(date)) {
    		return true;
    	}else {
    		return false; 
    	}
    	
    	
    }
    
    /**
     * 是否为当前时间段
     * @author Yeager
     * @Date    2017年8月21日下午5:10:58
     * @version 1.00
     * @throws ParseException 
     */
    public static Boolean isAtPresentDate(Map<Object,Object> map) throws ParseException{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String datestr = simpleDateFormat.format(new Date());
        long presentDate = simpleDateFormat.parse(datestr).getTime();
        Date startTime = (Date) map.get("startTime");
        Date endTime = (Date) map.get("endTime");
        if(startTime.getTime() < presentDate && presentDate < endTime.getTime()) {
            return false;
        }else {
            return true;
        }
    }
    
    
    /**
     * 时间戳转换为当前时间
     * @author Yeager
     * @Date    2017年8月21日下午5:10:58
     * @version 1.00
     */
    public static String dateHourMinuteSecond(Date date) {
    	
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
		String datestr = simpleDateFormat.format(date);
		return datestr;
		
    }
    
    
    /**
     * 获取准备时间
     * @author Yeager
     * @Date    2017年8月21日下午5:10:58
     * @version 1.00
     */
    public static Date getReadyTime(Date date) {
    	Date ready =new Date( date.getTime() - 120000);
		return ready;
		
    }
    

    /**
     * 获取抢购成功的猪
     * @author Yeager
     * @Date    2017年8月21日下午5:10:58
     * @version 1.00
     * @param pigSuccessfulMap 
     * @throws MyException 
     */
    public static String ForSuccessfulGoods(String userId, Map<Object, Object> pigSuccessfulMap) {
    	Object exclusiveId = null;
    	 for(Object key:pigSuccessfulMap.keySet()){
    		 if(!userId.equals(pigSuccessfulMap.get(key))) {
    			 exclusiveId = key;
    		 }
         }
    	return (String) exclusiveId;
		
    }
    
    /**
     * 获取N小时后的时间
     * @author Yeager
     * @Date    2017年8月21日下午5:10:58
     * @version 1.00
     * @param pigSuccessfulMap 
     * @throws MyException 
     */
    public static Date getNHourslater(Integer num) {
    	
    	 Calendar calendar = Calendar.getInstance();
    	 calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + num);
    	 return calendar.getTime();
    }
    
    /**
     * 获取差多少个小时
     * @author Yeager
     * @Date    2017年8月21日下午5:10:58
     * @version 1.00
     * @param pigSuccessfulMap 
     * @throws MyException 
     */
    public static Long getDuration(Date buyTime) {
    	
    	Date date = new Date();
    	Long durationLong = date.getTime() - buyTime.getTime();
    	Long duration = durationLong/1000/60/60;
    	return duration;
    }
    
    
    public static void main(String[] args) {
		String uuid = CommonUtils.getUUID(8);
		System.out.println(uuid);
	}
    
    
    
}
