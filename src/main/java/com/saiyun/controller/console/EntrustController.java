package com.saiyun.controller.console;

import com.github.pagehelper.PageInfo;
import com.saiyun.core.utils.ShiroUtil;
import com.saiyun.model.Acceptance;
import com.saiyun.model.Coin;
import com.saiyun.model.Entrust;
import com.saiyun.model.User;
import com.saiyun.model.console.Admin;
import com.saiyun.model.member.Member;
import com.saiyun.service.AcceptanceService;
import com.saiyun.service.CoinService;
import com.saiyun.service.EntrustService;
import com.saiyun.util.KeyId;
import com.saiyun.util.ReturnUtil;
import com.saiyun.util.ValidataUtil;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/console/entrust")
public class EntrustController {
    @Autowired
    private EntrustService entrustService;
    @Autowired
    private AcceptanceService acceptanceService;
    @Autowired
    private CoinService coinService;
    @RequiresPermissions("entrust:index")
    @RequestMapping(value = "/index",method = {RequestMethod.GET})
    public String index(Model model){
        return "console/entrust/index";
    }
    @RequiresPermissions("entrust:index")
    @RequestMapping(value = "/list",method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap from(Entrust entrust, Model model){
        Admin admin = ShiroUtil.getUserInfo();
        Acceptance acceptance = new Acceptance();
        acceptance.setAdminId(admin.getUid());
        Acceptance accept = acceptanceService.getOneByAcceptance(acceptance);//承兑商详情数据
        entrust.setUserId(accept.getUserId());
        ModelMap map = new ModelMap();
        List<Entrust> lists =  entrustService.getEntruse(entrust);
        map.put("pageInfo", new PageInfo<Entrust>(lists));
        map.put("queryParam", entrust);
        return ReturnUtil.success("加载成功", map, null);
    }
    @RequiresPermissions("entrust:save")
    @RequestMapping(value = "/from",method = {RequestMethod.GET})
    public String from(Model model){
        return "console/entrust/from";
    }
    @RequiresPermissions("entrust:save")
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @ResponseBody
    public ModelMap save(Entrust entrust) throws Exception {
        Admin admin = ShiroUtil.getUserInfo();
        Acceptance acceptance = new Acceptance();
        acceptance.setAdminId(admin.getUid());
        Acceptance accept = acceptanceService.getOneByAcceptance(acceptance);//承兑商详情数据
        Coin coin = coinService.getCoinByCoinNo(entrust.getCoinNo());//币种详情数据
        entrust.setEntrustId(KeyId.nextId());
        entrust.setEntrustNo(KeyId.nextId());
        entrust.setUserId(accept.getUserId());
        entrust.setRemainNum(entrust.getEntrustNum());
        entrust.setDayLimit(accept.getDayLimit());
        entrust.setPoundageScale(coin.getMinFee());
        entrust.setState("0");
        entrust.setCreatDate(ValidataUtil.dateFormat(new Date()));
        entrustService.save(entrust);
        return ReturnUtil.success("操作成功", null, "/console/entrust/index");
    }
}
