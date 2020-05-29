package com.saiyun.mapper;

import com.saiyun.model.Entrust;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface EntrustMapper {
    int deleteByPrimaryKey(String entrustId);

    int insert(Entrust record);

    int insertSelective(Entrust record);

    Entrust selectByPrimaryKey(String entrustId);

    Entrust selectByMaxPriceLimit(String entrustId);

    int updateByPrimaryKeySelective(Entrust record);

    int updateByPrimaryKey(Entrust record);

    List<Entrust> getByCondition(Entrust entrust);

    List<Entrust> selectByEntrust(Entrust entrust);
    //匹配买单
    List<Entrust> matchEntrust(Entrust entrust);
    //如果entrust.entrustType是0，那么返回最高价格，如果是1，那么返回最低价格
    BigDecimal matchMinOrMaxPrice(Entrust entrust);

    Entrust checkByEntrustId(@Param("entrustId") String entrustId, @Param("money") String money);
}