
import com.saiyun.model.Params;
import com.saiyun.model.vo.BPriceVo;
import com.saiyun.util.*;
import org.apache.http.HttpResponse;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * author saiyun
 * date 2017/1/11 0011 下午 14:08
 */
public class TestUtil{

    @Test
    public void testCamelCaseUtil() {
        System.out.println(CamelCaseUtil.toUnderlineName("ISOCertifiedStaff"));
        System.out.println(CamelCaseUtil.toUnderlineName("CertifiedStaff"));
        System.out.println(CamelCaseUtil.toUnderlineName("UserID"));
        System.out.println(CamelCaseUtil.toCamelCase("iso_certified_staff"));
        System.out.println(CamelCaseUtil.toCamelCase("certified_staff"));
        System.out.println(CamelCaseUtil.toCamelCase("member"));
    }

    @Test
    public void testDateUtil() {
        System.out.println(DateUtil.getCurrentTime());//获取当前时间
        System.out.println(DateUtil.getCurrentDate());//获取当前日期
    }

    @Test
    public void testUuidUtil() {
        String[] ss = UuidUtil.getUUID(10);
        for (int i = 0; i < ss.length; i++) {
            System.out.println(ss[i]);
        }
    }

    @Test
    public void testToken(){
        String sign = TokenUtil.sign("55555");
        System.out.println(
                sign
        );
        try{
            Thread.sleep(1000*12);
        }catch (Exception e){

        }

        String verify = TokenUtil.verify(sign);
        System.out.println(verify);
    }
    @Test
    public void testBtc(){
       try{
           BigDecimal btc_usdt = HttpRequest.getcoinRate("btc_usdt");
           BigDecimal btcMarket = HttpUtil.getcoinRate("btc_usdt");
           System.out.println(btc_usdt.toString());
           System.out.println(btcMarket.toString());
       }catch (Exception e){
           e.printStackTrace();
       }
    }
    @Test
    public void testPassword(){
        System.out.println("kkdkskk");
        BPriceVo bPriceVo = new BPriceVo();

        BigDecimal btcTransUsdt = new BigDecimal("8948.86"); //btc和usdt的转换率
        BigDecimal usdtTransCny = new BigDecimal("7.1158");//usdt和rmb的转换率
        BigDecimal eurTransCny = new BigDecimal("5");//欧元和rmb的转换率
        //1btc价值的cny
        BigDecimal oneBtcCny = btcTransUsdt.multiply(usdtTransCny);
        bPriceVo.setBtcCny(oneBtcCny);
        //1usdt价值的cny
        bPriceVo.setUsdtCny(usdtTransCny);
        //1eur价值的cny
        bPriceVo.setEurCny(eurTransCny);
        //1btc价值的eur
        BigDecimal oneBtcEur = oneBtcCny.multiply(eurTransCny);
        bPriceVo.setBtcEur(oneBtcEur);
        //1usdt价值的eur
        BigDecimal oneUsdtEur = usdtTransCny.multiply(eurTransCny);
        bPriceVo.setUsdtEur(oneUsdtEur);
        BigDecimal sum = new BigDecimal("200.00");
        BigDecimal divide = sum.divide(bPriceVo.getBtcCny(), 6, RoundingMode.HALF_DOWN);
        System.out.println(divide);
    }
    @Test
    public void test06(){
        try{
            HashMap map = new HashMap<>();
            String ss = (String)map.get("556");
            System.out.println(ss);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void jfdsdj(){
        System.out.println("sjjsjjsjj");
//        BPriceVo bPriceVo = new BPriceVo();
//
//        BigDecimal btcTransUsdt = new BigDecimal("8948.86"); //btc和usdt的转换率
//        BigDecimal usdtTransCny = new BigDecimal("7.1158");//usdt和rmb的转换率
//        BigDecimal eurTransCny = new BigDecimal("5");//欧元和rmb的转换率
//        //1btc价值的cny
//        BigDecimal oneBtcCny = btcTransUsdt.multiply(usdtTransCny);
//        bPriceVo.setBtcCny(oneBtcCny);
//        //1usdt价值的cny
//        bPriceVo.setUsdtCny(usdtTransCny);
//        //1eur价值的cny
//        bPriceVo.setEurCny(eurTransCny);
//        //1btc价值的eur
//        BigDecimal oneBtcEur = oneBtcCny.multiply(eurTransCny);
//        bPriceVo.setBtcEur(oneBtcEur);
//        //1usdt价值的eur
//        BigDecimal oneUsdtEur = usdtTransCny.multiply(eurTransCny);
//        bPriceVo.setUsdtEur(oneUsdtEur);
//        BigDecimal sum = new BigDecimal("200.00");
//        BigDecimal divide = sum.divide(bPriceVo.getBtcCny(), 6, RoundingMode.HALF_DOWN);
//        System.out.println(divide);
    }
    public static void main(String[] args) {
        String host = "https://cxwg.market.alicloudapi.com";
        String path = "/sendSms";
        String method = "POST";
        String appcode = "eb3f8b880b184344acec096575c143a6";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        //测试可用默认短信模板,测试模板为专用模板不可修改,如需自定义短信内容或改动任意字符,请联系旺旺或QQ726980650进行申请
        querys.put("content", "【创信】你的验证码是：5873，3分钟内有效！");
        querys.put("mobile", "15692827516");
        Map<String, String> bodys = new HashMap<String, String>();


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test(){
        String s1 = null;
        String s2 = "551145d4a534ds5ds2d1s2ds2ds2221s";
        String s3 = "lkflds5555";
        if (ValidataUtil.checkPwd(s1)){
            System.out.println("进入s1");
        }
        if(ValidataUtil.checkPwd(s2)){
            System.out.println("进入s2");
        }
        if (ValidataUtil.checkPwd(s3)){
            System.out.println("进入s3");
        }
    }
    public static String getCode1() {

        Integer intFlag = (int) (Math.random() * 1000000);

        String flag = intFlag.toString();

        if (flag.length() == 6 && flag.startsWith("9")) {
            return intFlag.toString();
        } else {
            intFlag = intFlag + 100000;
            return intFlag.toString();
        }


    }

    public static String getCode2() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }




}
