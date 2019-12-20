package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.SchoolDB;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SchoolDBMapper {
    int deleteByPrimaryKey(String scCode);

    int insert(SchoolDB record);

    int insertSelective(SchoolDB record);

    SchoolDB selectByPrimaryKey(String scCode);

    int updateByPrimaryKeySelective(SchoolDB record);

    int updateByPrimaryKey(SchoolDB record);

    List <SchoolDB> list(@Param("pageIndex") int pageIndex,@Param("pageSize") int pageSize);

    int listCount();
}