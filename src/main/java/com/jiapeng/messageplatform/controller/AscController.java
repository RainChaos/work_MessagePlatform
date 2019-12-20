package com.jiapeng.messageplatform.controller;

import com.jiapeng.messageplatform.annotation.LoginLimit;
import com.jiapeng.messageplatform.entity.Teacher;
import com.jiapeng.messageplatform.model.UnitTree;
import com.jiapeng.messageplatform.service.AscCountService;
import com.jiapeng.messageplatform.service.SchoolService;
import com.jiapeng.messageplatform.service.TeacherService;
import com.jiapeng.messageplatform.utils.*;
import org.apache.poi.ss.formula.functions.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 门禁通知信息控制器
 * Created by HZL on 2019/10/14.
 */
@RestController
@RequestMapping("/asc")
public class AscController {
    @Autowired
    AscCountService ascCountService;
    @Autowired
    TeacherService teacherService;

    @Autowired
    SchoolService schoolService;

    /**
     * 获取门禁记录推送信息记录
     * by hzl 2019-10-14 10:56:43
     * @param request
     * @return
     */
    @PostMapping("/list")
    public ReturnT<Object> getList(HttpServletRequest request) {

        try {
            Integer page = Integer.valueOf(RequestHelper.getRequest(request, "page"));
            Integer limit = Integer.valueOf(RequestHelper.getRequest(request, "limit"));
            String startDate = RequestHelper.getRequest(request, "startDate", true);
            String endDate = RequestHelper.getRequest(request, "endDate", true);
            String ascType = RequestHelper.getRequest(request, "ascType", true);//1校园、2公寓
            String inOutType = RequestHelper.getRequest(request, "inOutType", true);//1进入、2离开
            String actionState = RequestHelper.getRequest(request, "actionState", true);//1正常、2请假、3迟到
            String stuNo = RequestHelper.getRequest(request, "stuNo", true);//1正常、2请假、3迟到


            //默认超级管理员
            String scCode = null;
            //默认为空【不按班级查找出入记录，即用户为管理员】
            List<String> clCodeList = new ArrayList<>();

            //校验用户类型
            String sessionscCode = request.getSession().getAttribute("scCode").toString();
            if(sessionscCode!=null&&sessionscCode.length()>0){
                //说明该用户学校用户
                String teNo = SessionUtil.getLoginTeacherNo(request);
                if(teNo!=null&&teNo.length()>0){
                    //教师【获取该班级所有集合】
                    scCode=sessionscCode;
                    Teacher teacher = teacherService.load(teNo);
                    clCodeList = teacher.getClCodeList();
                    if(clCodeList.size()==0){
                        //防止教师端无班级显示所有数据
                        return new ReturnT<>();
                    }
                }else {
                    //学校管理员
                    scCode=sessionscCode;}
            }else {
                //超级管理员
                scCode = RequestHelper.getRequest(request, "scCode", true);
            }
            PageResult pageResult = ascCountService.list(page, limit, scCode, stuNo, startDate, endDate, ascType, inOutType, actionState, clCodeList);
            return new ReturnT<>(pageResult);
        } catch (Exception e) {
            return ReturnT.getFail(e.getMessage());
        }
    }



    @PostMapping("/getUnitTree")
    @ResponseBody
    public ReturnT<Object> getUnitTree1(HttpServletRequest request) {
        //默认超级管理员
        String scCode = null;
        //默认为空【不按班级查找出入记录，即用户为管理员】
        String teNo = null;
        String usertype = null;

        //校验用户类型
        String sessionscCode = request.getSession().getAttribute("scCode").toString();
        if(sessionscCode!=null&&sessionscCode.length()>0){
            //说明该用户学校用户
            String sessionteNo = SessionUtil.getLoginTeacherNo(request);
            if(sessionteNo!=null&&sessionteNo.length()>0){
                //教师
                scCode=sessionscCode;
                teNo=sessionteNo;
            }else {
                //学校管理员
                scCode=sessionscCode;
            }
        }else {
            //超级管理员
            scCode =null;
        }
        List list = schoolService.listGradeTree(scCode,teNo,usertype);
        return new ReturnT<>(list);
    }
}