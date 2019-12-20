package com.jiapeng.messageplatform.controller;

import com.jiapeng.messageplatform.annotation.LoginLimit;
import com.jiapeng.messageplatform.entity.Leave;
import com.jiapeng.messageplatform.service.LeaveService;
import com.jiapeng.messageplatform.utils.PageResult;
import com.jiapeng.messageplatform.utils.ReturnT;
import com.jiapeng.messageplatform.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by HZL on 2019/8/26.
 */
@RequestMapping("/leave")
@RestController
public class LeaveController{
    @Autowired
    LeaveService leaveService;

    @PostMapping("/add")
    public ReturnT<Object> add(HttpServletRequest request, Leave leave) throws Exception {
        String teNo = SessionUtil.getLoginTeacherNo(request);
        leave.setTeNo(teNo);
        leaveService.add(leave);

        return new ReturnT<>();
    }

    @PostMapping("/update")
    public ReturnT<Object> update(Leave leave) throws Exception {
        leaveService.update(leave);
        return new ReturnT<>();
    }

    @PostMapping("/del")
    public ReturnT<Object> del(Integer leaveId) {
        leaveService.del(leaveId);
        return new ReturnT<>();
    }

    @PostMapping("/load")
    public ReturnT<Object> load(Integer leaveId) throws Exception {
        leaveService.load(leaveId);
        return new ReturnT<>();
    }

    @PostMapping("/list")
    public ReturnT<Object> list(HttpServletRequest request,Integer page, Integer limit,String stuNoAndName, String startDate, String endDate,String cl_code) throws Exception {
        String teNo = SessionUtil.getLoginTeacherNo(request);
        PageResult list = leaveService.list(page,limit,stuNoAndName,startDate,endDate,cl_code,teNo);
        return new ReturnT<>(list);
    }


    //根据班级，时间获取该班的请假情况
        //班级为空时并且时间为空：获取该教师所有班级，所有时间段的学生请假情况
        //班级为空，时间不为空：获取所有班级在这段时间内学生请假情况
        //时间为空，班级不为空：获取该教师该班级所有的请假记录

    // 1.获取该教师下的所有班级
    // 2.统计该班级下请假的人数总数
    @GetMapping ("/totals")
    @ResponseBody
    public  List<Map<String, Object>> totals(String startDate, String endDate,HttpServletRequest request) throws Exception {
        String teNo = SessionUtil.getLoginTeacherNo(request);
        List<Map<String, Object>> list = leaveService.totals(startDate,endDate,teNo);
        return list;
    }
}
