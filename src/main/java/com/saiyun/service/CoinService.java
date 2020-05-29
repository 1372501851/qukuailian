
package com.saiyun.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.saiyun.mapper.CoinMapper;
import com.saiyun.mapper.ParamsMapper;
import com.saiyun.mapper.UserMapper;
import com.saiyun.mapper.UserWalletMapper;
import com.saiyun.model.Coin;
import com.saiyun.util.ValidataUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CoinService {

    @Autowired
    private CoinMapper coinMapper;

    public Coin getCoinByCoinNo(String coinNo){
        Coin coinByCoinNo = coinMapper.getCoinByCoinNo(Long.parseLong(coinNo));
        return coinByCoinNo;
    }


}
