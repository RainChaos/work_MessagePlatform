package com.jiapeng.messageplatform.service.impl;

import com.google.gson.JsonObject;
import com.jiapeng.messageplatform.dao.*;
import com.jiapeng.messageplatform.entity.*;
import com.jiapeng.messageplatform.model.ImportErrorResult;
import com.jiapeng.messageplatform.model.UnitTree;
import com.jiapeng.messageplatform.service.TeacherService;
import com.jiapeng.messageplatform.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HZL on 2019/8/7.
 */
@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    ClassTeacherMapper classTeacherMapper;
    @Autowired
    MessageDetailMapper messageDetailMapper;
    @Autowired
    MessageLogMapper messageLogMapper;
    @Autowired
    LeaveMapper leaveMapper;
    @Autowired
    TeacherWxInfoMapper teacherWxInfoMapper;

    @Override
    public void login(HttpServletRequest request, String loginName, String password,String verifyCode,String flag) throws Exception {
        String sessionVerifyCode = SessionUtil.getVerifyCode(request).toLowerCase();

//        if (StringUtils.isBlank(verifyCode.toLowerCase()))
//            throw new Exception(Constants.VERIFY_CODE_EMPTY);
//        if (!sessionVerifyCode.equals(verifyCode.toLowerCase()))
//            throw new Exception(Constants.VERIFY_CODE_ERROR);

        if (StringUtils.isBlank(loginName) || StringUtils.isBlank(password))
            throw new Exception(Constants.USER_PASSWORD_EMPTY);

        Teacher teacher = teacherMapper.findOneByLoginName(loginName);
        if (null == teacher)
            throw new Exception(Constants.USER_PASSWORD_ERROR);


        if(flag==null||!"wxLogin".equals(flag)){
            //说明不是微信登录
            if (!teacher.getPassword().equals(Md5Util.MD5(password)))
                throw new Exception(Constants.USER_PASSWORD_ERROR);
        }




        //设置登录教师编号Session
        SessionUtil.setSession(request, Constants.LOGIN_TEACHER_NO, teacher.getTeNo());
        SessionUtil.setSession(request, Constants.LOGIN_TEACHER_SCCODE, teacher.getScCode());
        SessionUtil.setSession(request, Constants.LOGIN_TEACHER_LOGINNAME, teacher.getLoginname());
        SessionUtil.setSession(request, Constants.LOGIN_TEACHER_OPENID, teacher.getIswxlogin());

    }

    @Override
    public Teacher getRecordByWxNo(String wxNo) {
        return teacherMapper.selectByWxNo(wxNo);
    }



    @Override
    public void add (Teacher teacher) throws Exception{
        teacher.setPassword(Md5Util.MD5(teacher.getPassword()));
      try {
          teacher.setIswxlogin("false");
          teacher.setIsdelete("0");
          teacherMapper.insertSelective(teacher);
      }catch (Exception e){
          throw new Exception("添加失败,已经存在编号为:"+teacher.getTeNo()+"的教师了");
      }

    }

    @Override
    public void update(Teacher teacher) {
        teacher.setPassword(null);
        teacherMapper.updateByPrimaryKeySelective(teacher);
    }


    @Override
    public void resetPwd(Teacher teacher) {
        teacher.setPassword(Md5Util.MD5("123456"));
        teacherMapper.updateByPrimaryKeySelective(teacher);
    }

    @Override
    public void updatePwd(String teNo, String oldPassword, String newPassword) throws Exception {
        Teacher teacher = load(teNo);
        if (!teacher.getPassword().equals(Md5Util.MD5(oldPassword)))
            throw new Exception("原密码不正确");
        if (teacher.getPassword().equals(Md5Util.MD5(newPassword)))
            throw new Exception("新密码不能与旧密码相同");
        if (StringUtils.isBlank(newPassword))
            throw new Exception("密码不能为空");
        if (newPassword.length() < 6)
            throw new Exception("密码长度不能小于6位");
        teacherMapper.updatePwd(teNo, Md5Util.MD5(newPassword));
    }

    @Transactional
    @Override
    public void setClass(String teNo, List<String> clCodeList) {
        classTeacherMapper.del(teNo);
        clCodeList.forEach(str -> {
            if (StringUtils.isNotBlank(str)) {
                String[] arr = str.split("@");
                ClassTeacher classTeacher = new ClassTeacher();
                classTeacher.setTeNo(teNo);
                classTeacher.setClCode(arr[0]);
                classTeacher.setHeadMaster(Boolean.parseBoolean(arr[1]));
                classTeacherMapper.insert(classTeacher);
            }
        });
    }


    @Override
    public void delClass(String teNo, String clCode) {
        classTeacherMapper.delClass(teNo, clCode);
    }

    @Override
    public void enable(String teNo, String enableType, String reason) throws Exception {
        Teacher teacher = load(teNo);
        if (enableType.equals("1")) {
            teacher.setEnable("1");
            teacher.setStatusreason("");
        } else {
            if (StringUtils.isBlank(reason))
                throw new Exception("停用理由不能为空");
            teacher.setEnable("0");
            teacher.setStatusreason(reason);
        }
        teacherMapper.updateByPrimaryKeySelective(teacher);
    }








    @Override
    @Transactional
    public void del(String teNo) {
        //删除班级教师表
        classTeacherMapper.del(teNo);
        //删除教师表（软删除）isdelete
        Teacher teacher = new Teacher();
        teacher.setTeNo(teNo);
        teacher.setIsdelete("1");
        teacherMapper.updateByPrimaryKeySelective(teacher);

        //删除教师信息绑定表
        teacherWxInfoMapper.deleteByTeNo(teNo);
        //删除请假表
//        leaveMapper.deleteByTeNo(teNo);



//        //删除短信表以及短信详情表
//        List<MessageLog> list = messageLogMapper.selectMessageByStuNo(teNo);
//        for (MessageLog messa: list) {
//            //删除该教师发送的信息记录（详细）
//            messageDetailMapper.deleteByPrimaryKey(messa.getId());
//        }
//        //删除信息表
//        messageLogMapper.deleteByTeNo(teNo);


    }

    @Override
    public Teacher load(String teNo) {
        return teacherMapper.selectByPrimaryKey(teNo);
    }

    @Override
    public PageResult pageList(int pageIndex, int pageSize, String scCode,String isdelete,String name) {
        int listCount = teacherMapper.pageListCount(scCode,isdelete,name);
        int offset = (pageIndex - 1) * pageSize;
        List<Teacher> list = teacherMapper.pageList(offset, pageSize, scCode,isdelete,name);
        return new PageResult(listCount, list);
    }

    @Override
    public void importTeacher(String fileName, String sheetName) {
        ImportTeacher importT = new ImportTeacher(fileName, sheetName);
        //importT.start();
        importT.run();
    }

    @Override
    public List<ClassInfo> getClassList(String teNo) {
        Teacher teacher = load(teNo);
        return teacher.getClassList();
    }


    @Override
    public  List<Map<String, Object>> getClassAndInfoList(String teNo) {
        return teacherMapper.getClassAndInfoList(teNo);
    }




    public static int total = 0;
    public static int totalSuccess = 0;
    public static int totalFail = 0;
    public static boolean isImporting = false;
    public static boolean isImportEnd = false;
    public static List<Object> errList = new ArrayList<>();
    public static String errMessage = null;

    class ImportTeacher extends Thread {
        private String fileName;
        private String sheetName;


        public ImportTeacher(String fileName, String sheetName) {
            this.fileName = fileName;
            this.sheetName = sheetName;
        }

        @Override
        public void run() {
            try {
                List<JsonObject> list = new Excel().getData(fileName, sheetName);
                checkFieldExist(list.get(0));

                TeacherServiceImpl.total = list.size();
                TeacherServiceImpl.totalSuccess = 0;
                TeacherServiceImpl.totalFail = 0;
                TeacherServiceImpl.isImporting = true;
                TeacherServiceImpl.isImportEnd = false;
                TeacherServiceImpl.errList.clear();
                TeacherServiceImpl.errMessage = null;

                list.forEach(jsonObject -> {

                    Teacher teacher = new Teacher();
                    teacher.setTeNo(jsonObject.get("编号").getAsString().trim());
                    teacher.setLoginname(jsonObject.get("登录名").getAsString().trim());
                    teacher.setName(jsonObject.get("姓名").getAsString().trim());
                    teacher.setPassword(Md5Util.MD5(jsonObject.get("密码").getAsString().trim()));
                    teacher.setSex(jsonObject.get("性别").getAsString().trim());
                    teacher.setPhoneno(jsonObject.get("电话号码").getAsString().trim());

                    teacher.setIswxlogin("false");
                    teacher.setIsdelete("0");

                    if (null != jsonObject.get("身份证号")){
                        if (jsonObject.get("身份证号").getAsString().trim().equals("")||jsonObject.get("身份证号").getAsString().trim()==null){
                            teacher.setIdcode("");
                        }else {
                            teacher.setIdcode(jsonObject.get("身份证号").getAsString().trim());
                        }
                    }
                    teacher.setScCode(jsonObject.get("学校代码").getAsString().trim());
                    String codeStr = jsonObject.get("班级代码").getAsString().trim();

                    String[] clCodeArr = codeStr.split(",");
                    try {
                        checkFieldConetentEmpty(jsonObject);
                        Teacher dbTeacher = teacherMapper.selectByPrimaryKey(teacher.getTeNo());
                        //  List<ClassTeacher> classList = teacher.getClassTeacherList();
                        //增加
                        if (dbTeacher == null) {
                            teacherMapper.insertSelective(teacher);
                            for (int i = 0; i < clCodeArr.length; i++) {
                                ClassTeacher classTeacher = new ClassTeacher();
                                classTeacher.setTeNo(teacher.getTeNo());
                                classTeacher.setClCode(clCodeArr[i]);
                                classTeacher.setHeadMaster(false);
                                classTeacherMapper.insert(classTeacher);
                            }
                            TeacherServiceImpl.totalSuccess++;
                        }
                        //修改
                        else {
                            teacherMapper.updateByPrimaryKeySelective(teacher);
                            classTeacherMapper.del(teacher.getTeNo());
//                            for (ClassTeacher classTeacher : classList) {
//                                classTeacherMapper.insert(classTeacher);
//                            }
                            for (int i = 0; i < clCodeArr.length; i++) {
                                ClassTeacher classTeacher = new ClassTeacher();
                                classTeacher.setTeNo(teacher.getTeNo());
                                classTeacher.setClCode(clCodeArr[i]);
                                classTeacher.setHeadMaster(false);
                                classTeacherMapper.insert(classTeacher);
                            }
                            TeacherServiceImpl.totalSuccess++;
                        }
                    } catch (Exception e) {
                        TeacherServiceImpl.totalFail++;
                        errList.add(new ImportErrorResult(teacher.getTeNo(), teacher.getName(), e.getMessage()));
                    }
                });
            } catch (Exception ex) {
                TeacherServiceImpl.errMessage = ex.getMessage();
            } finally {
                TeacherServiceImpl.isImporting = false;
                TeacherServiceImpl.isImportEnd = true;
            }
        }

        public void checkFieldExist(JsonObject jsonObject) throws Exception {
            if (null == jsonObject.get("编号"))
                throw new Exception("“编号”字段不存在");
            if (null == jsonObject.get("姓名"))
                throw new Exception("“姓名”字段不存在");
            if (null == jsonObject.get("登录名"))
                throw new Exception("“登录名”字段不存在");
            if (null == jsonObject.get("密码"))
                throw new Exception("“密码”字段不存在");
            if (null == jsonObject.get("性别"))
                throw new Exception("“性别”字段不存在");
            if (null == jsonObject.get("电话号码"))
                throw new Exception("“电话号码”字段不存在");
//            if (null == jsonObject.get("身份证号"))
//                throw new Exception("“身份证号”字段不存在");
            if (null == jsonObject.get("班级代码"))
                throw new Exception("“班级代码”字段不存在");
            if (null == jsonObject.get("学校代码"))
                throw new Exception("“学校代码”字段不存在");
        }

        public JsonObject checkFieldConetentEmpty(JsonObject jsonObject) throws Exception {
            if (jsonObject.get("编号").getAsString().trim().equals(""))
                throw new Exception("“编号”不能为空");
            if (jsonObject.get("姓名").getAsString().trim().equals(""))
                throw new Exception("“姓名”不能为空");
            if (jsonObject.get("登录名").getAsString().trim().equals(""))
                throw new Exception("“登录名”不能为空");
            if (jsonObject.get("密码").getAsString().trim().equals(""))
                throw new Exception("“密码”不能为空");
            if (jsonObject.get("性别").getAsString().trim().equals(""))
                throw new Exception("“性别”不能为空");
            if (jsonObject.get("电话号码").getAsString().trim().equals(""))
                throw new Exception("“电话号码”不能为空");
//            if (jsonObject.get("身份证号").getAsString().trim().equals(""))
//                throw new Exception("“身份证号”不能为空");
            if (jsonObject.get("班级代码").getAsString().trim().equals(""))
                throw new Exception("“班级代码”不能为空");
            if (jsonObject.get("学校代码").getAsString().trim().equals(""))
                throw new Exception("“学校代码”不能为空");
            return jsonObject;
        }
    }

    private String errXlsFileName = "import_teacher_err.xls";
    @Value("${system.config.downpath}")
    private String downPath;

    @Override
    public ReturnT<Object> importMsg() {
        if (TeacherServiceImpl.isImportEnd) {
            if (StringUtils.isNotBlank(TeacherServiceImpl.errMessage)) {
                return ReturnT.getFail(TeacherServiceImpl.errMessage);
            }
            if (TeacherServiceImpl.errList.size() > 0) {
                Map<String, String> colMap = new HashMap<>();
                colMap.put("编号", "no");
                colMap.put("姓名", "name");
                colMap.put("错误原因", "description");
                try {
                    String xlsFile = downPath + errXlsFileName;
                    new Excel().writeData(xlsFile, "导入出错数据", TeacherServiceImpl.errList, colMap);
                    return new ReturnT(200, "/download/" + errXlsFileName, "导入已结束，不过出现错误");
                } catch (Exception e) {
                    e.printStackTrace();
                    return ReturnT.getFail(e.getMessage());
                }
            } else {
                return new ReturnT<>();
            }
        } else {
            Map<String, Integer> map = new HashMap<>();
            map.put("total", TeacherServiceImpl.total);
            map.put("success", TeacherServiceImpl.totalSuccess);
            map.put("fail", TeacherServiceImpl.totalFail);
            return new ReturnT(map);
        }
    }

    @Override
    public List<UnitTree> getUnitTree(String teNo) {
        List<Map<String, Object>> list = teacherMapper.getClassAndInfoList(teNo);
        List<UnitTree> unitList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            String scCode = map.get("sc_code").toString();
            String grCode = map.get("gr_code").toString();
            String clCode = map.get("cl_code").toString();

            if (!isExistSchool(unitList, scCode)) {
                UnitTree unitTree = new UnitTree();
                unitTree.setUnitType("school");
                unitTree.setId(scCode);
                unitTree.setTitle(map.get("schoolName").toString());
                unitList.add(unitTree);
            }
            if (!isExistGrade(unitList, scCode, grCode)) {
                for (UnitTree ut : unitList) {
                    if (ut.getUnitType().equals("school") && ut.getId().equals(scCode)) {
                        UnitTree unitTree = new UnitTree();
                        unitTree.setUnitType("grade");
                        unitTree.setId(grCode);
                        unitTree.setTitle(map.get("gradeName").toString());
                        if (ut.getChildren() == null) {
                            List<UnitTree> gradeList = new ArrayList<>();
                            gradeList.add(unitTree);
                            ut.setChildren(gradeList);
                        } else {
                            List<UnitTree> gradeList = ut.getChildren();
                            gradeList.add(unitTree);
                            ut.setChildren(gradeList);
                        }
                    }
                }
            }
            if (!isExistClass(unitList, scCode, grCode, clCode)) {
                for (UnitTree ut : unitList) {
                    if (ut.getUnitType().equals("school") && ut.getId().equals(scCode)) {
                        List<UnitTree> grList = ut.getChildren();
                        for (UnitTree grt : grList) {
                            if (grt.getUnitType().equals("grade") && grt.getId().equals(grCode)) {
                                UnitTree unitTree = new UnitTree();
                                unitTree.setUnitType("class");
                                unitTree.setId(clCode);
                                unitTree.setTitle(map.get("className").toString());
                                if (grt.getChildren() == null) {
                                    List<UnitTree> gradeList = new ArrayList<>();
                                    gradeList.add(unitTree);
                                    grt.setChildren(gradeList);
                                } else {
                                    List<UnitTree> gradeList = grt.getChildren();
                                    gradeList.add(unitTree);
                                    grt.setChildren(gradeList);
                                }
                            }
                        }
                    }
                }
            }
        }
        return unitList;
    }

    //判断是否存在学校

    public boolean isExistSchool(List<UnitTree> list, String scCode) {
        for (UnitTree ut : list) {
            if (ut.getId().equals(scCode))
                return true;
        }
        return false;
    }

    //判断是否存在年级
    public boolean isExistGrade(List<UnitTree> list, String scCode, String grCode) {
        for (UnitTree ut : list) {
            if (ut.getUnitType().equals("school") && ut.getId().equals(scCode)) {
                List<UnitTree> grList = ut.getChildren();
                if (grList != null) {
                    for (UnitTree grTree : grList) {
                        if (grTree.getId().equals(grCode))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    //判断是否存在班级
    public boolean isExistClass(List<UnitTree> list, String scCode, String grCode, String clCode) {
        for (UnitTree ut : list) {
            if (ut.getUnitType().equals("school") && ut.getId().equals(scCode)) {
                List<UnitTree> grList = ut.getChildren();
                if (grList != null) {
                    for (UnitTree grTree : grList) {
                        if (grTree.getUnitType().equals("grade") && ut.getId().equals(grCode)) {
                            List<UnitTree> clList = grTree.getChildren();
                            if (clList != null) {
                                for (UnitTree clTree : grList) {
                                    if (clTree.getId().equals(clCode))
                                        return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    @Override
    public List<Student> getStuListByTeNo(String teNo){

        return studentMapper.getStuListByTeNo(teNo);
    }


    @Override
    public  PageResult getTeNoListByScCode (int pageIndex, int pageSize, String scCode,String isdelete) {
        int listCount = teacherMapper.pageListCount(scCode,isdelete,null);
        List<Teacher> list = teacherMapper.getTeacherByScCode(pageIndex, pageSize, scCode,isdelete,null);
        return new PageResult(listCount, list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void teacherHandle(String teNo, String password, String name, String sex, String idcode, String phone, String sccode, String enable ,String delete) {
        Teacher exitTeacher = teacherMapper.selectByPrimaryKey(teNo);
        //不存在插入
        if(exitTeacher==null&&"0".equals(delete)){
            Teacher teacher = new Teacher();
            teacher.setTeNo(teNo);
            teacher.setLoginname(teNo);//默认教师号登陆
            teacher.setPassword(password);
            teacher.setName(name);
            teacher.setSex(sex);
            teacher.setIdcode(idcode);
            teacher.setPhoneno(phone);
            teacher.setScCode(sccode);
            teacher.setEnable(enable);
            teacherMapper.insertSelective(teacher);
            return;
        }
        //存在修改
        if (exitTeacher!=null&&"0".equals(delete)){
            exitTeacher.setTeNo(teNo);
            exitTeacher.setPassword(password);
            exitTeacher.setName(name);
            exitTeacher.setSex(sex);
            exitTeacher.setIdcode(idcode);
            exitTeacher.setPhoneno(phone);
            exitTeacher.setScCode(sccode);
            exitTeacher.setEnable(enable);
            teacherMapper.updateByPrimaryKeySelective(exitTeacher);
            return;
        }
        //存在删除
        if (exitTeacher!=null&&"1".equals(delete)){
            //删除班级教师表
            classTeacherMapper.del(teNo);
            //删除教师表
            teacherMapper.deleteByPrimaryKey(teNo);
            //删除请假表
            leaveMapper.deleteByTeNo(teNo);

            //删除教师信息绑定表
            teacherWxInfoMapper.deleteByTeNo(teNo);

            //删除短信表以及短信详情表
            List<MessageLog> list = messageLogMapper.selectMessageByStuNo(teNo);
            for (MessageLog messa: list) {
                //删除该教师发送的信息记录（详细）
                messageDetailMapper.deleteByPrimaryKey(messa.getId());
            }
            //删除信息表
            messageLogMapper.deleteByTeNo(teNo);
            return;
        }
    }
}
