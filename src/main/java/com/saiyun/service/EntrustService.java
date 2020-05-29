package com.saiyun.service;

import com.github.pagehelper.PageHelper;
import com.saiyun.mapper.EntrustMapper;
import com.saiyun.mapper.UserMapper;
import com.saiyun.model.Entrust;
import com.saiyun.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EntrustService {
    @Autowired
    private EntrustMapper entrustMapper;
    @Autowired
    private UserMapper userMapper;

    public List<Entrust> getEntruse(Entrust entrust) {
        PageHelper.offsetPage(entrust.getOffset(), entrust.getLimit());
        return entrustMapper.getByCondition(entrust);
    }

    public void save(Entrust entrust) {
        entrustMapper.insertSelective(entrust);
    }

    public List<Map> queryEntry(Entrust entrust) {
        List<Map> list = new ArrayList<>();
        PageHelper.offsetPage(entrust.getOffset(), entrust.getLimit());
        List<Entrust> entrusts = entrustMapper.selectByEntrust(entrust);
        for (Entrust ent : entrusts) {
            Map<Object, Object> map = new HashMap<>();
            //取出承兑商信息
            User user = userMapper.selectByPrimaryKey(ent.getUserId());
            map.put("entrustId",ent.getEntrustId());
            map.put("coinNo",ent.getCoinNo());
            map.put("entrustPrice",ent.getEntrustPrice());
            map.put("entrustMinPrice",ent.getEntrustMinPrice());
            map.put("entrustMaxPrice",ent.getEntrustMaxPrice());
            map.put("remainNum",ent.getRemainNum());
            map.put("wechat",ent.getWechat());
            map.put("alipay",ent.getAlipay());
            map.put("bankcard",ent.getBankcard());
            map.put("iconUrl",user.getIconUrl());
            map.put("nickname",user.getNickname());
            map.put("moneyType",ent.getMoneyType());
            map.put("entrustType",ent.getEntrustType());
            list.add(map);
            //TODO:价格未格式化
            //TODO :用户认证模块暂未实现
//            ent.setAuthMark();
            //TODO :用户交易总量，需要在交易流程打通后设计
//            ent.setTradeNum();
//            ent.setSuccRate();

        }
        return list;
    }

    /**
     * 去匹配一个最低价格,然后再匹配列表中价格最低价格的筛选条件，
     * 最后返回满足的所有数据，遍历，如果购买者和发布者不是一个人将订单记录返回
     * @param entrust
     * @param user
     * @return
     */
    public Entrust match(Entrust entrust, User user) throws Exception {
        BigDecimal maxPrice = entrustMapper.matchMinOrMaxPrice(entrust);
        if (maxPrice == null || maxPrice.compareTo(new BigDecimal("0"))<=0){
            return null;
        }
        entrust.setMinPriceCondit(maxPrice);
        List<Entrust> entrusts = entrustMapper.matchEntrust(entrust);
        for (Entrust ent:entrusts) {
            if (!user.getUserId().equals(ent.getUserId())){
                return ent;
            }
        }
        return null;
    }

    public Entrust getOneById(String entrustId) {
        return entrustMapper.selectByPrimaryKey(entrustId);
    }
    public Entrust selectByMaxPriceLimit(String entrustId) {
        return entrustMapper.selectByMaxPriceLimit(entrustId);
    }


    public Entrust getMatchEntrust(String entrustId, String money) {
        return entrustMapper.checkByEntrustId(entrustId,money);
    }
}
