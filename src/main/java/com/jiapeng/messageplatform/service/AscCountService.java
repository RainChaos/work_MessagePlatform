package com.jiapeng.messageplatform.service;

import com.jiapeng.messageplatform.entity.AttendEntity;
import com.jiapeng.messageplatform.model.ComeInOutRecord;
import com.jiapeng.messageplatform.model.NameAndValue;
import com.jiapeng.messageplatform.utils.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 门禁考勤数据、统计
 * Created by HZL on 2019/10/8.
 */
public interface AscCountService {

    /**
     * 门禁出入记录
     *
     * @param scCode      学校代码
     * @param grCode      年级代码
     * @param stuNo       学号
     * @param startDate   开始时间
     * @param endDate     截止时间
     * @param ascType     门禁类型
     * @param inOutType   出入方向（1进入、2离开）
     * @param actionState 出入状态（1正常、2请假、3迟到）
     * @param clCodeList  教师对应所对应的班级代码列表（用于教师登录查询）
     * @return
     */
    List<ComeInOutRecord> getRecord(int pageIndex, int pageSize, String scCode, String grCode, String stuNo, String startDate, String endDate, String ascType, String inOutType, String actionState, List<String> clCodeList);

    PageResult list(int pageIndex, int pageSize, String scCode, String stuNo, String startDate, String endDate, String ascType, String inOutType, String actionState, List<String> clCodeList);

    /**
     * 门禁饼图统计数据（按时间范围统计，分为：正常、迟到、请假三种类型数据）
     * by hzl 2019-10-10 15:42:224
     *
     * @param scCode
     * @param stuNo
     * @param startDate
     * @param endDate
     * @return
     */
    Map<String, Object> ascPieCountGroupByState(String scCode, String stuNo, String startDate, String endDate, List<String> clCodeList);

    /**
     * 门禁折现统计图数据（按类型、月份统计，分为：正常、迟到、请假三种类型数据）
     * by hzl 2019-10-10 15:43:24
     *
     * @param scCode
     * @param stuNo
     * @param startDate
     * @param endDate
     * @return
     */
    Map<String, Object> ascLineCountGroupByState(String scCode, String stuNo, String startDate, String endDate, List<String> clCodeList);

    /***
     * 提供门户用的考勤统计接口
     * （只以进入方向数据作为统计）
     * by hzl 2019-10-25 15:10:45
     * @param scCode 学校代码
     * @param grCode 年级代码
     * @param type 0迟到、1请假
     * @param startDate
     * @param endDate
     * @return
     */
    List<NameAndValue> menhuAscCount(String scCode, String grCode, String type, String startDate, String endDate);

    PageResult menhuAttendCount(int page, int limit, String startDate, String endDate, String sc_code, String teacherNo) throws Exception;

    PageResult studentAttendCount(int page, int limit, String startDate, String endDate,String scCode,String code,String flag,String stuNo) throws Exception;
}
