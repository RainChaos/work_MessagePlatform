package com.jiapeng.messageplatform.controller;

import com.jiapeng.messageplatform.annotation.LoginLimit;
import com.jiapeng.messageplatform.dao.*;
import com.jiapeng.messageplatform.entity.*;
import com.jiapeng.messageplatform.model.ComeInOutRecord;
import com.jiapeng.messageplatform.model.UnitTree;
import com.jiapeng.messageplatform.service.*;
import com.jiapeng.messageplatform.service.impl.MessageServiceImpl;
import com.jiapeng.messageplatform.utils.*;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Null;
import java.util.*;

/**
 * Created by XJP on 2019/5/9.
 */
@Controller
@RequestMapping("/page")
public class PageController{

    private static final Logger logger = LoggerFactory.getLogger(PageController.class);

    private static int schoolNum = 500;

    @Autowired
    ModuleService moduleService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserService userService;
    @Autowired
    SchoolService schoolService;
    @Autowired
    SchoolMapper schoolMapper;
    @Autowired
    GuardianService guardianService;

    @Autowired
    ConfigDefineMapper configDefineMapper;

    @Autowired
    ConfigDefineService configDefineService;

    @Autowired
    AscCountService ascCountService;


    @Autowired
    GradeService gradeService;
    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    ClassService classService;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    GuardianMapper guardianMapper;
    @Autowired
    ConfigMapper configMapper;
    @Autowired
    TeacherService teacherService;
    @Autowired
    MessageService messageService;
    @Autowired
    LeaveService leaveService;
    @Autowired
    AscNoticeLogMapper ascNoticeLogMapper;
    @Autowired
    ClassTeacherMapper classTeacherMapper;
    @Autowired
    ScoresService scoresService;


    @RequestMapping("/menuList")
    public String mainPage(HttpServletRequest request) {
        return "classic/admin/menuList";
    }

    @RequestMapping("/addMenu/{id}")
    public String addMenuPage(@PathVariable("id") String id, Model model, HttpServletRequest request) {

        ModuleEntity menus = new ModuleEntity();
        if ("flag".equals(id)) {
            String yy = null;
            menus.setParentId(yy);
        } else {
            menus.setParentId(id);
        }
        model.addAttribute("menu", menus);
        return "classic/admin/menuForm";
    }

    @RequestMapping("/editMenu/{id}")
    public String editMenu(@PathVariable("id") String id, Model model, HttpServletRequest request) {
        ModuleEntity menus = moduleService.getModuleById(id);
        model.addAttribute("menu", menus);
        return "classic/admin/editMenu";
    }


    //管理员管理
    @RequestMapping("/adminList")
    public String admin(HttpServletRequest request) {
        return "classic/admin/adminList";
    }

    @RequestMapping("/addAdmin")
    public String addAdmin(HttpServletRequest request, Model model) {

        DataGridJson obj = schoolService.list(null, "", schoolNum, 1);
        model.addAttribute("schoollist", new ReturnT<>(obj));
        return "classic/admin/addAdmin";


    }

    @RequestMapping("/editAdmin/{id}")
    public String editAdmin(@PathVariable("id") int id, Model model, HttpServletRequest request) {
        UserEntity user = userService.get(id);
        model.addAttribute("admin", user);
        return "classic/admin/editAdmin";
    }


    //角色列表
    @RequestMapping("/roleList")
    public String role(HttpServletRequest request) {
        return "classic/admin/roleList";
    }

    @RequestMapping("/addRole")
    public String addRole(HttpServletRequest request) {
        return "classic/admin/addRole";

    }

    @RequestMapping("/editRole/{id}/{name}")
    public String editRole(@PathVariable("id") int id, @PathVariable("name") String name, Model model, HttpServletRequest request) {
        model.addAttribute("roleid", id);
        model.addAttribute("rolename", name);
        return "classic/admin/editRole";
    }

