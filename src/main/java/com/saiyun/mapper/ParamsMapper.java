package com.saiyun.mapper;

import com.saiyun.model.Params;

public interface ParamsMapper {

    Params queryByKey(String paramKey);

	Params getParams(String paramsKey);
	
	Long updateParams(Params param);
   
}

