
package com.saiyun.mapper;

import com.saiyun.model.Coin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CoinMapper {

    
    Coin getCoinByCoinNo(@Param("coinNo") Long coinNo);

	Coin getCoinByCondition(Coin coin);

    List<Coin> getCoinByConditions(Coin coin);

    int add(Coin record);

    List<Coin> queryOpenRoutineCoin();

    Coin queryCoinByCoinNo(Long coinNo);

	List<Coin> getCoinTypeByConditions();

	Coin findCoinByFiled(Coin coin);

    List<Coin> queryOpenPntCoin();

	List<Coin> getCoinByConditions();

    Coin queryByCoinName(String coinName);

    List<Coin> queryOpenCoinList();

    /**
     * 查询所有货币集合
     */
    List<Coin> queryAllCoinList();

    /**
     * 获取开放C2C的货币列表
     */
    List<Coin> getOpenTransCoinList();
}
