package com.saiyun.service;

import com.saiyun.mapper.ParamsMapper;
import com.saiyun.mapper.UserWalletMapper;
import com.saiyun.model.Params;
import com.saiyun.model.User;
import com.saiyun.model.UserWallet;
import com.saiyun.model.vo.BPriceVo;
import com.saiyun.model.vo.BVo;
import com.saiyun.model.vo.HomeVo;
import com.saiyun.model.vo.SellVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class HomeService {
    @Autowired
    private UserWalletMapper userWalletMapper;
    @Autowired
    private ParamsMapper paramsMapper;

    /**
     * 首页
     * @param user
     * @param bprice
     * @return
     */
    public Map index(User user, BPriceVo bprice) {
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<Object> list = new ArrayList<>();
        BigDecimal allBtcNum = new BigDecimal("0");
        BigDecimal allCnyNum = new BigDecimal("0");
        //取出钱包信息
        List<UserWallet> userWallets = userWalletMapper.getuserWallet(Long.parseLong(user.getUserId()));
        for (UserWallet userWallet:
             userWallets) {
            HashMap<String, Object> detailMap = new HashMap<>();
            detailMap.put("balance",userWallet.getBalance().setScale(6,RoundingMode.DOWN));
            detailMap.put("ubbalance",userWallet.getUnbalance().setScale(6,RoundingMode.DOWN));
            int type = userWallet.getType();
            if(1 == type){
                allBtcNum = allBtcNum.add(userWallet.getBalance());
                detailMap.put("coinNo","1");
                detailMap.put("cny",userWallet.getBalance().multiply(bprice.getBtcCny()).setScale(2,RoundingMode.DOWN));
                allCnyNum = allCnyNum.add(userWallet.getBalance().multiply(bprice.getBtcCny()));
            }else if(2 == type){
                BigDecimal usdtTransBtc = userWallet.getBalance().divide(bprice.getBtcUsdt(), 10, RoundingMode.DOWN);
                allBtcNum = allBtcNum.add(usdtTransBtc).setScale(6,RoundingMode.DOWN);
                detailMap.put("coinNo","2");
                detailMap.put("cny",userWallet.getBalance().multiply(bprice.getUsdtCny()).setScale(2,RoundingMode.DOWN));
                allCnyNum = allCnyNum.add(userWallet.getBalance().multiply(bprice.getUsdtCny()));
            }
            list.add(detailMap);
        }
        map.put("allBtc",allBtcNum);
        map.put("allCny",allCnyNum.setScale(2,RoundingMode.DOWN));
        map.put("detail",list);
        return map;
    }

//    /**
//     * 卖币初始界面数据
//     * @param user
//     * @param bprice
//     * @return
//     */
//    public List<SellVo> userSellHome(User user, BPriceVo bprice) throws Exception {
//        List<SellVo> sellVos = new ArrayList<>();
//        //取出钱包信息
//        UserWallet userWallet = userWalletMapper.selectOneByUserId(user.getUserId());
//        //查询usdt和btc价格
//        BigDecimal btcBalanceNumber = userWallet.getBtcBalance();//用户btc可用数量
//        BigDecimal usdtBalanceNumber = userWallet.getUsdtBalance();//用户usdt可用数量
//        SellVo btcSell = new SellVo();
//        btcSell.setbType("1");
//        btcSell.setMaxNumber(btcBalanceNumber.toPlainString());
//        BigDecimal btcMaxPrice = btcBalanceNumber.multiply(bprice.getBtcCny()).setScale(2, RoundingMode.HALF_DOWN);
//        btcSell.setMaxPrice(btcMaxPrice.toPlainString());
//        btcSell.setUnitPrice(bprice.getBtcCny().toPlainString());
//        sellVos.add(btcSell);
//        SellVo sell2 = new SellVo();
//        sell2.setbType("2");
//        sell2.setMaxNumber(usdtBalanceNumber.toPlainString());
//        BigDecimal usdtMaxPrice = usdtBalanceNumber.multiply(bprice.getUsdtCny()).setScale(2, RoundingMode.HALF_DOWN);
//        sell2.setMaxPrice(usdtMaxPrice.toPlainString());
//        sell2.setUnitPrice(bprice.getUsdtCny().toPlainString());
//        sellVos.add(sell2);
//        return sellVos;
//    }
//
//    /**
//     *
//     * @param bType 货币类型 1，btc 2,usdt
//     * @param cType 价格类型 1,cny 2,eur
//     * @param sType 出售类型 1，价格出售 2，按数量出售
//     * @param sum 金额总数
//     * @param bprice 币单价相关;
//     * @return
//     */
//    public Map countNum(String bType, String cType, String sType, String sum, BPriceVo bprice) {
//        HashMap<String, String> map = new HashMap<>();
//        //返回金额，单价，数量
//        BigDecimal unitPrice = null;
//        BigDecimal sumDecimal = new BigDecimal(sum);
//        if("1".equals(bType)){//btc
//            unitPrice = bprice.getBtcCny();
//            if("2".equals(cType)){//欧元
//                unitPrice = bprice.getBtcEur();
//            }
//        }else if("2".equals(bType)){//usdt
//            unitPrice = bprice.getUsdtCny();
//            if("2".equals(cType)){//欧元
//                unitPrice = bprice.getUsdtEur();
//            }
//        }
//        map.put("unitPrice",unitPrice.toString());
//        if("1".equals(sType)){//sum是金额,计算可购买数量
//            BigDecimal bSum = sumDecimal.divide(unitPrice, 6, RoundingMode.HALF_DOWN);
//            map.put("bSum",bSum.toPlainString());
//            map.put("money",sum);
//        }else if("2".equals(sType)){//sum是数量，计算金额
//            BigDecimal money = sumDecimal.multiply(unitPrice);
//            map.put("bSum",sum);
//            map.put("money",money.toPlainString());
//        }
//        return map;
//    }
//
//    public Map<String,Map> getUnitPrice(BPriceVo bprice) {
//        Map<String,Map> maps = new HashMap<>();
//        //cny
//        Map<String, Object> cnyMap = new HashMap<>();
//        cnyMap.put("BTC",bprice.getBtcCny());//1btc价值的cny
//        cnyMap.put("USDT",bprice.getUsdtCny());//1USDT价值的cny
//        maps.put("cny",cnyMap);
//        //eur
//        Map<String,Object> eurMap = new HashMap<>();
//        eurMap.put("BTC",bprice.getBtcEur());
//        eurMap.put("USDT",bprice.getUsdtEur());
//        maps.put("eur",eurMap);
//        return maps;
//    }
}
