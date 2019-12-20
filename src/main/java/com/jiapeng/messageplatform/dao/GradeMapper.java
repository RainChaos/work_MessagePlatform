package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.GradeEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface GradeMapper {
    @Select("select max(gr_code) from grade")
    String getLastId();

    @Insert("insert into grade(gr_code,name,orderNumber,sc_code,uuidstr) values(#{gr_code},#{name},#{orderNumber},#{sc_code},#{uuidStr})")
    void insert(GradeEntity entity);

    @Update("update grade set name=#{name},orderNumber=#{orderNumber} where gr_code=#{gr_code}")
    void update(GradeEntity entity);

    @Delete("delete from grade where gr_code=#{gr_code}")
    void del(String gr_code);

    @Select("select top 1 * from grade where name=#{name} and sc_code = #{sc_code}")
    GradeEntity getByName(String name,String sc_code);


    @SelectProvider(type=GradeProvider.class,method = "listSql")
    List<Map<String,Object>> list(@Param("sc_code") String sc_code);

    @Select("select * from grade where gr_code in" +
            "(select gr_code from class where sc_code=#{sc_code} group by gr_code)" +
            "order by orderNumber")
    List<Map<String,Object>> listSchoolGrade(String sc_code);

    @Select("select * from grade where gr_code = #{gr_code}")
    GradeEntity getGradeByGrCode(String gr_code);

    @Select("select max(ordernumber) from grade")
    Integer getLastOrderNumber();

    @Select("select * FROM  grade where uuidstr = #{uuidStr} ")
    GradeEntity getGradeByUUID(String uuidStr);


    @Delete("delete  FROM  grade where uuidstr = #{uuidStr} ")
    void deleteGradeByUUID(String uuidStr);
}
