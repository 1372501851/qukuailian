package com.saiyun.service.console;

import com.saiyun.mapper.console.LogMapper;
import com.saiyun.model.console.Log;
import com.saiyun.util.CamelCaseUtil;
import com.saiyun.util.DateUtil;
import com.saiyun.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author saiyun
 */
@Service
public class LogService {

    @Autowired
    private LogMapper logMapper;

    public List<Log> getPageList(Log log) {
        PageHelper.offsetPage(log.getOffset(), log.getLimit());
        PageHelper.orderBy(CamelCaseUtil.toUnderlineName(log.getSort())+" "+log.getOrder());
        return logMapper.selectAll();
    }

    public void insert(Log log){
        logMapper.insert(log);
    }

    public void insertLoginLog(String username, String ip, String action){
        Log  log = new Log();
        log.setLogId(UuidUtil.getUUID());
        log.setLogUser(username);
        log.setLogTime(DateUtil.getCurrentTime());
        log.setLogIp(ip);
        log.setLogAction(action);
        this.insert(log);
    }


}
