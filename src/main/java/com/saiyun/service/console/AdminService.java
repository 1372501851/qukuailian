package com.saiyun.service.console;

import com.saiyun.mapper.console.AdminMapper;
import com.saiyun.model.console.Admin;
import com.saiyun.util.CamelCaseUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author saiyun
 */
@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    public List<Admin> getPageList(Admin admin) {
        PageHelper.offsetPage(admin.getOffset(), admin.getLimit());
        PageHelper.orderBy(CamelCaseUtil.toUnderlineName(admin.getSort())+" "+admin.getOrder());
        return adminMapper.selectAll();
    }

    public Integer getCount(Example example){
        return adminMapper.selectCountByExample(example);
    }

    public Admin getById(String id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    public Admin findByUsername(String username) {
        return adminMapper.selectByUsername(username);
    }

    public void deleteById(String id) {
        adminMapper.deleteByPrimaryKey(id);
    }

    public void insert(Admin admin){
        adminMapper.insert(admin);
    }

    public void save(Admin admin) {
        if (admin.getUid() != null) {
            adminMapper.updateByPrimaryKey(admin);
        } else {
            adminMapper.insert(admin);
        }
    }

    public void updateExample(Admin admin, Example example){
        adminMapper.updateByExampleSelective(admin, example);
    }



}
