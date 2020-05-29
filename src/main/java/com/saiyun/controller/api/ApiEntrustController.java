package com.saiyun.controller.api;

import com.github.pagehelper.PageInfo;
import com.saiyun.annotation.TokenRequired;
import com.saiyun.exception.TokenException;
import com.saiyun.model.Entrust;
import com.saiyun.model.User;
import com.saiyun.model.UserWallet;
import com.saiyun.model.member.Member;
import com.saiyun.service.EntrustService;
import com.saiyun.service.UserService;
import com.saiyun.service.UserWalletService;
import com.saiyun.util.ReturnUtil;
import com.saiyun.util.StringCheckUtil;
import com.saiyun.util.ValidataUtil;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/app/entrust")
public class ApiEntrustController {
    @Autowired
    private EntrustService entrustService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserWalletService userWalletService;

    /**
     *挂单显示在用户端
     * @param map
     * @return
     */
    @PostMapping(value = "query")
    public ModelMap queryEntry(@RequestBody TreeMap<String,Object> map){
        Integer pageNum = (Integer) map.get("pageNum");
        Integer pageSize = (Integer)map.get("pageSize");
        String money = (String) map.get("money");//最大金额
        String payType = (String)map.get("payType");//1,微信，2，支付宝，3，银行卡.不传,所有方式
        String moneyType = (String) map.get("moneyType");//货币类型1,cny 2,eur
        String entrustType = (String)map.get("entrustType");//0.买1.卖
        String coinNo = (String) map.get("coinNo");
        if (StringCheckUtil.isEmpty(moneyType,entrustType,coinNo)){
            return ReturnUtil.error("参数不全");
        }
        if(pageNum == null || pageNum == 0){
            pageNum = 1;
        }
        if (pageSize == null || pageSize == 0){
            pageSize = 10;
        }
        Entrust entrust = new Entrust();
        entrust.setEntrustType(entrustType);
        entrust.setCoinNo(coinNo);
        entrust.setOffset(pageNum);
        entrust.setLimit(pageSize);
        //todo:金额判断

        if(money != null && ValidataUtil.checkMoney(money)){
            entrust.setEntrustMinPrice(new BigDecimal(money));//是否把价格作为筛选条件
            entrust.setEntrustMaxPrice(new BigDecimal(money));//是否把价格作为筛选条件
        }
        if(!StringUtils.isEmpty(payType)){
            if(payType.equals("1")){
                entrust.setWechat("1");
            }else if(payType.equals("2")){
                entrust.setAlipay("1");
            }else if(payType.equals("3")){
                entrust.setBankcard("1");
            }
        }
        entrust.setMoneyType(moneyType);
        entrust.setEntrustType(entrustType);
        List<Map> entrusts = entrustService.queryEntry(entrust);
        return ReturnUtil.success("",new PageInfo<Map>(entrusts));
    }

