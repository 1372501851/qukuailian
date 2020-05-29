package com.saiyun.controller.console;

import com.github.pagehelper.PageInfo;
import com.saiyun.mapper.UserMapper;
import com.saiyun.model.Acceptance;
import com.saiyun.model.BaseEntity;
import com.saiyun.model.User;
import com.saiyun.model.console.Role;
import com.saiyun.model.member.Member;
import com.saiyun.service.AcceptanceService;
import com.saiyun.service.UserService;
import com.saiyun.util.ReturnUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/console/acceptance")
public class AcceptanceController {
    @Autowired
    private AcceptanceService acceptanceService;
    @Autowired
    private UserService userService;
    @RequiresPermissions("acceptance:index")
    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        return "console/acceptance/index";
    }
    @RequiresPermissions("acceptance:index")
    @RequestMapping(value = "/list",method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(Acceptance acceptance){
        ModelMap map = new ModelMap();
        List<Map> lists = acceptanceService.getPageList(acceptance);
        map.put("pageInfo",new PageInfo<Map>(lists));
        map.put("queryParam", acceptance);
        return ReturnUtil.success("加载成功", map, null);
    }

    @RequiresPermissions("acceptance:edit")
    @RequestMapping(value = "/from",method = {RequestMethod.GET})
    public String from (Acceptance acceptance,Model model){
        Map<String, Object> map = new HashMap<>();
        if (acceptance.getAcceptanceId()!= null){
            map = acceptanceService.getOneAcceptDetailInfo(acceptance);
     }
        model.addAttribute("accept",map);
        return "console/acceptance/from";
    }
    @RequestMapping("sysuserpage")
    @RequiresPermissions("acceptance:edit")
	public String sysindex(String acceptanceId,Model model) {
		return "/console/acceptance/sysuser";
	}
	@RequestMapping("/sysuser")
	@ResponseBody
    @RequiresPermissions("acceptance:edit")
	public ModelMap getSysUserList(User user) {
		ModelMap map = new ModelMap();
		try {
			List<User> list = userService.getAll(user);
			map.put("pageInfo", new PageInfo<User>(list));
	        map.put("queryParam", user);
			return ReturnUtil.success("操作成功", map, null);
		}catch(Exception e) {
			e.printStackTrace();
			return ReturnUtil.error("操作失败", null, null);
		}
	}
	@RequestMapping("update")
	@ResponseBody
    @RequiresPermissions("acceptance:edit")
	public ModelMap display(Acceptance acceptance) {
        acceptanceService.updateByAcceptance(acceptance);
        return ReturnUtil.success("操作成功", null, "/console/acceptance/index");
	}
}
