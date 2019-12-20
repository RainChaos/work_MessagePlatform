package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.SchoolWX;

public interface SchoolWXMapper {
    int deleteByPrimaryKey(String scCode);

    int insert(SchoolWX record);

    int insertSelective(SchoolWX record);

    SchoolWX selectByPrimaryKey(String scCode);

    int updateByPrimaryKeySelective(SchoolWX record);

    int updateByPrimaryKey(SchoolWX record);
}