    @PostMapping(value = "matchEntrust")
    /**
     * 匹配卖单和买单
     */
    @TokenRequired
    public ModelMap matchEntrust(@RequestBody TreeMap<String,Object> map) throws Exception {
        Map<Object, Object> returnMap = new HashMap<>();
        String coinNo = (String) map.get("coinNo");//币种类型
        String moneyType = (String) map.get("moneyType");//货币类型1,cny 2,eur
        String buyType = (String)map.get("buyType");//购买类型 1,金额,2数量
        String num = (String)map.get("num");//数量
        String money = (String)map.get("money");//金额
        String payType = (String)map.get("payType");//1,微信，2，支付宝，3，银行卡
        String entrustType = (String)map.get("entrustType");//0.买1.卖
        String token = (String) map.get("token");
        //todo:数量，只能到小数点6位，最小数量暂不清楚
        if(StringCheckUtil.isEmpty(coinNo,moneyType,buyType,payType,token,entrustType)){
            return ReturnUtil.error("参数不全");
        }
        User user = userService.getUserByToken(token);
        Entrust entrust = new Entrust();
        entrust.setEntrustType(entrustType);
        entrust.setCoinNo(coinNo);
        entrust.setMoneyType(moneyType);
        if(payType.equals("1")){
            entrust.setWechat("1");
        }else if(payType.equals("2")){
            entrust.setAlipay("1");
        }else if(payType.equals("3")){
            entrust.setBankcard("1");
        }else{
            return ReturnUtil.error("参数错误");
        }
        if(!"0".equals(entrust.getEntrustType()) && !"1".equals(entrust.getEntrustType())){
            return ReturnUtil.error("参数不合法");
        }
        returnMap.put("entrustType",entrustType);
        returnMap.put("coinNo",coinNo);
        returnMap.put("moneyType",moneyType);
        returnMap.put("payType",payType);//付款方式
        if("1".equals(buyType)){//按金额购买
            //取出金额，判断币种类型金额，
            if(StringUtils.isEmpty(money) || !ValidataUtil.checkMoney(money)){
                return ReturnUtil.error("参数不全");
            }
            BigDecimal bigMoney = new BigDecimal(money);
            if(bigMoney.compareTo(new BigDecimal(100))<0){
                return ReturnUtil.error("最小金额为100元");
            }
            returnMap.put("money",money);
            entrust.setMoneyCondit(money);
            Entrust entrust1 = entrustService.match(entrust,user);
            if (entrust1 == null){
                return ReturnUtil.error("暂时没有匹配到，请稍后重试");
            }
            returnMap.put("unitPrice",entrust1.getEntrustPrice());
            BigDecimal amount = bigMoney.divide(entrust1.getEntrustPrice(), 10, RoundingMode.DOWN);//对数量进行截尾，因为java精度不如mysql,如果数量变大可能会出现数量不足的情况
            returnMap.put("num",amount);
            returnMap.put("entrustId",entrust1.getEntrustId());
            return ReturnUtil.success("",returnMap);
        }
        if("2".equals(buyType)){
            if(StringUtils.isEmpty(num)){
                return ReturnUtil.error("参数不全");
            }
            returnMap.put("num",num);
            entrust.setNumCondit(num);
            Entrust entrust1 = entrustService.match(entrust,user);
            if (entrust1 == null){
                return ReturnUtil.error("暂时没有匹配到，请稍后重试");
            }
            returnMap.put("unitPrice",entrust1.getEntrustPrice());
            BigDecimal bigNum = new BigDecimal(num);
            BigDecimal momeys = bigNum.multiply(entrust1.getEntrustPrice()).setScale(2, RoundingMode.UP);//
            returnMap.put("money",momeys);
            returnMap.put("entrustId",entrust1.getEntrustId());
            return ReturnUtil.success("",returnMap);
        }else {
            return ReturnUtil.error("参数不合法");
        }
    }

  @PostMapping("entrustDetail")
  @TokenRequired
  public ModelMap  entrustDetail(@RequestBody TreeMap<String,Object> map){
      Map<Object, Object> maps = new HashMap<>();
      String entrustId = (String) map.get("entrustId");
      String token = (String) map.get("token");
      String buyType = (String)map.get("buyType");//购买类型 1,金额,2数量
      Entrust entrust = entrustService.selectByMaxPriceLimit(entrustId);
      if (entrust == null){
          return ReturnUtil.error("参数错误");
      }
      if ("1".equals(buyType)){
          String money = (String)map.get("money");//金额
          maps.put("money",money);
          maps.put("num",new BigDecimal(money).divide(entrust.getEntrustPrice(),6,RoundingMode.DOWN));
      }else if("2".equals(buyType)){
          String num = (String)map.get("num");//数量
          maps.put("num",num);
          maps.put("money",new BigDecimal(num).multiply(entrust.getEntrustPrice()).setScale(2,RoundingMode.DOWN));
      }
      maps.put("entrustId",entrustId);
      maps.put("min",entrust.getEntrustMinPrice());
      maps.put("max",entrust.getEntrustMaxPrice());
      maps.put("coinNo",entrust.getCoinNo());
      maps.put("unitPrice",entrust.getEntrustPrice());
      return ReturnUtil.success("",maps);
  }


}
