package com.saiyun.controller.api;

import com.github.pagehelper.PageInfo;
import com.saiyun.annotation.TokenRequired;
import com.saiyun.model.Product;
import com.saiyun.model.User;
import com.saiyun.model.member.Member;
import com.saiyun.model.vo.BPriceVo;
import com.saiyun.model.vo.ProductVo;
import com.saiyun.service.ParamsService;
import com.saiyun.service.ProductService;
import com.saiyun.service.UserService;
import com.saiyun.util.ReturnUtil;
import com.saiyun.util.StringCheckUtil;
import com.saiyun.util.ValidataUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/app/product")
public class ApiProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ParamsService paramsService;
    @Autowired
    private UserService userService;
    /**
     * 快捷买币匹配卖家并计算价格和数量
     * @param map
     * @return
     * @throws Exception
     */
    @PostMapping("matchSelf")
    public ModelMap matchSelf(@RequestBody TreeMap<String,String> map) throws Exception{
        String bType = map.get("bType");//b类型
        String payType = map.get("payType");//付款方式
        String buyType = map.get("buyType");//购买类型 1,金额,2数量
        String amount = map.get("amount");//数量
        String money = map.get("money");//金额
        if ("1".equals(buyType)){
            Product product = productService.matchMoney(bType, payType, money);//根据金额匹配卖家;
            if (product == null){
                return ReturnUtil.error("没有匹配到卖家，请稍后再试");
            }
            Map<String, Object> returnMap = new HashMap<>();
            returnMap.put("productId",product.getProductId());
            returnMap.put("payType",payType);
            returnMap.put("unitPrice",product.getUnitPrice());
            returnMap.put("money",money);
            BigDecimal bAmount = new BigDecimal(money).divide(product.getUnitPrice())
                    .setScale(6, RoundingMode.DOWN);//cny金额除以卖家设定的单价==币数量
            returnMap.put("amount",bAmount);
            return ReturnUtil.success("",returnMap);
        }else if ("2".equals(buyType)){
            Product product = productService.matchAmount(bType,payType,amount);//根据数量匹配卖家
            if (product == null){
                return ReturnUtil.error("没有匹配到卖家，请稍后再试");
            }
            Map<String, Object> returnMap = new HashMap<>();
            returnMap.put("productId",product.getProductId());
            returnMap.put("payType",payType);
            returnMap.put("unitPrice",product.getUnitPrice());
            returnMap.put("amount",amount);
            BigDecimal bMoney = new BigDecimal(amount).multiply(product.getUnitPrice())
                    .setScale(2, RoundingMode.DOWN);//cny金额除以卖家设定的单价==币数量
            returnMap.put("money",bMoney);
            return ReturnUtil.success("",returnMap);
        }
        return ReturnUtil.error("参数错误");
    }
    @PostMapping("generatedOrder")
    @TokenRequired
    public ModelMap generatedOrder (@RequestBody TreeMap<String,String> map) throws Exception{
        String token = map.get("token");
        String productId = map.get("productId");
        String money = map.get("money");
        String amount = map.get("amount");
        boolean flag = productService.checkPrice(amount, money, productId);//验证数量和金额是否合理
        if (!flag){
            return ReturnUtil.error("价格计算错误");
        }else {
            //生成订单
            //
        }
        return null;
    }
    /**
     * 快捷购买
     * 币价格可以
     * @param map
     * @return
     */

    @PostMapping("shortcutBuy")
    public ModelMap selfBuy(@RequestBody TreeMap<String,String> map){
        try{
            String token = map.get("token");//token
            String bType = map.get("bType");//b类型
            String payType = map.get("payType");//付款方式
            String amount = map.get("amount");//数量
            String money = map.get("money");//金额
            User user = userService.getUserByToken(token);
            BPriceVo bprice = paramsService.getBprice();
//            boolean flag = productService.checkPrice(amount, money, bType, bprice);
//            if (flag){
                //生成订单

//            }else {
//                return ReturnUtil.error("价格不匹配");
//            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return ReturnUtil.error("后台错误，请联系管理员");
        }
    }




}