    //设置用户id的角色
    @RequestMapping("/editUserRole/{id}/{name}")
    public String editUserRole(@PathVariable("id") int id, @PathVariable("name") String name, Model model, HttpServletRequest request) {
        List<Map<String, Object>> list = roleService.getAll();
        //根据用户id获取用户角色
        List<Map<String, Object>> userRolelist = userService.getRoles(id);

        List<String> aList = new ArrayList<String>();

        //获取用户角色名称
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < userRolelist.size(); i++) {
                String key = String.valueOf(userRolelist.get(i).get("name"));
                aList.add(key);
            }
        }
        model.addAttribute("userrole", aList);
        model.addAttribute("userid", id);
        model.addAttribute("username", name);
        model.addAttribute("listrole", list);
        return "classic/admin/editUserRole";
    }

    //设置角色id的权限树
    @RequestMapping("/editRoleMenu/{id}/{name}")
    public String editRoleMenu(@PathVariable("id") int id, @PathVariable("name") String name, Model model, HttpServletRequest request) {
        //根据用户的角色id获取用户所拥有的菜单id
        List<Map<String, Object>> list = roleService.getModules(String.valueOf(id));

        List<String> aList = new ArrayList<String>();

        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                String key = "value" + String.valueOf(list.get(i).get("id") + "value");
                aList.add(key);
            }
        }
        model.addAttribute("usermenu", aList);
        model.addAttribute("roleid", id);
        model.addAttribute("rolename", name);
        return "classic/admin/editRoleMenu";
    }


    //学校管理
    @RequestMapping("/schoolList")
    public String schoolList(HttpServletRequest request) {
        return "classic/school/schoolList";
    }

    @RequestMapping("/addSchool")
    public String addSchool(HttpServletRequest request) {
        return "classic/school/addSchool";

    }





    //学校微信配置
    @RequestMapping("/school/wxConfig")
    public String wxConfig(HttpServletRequest request, String scCode, Model model) {
        model.addAttribute("scCode", scCode);
        return "classic/school/wxConfig";
    }

    @RequestMapping("/school/dbConfig")
    public String dbConfig(HttpServletRequest request, String scCode, Model model) throws Exception {
        SchoolDB SchoolDB = schoolService.loadSchoolDB(scCode);
        model.addAttribute("SchoolDB", SchoolDB);
        return "classic/school/dbConfig";
    }

    @RequestMapping("/school/addSchoolDB")
    public String addDBConfig(HttpServletRequest request,Model model) throws Exception {
        DataGridJson obj = schoolService.list(null, "", schoolNum, 1);
        model.addAttribute("schoollist", new ReturnT<>(obj));
        return "classic/school/addSchoolDB";
    }




    @RequestMapping("/editSchool")
    public String editSchool(HttpServletRequest request) {
        return "classic/school/editSchool";
    }

    //年级管理
    @RequestMapping("/gradeList")
    public String gradeList(HttpServletRequest request, Model model) {

        // 获取学校列表
        String scCode = request.getSession().getAttribute("scCode").toString();

        if (scCode == null && scCode.length() == 0) {
            //超级管理员
            scCode = null;
        }

        DataGridJson obj = schoolService.list(scCode, "", schoolNum, 1);
        model.addAttribute("schoollist", new ReturnT<>(obj));
        return "classic/grade/gradeList";
    }

    @RequestMapping("/addGrade")
    public String addGrade(HttpServletRequest request, Model model) {
        // 获取学校列表
        String scCode = request.getSession().getAttribute("scCode").toString();
        if (scCode == null && scCode.length() == 0) {
            //超级管理员
            scCode = null;
        }
        DataGridJson obj = schoolService.list(scCode, "", schoolNum, 1);
        model.addAttribute("schoollist", new ReturnT<>(obj));
        return "classic/grade/addGrade";

    }

    @RequestMapping("/school/IPForm")
    public String IPForm(HttpServletRequest request, String sc_code, Model model) {
        //获取该学校的ip地址白名单
        model.addAttribute("list", schoolService.getIPListByScCode(sc_code));
        model.addAttribute("sc_code", sc_code);
        return "classic/school/IPForm";

    }


    //*******************************************************教师管理【管理端】************************************************/
    @RequestMapping("/teacherList")
    public String teacherList(HttpServletRequest request, Model model) {
        //获取学校列表
        String scCode = request.getSession().getAttribute("scCode").toString();
        DataGridJson obj = schoolService.list(scCode, "", schoolNum, 1);
        model.addAttribute("schoollist", new ReturnT<>(obj));
        return "classic/teacher/teacherList";
    }

    @RequestMapping("/addTeacher")
    public String addTeacher(HttpServletRequest request, Model model, String sc_code) {
        model.addAttribute("sc_code", sc_code);
        return "classic/teacher/addTeacher";
    }

    @RequestMapping("/editTeacher")
    public String editTeacher(HttpServletRequest request, Model model, String teNo) {
        Teacher teacher = teacherMapper.selectByPrimaryKey(teNo);
        model.addAttribute("teacher", teacher);
        return "classic/teacher/editTeacher";
    }

    //设置老师管理的班级
    @RequestMapping("/teacher/addClass")
    public String setTeacherClass(HttpServletRequest request, Model model, String teNo, String sc_code) {
        model.addAttribute("teNo", teNo);
        model.addAttribute("sc_code", sc_code);

        //获取该学校的所有年级列表
        List l = gradeService.list(sc_code);
        DataGridJson obj = new DataGridJson(l);
        model.addAttribute("gradelist", new ReturnT<>(obj));
        return "classic/teacher/editTeacherClass";
    }

    //教师管理中的班级设置
    @RequestMapping("/teacher/setTeacherClass")
    public String teacherClassList(HttpServletRequest request, String teNo, String teName, String scCode, Model model) {
        model.addAttribute("teNo", teNo);
        model.addAttribute("teName", teName);
        model.addAttribute("scCode", scCode);
        return "classic/teacher/editTeacherClass";

    }


    //*******************************************************教师管理************************************************/


    //学生管理
    @RequestMapping("/studentList")
    public String studentList(HttpServletRequest request, Model model) {
        //获取学校列表
        String scCode = request.getSession().getAttribute("scCode").toString();
        DataGridJson obj = schoolService.list(scCode, "", schoolNum, 1);
        model.addAttribute("schoollist", new ReturnT<>(obj));
        return "classic/student/studentList";
    }

    @RequestMapping("/addStudent")
    public String addStudent(HttpServletRequest request, String cl_code, Model model) {
        model.addAttribute("cl_code", cl_code);
        return "classic/student/addStudent";

    }

    @RequestMapping("/editStudent")
    public String editStudent(HttpServletRequest request, String stuId, Model model) {
        Student student = studentMapper.selectByPrimaryKey(stuId);
        model.addAttribute("student", student);
        return "classic/student/editStudent";
    }


    //根据学生编号获取该学生的父母信息
    @RequestMapping("/student/guardian")
    public String editStudentGuardian(HttpServletRequest request, String stuId, Model model) {
        model.addAttribute("stuId", stuId);
        return "classic/student/guardianList";
    }

    @RequestMapping("/setGuardian")
    public String setGuardian(HttpServletRequest request, String stuId, Model model) {
        model.addAttribute("stuId", stuId);
        return "classic/student/setGuardian";

    }


    //导入学生文件界面【管理端与教师端共用】
    @RequestMapping("/inportSudent")
    public String inportSudent(HttpServletRequest request) {
        return "classic/teacherPC/importStudent";

    }

    //导入学生文件界面【管理端用】
    @RequestMapping("/inportTeacher")
    public String inportTeacher(HttpServletRequest request) {
        return "classic/teacher/importTeacher";

    }


    @RequestMapping("/changeUserData")
    public String changeUserData(Model model, HttpServletRequest request) {
        String userid = request.getSession().getAttribute("id").toString();
        Integer id = Integer.parseInt(userid);
        UserEntity user = userService.get(id);
        model.addAttribute("user", user);
        return "classic/admin/changeUserData";
    }

    @RequestMapping("/changeTeacherPwd")
    public String updatePwd() {
        return "classic/teacherPC/changePwd";
    }


    /****************************************班级管理******************************************/
    @RequestMapping("/classList")
    public String classList(HttpServletRequest request, Model model) {
        //获取学校列表
        String scCode = request.getSession().getAttribute("scCode").toString();
        DataGridJson obj = schoolService.list(scCode, "", schoolNum, 1);

        model.addAttribute("schoollist", new ReturnT<>(obj));

        return "classic/class/classList";
    }

    @RequestMapping("/addClass")
    public String addClass(HttpServletRequest request, Model model) {
        String scCode = request.getSession().getAttribute("scCode").toString();
        DataGridJson obj = schoolService.list(scCode, "", schoolNum, 1);
        model.addAttribute("schoollist", new ReturnT<>(obj));

        return "classic/class/addClass";

    }

    //班级管理下的学生管理
    @RequestMapping("/class/studentList")
    public String classStudentList(HttpServletRequest request, Model model, String cl_code) {
        model.addAttribute("cl_code", cl_code);
        return "classic/class/studentList";
    }

