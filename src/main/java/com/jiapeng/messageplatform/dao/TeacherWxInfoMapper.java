package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.TeacherWxInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TeacherWxInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TeacherWxInfo record);

    int insertSelective(TeacherWxInfo record);

    TeacherWxInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TeacherWxInfo record);

    int updateByPrimaryKey(TeacherWxInfo record);

    @Delete("delete from teacher_wx_info where teNo = #{teNo}")
    int deleteByTeNo(String teNo);

    @Delete("delete from teacher_wx_info where clCode = #{clCode}")
    int deleteByClCode(String clCode);



    @Select("select * from teacher_wx_info a " +
            "left join class_teacher b on a.teNo = b.te_no " +
            " where b.isheadmaster=1 and a.clCode = #{clCode}")
    List<TeacherWxInfo> getListByClCode(@Param("clCode") String clCode);
}