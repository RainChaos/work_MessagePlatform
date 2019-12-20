package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.MessageDetail;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface MessageDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MessageDetail record);

    int insertSelective(MessageDetail record);

    MessageDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessageDetail record);

    int updateByPrimaryKey(MessageDetail record);

    @Select("select top 1 * from message_detail where stuId=#{stuId} ")
    MessageDetail getOneRecordByStuId(String stuId);

    @Select("select * from message_detail where logId=#{logId}")
    List<MessageDetail> listByLogId(Integer logId);

    ;
    @Delete("delete from message_detail where stuId=#{stuId}")
    int deleteByStuId(String stuId);

    List<MessageDetail> pageList(@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize, @Param("logId") Integer logId, @Param("state") String state);

    Integer pageListCount(@Param("logId") Integer logId, @Param("state") String state);

    @Update("update message_detail  set message_detail.state = #{state},message_detail.remark = #{remark} " +
            "from message_detail,message_log " +
            "where message_detail.logId=message_log.id " +
            "and batchId = #{batchId} and message_detail.id = #{id}")
    void updatState(@Param("batchId") String batchId, @Param("id") String id, @Param("state") String state, @Param("remark") String failReason);

    @Update("update message_detail  set message_detail.state = #{state},message_detail.remark = #{remark} " +
            "from message_detail " +
            "where message_detail.id = #{id}")
    void updatStateById(@Param("id") String id, @Param("state") String state, @Param("remark") String failReason);



    //可按照学校，年级，班级，学生姓名
    @SelectProvider(type=MessageDetailProvider.class,method = "listSql")
    List<Map<String,Object>> list(@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize,
                                  @Param("logId") String logId,
                                  @Param("state") String state,
                                  @Param("scCode")String scCode,
                                  @Param("graCode")String graCode,
                                  @Param("clCode")String clCode,
                                  @Param("stuName")String stuName);

    @SelectProvider(type=MessageDetailProvider.class,method = "countSql")
    int count(@Param("logId") String logId,
              @Param("state") String state,
              @Param("scCode")String scCode,
              @Param("graCode")String graCode,
              @Param("clCode")String clCode,
              @Param("stuName")String stuName);



}