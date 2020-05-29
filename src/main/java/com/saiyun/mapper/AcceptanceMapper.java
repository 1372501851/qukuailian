package com.saiyun.mapper;

import com.saiyun.model.Acceptance;

import java.util.List;

public interface AcceptanceMapper {
    int deleteByPrimaryKey(String acceptanceId);

    int insert(Acceptance record);

    int insertSelective(Acceptance record);

    Acceptance selectByPrimaryKey(String acceptanceId);

    int updateByPrimaryKeySelective(Acceptance record);

    int updateByPrimaryKey(Acceptance record);

    Acceptance selectByAcceptance(Acceptance acceptance);

    List<Acceptance> selectAll();
}