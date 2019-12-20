package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.ConfigDefine;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ConfigDefineMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ConfigDefine record);

    int insertSelective(ConfigDefine record);

    ConfigDefine selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConfigDefine record);

    int updateByPrimaryKey(ConfigDefine record);

    @Select("select * from config_define")
    List<ConfigDefine> list();

    @Select("select * from config_define where code = #{code}")
    ConfigDefine selectByCode(@Param("code") String code);
}