package com.saiyun.mapper;

import com.saiyun.model.Product;
import com.saiyun.model.vo.ProductVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProductMapper {
    int deleteByPrimaryKey(String productId);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(String productId);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<ProductVo> getProductByType(Product record);

    Product matchMoney(Map<String, String> map);

    Product matchAmount(Map<String, String> map);
}