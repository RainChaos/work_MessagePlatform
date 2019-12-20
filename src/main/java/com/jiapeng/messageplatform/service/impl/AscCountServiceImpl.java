package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.dao.AscNoticeLogMapper;
import com.jiapeng.messageplatform.dao.ClassMapper;
import com.jiapeng.messageplatform.dao.ConfigMapper;
import com.jiapeng.messageplatform.dao.TeacherMapper;
import com.jiapeng.messageplatform.entity.AttendDetailsEntity;
import com.jiapeng.messageplatform.entity.AttendEntity;
import com.jiapeng.messageplatform.entity.Config;
import com.jiapeng.messageplatform.model.ComeInOutRecord;
import com.jiapeng.messageplatform.model.NameAndValue;
import com.jiapeng.messageplatform.service.AscCountService;
import com.jiapeng.messageplatform.service.ClassService;
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
public class AscCountServiceImpl implements AscCountService {
    @Autowired
    AscNoticeLogMapper ascNoticeLogMapper;
    @Autowired
    ConfigMapper configMapper;
    @Autowired
    ClassMapper classMapper;

    @Autowired
    ClassService classService;

    @Autowired
    TeacherMapper teacherMapper;

    @Override
    public List<ComeInOutRecord> getRecord(int pageIndex, int pageSize, String scCode, String grCode, String stuNo, String startDate, String endDate, String ascType, String inOutType, String actionState, List<String> clCodeList) {
        int offset = (pageIndex == -1) ? pageIndex : (pageIndex - 1) * pageSize;
        List<ComeInOutRecord> dbList = ascNoticeLogMapper.selectRecord(offset, pageSize, scCode, grCode, stuNo, startDate, endDate, ascType, inOutType, actionState, clCodeList);
        return dbList;
    }

    @Override
    public PageResult list(int pageIndex, int pageSize, String scCode, String stuNo, String startDate, String endDate, String ascType, String inOutType, String actionState, List<String> clCodeList) {
        int offset = (pageIndex == -1) ? pageIndex : (pageIndex - 1) * pageSize;
        List<ComeInOutRecord> list = getRecord(pageIndex, pageSize, scCode, null, stuNo, startDate, endDate, ascType, inOutType, actionState, clCodeList);
        int count = ascNoticeLogMapper.selectRecordCount(scCode,null, stuNo, startDate, endDate, ascType, inOutType, actionState, clCodeList);
        return new PageResult(count, list);
    }

    @Override
    public Map<String, Object> ascPieCountGroupByState(String scCode, String stuNo, String startDate, String endDate, List<String> clCodeList) {
        List<ComeInOutRecord> list = getRecord(-1, -1, scCode, null, stuNo, startDate, endDate, null, null, null, clCodeList);
        String[] itemNameArr = {"正常", "迟到", "请假"};

        Map<String, Object> result = new HashMap<>();
        result.put("itemNameData", itemNameArr);
        result.put("inData", detialPieCommon(list, 1));
        result.put("outData", detialPieCommon(list, 2));
        return result;
    }

    @Override
    public List<NameAndValue> menhuAscCount(String scCode, String grCode, String type, String startDate, String endDate) {
        List<ComeInOutRecord> list = getRecord(-1, -1, scCode, grCode, null, startDate, endDate, null, null, null, null);
        List<Map<String, Object>> classList = classMapper.list(scCode, grCode, 9999999, 1);
        List<String> classNameList = new ArrayList<>();
        for (Map<String, Object> map : classList) {
            String className = null == map.get("name") ? "" : map.get("name").toString();
            if (StringUtils.isNotBlank(className)) {
                classNameList.add(className);
            }
        }

        List<NameAndValue> resultList = new ArrayList<>();
        for (String cName : classNameList) {
            int total = 0;
            for (ComeInOutRecord record : list) {
                String reClassName = record.getClassName();
                String reState = record.getActionState();
                int reActionType = record.getActionType();
                if (cName.equals(reClassName) && (reActionType == 1 || reActionType == 3 || reActionType == 4)) {
                    if (StringUtils.isNotBlank(reState)) {
                        if ((type.equals("0") && reState.equals("迟到")) || (type.equals("1") && reState.equals("请假"))) {
                            total++;
                        }
                    }
                }
            }
            resultList.add(new NameAndValue(cName, total));
        }
        return resultList;
    }


    @Override
    public PageResult menhuAttendCount(int page, int limit, String startDate, String endDate, String sc_code, String teacherNo) throws Exception{

        List<String> clCodeList = new ArrayList<>();
        List<Map<String, Object>> clCodeListMap = teacherMapper.getClassAndInfoList(teacherNo);
        for (Map<String, Object> map : clCodeListMap) {
            String className = null == map.get("cl_code") ? "" : map.get("cl_code").toString();
            if (StringUtils.isNotBlank(className)) {
                clCodeList.add(className);
            }
        }
        if(clCodeList.size()==0){
            throw  new  Exception("没有找到相关数据");
        }

        List<ComeInOutRecord> list = getRecord(page, limit, null, null, null, startDate, endDate, null, null, null, clCodeList);
        int count = ascNoticeLogMapper.selectRecordCount(null,null, null, startDate, endDate, null, null, null, clCodeList);
        List<AttendEntity> resultList = new ArrayList<>();
        int total = 0;
        for (ComeInOutRecord record : list) {
            //班级，总数
            String reState = record.getActionState();
            resultList.add(new AttendEntity(record.getStuNo(), record.getStuName(),reState,record.getActionTimeStr()));
        }
        return new PageResult(count, resultList);
    }



