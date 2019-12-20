package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.ClassEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface ClassMapper {
    @Select("select max(cl_code) from class")
    String getLastId();

    @Insert("insert into class(cl_code,cl_number,name,sc_code,gr_code,uuidstr) " +
            "values(#{cl_code},#{cl_number},#{name},#{sc_code},#{gr_code},#{uuidStr})")
    void insert(ClassEntity entity);

    @Update("update class set cl_number=#{cl_number},name=#{name},gr_code=#{gr_code} where cl_code=#{cl_code}")
    void update(ClassEntity entity);

    @Delete("delete from class where cl_code=#{cl_code}")
    void del(String cl_code);

    @Select("select * from class where sc_code=#{sc_code} and cl_number=#{cl_number}")
    ClassEntity getByNumber(String sc_code, String cl_number);

    @Select("select * from class where sc_code=#{sc_code} and name=#{name}")
    ClassEntity getByName(@Param("sc_code") String sc_code, @Param("name") String name);

    @SelectProvider(type = ClassProvider.class, method = "listSql")
    List<Map<String, Object>> list(@Param("sc_code") String sc_code, @Param("gr_code") String gr_code, @Param("limit") int rows, @Param("page") int page);

    @Select("select cla.cl_code from class cla \n" +
            " left join class_teacher ct on  ct.cl_code = cla.cl_code \n" +
            " where cla.sc_code=#{sc_code} and cla.gr_code = #{gr_code} and ct.te_no = #{teNo}")
    List<String> listByCodeAndTeNo(@Param("sc_code") String sc_code, @Param("gr_code") String gr_code, @Param("teNo") String teNo);

    @SelectProvider(type = ClassProvider.class, method = "countSql")
    int count(@Param("sc_code") String sc_code, @Param("gr_code") String gr_code);

    @Select("select  top 1 *  FROM  class where gr_code=#{gr_code} ")
    ClassEntity findByGradeCode(String gr_code);

    @Select("select * FROM  class where cl_code=#{cl_code} ")
    ClassEntity getClassByClCode(String gr_code);

    @Select("select * FROM  class where uuidstr=#{uuidStr} ")
    ClassEntity getClassByUUID(String uuidStr);

    @Delete("delete  FROM  class where uuidstr=#{uuidStr} ")
    void deleteClassByUUID(String uuidStr);

    @Select("select * FROM  class where gr_code=#{gr_code} ")
    List<Map<String, Object>> listByGradeCode(String gr_code);

}
