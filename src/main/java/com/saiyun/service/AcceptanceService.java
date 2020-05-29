package com.saiyun.service;

import com.github.pagehelper.PageHelper;
import com.saiyun.mapper.AcceptanceMapper;
import com.saiyun.mapper.UserMapper;
import com.saiyun.mapper.console.AdminMapper;
import com.saiyun.model.Acceptance;
import com.saiyun.model.User;
import com.saiyun.model.console.Admin;
import com.saiyun.util.CamelCaseUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AcceptanceService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AcceptanceMapper acceptanceMapper;
    @Autowired
    private AdminMapper adminMapper;
    public Acceptance getOneByAcceptance(Acceptance acceptance){
        Acceptance acceptance1 = acceptanceMapper.selectByAcceptance(acceptance);
        return acceptance1;
    }


    public void insertAcceptance(Acceptance acceptance) {
        acceptanceMapper.insertSelective(acceptance);
    }

    public List<Map> getPageList(Acceptance acceptance) {
        List<Map> lists = new ArrayList<>();
        PageHelper.offsetPage(acceptance.getOffset(), acceptance.getLimit());
        PageHelper.orderBy(CamelCaseUtil.toUnderlineName(acceptance.getSort())+" "+acceptance.getOrder());
        List<Acceptance> acceptances = acceptanceMapper.selectAll();
        for (Acceptance list :
                acceptances) {
            Map<String, Object> map = new HashMap<>();
            map.put("dayLimit",list.getDayLimit());
            map.put("acceptId",list.getAcceptanceId());
            if (list.getAdminId()!=null){
                Admin admin = adminMapper.selectByPrimaryKey(list.getAdminId());
                if (admin != null){
                    map.put("adminId",admin.getUid());
                    map.put("adminUsername",admin.getUsername());
                }
            }
            if (list.getUserId() != null){
                User user = userMapper.selectByPrimaryKey(list.getUserId());
                if (user!=null){
                    map.put("userId",user.getUserId());
                    map.put("username",user.getNickname());
                }
            }
            lists.add(map);
        }
        return lists;
    }

    public Map getOneAcceptDetailInfo(Acceptance acceptance) {
        Map<String, Object> map = new HashMap<>();
        map.put("dayLimit",acceptance.getDayLimit());
        map.put("acceptId",acceptance.getAcceptanceId());
        if(!StringUtils.isEmpty(acceptance.getUserId())){
            User user = userMapper.selectByPrimaryKey(acceptance.getUserId());
            if (user!=null){
                map.put("userId",user.getUserId());
                map.put("username",user.getNickname());
            }
        }
        return map;
    }

    public void updateByAcceptance(Acceptance acceptance) {
        acceptanceMapper.updateByPrimaryKeySelective(acceptance);
    }
}
