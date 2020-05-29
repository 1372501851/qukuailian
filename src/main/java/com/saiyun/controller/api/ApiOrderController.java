package com.saiyun.controller.api;

import com.saiyun.annotation.TokenRequired;
import com.saiyun.exception.MyException;
import com.saiyun.exception.TokenException;
import com.saiyun.model.*;
import com.saiyun.service.CoinService;
import com.saiyun.service.EntrustService;
import com.saiyun.service.OrderService;
import com.saiyun.service.UserService;
import com.saiyun.util.ReturnUtil;
import com.saiyun.util.StringCheckUtil;
import com.saiyun.util.ValidataUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/app/order")
public class ApiOrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private EntrustService entrustService;
    @Autowired
    private UserService userService;
    @Autowired
    private CoinService coinService;
    @RequestMapping(value = "createBuyOrder")
    //创建买单
    @TokenRequired
    public ModelMap createBuyOrder(@RequestBody TreeMap<String,Object> map) throws Exception {
        String entrustId = (String) map.get("entrustId");
        String money = (String) map.get("money");
        String payType = (String)map.get("payType");//1,微信，2，支付宝，3，银行卡
        String token = (String) map.get("token");
        User user = userService.getUserByToken(token);
        if(StringCheckUtil.isEmpty(entrustId,money,payType)){
            return ReturnUtil.error("参数不全");
        }
        if(!ValidataUtil.checkMoney(money)){
            return ReturnUtil.error("金额不合法");
        }
        BigDecimal bigMoney = new BigDecimal(money);

        if(!ValidataUtil.checkPayType(payType)){//验证支付方式
            return ReturnUtil.error("参数不合法");
        }
        //todo:数据库验证 判断委托单的剩余数量和最小限额，金额是否在限额范围内，数量+手续费是否比剩余数量小
        Entrust entrust = entrustService.getMatchEntrust(entrustId,money);
        if (entrust == null){
            throw new MyException("请输入限额内的金额");
        }
        Order order = orderService.createBuyOrder(entrust, payType, money, user);
        if (order != null){
            Map<Object, Object> returnMap = new HashMap<>();
            returnMap.put("orderId",order.getOrderId());
            return ReturnUtil.success("",returnMap);
        }
        return ReturnUtil.error("后台错误，请联系管理员");
    }
    @RequestMapping(value = "buyOrderDetail")
    @TokenRequired
    /*
    购买订单详情
     */
    public ModelMap buyOrderDetail(@RequestBody TreeMap<String,Object> map) throws TokenException {
        //todo:订单验证搁置，暂不清楚该接口设计成只返回买单，还是可以返回卖单和买单
        String orderId = (String)map.get("orderId");
        String token = (String) map.get("token");
        User user = userService.getUserByToken(token);
        if (StringUtils.isEmpty(orderId)){
            return ReturnUtil.error("参数不全");
        }
        Order order = orderService.getOrderById(orderId);
        if(order==null){
            return ReturnUtil.error("参数不合法");
        }
        //判断用户和订单是否关联
//        if(!user.getUserId().equals(order.getBuyUserId())){
//            return ReturnUtil.error("后台错误，请联系管理员");
//        }
//        Coin coin = coinService.getCoinByCoinNo(order.getCoinNo());
        User sellUser = userService.getUserById(order.getSellUserId());//卖家昵称
        UserIdentity userIdentity = userService.getUserIdentity(order.getSellUserId());//卖家姓名
        //todo:委托时需要验证该用户有没有经过1级认证，（暂时未有该逻辑）
//        if (sellUser == null || userIdentity == null || !"1".equals(sellUser.getOneAuth())
//                || StringUtils.isEmpty(userIdentity.getRealname())){
//            return ReturnUtil.error("后台错误，请联系管理员");
//        }
        //todo:付款方式模块暂未设计，付款信息暂时搁置
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("createDate",order.getCreatDate());
        returnMap.put("updateDate",order.getUpdateTime());
        returnMap.put("coinNo",order.getCoinNo());
        returnMap.put("money",order.getDealNum());
        returnMap.put("unitPrice",order.getEntrustPrice());
        returnMap.put("num",order.getDealNum());
        returnMap.put("orderNo",order.getOrderNo());
        returnMap.put("phone",user.getPhone());
        returnMap.put("nickname",sellUser.getNickname());
        returnMap.put("payType",order.getReceivablesType());
        Entrust entrust = entrustService.getOneById(order.getEntrustId());
        returnMap.put("moneyType",entrust.getMoneyType());
        return ReturnUtil.success("",returnMap);
    }
    @RequestMapping("buyCancelOrder")
    @TokenRequired
    /**
     * 用户取消购买的订单
     */
    public ModelMap buyCancelOrder(@RequestBody TreeMap<String,Object> map) throws Exception {
        String token = (String) map.get("token");
        User user = userService.getUserByToken(token);
        String orderId = (String) map.get("orderId");
        Order order = orderService.getOrderById(orderId);
        orderService.buyCancelOrder(order,user);
        return ReturnUtil.success("撤销成功");
    }

    /**
     * 买家确认付款
     * @param map
     * @return
     * @throws Exception
     */
    @RequestMapping("buyAffirmOrder")
    @TokenRequired
    public ModelMap buyAffirmOrder(@RequestBody TreeMap<String,Object> map) throws Exception {
        String token = (String) map.get("token");
        User user = userService.getUserByToken(token);
        String orderId = (String) map.get("orderId");
        Order order = orderService.getOrderById(orderId);
        //将订单验证专门在service层设置一个方法，可以做到高复用
        //todo:订单验证，存在，时间，用户关联，订单状态（搁置）
        orderService.buyAffirmOrder(order,user);
        return ReturnUtil.success("购买成功，请等待卖家确认");
    }
    @RequestMapping("cancelOrder")
    @TokenRequired
    /**
     * 取消订单
     */
    public ModelMap cancelOrder(@RequestBody TreeMap<String,Object> map) throws Exception {
        String token = (String) map.get("token");
        User user = userService.getUserByToken(token);
        String orderId = (String) map.get("orderId");
        Order order = orderService.getOrderById(orderId);
        //todo:订单验证，存在，时间，用户关联，订单状态（搁置）
        orderService.cancelOrder(order);
        return ReturnUtil.success("撤销成功");
    }
    @RequestMapping("sellAffirmOrder")
    @TokenRequired
    /**
     * 卖家确认收款
     */
    public ModelMap sellAffirmOrder(@RequestBody TreeMap<String,Object> map) throws Exception {
        String token = (String) map.get("token");
        User user = userService.getUserByToken(token);
        String orderId = (String) map.get("orderId");
        Order order = orderService.getOrderById(orderId);
        //todo:订单验证，存在，时间，用户关联，订单状态（搁置）
        orderService.sellAffirmOrder(order,user);
        return ReturnUtil.success("购买成功");
    }


}
