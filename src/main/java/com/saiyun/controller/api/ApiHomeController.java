package com.saiyun.controller.api;

import com.saiyun.annotation.TokenRequired;
import com.saiyun.model.User;
import com.saiyun.model.UserWallet;
import com.saiyun.model.vo.BPriceVo;
import com.saiyun.model.vo.HomeVo;
import com.saiyun.service.*;
import com.saiyun.util.ReturnUtil;
import com.saiyun.util.StringCheckUtil;
import net.bytebuddy.asm.Advice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/app/home")
public class ApiHomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private HomeService homeService;
    @Autowired
    private ParamsService paramsService;
    @Autowired
    private UserWalletService userWalletService;
    @Autowired
    private ProductService productService;

    /**
     * 首页数据
     * @param map
     * @return
     */
    @RequestMapping("index")
    @TokenRequired
    public ModelMap index(@RequestBody TreeMap<String,String> map){
        try{
            String token = map.get("token");
            User user = userService.getUserByToken(token);
            HomeVo home = homeService.index(user);
            return ReturnUtil.success("操作成功",home);
        }catch (Exception e){
            e.printStackTrace();
            return ReturnUtil.error("后台错误，请联系管理员");
        }
    }

    /**
     * 用户最大购买，单价
     * @param map
     * @return
     */
    @RequestMapping("userSellHome")
    @TokenRequired
    public ModelMap userSellHome(@RequestBody TreeMap<String,String> map){
        try{
            String token = map.get("token");
            User user = userService.getUserByToken(token);
            Map<String,String> returnMap = homeService.userSellHome(user);
            return ReturnUtil.success("操作成功",returnMap);
        }catch (Exception e){
            e.printStackTrace();
            return ReturnUtil.error("后台错误，请联系管理员");
        }
    }

    /**
     * 我要卖用户输入计算总价和数量
     * @param map
     * @return
     */
    @RequestMapping("countNum")
//    @TokenRequired
    public ModelMap countNum(@RequestBody TreeMap<String,String> map){
        try{
//            String token = map.get("token");
//            User user = userService.getUserByToken(token);
            String bType = (String)map.get("bType");//货币类型 1，btc 2,usdt
            String cType = (String)map.get("cType");//价格类型 1,cny 2,eur
            String sType = (String)map.get("sType");//出售类型 1，价格出售 2，按数量出售
            String sum = (String)map.get("sum");//金额总数
            BPriceVo bprice = paramsService.getBprice();//币单价相关;
            if(StringCheckUtil.isEmpty(bType,cType,sType,sum)){
                return ReturnUtil.error("参数不全");
            }
            Map returnMap = homeService.countNum(bType,cType,sType,sum,bprice);
            return ReturnUtil.success("",returnMap);
        }catch (Exception e){
            e.printStackTrace();
            return ReturnUtil.error("后台错误，请联系管理员");
        }
    }
    /**
     * 用户上架货币
     * @param map
     * @return
     */
    @RequestMapping("userSell")
    @TokenRequired
    public ModelMap userSell(@RequestBody TreeMap<String,String> map){
        try{
            String token = map.get("token");
            User user = userService.getUserByToken(token);
            String sum = (String)map.get("sum");//币数量
            String bType = (String)map.get("bType");//货币类型 1，btc 2,usdt
            if(StringCheckUtil.isEmpty(sum,bType)){
                return ReturnUtil.error("参数不全");
            }
            boolean enough = userWalletService.enough(sum, bType, user);//判断货币是否足够
            if(!enough){
                return ReturnUtil.error("货币不足");
            }
            boolean flag = productService.addProduct(user,sum,bType);
            if(flag){
                return ReturnUtil.success("上架成功");
            }else {
                return ReturnUtil.error("上架失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ReturnUtil.error("后台错误，请联系管理员");
        }
    }

}
