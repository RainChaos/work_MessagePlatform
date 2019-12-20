package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.ModuleEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ModuleMapper {
    @SelectProvider(type = ModuleProvider.class,method = "getLastId")
    String getLastId(String parentId);

    @Insert("insert into sys_module(id,parentId,name,icon,path,orderNumber,idName)"
            + "values(#{id},#{parentId},#{name},#{icon},#{path},#{orderNumber},#{idName})")
    void insert(ModuleEntity entity);

    @Update("update sys_module set name=#{name},icon=#{icon},path=#{path},idName=#{idName},"
            + "orderNumber=#{orderNumber} where id=#{id}")
    void update(ModuleEntity entity);

    @Delete("delete from sys_module where id=#{id}")
    void del(String id);

    @Select("select * from sys_module order by parentId,orderNumber")
    List<ModuleEntity> getAll();

    @Select("select * from sys_module where id=#{id}")
    ModuleEntity getModuleById(String id);
}
