package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.ClassInfo;
import com.jiapeng.messageplatform.entity.Leave;
import com.jiapeng.messageplatform.entity.LeaveTotal;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface LeaveMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Leave record);

    int insertSelective(Leave record);

    Leave selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Leave record);

    int updateByPrimaryKey(Leave record);

    //
    @Select("select * from [leave] where stuNo=#{stuNo} and " +
            "((startDate >=#{startDate} and startDate<= #{endDate}) " +
            "or (endDate <=#{startDate} and endDate>= #{endDate}))")
    Leave findOneByStuNoAndTime(@Param("stuNo") String stuNo, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //用户输入的时间大于或等于请假开始时间，小于或等于请假结束时间
    @Select("select lea.* from [leave] lea left join teacher tea on tea.te_no = lea.teNo where tea.sc_code=#{sc_code} and lea.stuNo=#{stuNo}" +
            " and startDate <= #{dateTime} and endDate >= #{dateTime}")
    List<Leave> finfByStuNoAndDate(@Param("sc_code") String sc_code,@Param("stuNo") String stuNo, @Param("dateTime") Date dateTime);

//    @Select("select cla.cl_code,cla.name,stu.stuNo,stu.stuName,stu.sex,lea.* from leave lea " +
//            "left join student stu on stu.stuNo = lea.stuNo\n" +
//            "left join class cla on cla.cl_code = stu.cl_code \n" +
//            "where lea.teNo=#{teNo}")
//            List<Map<String,Object>> list(String teNo);

    List<Map<String,Object>> list(@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize, @Param("stuNoAndName") String stuNoAndName, @Param("startDate") String startDate, @Param("endDate") String endDate,@Param("cl_code") String cl_code,@Param("teNo") String teNo);

    int listCount(@Param("stuNoAndName") String stuNoAndName, @Param("startDate") String startDate, @Param("endDate") String endDate,@Param("cl_code") String cl_code,@Param("teNo") String teNo);


    //获取每个班的请假情况
    List<LeaveTotal> totals(@Param("cl_code") String cl_code, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("teNo") String teNo);

    //获取每个班的请假情况
    List<LeaveTotal> totalsNum(@Param("cl_code") String cl_code, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("teNo") String teNo);

    @Delete("delete from [leave] where teNo = #{teNo}")
    int deleteByTeNo(String teNo);

}
