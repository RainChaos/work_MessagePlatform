package com.jiapeng.messageplatform.service;

import com.jiapeng.messageplatform.entity.Leave;
import com.jiapeng.messageplatform.utils.PageResult;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by HZL on 2019/8/26.
 */
public interface LeaveService {
    void add(Leave leave) throws Exception;

    void update(Leave leave) throws Exception;

    void del(int leaveId);

    Leave load(int leaveId) throws Exception;

    Map<String, Object> getLeaveInfo(String sc_code,String stuNo, Date actionTime) throws Exception;

//    List<Map<String,Object>> list(String  teNo);

    PageResult list(int pageIndex, int pageSize, String stuNoAndName, String startDate, String endDate,String cl_code,String teNo);

    List<Map<String, Object>> totals(String startDate, String endDate,String teNo);



}
