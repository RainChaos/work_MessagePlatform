package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.ModuleEntity;
import com.jiapeng.messageplatform.entity.RoleEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface RoleMapper {
    @Select("select max(id) from sys_role")
    String getLastId();

    @Insert("insert into sys_role(id,name) values(#{id},#{name})")
    void insert(RoleEntity entity);

    @Update("update sys_role set name=#{name} where id=#{id}")
    void update(RoleEntity entity);

    @Delete("delete from sys_role where id=#{id}")
    void del(String id);

    @Select("select * from sys_role order by id")
    List<Map<String,Object>> getAll();

    @Select("select * from sys_role_module order by rid")
    List<Map<String,Object>> getRoleModuleAll();

    @Delete("delete from sys_role_module where rid=#{rid}")
    void clearModule(String rid);

    @Insert("insert into sys_role_module(rid,mid) values(#{rid},#{mid})")
    void setModule(@Param("rid") String rid, @Param("mid") String mid);

    @Select("select a.*,r.name rname,m.* from sys_role_module a\n" +
            "left join sys_module m\n" +
            "on a.mid=m.id\n" +
            "left join sys_role r\n" +
            "on a.rid=r.id\n" +
            "where a.rid=#{id}")
    List<Map<String,Object>> getModule(String rid);

    @Select("select a.*,r.name rname,m.* from sys_role_module a\n" +
            "left join sys_module m\n" +
            "on a.mid=m.id\n" +
            "left join sys_role r\n" +
            "on a.rid=r.id\n" +
            "where a.rid=#{id}")
    List<ModuleEntity> getModuleByRid(String rid);
}
