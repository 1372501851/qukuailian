package com.saiyun.service;

import com.saiyun.exception.MyException;
import com.saiyun.mapper.*;
import com.saiyun.model.*;
import com.saiyun.util.KeyId;
import com.saiyun.util.ValidataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private EntrustMapper entrustMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserWalletMapper userWalletMapper;
    @Autowired
    private AcceptanceMapper acceptanceMapper;
    @Transactional
    //创建订单，当卖家是承兑商时去委托表冻结委托单，
    //当卖家是用户时去用户钱包冻结钱包余额.
    public Order createOrder(Entrust entrust, String payType, String money, User user) throws Exception {
        BigDecimal bigMoney = new BigDecimal(money);
        //计算交易数量
        //数量=金额/单价
        BigDecimal amount = bigMoney.divide(entrust.getEntrustPrice(), 10, RoundingMode.DOWN);//对数量进行截尾，因为java精度不如mysql,如果数量变大可能会出现数量不足的情况
        BigDecimal poundage = amount.multiply(entrust.getPoundageScale()).setScale(10, RoundingMode.DOWN);
        BigDecimal allNum = amount.add(poundage);//总数量
        Order order = new Order();
        order.setOrderId(KeyId.nextId());
        order.setOrderNo(KeyId.nextId());
        if("1".equals(entrust.getEntrustType())){//委托单类型是卖，那么订单是用户买
            order.setDealType("1");
            order.setBuyUserId(user.getUserId());
            order.setSellUserId(entrust.getUserId());
        }else if("0".equals(entrust.getEntrustType())){//委托单类型是买，那么订单是用户卖
            order.setDealType("2");
            order.setBuyUserId(entrust.getUserId());
            order.setSellUserId(user.getUserId());
        }
        order.setEntrustId(entrust.getEntrustId());
        order.setCoinNo(entrust.getCoinNo());
        order.setEntrustPrice(entrust.getEntrustPrice());
        order.setDealPrice(bigMoney);
        order.setDealNum(amount);
        order.setState("3");
        order.setCreatDate(ValidataUtil.dateFormat(new Date()));
        order.setReceivablesType(payType);
        order.setPoundage(poundage);
        orderMapper.insertSelective(order);
        //限制卖家库存，承兑商去委托表限制，用户去钱包限制
        if("1".equals(entrust.getEntrustType())){//委托单类型是卖，那么订单是用户买
            //todo:不能小于0
            entrust.setDayLimit(entrust.getDayLimit().subtract(allNum).setScale(10,RoundingMode.DOWN));
            entrust.setRemainNum(entrust.getRemainNum().subtract(allNum));
        }else if("0".equals(entrust.getEntrustType())){//委托单类型是买，那么订单是用户卖
            //todo:不能小于0
            entrust.setDayLimit(entrust.getDayLimit().subtract(amount).subtract(poundage).setScale(10,RoundingMode.DOWN));
            entrust.setRemainNum(entrust.getRemainNum().subtract(amount).subtract(poundage).setScale(10,RoundingMode.DOWN));
            //用户资产冻结
            UserWallet userWallet = new UserWallet();
            userWallet.setType(Integer.parseInt(entrust.getCoinNo()));//货币编号
            userWallet.setUserId(user.getUserId());
            userWallet.setType(1);
            UserWallet userWalletByCondition = userWalletMapper.getUserWalletByCondition(userWallet);
//            if ()
        }
        entrustMapper.updateByPrimaryKeySelective(entrust);
        return order;
    }

    public Order getOrderById(String orderId) {
        return orderMapper.selectByPrimaryKey(orderId);
    }
    @Transactional
    public void cancelOrder(Order order) throws Exception {
        //todo:，每日限额
        //取出承兑商信息
        Acceptance acceptance = new Acceptance();//查詢賣家每日限額;
        if("1".equals(order.getDealType())){//用户匹配卖家
            acceptance.setUserId(order.getSellUserId());
        }else {
            acceptance.setUserId(order.getBuyUserId());
        }

        Acceptance acceptance1 = acceptanceMapper.selectByAcceptance(acceptance);

        BigDecimal allNum = order.getDealNum().add(order.getPoundage());//縂數量
        //委托表逻辑
        Entrust entrust = entrustMapper.selectByPrimaryKey(order.getEntrustId());
        BigDecimal nowDayLimit = entrust.getDayLimit().add(allNum);//撤销订单后的每日限额
        entrust.setDayLimit(nowDayLimit.compareTo(acceptance1.getDayLimit()) >= 0 ?
                acceptance1.getDayLimit() : nowDayLimit);//每日限额不能超过平台设定的
        entrust.setRemainNum(entrust.getRemainNum().add(allNum));
        entrustMapper.updateByPrimaryKeySelective(entrust);
        //订单逻辑
        order.setUpdateTime(ValidataUtil.dateFormat(new Date()));
        order.setState("2");
        orderMapper.updateByPrimaryKeySelective(order);
    }
    //todo:撤销订单定时任务加每日限额时要考虑最大值，不能超过数据库的
    @Transactional
    /**
     * 卖家确认订单
     */
    public void affirmOrder(Order order) throws Exception {
        //todo:订单和委托单联合验证
        Entrust entrust = entrustMapper.selectByPrimaryKey(order.getEntrustId());
        //获取平台用户钱包
        String sysUserStr = "3";
        User sysUser = userMapper.getOneByUserType(sysUserStr);
        UserWallet userWallet = new UserWallet();
        userWallet.setUserId(sysUser.getUserId());
        userWallet.setType(Integer.parseInt(order.getCoinNo()));
        UserWallet sysUserWallet = userWalletMapper.getUserWalletByCondition(userWallet);//系统钱包
        //用户钱包
        User buyUser = userMapper.selectByPrimaryKey(order.getBuyUserId());
        userWallet.setUserId(buyUser.getUserId());
        UserWallet buyUserWallet = userWalletMapper.getUserWalletByCondition(userWallet);//购买人钱包
        User sellUser = userMapper.selectByPrimaryKey(order.getSellUserId());
        userWallet.setUserId(sellUser.getUserId());
        UserWallet sellUserWallet = userWalletMapper.getUserWalletByCondition(userWallet);//卖家钱包
        BigDecimal allNum = order.getDealNum().add(order.getPoundage());//总数量
        if("1".equals(order.getDealType())){//用户匹配卖家,承兑商是卖家
            //承兑商扣币扣币
            sellUserWallet.setUnbalance(sellUserWallet.getUnbalance().subtract(allNum));
            //用户扣币加币
            buyUserWallet.setBalance(buyUserWallet.getBalance().add(order.getDealNum()));

        }else {//用户匹配买家，承兑商是买家
            //承兑商可用币加上订单数量减去手续费
            BigDecimal subtract = order.getDealNum().subtract(order.getPoundage());
            buyUserWallet.setBalance(buyUserWallet.getBalance().add(subtract));
            //卖家用户可用币减去订单数量
            sellUserWallet.setBalance(sellUserWallet.getBalance().subtract(order.getDealNum()));
        }
        //平台加币
        sysUserWallet.setBalance(sysUserWallet.getBalance().add(order.getPoundage()));
        userWalletMapper.updateUserWalletByCondition(sellUserWallet);
        userWalletMapper.updateUserWalletByCondition(buyUserWallet);
        userWalletMapper.updateUserWalletByCondition(sysUserWallet);
        //订单表操作
        order.setState("1");
        order.setUpdateTime(ValidataUtil.dateFormat(new Date()));
        orderMapper.updateByPrimaryKeySelective(order);
        //委托表操作
        entrust.setDealNum(entrust.getDealNum().add(allNum));
        entrust.setPoundage(entrust.getPoundage().add(order.getPoundage()));
        entrustMapper.updateByPrimaryKeySelective(entrust);
    }
    @Transactional
    public void buyAffirmOrder(Order order, User user) throws Exception {
        //判断买家
        if (!order.getBuyUserId().equals(user.getUserId())){
            throw new MyException("订单和用户不匹配，请联系管理员");
        }
        //判断订单状态，只能取消买房未付款订单
        if(!"3".equals(order.getState())){
            throw new MyException("只能对等待付款的订单付款，请联系管理员");
        }
        //时间为15分钟，超时用户只能读，不能写 1000*60*15  15分钟
        long createLong = ValidataUtil.dateToStamp(order.getCreatDate());
        if (createLong<System.currentTimeMillis()+(1000*60*15)){
            throw new Exception("订单超时，付款失败");
        }
        order.setState("4");
        order.setUpdateTime(ValidataUtil.dateFormat(new Date()));
        orderMapper.updateByPrimaryKeySelective(order);
    }
    //创建用户买订单
    @Transactional
    public Order createBuyOrder(Entrust entrust, String payType, String money, User user) throws Exception {

        BigDecimal moneyDecimal = new BigDecimal(money);
        //计算交易数量
        BigDecimal dealNum = moneyDecimal.divide(entrust.getEntrustPrice(), 10, RoundingMode.DOWN);
        //手续费
        BigDecimal poundage = dealNum.multiply(entrust.getPoundageScale()).setScale(10, RoundingMode.DOWN);
        //总数量
        BigDecimal allNum = dealNum.add(poundage);
        //总数量对应的金额
        BigDecimal allMoney = allNum.multiply(entrust.getEntrustPrice());
        //总数量和剩余数量，限额，每日最大限额比较
        this.checkEntrust(entrust, allNum);
       //用户买单
        Order order = new Order();
        order.setOrderId(KeyId.nextId());
        order.setOrderNo(KeyId.nextId());
        order.setBuyUserId(user.getUserId());
        order.setSellUserId(entrust.getUserId());
        order.setDealType("1");//1,用户匹配卖家，2,用户匹配买家
        order.setEntrustId(entrust.getEntrustId());
        order.setCoinNo(entrust.getCoinNo());
        order.setEntrustPrice(entrust.getEntrustPrice());
        order.setDealPrice(moneyDecimal);
        order.setDealNum(dealNum);
        order.setState("3");
        order.setCreatDate(ValidataUtil.dateFormat(new Date()));
        order.setUpdateTime(ValidataUtil.dateFormat(new Date()));
        order.setReceivablesType(payType);
        order.setPoundage(poundage);
        orderMapper.insertSelective(order);

        //减去委托表库存信息
        entrust.setRemainNum(entrust.getRemainNum().subtract(allNum));
        entrust.setDayLimit(entrust.getDayLimit().subtract(allMoney));
        entrustMapper.updateByPrimaryKeySelective(entrust);
        return order;
    }
    //验证该委托单是否满足条件
    public void checkEntrust(Entrust entrust,BigDecimal num){
        //todo；暂时完成了每日限额和剩余数量，其他还未验证
        //总数量对应的金额
        BigDecimal money = num.multiply(entrust.getEntrustPrice());
        boolean flag = false;
        if(entrust.getDayLimit().compareTo(money)>=0){
            flag = true;
        }
        if (entrust.getRemainNum().compareTo(money)>=0){
            flag = true;
        }
       if(!flag){
           throw new MyException("数量不足，请稍后重试");
       }
    }
    //用户取消购买订单
    public void buyCancelOrder(Order order, User user) throws Exception {
        //判断买家，必须是用户匹配卖家
        if (!order.getBuyUserId().equals(user.getUserId()) || !"1".equals(order.getDealType())){
            throw new MyException("订单和用户不匹配，取消失败");
        }
        //判断订单状态，只能取消买房未付款订单
        if(!"3".equals(order.getState())){
            throw new MyException("只能取消等待付款的订单，取消失败，请联系管理员");
        }
        //时间为15分钟，超时用户只能读，不能写 1000*60*15  15分钟
        long createLong = ValidataUtil.dateToStamp(order.getCreatDate());
        if (createLong<System.currentTimeMillis()+(1000*60*15)){
            throw new Exception("订单超过取消时间，取消失败");
        }
        //释放委托表剩余数量，和每日限额
        Entrust entrust = entrustMapper.selectByPrimaryKey(order.getEntrustId());
        //委托表逻辑
        BigDecimal allNum = order.getDealNum().add(order.getPoundage());//縂數量
        entrust.setDayLimit(entrust.getDayLimit().add(allNum));
        entrust.setRemainNum(entrust.getRemainNum().add(allNum));
        entrustMapper.updateByPrimaryKeySelective(entrust);
        order.setUpdateTime(ValidataUtil.dateFormat(new Date()));
        order.setState("2");
        orderMapper.updateByPrimaryKeySelective(order);
    }
    @Transactional
    /**
     *
     */
    public void sellAffirmOrder(Order order, User user) throws Exception {
        //判断确认订单的是否是承兑商
       if (!order.getSellUserId().equals(user.getUserId()) || !order.getDealType().equals("1")){
           throw new MyException("订单和用户不匹配，确认失败");
       }
       //判断订单状态,
        if(!"4".equals(order.getState())){
            throw new MyException("只能确认已付款的订单");
        }
        //时间为15分钟，超时用户只能读，不能写 1000*60*60*2  15分钟
        long createLong = ValidataUtil.dateToStamp(order.getUpdateTime());
        if (createLong<System.currentTimeMillis()+(1000*60*60*2)){
            throw new Exception("订单超过确认时间，确认失败");
        }
        Entrust entrust = entrustMapper.selectByPrimaryKey(order.getEntrustId());
        if (!user.getUserId().equals(entrust) || !"1".equals(entrust.getEntrustType())){
            throw new Exception("该用户和委托单信息不符，请联系管理员");
        }

        entrust.setDealNum(entrust.getDealNum().add(order.getDealNum()));
        entrust.setPoundage(entrust.getPoundage().add(order.getPoundage()));
        entrustMapper.updateByPrimaryKeySelective(entrust);

        //获取平台用户钱包
        String sysUserStr = "3";
        User sysUser = userMapper.getOneByUserType(sysUserStr);
        UserWallet userWallet = new UserWallet();
        userWallet.setUserId(sysUser.getUserId());
        userWallet.setType(Integer.parseInt(order.getCoinNo()));
        UserWallet sysUserWallet = userWalletMapper.getUserWalletByCondition(userWallet);//系统钱包
        //用户钱包
        User buyUser = userMapper.selectByPrimaryKey(order.getBuyUserId());
        userWallet.setUserId(buyUser.getUserId());
        UserWallet buyUserWallet = userWalletMapper.getUserWalletByCondition(userWallet);//购买人钱包
        User sellUser = userMapper.selectByPrimaryKey(order.getSellUserId());
        userWallet.setUserId(sellUser.getUserId());
        UserWallet sellUserWallet = userWalletMapper.getUserWalletByCondition(userWallet);//卖家钱包
        BigDecimal allNum = order.getDealNum().add(order.getPoundage());//总数量
        sellUserWallet.setUnbalance(sellUserWallet.getUnbalance().subtract(allNum));
        if (sellUserWallet.getUnbalance().compareTo(new BigDecimal("0"))<0){
            throw new MyException("卖家冻结币不足，请联系管理员");
        }
        //卖家扣款，卖家和平台加款
        userWalletMapper.updateUserWalletByCondition(sellUserWallet);
        sysUserWallet.setBalance(sysUserWallet.getBalance().add(order.getPoundage()));
        userWalletMapper.updateUserWalletByCondition(sysUserWallet);
        buyUserWallet.setBalance(buyUserWallet.getBalance().add(order.getDealNum()));
        userWalletMapper.updateUserWalletByCondition(buyUserWallet);
        order.setState("1");
        order.setUpdateTime(ValidataUtil.dateFormat(new Date()));
        orderMapper.updateByPrimaryKeySelective(order);
        //去订单表修改订单状态，更新时间
    }
}