//    //班级管理下的教师管理
//    @RequestMapping("/class/teacherList")
//    public String classteacherList(HttpServletRequest request,Model model,String cl_code) {
//
//        //获取学校列表
//        DataGridJson obj = schoolService.list("",10,1);
//        new ReturnT<>(0,obj,"");
//        model.addAttribute("schoollist",new ReturnT<>(0,obj,""));
//        model.addAttribute("cl_code",cl_code);
//        return getRootPath.getPath(request) + "/class/teacherList";
//    }

    /****************************************班级管理******************************************/

    //信息管理列表
    @RequestMapping("/messageList")
    public String messageList(HttpServletRequest request, Model model) {
        return "classic/message/messageList";
    }

    //信息管详情表
    @RequestMapping("/detailsList")
    public String detailsList(HttpServletRequest request, String logId, String typemsg, Model model) {
        model.addAttribute("typemsg", typemsg);
        model.addAttribute("logId", logId);
        return "classic/message/detailsList";
    }

    @Value("${system.config.downurl}")
    private String downUrl;

    /**
     * 门禁信息详情页
     * by hzl 2019-8-29 16:49:324
     *
     * @param request
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/ascNoticeInfo")
    public String ascNoticeInfo(HttpServletRequest request, String id, Model model) throws Exception {
        AscNoticeLog ascNoticeLog = ascNoticeLogMapper.loadByBatchId(id);
        String stuId = "";
        //获取门禁信息
        if (ascNoticeLog != null) {
            Student student = studentMapper.selectBystuId(ascNoticeLog.getStuId());

            model.addAttribute("student", student);
            // 获取请假信息
            if (ascNoticeLog.getLeaveId() != null) {
                Leave leave = leaveService.load(ascNoticeLog.getLeaveId());
                model.addAttribute("leave", leave);
            }
            //设置图片路径
            if (ascNoticeLog.getImgName() != null && !"".equals(ascNoticeLog.getImgName())) {
                ascNoticeLog.setImgName(downUrl + "/" + Constants.ASC_IMG_UPLOAD_PATH_NAME + ascNoticeLog.getImgName());
            } else {
                ascNoticeLog.setImgName(null);
            }
        }
        model.addAttribute("ascNoticeLog", ascNoticeLog);
        return "classic/ascNoticeInfo/ascNoticeInfo";
    }

    //信息接收人详情表
    @RequestMapping("/messageDetails")
    public String messageDetails(HttpServletRequest request, Integer id, Model model) {
        String messageLog = messageService.getMessageContent(id);
        model.addAttribute("message", messageLog);
        return "classic/message/messageDetails";
    }

    //信息内容详情表
    @RequestMapping("/messageContent")
    public String messageContent(HttpServletRequest request, MessageLog messageLog, Model model) {
        model.addAttribute("message", messageLog);
        return "classic/teacherPC/messageContent";
    }


    //****************************************教师端单独模块**************************************
    //教师端班级列表
    @RequestMapping("/teacher/getClassList")
    public String getClassList(HttpServletRequest request, Model model) {
        return "classic/teacherPC/classList";
    }

    //教师端学生列表
    @RequestMapping("/teacher/studentList")
    public String getStudentList(HttpServletRequest request, Model model) {
        String teNo = SessionUtil.getLoginTeacherNo(request);

        ClassTeacher classTeacher = classTeacherMapper.getByteNo(teNo);
        model.addAttribute("classTeacher", classTeacher);

        return "classic/teacherPC/studentList";
    }

    //教师端信息绑定
    @RequestMapping("/teacher/infoForm")
    public String infoForm(HttpServletRequest request, Model model, String phoneNo, String clName, String clCode, String teNo) {
        //获取微信号
        String openid = request.getSession().getAttribute("OPENID").toString();
        //获取手机号
        model.addAttribute("openid", openid);
        model.addAttribute("phoneNo", phoneNo);
        model.addAttribute("clName", clName);
        model.addAttribute("clCode", clCode);
        model.addAttribute("teNo", teNo);


        return "classic/teacherPC/infoForm";
    }

    //教师端设置班级
    @RequestMapping("/teacher/setClass")
    public String setClass(HttpServletRequest request, String stuId, String stuNo, String classCode, Model model) {
        model.addAttribute("stuId", stuId);
        model.addAttribute("stuNo", stuNo);
        model.addAttribute("classCode", classCode);
        String teNo = SessionUtil.getLoginTeacherNo(request);
        model.addAttribute("classlist", new ReturnT<>(teacherService.getClassList(teNo)));
        return "classic/teacherPC/setClass";
    }

    //教师端短信信息列表
    @RequestMapping("/teacher/messageList")
    public String tMessageList(HttpServletRequest request, Model model) {
        return "classic/teacherPC/messageList";
    }

    @RequestMapping("/teacher/update")
    public String updateTeacherPage(HttpServletRequest request, Model model) {
        String teNo = request.getSession().getAttribute("LOGINTEACHERNO").toString();
        Teacher teacher = teacherMapper.selectByPrimaryKey(teNo);
        model.addAttribute("teacher", teacher);
        return "classic/teacherPC/editTeacher";
    }

    //发送信息
    @RequestMapping("/teacher/messageSend")
    public String addMessage(HttpServletRequest request, Model model) {
        String teNo = SessionUtil.getLoginTeacherNo(request);
        Teacher teacher = teacherService.load(teNo);
        model.addAttribute("teacher", teacher);


        //获取学校名称
        SchoolEntity schoolEntity = schoolMapper.load(teacher.getScCode());
        model.addAttribute("schoolName", schoolEntity.getName());

        //获取该教师下班级树形结构图
        List<UnitTree> list = teacherService.getUnitTree(teNo);

        model.addAttribute("list", list);

        return "classic/teacherPC/messageSend";

    }


    @RequestMapping("/teacher/msgSendByOne")
    public String msgSendByOne(HttpServletRequest request, Model model) {
        String teNo = SessionUtil.getLoginTeacherNo(request);

        Teacher teacher = teacherService.load(teNo);
        model.addAttribute("teacher", teacher);
        //获取学校名称
        SchoolEntity schoolEntity = schoolMapper.load(teacher.getScCode());
        model.addAttribute("schoolName", schoolEntity.getName());

        List<Student> list = teacherService.getStuListByTeNo(teNo);
        model.addAttribute("stuList", list);

        return "classic/teacherPC/msgSendByOne";

    }


    //教师端学生请假
    @RequestMapping("/teacher/stuLeave")
    public String stuLeave(HttpServletRequest request, Model model) {
        String teNo = SessionUtil.getLoginTeacherNo(request);
        model.addAttribute("classlist", new ReturnT<>(teacherService.getClassList(teNo)));
        return "classic/teacherPC/stuLeave";
    }

    //教师端学生请假记录
    @RequestMapping("/teacher/stuLeaveList")
    public String stuLeaveList(HttpServletRequest request, Model model) {
        String teNo = SessionUtil.getLoginTeacherNo(request);
        model.addAttribute("classlist", new ReturnT<>(teacherService.getClassList(teNo)));
        return "classic/teacherPC/stuLeaveList";
    }

    //修改请假页面
    @RequestMapping("/teacher/editLeave")
    public String editLeave(HttpServletRequest request, Model model, String id, String stuName) throws Exception {
        model.addAttribute("leave", leaveService.load(Integer.parseInt(id)));
        model.addAttribute("stuName", stuName);

        return "classic/teacherPC/editLeave";
    }

    @Autowired
    WxService wxService;
    @Autowired
    WxMpService wxMpService;

    //wxtest界面控制器

    @RequestMapping("/bindGuardian")
    public String bindGuardian(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String openid = request.getParameter("openid");
        logger.info(openid);
//        String code = request.getParameter("code");
//        String scCode = RequestHelper.getRequest(request, "sccode");

        //重新发起请求openid
//        if (StringUtils.isBlank(openid)) {
//            SchoolWX schoolWX = schoolService.loadSchoolWX(scCode);
//            if (StringUtils.isBlank(code)) {
//                StringBuffer url = request.getRequestURL();
//                //项目域名
//                String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append(request.getServletContext().getContextPath()).append("/").toString();
//                //回调地址
//                String redirectUrl = tempContextUrl + "page/bindGuardian";
//                //回调state参数
//                String state = "";
//                String wxUrl = schoolService.getWxMpService(scCode).oauth2buildAuthorizationUrl(redirectUrl, WxConsts.OAuth2Scope.SNSAPI_BASE, state);
//                return "redirect:" + wxUrl;
//            } else {
//                WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
//                try {
//                    wxMpOAuth2AccessToken = schoolService.getWxMpService(scCode).oauth2getAccessToken(code);
//                } catch (WxErrorException e) {
//                    logger.error("【微信网页授权】{}", e);
//                    //抛出异常 自定义的  方便处理  可自己定义
//                    throw new Exception("微信网页授权异常" + e.getError().getErrorMsg());
//                }
//                openid = wxMpOAuth2AccessToken.getOpenId();
//            }
//        }

        model.addAttribute("openid", openid);
        if (openid == null || "".equals(openid)) {
            openid = UUID.randomUUID().toString();
        }
        DataGridJson obj = schoolService.list(null, "", schoolNum, 1);
        model.addAttribute("schoollist", new ReturnT<>(obj));
        List<WxBandEntity> guardian = guardianService.getByOpenId(openid);
        model.addAttribute("guardianList", guardian);

        String flag = request.getParameter("flag");
        String page = "classic/wxPage/nobind";
        if (guardian.size() > 0) {
            //家长有绑定
            page = "classic/wxPage/bind";
        }
        return page;
    }


    @RequestMapping("/bindPage")
    public String bingPage(HttpServletRequest request, Model model, String flag) {
        DataGridJson obj = schoolService.list(null, "", schoolNum, 1);
        model.addAttribute("schoollist", new ReturnT<>(obj));
        model.addAttribute("openid", request.getParameter("openid"));
        return "classic/wxPage/nobind";
    }

    //wxtest界面控制器

    @RequestMapping("/configList")
    public String configPage(HttpServletRequest request, Model model) {


        //获取学校
        DataGridJson obj = schoolService.list(null, "", schoolNum, 1);
        model.addAttribute("schoollist", new ReturnT<>(obj));

        //获取配置项
        List<ConfigDefine> list = configDefineService.listAll();
        model.addAttribute("configItemList", new ReturnT<>(list));
        return "classic/config/configList";
    }


    @RequestMapping("/addConfig")
    public String addConfig(HttpServletRequest request, Model model) {

        //获取学校
        DataGridJson obj = schoolService.list(null, "", schoolNum, 1);
        model.addAttribute("schoollist", new ReturnT<>(obj));

        List<ConfigDefine> list = configDefineService.listAll();
        model.addAttribute("configItemList", new ReturnT<>(list));

        return "classic/config/addConfig";
    }

    @RequestMapping("/editConfig")
    public String editConfig(Integer configId, String scName, String scCode, String cfCode, String value, Model model) {
        //存配置设置id
        model.addAttribute("configId", configId);
        //学校名称
        model.addAttribute("scName", scName);
        model.addAttribute("value", value);
        //获取配置项的选择值
        Config config = configMapper.selectByScCodeAndCfCode(scCode, cfCode);
        ConfigDefine configDefine = configDefineMapper.selectByPrimaryKey(config.getCfDeId());

        if (configDefine.getType() != null && ("Select").equals(configDefine.getType())) {

            String[] selectValue = configDefine.getSelectValue().split(",");
            List list = new ArrayList();
            for (int i = 0; i < selectValue.length; i++) {
                list.add(selectValue[i]);
                if (value.equals(selectValue[i])) {
                    Collections.swap(list, i, 0);
                }

            }
            model.addAttribute("list", list);
        }
        model.addAttribute("data", configDefine);
        return "classic/config/editConfig";
    }


    @RequestMapping("/configItemList")
    public String configItemList(HttpServletRequest request, Model model) {
        return "classic/config/item/itemList";
    }

    @RequestMapping("/addItemConfig")
    public String addItemConfig(HttpServletRequest request, Model model) {
        return "classic/config/item/addItem";
    }

    @RequestMapping("/editItemConfig")
    public String editItemConfig(HttpServletRequest request, Model model, Integer id) {

        ConfigDefine configDefine = configDefineMapper.selectByPrimaryKey(id);
        model.addAttribute("configDefine", configDefine);
        return "classic/config/item/editItem";
    }


    @RequestMapping("/totals/leaveTotal")
    public String leaveTotal(HttpServletRequest request, Model model) {

        return "classic/leaveTotal/stuLeaveTotals";
    }

    @RequestMapping("/totals/stuLeaveTotals")
    public String stuLeaveTotals(HttpServletRequest request, String stuNoList, Model model) {
        model.addAttribute("stuNoList", stuNoList);
        return "classic/leaveTotal/stuLeaveList";
    }


    //门禁信息统计

    //门禁出入记录
    @RequestMapping("/ascList")
    public String ascList(HttpServletRequest request, Model model) {

/*
        1.SESSION里含有scCode不含TeNo说明是学校管理人员
        2.SESSION里含有scCode含TeNo说明是学校教师
        3.SESSION不含scCode说明是超级管理员
*/

        // 获取学校列表
        String scCode = request.getSession().getAttribute("scCode").toString();

        if (scCode == null && scCode.length() == 0) {
            //超级管理员
            scCode = null;
        }
        DataGridJson obj = schoolService.list(scCode, "", schoolNum, 1);
        model.addAttribute("schoollist", new ReturnT<>(obj));
        return "classic/ascNoticeInfo/ascList";
    }


    //门禁出入统计
    @RequestMapping("/ascTotal")
    public String ascTotal(HttpServletRequest request, String stuNoList, Model model) {
        model.addAttribute("stuNoList", stuNoList);
        return "classic/ascNoticeInfo/ascTotals";
    }


    @PostMapping("/getAscPieCountGroupByState")
    @ResponseBody
    public ReturnT<Object> getAscPieCountGroupByState(HttpServletRequest request, String stuNo, String startDate, String endDate, String clCode) {

        List<String> clCodeList = new ArrayList<>();
        if (clCode != null && clCode.length() > 0) {
            clCodeList.add(clCode);
        }else {
            clCodeList.add("message");
        }
        Map<String, Object> list1 = ascCountService.ascPieCountGroupByState(null, stuNo, startDate, endDate, clCodeList);
        return new ReturnT<>(list1);
    }


    @PostMapping("/getAscLineCountGroupByState")
    @ResponseBody
    public ReturnT<Object> getAscLineCountGroupByState(HttpServletRequest request, String stuNo, String startDate, String endDate, String clCode) {

        List<String> clCodeList = new ArrayList<>();
        if (clCode != null && clCode.length() > 0) {
            clCodeList.add(clCode);
        }else {
            clCodeList.add("message");
        }

        Map<String, Object> list1 = ascCountService.ascLineCountGroupByState(null, stuNo, startDate, endDate, clCodeList);
        return new ReturnT<>(list1);
    }

    //人脸采集页面（各学校管理员使用） by hzl 2019-10-29 15:48:46
    @RequestMapping("/rlcj")
    @LoginLimit(limit = false)
    public String rlcj(HttpServletRequest request, Model model) {
        String scCode = request.getSession().getAttribute("scCode").toString();
        List list = gradeService.list(scCode);
        model.addAttribute("gradeList", new ReturnT<>(list));
        return "classic/student/imgUpload";
    }



    //家长微信操作记录
    @RequestMapping("/guadian/wxOperateList")
    public String wxOperateList(HttpServletRequest request, String stuNoList, Model model) {
        // 获取学校列表
        String scCode = request.getSession().getAttribute("scCode").toString();

        if (scCode == null && scCode.length() == 0) {
            //超级管理员
            scCode = null;
        }
        DataGridJson obj = schoolService.list(scCode, "", schoolNum, 1);
        model.addAttribute ("schoollist", new ReturnT<>(obj));
        return "classic/wxLog/wxLogList";
    }

    //学校数据库源管理
    @RequestMapping("/schoolDBList")
    public String schoolDBList(HttpServletRequest request) {
        return "classic/school/schoolDBList";
    }


    //考勤状态记录
    @RequestMapping("/attendList")
    public String attList(HttpServletRequest request, Model model) {

        String scCode = request.getSession().getAttribute("scCode").toString();
        if (scCode == null && scCode.length() == 0) {
            //超级管理员
            scCode = null;
        }
        DataGridJson obj = schoolService.list(scCode, "", schoolNum, 1);
        model.addAttribute("schoollist", new ReturnT<>(obj));
        return "classic/attenDance/attList";
    }

    //成绩查询模块

    @RequestMapping("/getKeMuList")
    public String getKeMuList(HttpServletRequest request, Model model) {
        return "classic/score/kemuList";
    }

    @RequestMapping("/getScoreList")
    public String getScoreList(HttpServletRequest request, Model model) {

        String scCode = request.getSession().getAttribute("scCode").toString();
        if (scCode == null && scCode.length() == 0) {
            //超级管理员
            scCode = null;
        }
        List<String> list = scoresService.getRows("score"+scCode);

        scoresService.list("score_100001",null,null,null,10,10);

        return "classic/score/scoreList";
    }






}
