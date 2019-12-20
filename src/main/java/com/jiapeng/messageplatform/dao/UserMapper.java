package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.ModuleEntity;
import com.jiapeng.messageplatform.entity.UserEntity;
import com.jiapeng.messageplatform.utils.DataGridJson;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.management.relation.Role;
import java.util.List;
import java.util.Map;

public interface UserMapper {
    @Insert("insert into sys_user(name,passWord,scCode) " +
            "values(#{name},#{passWord},#{scCode})")
    void add(UserEntity entity);

    @UpdateProvider(type = UserProvider.class, method = "updateSql")
    void update(UserEntity entity);

    @Delete("delete from sys_user where id=#{id}")
    void del(int id);

    @Select("select * from sys_user where id=#{id}")
    UserEntity get(int id);

    @Select("select * from sys_user where name=#{name}")
    UserEntity getByName(String name);

    @Update("update sys_user set disLog=#{disLog},disLogReason=#{disLogReason},disLogTime=#{disLogTime} where id=#{id}")
    void setDis(UserEntity entity);

    @SelectProvider(type = UserProvider.class, method = "listSql")
   //List<Map<String,Object>> list(String key, int rows, int page);
    List<Map<String, Object>> list(@Param("key") String key, @Param("rows") int rows, @Param("page") int page);

    @SelectProvider(type = UserProvider.class, method = "countSql")
    int count(String key);

    @Delete("delete from sys_user_role where uid=#{id}")
    void clearRole(@Param("id")int id);

    @Insert("insert into sys_user_role(uid,rid) values(#{uid},#{rid})")
    void setRole(@Param("uid") int uid, @Param("rid") String rid);

    @Select("select * from sys_role where id in(select rid from sys_user_role where uid=#{uid})")
    List<Map<String, Object>> getRoles(int uid);

    @Select("select * from sys_module where id in" +
            "(select mid from sys_role_module where rid in" +
            "(select rid from sys_user_role where uid=#{uid})) order by parentId,orderNumber")
    List<ModuleEntity> getModules(int uid);

    @Select("select * from sys_user_role")
    List<Map<String, Object>> getUserRolelist();
}
