package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.dao.ClassMapper;
import com.jiapeng.messageplatform.dao.LeaveMapper;
import com.jiapeng.messageplatform.entity.*;
import com.jiapeng.messageplatform.model.UnitTree;
import com.jiapeng.messageplatform.service.ClassService;
import com.jiapeng.messageplatform.service.LeaveService;
import com.jiapeng.messageplatform.service.SchoolService;
import com.jiapeng.messageplatform.service.TeacherService;
import com.jiapeng.messageplatform.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by HZL on 2019/8/26.
 */
@Service
public class LeaveServiceImpl implements LeaveService {
    @Autowired
    TeacherService teacherService;

    @Autowired
    LeaveMapper leaveMapper;


    @Autowired
    SchoolService schoolService;

    @Override
    public void add(Leave leave) throws Exception {
        Date startDate = leave.getStartDate();
        Date endDate = leave.getEndDate();
        String stuNo = leave.getStuNo();
        String reason = leave.getReason();
        Date date = new Date();
//        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
//        System.out.println(dateFormat.format(date));
        leave.setCreateDate(date);

//        Date  dat1=new Date();
//        Date  dat2=new Date();
//
//        int num = dat1.compareTo(dat2);

//        如果 dat1>dat2  num = 1;
//
//        dat1=dat2  num = 0;
//
//        dat1<dat2  num = -1;


        if (startDate.compareTo(endDate) == 1) {
            throw new Exception("截至时间不能小于或等于起始时间");
        }
        if (StringUtils.isBlank(reason)) {
            throw new Exception("请假原因不能为空");
        }
        //查看是否存在有时间段重叠的记录
        Leave leave1 = leaveMapper.findOneByStuNoAndTime(stuNo, startDate, endDate);
        if (leave1 != null) {
            throw new Exception("提交的请假时间段与该生之前提交的请假时间段有重叠，请修改请假时间段");
        }
        leaveMapper.insertSelective(leave);
    }

    @Override
    public void update(Leave leave) throws Exception {
        leave.setUpdateDate(new Date());
        leaveMapper.updateByPrimaryKeySelective(leave);
    }

    @Override
    public void del(int leaveId) {
        leaveMapper.deleteByPrimaryKey(leaveId);
    }

    @Override
    public Leave load(int leaveId) throws Exception {
        Leave leave = leaveMapper.selectByPrimaryKey(leaveId);
        if (leave == null)
            throw new Exception("未找到请假相关信息");
        return leave;
    }

//    @Override
//    public List<Map<String,Object>> list(String teNo){
//        List<Map<String,Object>> leave = leaveMapper.list(teNo);
//        return leave;
//    }


    @Override
    public Map<String, Object> getLeaveInfo(String sc_code,String stuNo, Date actionTime) throws Exception {
        List<Leave> list = leaveMapper.finfByStuNoAndDate(sc_code,stuNo, actionTime);
        if (list.size() == 0) {
            throw new Exception("no find leave info");
        }
        Leave leave = list.get(0);
        Map<String, Object> map = new HashMap<>();
        map.put("leaveId", leave.getId());
		map.put("stuNo", leave.getStuNo());
        map.put("startDate", VeDate.dateToStrLong(leave.getStartDate()));
        map.put("endDate", VeDate.dateToStrLong(leave.getEndDate()));
        return map;
    }

    @Override
    public PageResult list(int pageIndex, int pageSize, String stuNoAndName, String startDate, String endDate,String cl_code,String teNo) {
        int listCount = leaveMapper.listCount(stuNoAndName, startDate, endDate,cl_code,teNo);
        int offset = (pageIndex - 1) * pageSize;
        List<Map<String,Object>> list = leaveMapper.list(offset, pageSize, stuNoAndName, startDate, endDate,cl_code,teNo);
        return new PageResult(listCount, list);
    }

    @Override
    public List<Map<String, Object>>  totals(String startDate, String endDate,String teNo) {

        // 1.获取该教师下的所有班级
        // 2.统计该班级下请假的人数总数

        List<ClassInfo> list = teacherService.getClassList(teNo);

           List<Map<String, Object>> result = new ArrayList<>();
           ArrayList<String> xList = new ArrayList<String>();
           List<EchartEntity> valueList = new ArrayList<>();


           Map<String, Object> resultList = new HashMap<>();

            for(int i = 0; i < list.size();i++){
                String cl_code = list.get(i).getClassCode();
                //获取班级代码封装到xlist集合【categories】
                xList.add(list.get(i).getClassName());
                //获取班级代码封装到xlist集合【values】
                EchartEntity echartEntity = new EchartEntity();
                echartEntity.setName(list.get(i).getClassName());
                echartEntity.setCode(cl_code);

                //获取班级请假人数，不是次数【一个人也可以有多次请假】
                List<LeaveTotal> list2  = leaveMapper.totals(cl_code,startDate, endDate,teNo);
//                echartEntity.setValue(Integer.parseInt(list2.get(0).getLeaveTotal()));
               //获取班级每个请假人列表
                List<LeaveTotal> list3  = leaveMapper.totalsNum(cl_code,startDate, endDate,teNo);
                echartEntity.setDataList(list3);
                echartEntity.setValue(list3.size());
                valueList.add(echartEntity);
        }

        resultList.put("categories",xList);
        resultList.put("values",valueList);

        result.add(resultList);

     return result;

    }










}
