package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.dao.*;
import com.jiapeng.messageplatform.entity.AttendEntity;
import com.jiapeng.messageplatform.entity.GuadianWxLog;
import com.jiapeng.messageplatform.model.ComeInOutRecord;
import com.jiapeng.messageplatform.model.NameAndValue;
import com.jiapeng.messageplatform.service.AscCountService;
import com.jiapeng.messageplatform.service.GuadianWxLogService;
import com.jiapeng.messageplatform.utils.PageResult;
import com.jiapeng.messageplatform.utils.VeDate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by HZL on 2019/10/9.
 */
@Service
public class GuadianWxLogServiceImpl implements GuadianWxLogService {
    @Autowired
    GuardianWxLogMapper guardianWxLogMapper;
    @Override
    public PageResult getRecord(int pageIndex, int pageSize, String scCode, String stuNo,String stuName,  String startDate, String endDate) {
        int offset = (pageIndex == -1) ? pageIndex : (pageIndex - 1) * pageSize;
        List<GuadianWxLog> dbList = guardianWxLogMapper.selectRecord(offset, pageSize, scCode, stuNo,stuName,startDate, endDate);
        int count = guardianWxLogMapper.selectRecordCount(scCode, stuNo,stuName, startDate, endDate);
        return new PageResult(count, dbList);
    }
}
