package com.saiyun.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.saiyun.mapper.ParamsMapper;
import com.saiyun.model.Params;
import com.saiyun.util.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class MarketTask {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Autowired
    private ParamsMapper paramsMapper;
    //5分钟获取一次

    /**
     * 获取btc转usdt
     */
    @Scheduled(cron = "0 */5 * * * ? ")
    public void Market(){
        try{
            System.out.println("开始执行btc任务");
            BigDecimal btcMarket = HttpRequest.getcoinRate("btc_usdt");
            if (btcMarket.compareTo(new BigDecimal("0")) == 1) {
                Params params = new Params();
                params.setParamKey("btc_usdt");
                params.setParamValue(btcMarket.toString());
                paramsMapper.updateParams(params);
            }
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("btc定时器异常");
        }
    }
    //每三十分钟执行一次

    /**
     * 获取usdt转rmb
     */
    @Scheduled(cron="0 0/5 * * * ?")
    public void cronJob(){
        try{
            BigDecimal financeRate =  HttpRequest.getcoinRate("usdt_qc");

            if (financeRate.compareTo(new BigDecimal("0")) == 1) {
                Params params = new Params();
                params.setParamKey("usd_cny_rate");
                params.setParamValue(financeRate.toString());
                paramsMapper.updateParams(params);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("udst换rmb定时器异常");
        }
    }

}
