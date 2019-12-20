package com.jiapeng.messageplatform.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * Created by HZL on 2019/11/7.
 */
@Mapper
public interface TestMapper {
    @Select("select top 10 * from t_scs_student ")
    List<Map<String, Object>> stuList();

    @Select("select top 10 * from student where stuId = #{stuId}")
    List<Map<String, Object>> msstuList(String stuId);


    @Select("select top 10 * from T_SCS_SchoolStructure ")
    List<Map<String, Object>> unitList();
}
