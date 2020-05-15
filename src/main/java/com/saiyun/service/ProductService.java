package com.saiyun.service;

import com.saiyun.constant.DefaultConfig;
import com.saiyun.mapper.ProductMapper;
import com.saiyun.mapper.UserWalletMapper;
import com.saiyun.model.Product;
import com.saiyun.model.User;
import com.saiyun.model.UserWallet;
import com.saiyun.model.vo.ProductVo;
import com.saiyun.util.KeyId;
import com.saiyun.util.ValidataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private UserWalletMapper userWalletMapper;

    /**
     * 上架货币
     * @param user 卖方
     * @param sum 数量
     * @param bType b类型
     * @return
     */
    @Transactional
    public boolean addProduct(User user, String sum, String bType) throws Exception {
        BigDecimal sumDecimal = new BigDecimal(sum);
        UserWallet userWallet = userWalletMapper.selectOneByUserId(user.getUserId());
        Product product = new Product();
        product.setProductId(KeyId.nextId());
        product.setUserId(user.getUserId());
        product.setbType(bType);
        product.setMaxAmount(new BigDecimal(DefaultConfig.PRODUCT_MIN_BUY_COUNT));
        product.setMinAmount(new BigDecimal(DefaultConfig.PRODUCT_MIN_BUY_COUNT));
        product.setAmount(sumDecimal);
        product.setcreatdate(ValidataUtil.dateFormat(new Date()));
        if("1".equals(bType)){//btc
            //冻结币
            BigDecimal btcBalance = userWallet.getBtcBalance();//可用币
            BigDecimal btcUnbalance = userWallet.getBtcUnbalance();//冻结币
            if(btcBalance.compareTo(sumDecimal)<0){
                throw new Exception("货币不足");
            }
            userWallet.setBtcUnbalance(btcUnbalance.add(sumDecimal));//加冻结币
            userWallet.setBtcBalance(btcBalance.subtract(sumDecimal));//减可用币
            userWalletMapper.updateByPrimaryKeySelective(userWallet);
            //上架币
            productMapper.insertSelective(product);
            return  true;
        }else if("2".equals(bType)){
            //冻结币
            BigDecimal usdtBalance = userWallet.getUsdtBalance();//可用币
            BigDecimal usdtUnbalance = userWallet.getUsdtUnbalance();//冻结币
            if(usdtBalance.compareTo(sumDecimal)<0){
                throw new Exception("货币不足");
            }
            userWallet.setUsdtUnbalance(usdtUnbalance.add(sumDecimal));//加冻结币
            userWallet.setUsdtBalance(usdtBalance.subtract(sumDecimal));//减可用币
            userWalletMapper.updateByPrimaryKeySelective(userWallet);
            //上架币
            productMapper.insertSelective(product);
            return true;
        }
        return false;
    }

    public List<ProductVo> getProductByType(String bType) {
        String putawayState = "0";
        List<Product> productByType = productMapper.getProductByType(bType, putawayState);
        return null;
    }
}
