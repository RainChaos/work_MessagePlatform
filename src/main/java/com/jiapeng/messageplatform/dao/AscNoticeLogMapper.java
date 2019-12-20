package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.AscNoticeLog;
import com.jiapeng.messageplatform.model.ComeInOutRecord;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface AscNoticeLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AscNoticeLog record);

    int insertSelective(AscNoticeLog record);

    AscNoticeLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AscNoticeLog record);

    int updateByPrimaryKey(AscNoticeLog record);

    @Select("select asclog.*,stu.stuName,sch.name as schoolName,cla.name as className,gra.name as graName,lea.startDate,lea.endDate,lea.reason from asc_notice_log asclog\n" +
            "left join school sch\n" +
            "on asclog.sc_code=sch.sc_code\n" +
            "left join student stu\n" +
            "on asclog.stuNo=stu.stuNo\n" +
            "left join class cla\n" +
            "on stu.cl_code=cla.cl_code\n" +
            "left join grade gra\n" +
            "on gra.gr_code=cla.gr_code\n" +
            "left join leave lea\n" +
            "on lea.id=asclog.leaveId\n" +
            "where asclog.id=#{id}")
    List<Map<String, Object>> getOneByPrimaryKey(String id);

    @Select("select * from asc_notice_log where batchId = #{batchId}")
    AscNoticeLog loadByBatchId(@Param("batchId") String batchId);

    @Delete("delete from asc_notice_log where stuId = #{stuId}")
    int deleteByStuId(@Param("stuId") String stuId);


    @Select("select * from asc_notice_log where stuId = #{stuId}")
    List<AscNoticeLog> getRecordByStuId(@Param("stuId") String stuId);

    List<ComeInOutRecord> selectRecord(@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize,
                                       @Param("scCode") String scCode, @Param("grCode") String grCode, @Param("stuNo") String stuNo,
                                       @Param("startDate") String startDate, @Param("endDate") String endDate,
                                       @Param("ascType") String ascType, @Param("inOutType") String inOutType,
                                       @Param("actionState") String actionState, @Param("clCodeList") List<String> clCodeList);

    int selectRecordCount(@Param("scCode") String scCode,@Param("grCode") String grCode, @Param("stuNo") String stuNo, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("ascType") String ascType, @Param("inOutType") String inOutType, @Param("actionState") String actionState, @Param("clCodeList") List<String> clCodeList);
}