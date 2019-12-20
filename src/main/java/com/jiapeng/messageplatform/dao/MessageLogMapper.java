package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.MessageLog;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MessageLog record);

    int insertSelective(MessageLog record);

    MessageLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessageLog record);

    int updateByPrimaryKey(MessageLog record);

    List<MessageLog> pageList(@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize, @Param("sortCondition") String sortCondition, @Param("teNoList") List<String> teNoList, @Param("msgType") String msgType, @Param("state") String state);

    Integer pageListCount(@Param("teNoList") List<String> teNoList, @Param("msgType") String msgType, @Param("state") String state,@Param("sortCondition") String sortCondition);

    /**
     * 根据教师号获取全部记录
     * @author 刘超
     */
    List<MessageLog> selectMessageByStuNo(String stuNo);

    @Delete("delete from message_log where te_no = #{teNo}")
    int deleteByTeNo(String teNo);

}