    /**
     * 整理pie图通用方法
     *
     * @param list
     * @param directionType (1进入、2离开)
     * @return
     */
    public List<NameAndValue> detialPieCommon(List<ComeInOutRecord> list, int directionType) {
        List<NameAndValue> resultList = new ArrayList<>();
        int normalNum = 0, lateNum = 0, leaveNum = 0;
        for (ComeInOutRecord record : list) {
            String reState = record.getActionState();
            int reActionType = record.getActionType();
            if ((directionType == 1 && (reActionType == 1 || reActionType == 3 || reActionType == 4)
                    || (directionType == 2 && (reActionType == 0 || reActionType == 2)))) {
                if (StringUtils.isNotBlank(reState)) {
                    if (reState.equals("正常")) {
                        normalNum++;
                    } else if (reState.equals("请假")) {
                        leaveNum++;
                    } else if (reState.equals("迟到")) {
                        lateNum++;
                    }
                }
            }
        }
        resultList.add(new NameAndValue("正常", normalNum));
        resultList.add(new NameAndValue("请假", leaveNum));
        resultList.add(new NameAndValue("迟到", lateNum));
        return resultList;
    }

    @Override
    public Map<String, Object> ascLineCountGroupByState(String scCode, String stuNo, String startDate, String endDate, List<String> clCodeList) {
        //转换时间：月的第一天和最后一天
//        startDate = VeDate.dateToStrLong(VeDate.StrToDateByFormat(startDate, "yyyy-MM"));
//        endDate = VeDate.dateToStrLong(VeDate.getLastDate(VeDate.StrToDateByFormat(endDate, "yyyy-MM")));

        List<String> dateList = VeDate.calcTowDateDiffArr(VeDate.strToDateLong(startDate), VeDate.strToDateLong(endDate), "month", "yyyy-MM");
        List<ComeInOutRecord> dbList = getRecord(-1, 10000, scCode, null, stuNo, startDate, endDate, null, null, null, clCodeList);
        String[] itemNameArr = {"正常", "迟到", "请假"};

        Map<String, Object> result = new HashMap<>();
        result.put("itemNameData", itemNameArr);
        result.put("timeData", dateList);
        result.put("inData", detialLineCommon(dateList, dbList, 1));
        result.put("outData", detialLineCommon(dateList, dbList, 2));
        return result;
    }

    /**
     * 整理line图通用方法
     *
     * @param dateList
     * @param list
     * @param directionType (1进入、2离开)
     * @return
     */
    public List<NameAndValue> detialLineCommon(List<String> dateList, List<ComeInOutRecord> list, int directionType) {
        List<Integer> normalNumList = new ArrayList<>();
        List<Integer> lateNumList = new ArrayList<>();
        List<Integer> leaveNumList = new ArrayList<>();
        for (String date : dateList) {
            int normalNum = 0, lateNum = 0, leaveNum = 0;
            for (ComeInOutRecord record : list) {
                String reState = record.getActionState();
                Date reDate = record.getActionTime();
                String reDateStr = VeDate.dateToStr(reDate, "yyyy-MM");
                int reActionType = record.getActionType();
                if ((directionType == 1 && (reActionType == 1 || reActionType == 3 || reActionType == 4)
                        || (directionType == 2 && (reActionType == 0 || reActionType == 2)))) {
                    if (date.equals(reDateStr)) {
                        if (StringUtils.isNotBlank(reState)) {
                            if (reState.equals("正常")) {
                                normalNum++;
                            } else if (reState.equals("请假")) {
                                leaveNum++;
                            } else if (reState.equals("迟到")) {
                                lateNum++;
                            }
                        }
                    }
                }
            }
            normalNumList.add(normalNum);
            lateNumList.add(lateNum);
            leaveNumList.add(leaveNum);
        }
        List<NameAndValue> resultList = new ArrayList<>();
        resultList.add(new NameAndValue("正常", normalNumList));
        resultList.add(new NameAndValue("迟到", lateNumList));
        resultList.add(new NameAndValue("请假", leaveNumList));
        return resultList;
    }



    @Override
    public PageResult studentAttendCount(int page, int limit, String startDate, String endDate,String scCode,String code,String flag,String stuNo) throws Exception{
        List<String> clCodeList = new ArrayList<>();
        String clCode = "";
        List<Map<String, Object>> classList = new ArrayList<>();
        if ("school".equals(flag)) {
            //学校
            classList = classService.list(scCode, "allClassData", 10000, 1);
        } else if ("grade".equals(flag)) {
            //年级
            classList = classService.list(scCode, code, 10000, 1);
        } else if ("class".equals(flag)) {
            //班级
            clCode =code;
        }

        if("class".equals(flag)){
            clCodeList.add(clCode);
        }else {
            for (Map<String, Object> m :
                    classList) {
                clCodeList.add(m.get("cl_code").toString());
            }
        }

        if(clCodeList.size()==0){
            throw  new  Exception("没有找到相关数据");
        }


        List<ComeInOutRecord> list = getRecord(page, limit, null, null, stuNo, startDate, endDate, null, null, null, clCodeList);
        int count = ascNoticeLogMapper.selectRecordCount(null,null, stuNo, startDate, endDate, null, null, null, clCodeList);
        List<AttendDetailsEntity> resultList = new ArrayList<>();
        int total = 0;
        for (ComeInOutRecord record : list) {
            //班级，总数
            String reState = record.getActionState();
//            (String stuNo, String stuName, String className, String gradeName, String schoolName, String actionTimeStr, String reason, String type) {
            resultList.add(new AttendDetailsEntity(record.getStuNo(), record.getStuName(),record.getClassName(),record.getGradeName(),record.getSchoolName(),record.getActionTimeStr(),record.getReason(),reState));
        }
        return new PageResult(count, resultList);
    }
}
