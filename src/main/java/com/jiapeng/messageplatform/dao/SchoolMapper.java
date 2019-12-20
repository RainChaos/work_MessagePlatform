package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.SchoolEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface SchoolMapper {
    @Select("select max(sc_code) from school")
    String getLastId();

    @Select("select * from school")
    String getSchoolAll();

    @Insert("insert into school(sc_code,name,token,address,contacts,contactPhone,createTime,menhukey,uuidstr) " +
            "values(#{sc_code},#{name},#{token},#{address},#{contacts},#{contactPhone},#{createTime},#{menhuKey},#{uuidStr})")
    void insert(SchoolEntity entity);

    @Update("update school set name=#{name},address=#{address},contacts=#{contacts}," +
            "contactPhone=#{contactPhone} where sc_code=#{sc_code}")
    void update(SchoolEntity entity);

    @Update("update school set menhukey=#{menhuKey} where sc_code=#{sc_code}")
    void setMenhuKey(SchoolEntity entity);

    @Delete("delete from school where sc_code=#{sc_code}")
    int del(String sc_code);

    @Update("update school set status=#{status},statusTime=#{statusTime},statusReason=#{statusReason}" +
            " where sc_code=#{sc_code}")
    void setStatus(SchoolEntity entity);

    @SelectProvider(type = SchoolProvider.class, method = "listSql")
    List<Map<String, Object>> list(@Param("scCode") String scCode, @Param("key") String key,@Param("rows") int rows,@Param("page") int page);

    @SelectProvider(type = SchoolProvider.class, method = "countSql")
    int count(@Param("scCode") String scCode, @Param("key") String key);

    @Update("update school set token=#{token} where sc_code=#{sc_code}")
    void updateToken(String sc_code, String token);

    @Select("select * from school where sc_code=#{sc_code}")
    SchoolEntity load(String scCode);

    @Select("SELECT * FROM  school   WHERE sc_code = (SELECT sc_code FROM class WHERE cl_code=#{clcode})")
    SchoolEntity selectByClassCode(String clcode);

    @Select("select * from school where uuidstr=#{uuidStr}")
    SchoolEntity getSchoolByUUID(String uuidStr);


    @Delete("delete  from school where uuidstr=#{uuidStr}")
    void deleteSchoolByUUID(String uuidStr);
}
