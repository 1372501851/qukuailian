package com.saiyun.service;

import com.saiyun.mapper.ParamsMapper;
import com.saiyun.model.Params;
import com.saiyun.model.vo.BPriceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ParamsService {
    @Autowired
    private ParamsMapper paramsMapper;
    public BPriceVo getBprice(){
        BPriceVo bPriceVo = new BPriceVo();
        Params btcUsdtRate = paramsMapper.getParams("btc_usdt");//btc转usdt
        Params usdCnyRate = paramsMapper.getParams("usd_cny_rate");//usdt转cny
        Params eurCnyRate = paramsMapper.getParams("eur_cny_rate");//eur转cny
        BigDecimal btcTransUsdt = new BigDecimal(btcUsdtRate.getParamValue()); //btc和usdt的转换率
        BigDecimal usdtTransCny = new BigDecimal(usdCnyRate.getParamValue());//usdt和rmb的转换率
        BigDecimal eurTransCny = new BigDecimal(eurCnyRate.getParamValue());//欧元和rmb的转换率
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
        return bPriceVo;
    }

}
