package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.ClassInfo;
import com.jiapeng.messageplatform.entity.ClassTeacher;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by HZL on 2019/8/7.
 */
public interface ClassTeacherMapper {

    @Insert("insert into class_teacher(cl_code,te_no,isheadmaster) " +
            "values(#{clCode},#{teNo},#{isHeadMaster})")
    int insert(ClassTeacher classTeacher);

    @Delete("delete class_teacher where te_no=#{teNo} ")
    void del(String teNo);

    @Delete("delete class_teacher where cl_code=#{cl_code} ")
    void delClassTeacherByClCode(String cl_code);

    @Delete("delete class_teacher where te_no=#{teNo} and cl_code= #{clCode}")
    void delClass(@Param("teNo") String teNo, @Param("clCode") String clCode);

    //查找该老师是否有对应的班级
    @Select("select top 1 * from class_teacher where te_no=#{teNo}")
    ClassTeacher getByteNo(@Param("teNo") String teNo);

}
