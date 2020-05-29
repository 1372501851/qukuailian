package com.saiyun.controller.api;

import com.saiyun.annotation.TokenRequired;
import com.saiyun.model.User;
import com.saiyun.model.UserWallet;
import com.saiyun.model.vo.BPriceVo;
import com.saiyun.model.vo.HomeVo;
import com.saiyun.model.vo.SellVo;
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

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 初始化数据处理
 */
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
    private ProductService productService;

    /**
     * 首页数据
     * @param map
     * @return
     */
    @RequestMapping("index")
    @TokenRequired
    //TODO:数据库变迁，需要重新写逻辑
    public ModelMap index(@RequestBody TreeMap<String,String> map){
        try{
            String token = map.get("token");
            User user = userService.getUserByToken(token);
            BPriceVo bprice = paramsService.getBprice();
            Map index = homeService.index(user, bprice);
            return ReturnUtil.success("操作成功",index);
        }catch (Exception e){
            e.printStackTrace();
            return ReturnUtil.error("后台错误，请联系管理员");
        }
    }

//    /**
//     * 用户卖出初始界面
//     * @param map
//     * @return
//     */
//    //TODO:数据库变迁，重新设计
//    @RequestMapping("userSellHome")
//    @TokenRequired
//    public ModelMap userSellHome(@RequestBody TreeMap<String,String> map){
//        try{
//            String token = map.get("token");
//            User user = userService.getUserByToken(token);
//            BPriceVo bprice = paramsService.getBprice();
//            List<SellVo> list = homeService.userSellHome(user,bprice);
//            return ReturnUtil.success("操作成功",list);
//        }catch (Exception e){
//            e.printStackTrace();
//            return ReturnUtil.error("后台错误，请联系管理员");
//        }
//    }
//
//    /**
//     * 获取所有法币下所有币种的单价
//     * @param map
//     * @return
//     */
//    @RequestMapping
//    public ModelMap getUnitPrice(@RequestBody TreeMap<String,String> map){
//        BPriceVo bprice = paramsService.getBprice();//币单价相关;
//        Map<String,Map> maps = homeService.getUnitPrice(bprice);
//        return ReturnUtil.success("",maps);
//    }
//
//    /**
//     * 我要卖用户输入计算总价和数量
//     * @param map
//     * @return
//     */
//    //TODO:重新设计
//    @RequestMapping("countNum")
////    @TokenRequired
//    public ModelMap countNum(@RequestBody TreeMap<String,String> map){
//        try{
////            String token = map.get("token");
////            User user = userService.getUserByToken(token);
//            String bType = (String)map.get("bType");//货币类型 1，btc 2,usdt
//            String cType = (String)map.get("cType");//价格类型 1,cny 2,eur
//            String sType = (String)map.get("sType");//出售类型 1，价格出售 2，按数量出售
//            String sum = (String)map.get("sum");//金额总数
//            BPriceVo bprice = paramsService.getBprice();//币单价相关;
//            if(StringCheckUtil.isEmpty(bType,cType,sType,sum)){
//                return ReturnUtil.error("参数不全");
//            }
//            Map returnMap = homeService.countNum(bType,cType,sType,sum,bprice);
//            return ReturnUtil.success("",returnMap);
//        }catch (Exception e){
//            e.printStackTrace();
//            return ReturnUtil.error("后台错误，请联系管理员");
//        }
//    }


}
