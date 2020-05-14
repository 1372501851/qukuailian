
import com.saiyun.model.Params;
import com.saiyun.model.vo.BPriceVo;
import com.saiyun.util.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

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




}
