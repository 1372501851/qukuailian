package com.saiyun.controller.api;

import com.saiyun.model.Product;
import com.saiyun.model.vo.ProductVo;
import com.saiyun.service.ProductService;
import com.saiyun.util.ReturnUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.TreeMap;

@RestController
@RequestMapping("/app/product")
public class ApiProductController {
    @Autowired
    private ProductService productService;
    @RequestMapping("buyHome")
    public ModelMap buyHome(@RequestBody TreeMap<String,String> map){
        try{
            String bType = map.get("bType");
            if(StringUtils.isEmpty(bType)){
                return ReturnUtil.error("参数错误");
            }
            List<ProductVo> products = productService.getProductByType(bType);
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return ReturnUtil.error("后台错误，请联系管理员");
        }
    }
}
