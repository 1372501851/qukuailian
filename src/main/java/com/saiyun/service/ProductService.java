package com.saiyun.service;

import com.github.pagehelper.PageHelper;
import com.saiyun.constant.DefaultConfig;
import com.saiyun.mapper.ProductMapper;
import com.saiyun.mapper.ReceptionAccountMapper;
import com.saiyun.mapper.UserWalletMapper;
import com.saiyun.model.Product;
import com.saiyun.model.ReceptionAccount;
import com.saiyun.model.User;
import com.saiyun.model.UserWallet;
import com.saiyun.model.vo.AccountVo;
import com.saiyun.model.vo.BPriceVo;
import com.saiyun.model.vo.ProductVo;
import com.saiyun.util.KeyId;
import com.saiyun.util.ValidataUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private UserWalletMapper userWalletMapper;
    @Autowired
    private ReceptionAccountMapper receptionAccountMapper;



    /**
     * 验证价格
     * @param amount
     * @param money
     */
    public boolean checkPrice(String amount, String money, String productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        //去商品表取出商品信息，然后用传入的数量乘以单价，如果和传入的金额的相等就返回true
        BigDecimal bMoney = new BigDecimal(amount).multiply(product.getUnitPrice()).
                setScale(2, RoundingMode.DOWN);
        BigDecimal one = new BigDecimal("1.00");
        //用传入金额减去1块和加上1块和计算金额比较，如果计算金额在这个区间则传入价格没问题
        if (new BigDecimal(money).subtract(one).compareTo(bMoney)<=0 && new BigDecimal(money).add(one).compareTo(bMoney)>=0){

            return true;
        }else {
            return false;
        }
    }

    public Product matchMoney(String bType, String payType, String money) throws Exception {
        Map<String, String> map = new HashMap<>();
        //去数据库查找数据
        if("1".equals(payType)){
            map.put("wechat","wechat");
        }else if ("2".equals(payType)){
            map.put("alipay","alipay");
        }else if("3".equals(payType)){
            map.put("bankcard","bankcard");
        }
        map.put("bType",bType);
        map.put("money",money);
        Product product = productMapper.matchMoney(map);
        return product;
    }

    public Product matchAmount(String bType, String payType, String amount) {
        Map<String, String> map = new HashMap<>();
        //去数据库查找数据
        if("1".equals(payType)){
            map.put("wechat","wechat");
        }else if ("2".equals(payType)){
            map.put("alipay","alipay");
        }else if("3".equals(payType)){
            map.put("bankcard","bankcard");
        }
        map.put("bType",bType);
        map.put("amount",amount);
        Product product = productMapper.matchAmount(map);
        return product;
    }
}
