package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.AscNoticeDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface AscNoticeDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AscNoticeDetail record);

    int insertSelective(AscNoticeDetail record);

    AscNoticeDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AscNoticeDetail record);

    int updateByPrimaryKey(AscNoticeDetail record);

    @Update("update asc_notice_detail  set asc_notice_detail.state = #{state},asc_notice_detail.remark = #{remark} " +
            "from asc_notice_detail,asc_notice_log " +
            "where asc_notice_detail.logId=asc_notice_log.id " +
            "and batchId = #{batchId} and asc_notice_detail.id = #{id}")
    void updatState(@Param("batchId") String batchId, @Param("id") String id, @Param("state") String state, @Param("remark") String failReason);

    @Update("update asc_notice_detail  set asc_notice_detail.state = #{state},asc_notice_detail.remark = #{remark} " +
            "from asc_notice_detail " +
            "where asc_notice_detail.id = #{id}")
    void updatStateById(@Param("id") String id, @Param("state") String state, @Param("remark") String failReason);
}