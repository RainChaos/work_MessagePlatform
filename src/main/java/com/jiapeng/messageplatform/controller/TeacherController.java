package com.jiapeng.messageplatform.controller;

import com.jiapeng.messageplatform.annotation.LoginLimit;
import com.jiapeng.messageplatform.entity.Student;
import com.jiapeng.messageplatform.entity.Teacher;
import com.jiapeng.messageplatform.model.UnitTree;
import com.jiapeng.messageplatform.service.*;
import com.jiapeng.messageplatform.utils.*;
import jxl.read.biff.BiffException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by HZL on 2019/8/9.
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController{
    @Autowired
    TeacherService teacherService;
    @Autowired
    StudentService studentService;
    @Autowired
    MessageService messageService;
    @Autowired
    ClassService classService;
    @Autowired
    DataSyncService dataSyncService;

    @LoginLimit(limit = false)
    @PostMapping("/login")
    @ResponseBody
    public ReturnT<Object> login(HttpServletRequest request, String name, String passWord,String verifyCode) throws Exception {
        teacherService.login(request, name, passWord,verifyCode,null);

        return new ReturnT<>();
    }

    @LoginLimit(limit = false)
    @PostMapping("/getAccountByWxNo")
    @ResponseBody
    public ReturnT<Object> getAccountByWxNo(HttpServletRequest request, String wxNo) throws Exception {
        Teacher teacher = teacherService.getRecordByWxNo(wxNo);
        if(teacher!=null){
            teacherService.login(request, teacher.getLoginname(), teacher.getPassword(),null,"wxLogin");
            return new ReturnT<>();
        }else {
            return new ReturnT<>(500,"用户未绑定微信！");
        }
    }

    @PostMapping("/add")
    public ReturnT<Object> add (Teacher teacher) throws Exception {
        teacherService.add(teacher);
        dataSyncService.teacherSync(teacher,"0");
        return new ReturnT<>();
    }

    @PostMapping("/update")
    public ReturnT<Object> update(Teacher teacher) {
        teacherService.update(teacher);
        dataSyncService.teacherSync(teacher,"0");
        return new ReturnT<>();
    }


    @PostMapping("/resetPwd")
    public ReturnT<Object> resetPwd(Teacher teacher) {
        teacherService.resetPwd(teacher);
        return new ReturnT<>();
    }


    @PostMapping("/del")
    public ReturnT<Object> del(String teNo) {
        dataSyncService.teacherDelSync(teNo);
        teacherService.del(teNo);
        return new ReturnT<>();
    }

    @PostMapping("/updatePwd")
    public ReturnT<Object> updatePwd(String teNo, String oldPassword, String newPassword) throws Exception {
        teacherService.updatePwd(teNo, oldPassword, newPassword);
        return new ReturnT<>();
    }


    @PostMapping("/WxLogin")
    public ReturnT<Object> WxLogin(HttpServletRequest request) throws Exception {
        //获取教师信息
        String openid = "false";
        String teNo = SessionUtil.getLoginTeacherNo(request);
        String flag = request.getParameter("flag");
        if("1".equals(flag)){
            openid = request.getParameter("wxNo");
        }
        if(openid==null||openid.length()==0){
            //用户用电脑端登录
            return new ReturnT<>(500,"请在微信端打开");
        }

        Teacher teacher = new Teacher();
        teacher.setTeNo(teNo);
        teacher.setIswxlogin(openid);
        teacherService.update(teacher);
        return new ReturnT<>();
    }


    /**
     * 导入学生
     *
     * @param
     * @param
     * @return (String fileName, String sheetName
     */
    @PostMapping("/import")
    public ReturnT<Object> importTeacher(String fileName, String sheetName, HttpServletRequest request) throws Exception {
        //todo 批量同步待做
        teacherService.importTeacher(fileName, sheetName);
        return new ReturnT<>();
    }

    /**
     * 查看导入教师进度信息，导入后通过轮询查看该方法是否导入完成
     *
     * @return
     */
    @PostMapping("/importMsg")
    public ReturnT<Object> importMsg() {
        return teacherService.importMsg();
    }


    /**
     * 启用、停用
     *
     * @param teNo
     * @param enableType “1”为启用、“0”为禁用
     * @param reason     当enableType为“1”时，reason不能为空
     * @return
     * @throws Exception
     */
    @PostMapping("/enable")
    public ReturnT<Object> enable(String teNo, String enableType, String reason) throws Exception {
        teacherService.enable(teNo, enableType, reason);
        return new ReturnT<>();
    }

    /**
     * 教师列表（供管理端使用）
     *
     * @param page
     * @param limit
     * @param scCode
     * @return
     */
    @PostMapping("/list")
    public ReturnT<Object> getList(HttpServletRequest request,Integer page, Integer limit, String scCode,String isdelete,String teName) {
        if (scCode==null||scCode.length()==0){
            scCode=null;
        }
       String session_scCode = request.getSession().getAttribute("scCode").toString();
        //判断是系统管理员还是学校管理员【学校代码为空，说明是系统管理员，不为空说明微学校管理员】
        if(session_scCode != null && session_scCode.length() != 0){
            scCode = session_scCode;
        }
        if(isdelete==null){
            isdelete = "0";
        }
        PageResult pageResult = teacherService.pageList(page, limit, scCode,isdelete,teName);
        return new ReturnT<>(pageResult);
    }

    /**
     * 获取班级列表（供管理端调用）
     *
     * @param request
     * @return
     */
    @PostMapping("/getClassListByteNo")
    public ReturnT<Object> getClassListByteNo(HttpServletRequest request, String teNo) {
        return new ReturnT<>(teacherService.getClassList(teNo));
    }

    //设置教师班级
    @PostMapping("/setTeacherClass")
    public ReturnT<Object> setTeacherClass(String teNo, String clCodeList) {
        List<String> list = new ArrayList<>();

        if(clCodeList != null && clCodeList.length()!=0){
            String[] cl = clCodeList.split(",");
            for (int i = 0; i < cl.length; i++){
                if(!cl[i].contains("@")){
                    cl[i]=cl[i]+"@false";
                }
                list.add(cl[i]);
            }
        }
        teacherService.setClass(teNo, list);
        return new ReturnT<>();
    }

    //解除教师班级关系
    @PostMapping("/delTeacherClass")
    public ReturnT<Object> delTeacherClass(String teNo, String clCodeList) {
        List<String> list = new ArrayList<>();
        teacherService.delClass(teNo, clCodeList);
        return new ReturnT<>();
    }


//    /**
//     * 获取班级列表（教师用户调用）
//     *
//     * @param request
//     * @return
//     */
//    @LoginLimit(type = "teacher")
//    @PostMapping("/getClassList")
//    public ReturnT<Object> getClassList(HttpServletRequest request) {
//        String teNo = SessionUtil.getLoginTeacherNo(request);
//        return new ReturnT<>(teacherService.getClassList(teNo));
//    }

    /**
     * 获取班级列表（教师用户调用）
     *
     * @param request
     * @return
     */
    @LoginLimit(type = "teacher")
    @PostMapping("/getClassList")
    public ReturnT<Object> getClassList(HttpServletRequest request) {
        String teNo = SessionUtil.getLoginTeacherNo(request);
        return new ReturnT<>(teacherService.getClassAndInfoList(teNo));
    }


    /**
     * 获取学生列表（教师用户调用）
     *
     * @param
     * @return
     */
    @LoginLimit(type = "teacher")
    @PostMapping("/getStuList")
    public ReturnT<Object> getStudentList(HttpServletRequest request, Integer page, Integer limit, String clCode, String stuName,String flag) {
        String teNo = SessionUtil.getLoginTeacherNo(request);
        Teacher teacher = teacherService.load(teNo);
        List<String> clCodeList = teacher.getClCodeList();
        if(clCodeList.size()==0){
            return new ReturnT<>();
        }

        if("school".equals(flag)){
            //用户点击的是学校
            clCode = null;
        }

        if("grade".equals(flag)){
            //用户点击的是年级
            //根据学校，年级，教师编号查找班级
            String sc_code = teacher.getScCode();
            String gr_code = clCode;
            clCode=null;
            clCodeList = classService.listByCodeAndTeNo(sc_code,gr_code,teNo);
        }

        if("class".equals(flag)){
            clCodeList.add(clCode);
        }

        PageResult pageResult = studentService.listStudent(page, limit, clCodeList, null, stuName,null);
        return new ReturnT<>(pageResult);
    }

    /**
     * 获取短信消息列表（教师用户调用）
     *
     * @param request
     * @param page
     * @param limit
     * @param msgType
     * @param state
     * @return
     */
    @LoginLimit(type = "teacher")
    @PostMapping("/getMsgList")
    public ReturnT<Object> getMsgList(HttpServletRequest request, Integer page, Integer limit, String msgType, String state) {
        String teNo = SessionUtil.getLoginTeacherNo(request);
        List<String> list = new ArrayList<>();
        list.add(teNo);
        PageResult pageResult = messageService.messagePageList(page, limit, list, msgType, state);
        return new ReturnT<>(pageResult);
    }

    /**
     * 获取短信明细列表（教师用户调用）
     *
     * @param request
     * @param pageIndex
     * @param pageSize
     * @param logId
     * @param state
     * @return
     */
    @LoginLimit(type = "teacher")
    @PostMapping("/getMsgDetialList")
    public ReturnT<Object> getMsgDetialList(HttpServletRequest request, Integer pageIndex, Integer pageSize, Integer logId, String state) {
        PageResult pageResult = messageService.messageDetailPageList(pageIndex, pageSize, logId, state);
        return new ReturnT<>(pageResult);
    }


    /**
     * 获取教师单位结构树
     * by hzl 2019-8-20 15:49:15
     *
     * @param request
     * @return
     */
//    @LoginLimit(type = "teacher")
    @PostMapping("/getUnitTree")
    @ResponseBody
    public ReturnT<Object> getUnitTree(HttpServletRequest request) {
        String teNo = SessionUtil.getLoginTeacherNo(request);
        List<UnitTree> list = teacherService.getUnitTree(teNo);
        return new ReturnT<>(list);
    }

    @PostMapping("/getStuListByTeNo")
    @ResponseBody
    public ReturnT<Object> getStuListByTeNo(HttpServletRequest request) {
        String teNo = SessionUtil.getLoginTeacherNo(request);
        List<Student> list = teacherService.getStuListByTeNo(teNo);
        return new ReturnT<>(list);
    }

}
