package com.saiyun.service;

import com.saiyun.mapper.ParamsMapper;
import com.saiyun.mapper.UserWalletMapper;
import com.saiyun.model.Params;
import com.saiyun.model.User;
import com.saiyun.model.UserWallet;
import com.saiyun.model.vo.BPriceVo;
import com.saiyun.model.vo.HomeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Service
public class HomeService {
    @Autowired
    private UserWalletMapper userWalletMapper;
    @Autowired
    private ParamsMapper paramsMapper;

    /**
     * 首页
     * @param user
     * @return
     */
    public HomeVo index(User user) {
        //取出钱包信息
        UserWallet userWallet = userWalletMapper.selectOneByUserId(user.getUserId());
        //查询usdt和btc价格
        Params btcUsdt = paramsMapper.getParams("btc_usdt");
        Params usdCnyRate = paramsMapper.getParams("usd_cny_rate");
        HomeVo homeVo = new HomeVo();
        BigDecimal btcBigDecimal = userWallet.getBtcBalance();//用户btc可用数量
        BigDecimal usdtBigDecimal = userWallet.getUsdtBalance();//用户usdt可用数量
        BigDecimal btcUsdtBigDecimal = new BigDecimal(btcUsdt.getParamValue()); //btc和usdt的转换率
        BigDecimal usdtCnyBigDecimal = new BigDecimal(usdCnyRate.getParamValue());//usdt和rmb的转换率
        if(btcBigDecimal.compareTo(new BigDecimal("0"))== -1){
            btcBigDecimal = new BigDecimal("0");
        }
        if(usdtBigDecimal.compareTo(new BigDecimal("0"))== -1){
            usdtBigDecimal = new BigDecimal("0");
        }
        //btc的数据
        homeVo.setBtcBalance(btcBigDecimal.toString());//可用币
        homeVo.setBtcUnbalance(userWallet.getBtcUnbalance().toString());//冻结币
            BigDecimal btcTransUsdtNumber = btcBigDecimal.multiply(btcUsdtBigDecimal);//用户btc数量转成usdt后的数量
            BigDecimal btcTransCny = btcTransUsdtNumber.multiply(usdtCnyBigDecimal);//btc对应的rmb
        homeVo.setBtcCny(btcTransCny.toString());//btc这算cny
        //usdt的数据
        homeVo.setUsdtBalance(usdtBigDecimal.toString());//可用币
        homeVo.setUsdtUnbalance(userWallet.getUsdtUnbalance().toString());//冻结币
            BigDecimal usdtTransCny = usdtBigDecimal.multiply(usdtCnyBigDecimal);//usdt转换成rmb的数量
        homeVo.setUsdtCny(usdtTransCny.toString());
        BigDecimal usdtTransBtc = usdtBigDecimal.divide(btcUsdtBigDecimal,6,RoundingMode.HALF_DOWN);//usdt对应的btc ,RoundingMode.HALF_DOWN如果除不尽就把超过位数的舍弃掉
        BigDecimal allBtc = usdtTransBtc.add(btcBigDecimal);//usdt和btc一共有多少btc
        BigDecimal allCny = btcTransCny.add(usdtTransCny);//usdt和btc一共有多少rmb
        homeVo.setBtcValue(allBtc.toString());
        homeVo.setCny(allCny.toString());
        //处理0E-10
        if(btcBigDecimal.compareTo(new BigDecimal("0")) < 1){
            homeVo.setBtcCny("0");
        }
        if(usdtBigDecimal.compareTo(new BigDecimal("0"))< 1){
            homeVo.setUsdtCny("0");
        }if(btcBigDecimal.compareTo(new BigDecimal("0")) < 1 && usdtBigDecimal.compareTo(new BigDecimal("0"))< 1){
            homeVo.setCny("0");
        }
        return homeVo;
    }

    /**
     * 卖币初始界面数据
     * @param user
     * @return
     */
    public Map<String, String> userSellHome(User user) throws Exception {
        Map<String, String> map = new HashMap<>();
        //取出钱包信息
        UserWallet userWallet = userWalletMapper.selectOneByUserId(user.getUserId());
        //查询usdt和btc价格
        Params btcUsdt = paramsMapper.getParams("btc_usdt");
        Params usdCnyRate = paramsMapper.getParams("usd_cny_rate");
        Params eurCnyRate = paramsMapper.getParams("eur_cny_rate");
        BigDecimal btcBalanceNumber = userWallet.getBtcBalance();//用户btc可用数量
        BigDecimal usdtBalanceNumber = userWallet.getUsdtBalance();//用户usdt可用数量
        BigDecimal btcTransUsdt = new BigDecimal(btcUsdt.getParamValue()); //btc和usdt的转换率
        BigDecimal usdtTransCny = new BigDecimal(usdCnyRate.getParamValue());//usdt和rmb的转换率
        BigDecimal eurTransCny = new BigDecimal(eurCnyRate.getParamValue());//欧元和rmb的转换率
        //BTC计算单价
        BigDecimal oneBtcTransCny = btcTransUsdt.multiply(usdtTransCny);
        map.put("btcUnitPrice",oneBtcTransCny.toString());
        //BTC最大价格
        BigDecimal allBtcPrice = oneBtcTransCny.multiply(btcBalanceNumber);
        map.put("btcMaxPrice",allBtcPrice.toString());
        map.put("btcMaxNumber",btcBalanceNumber.toString());
        map.put("usdtUnitPrice",usdtTransCny.toString());
        map.put("usdtMaxPrice",usdtTransCny.multiply(usdtBalanceNumber).toString());
        map.put("usdtMaxNumber",usdtBalanceNumber.toString());
        map.put("eurTransCny",eurTransCny.toString());
        return map;
    }

    public ModelMap userSell(TreeMap<String, String> map, User user) {
        return null;
    }

    /**
     *
     * @param bType 货币类型 1，btc 2,usdt
     * @param cType 价格类型 1,cny 2,eur
     * @param sType 出售类型 1，价格出售 2，按数量出售
     * @param sum 金额总数
     * @param bprice 币单价相关;
     * @return
     */
    public Map countNum(String bType, String cType, String sType, String sum, BPriceVo bprice) {



        HashMap<String, String> map = new HashMap<>();
        //返回金额，单价，数量
        BigDecimal sumDecimal = new BigDecimal(sum);
        if("1".equals(sType)){ //按价格出售
            if("1".equals(cType)){//人民币
                
            }

        }
    }

}
