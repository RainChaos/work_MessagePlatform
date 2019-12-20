package com.jiapeng.messageplatform.service.impl;

import com.google.gson.JsonObject;
import com.jiapeng.messageplatform.dao.*;
import com.jiapeng.messageplatform.entity.*;
import com.jiapeng.messageplatform.model.ImportErrorResult;
import com.jiapeng.messageplatform.service.StudentService;
import com.jiapeng.messageplatform.utils.Excel;
import com.jiapeng.messageplatform.utils.PageResult;
import com.jiapeng.messageplatform.utils.ReturnT;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by HZL on 2019/8/2.
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    GuardianMapper guardianMapper;
    @Autowired
    AscNoticeLogMapper ascNoticeLogMapper;
    @Autowired
    AscNoticeDetailMapper ascNoticeDetailMapper;
    @Autowired
    MessageDetailMapper messageDetailMapper;
    @Autowired
    MessageLogMapper messageLogMapper;
    @Autowired
    LeaveMapper leaveMapper;
    @Autowired
    private ClassMapper classMapper;

    @Transactional
    @Override
    public void add(Student student) throws Exception {
        Student dbStudent = studentMapper.selectByClCodeAndStuNo(student.getClCode(), student.getStuNo());
        if (dbStudent != null) {
            throw new Exception("该班级已经存在学生编号为" + student.getStuNo() + "的学生了");
        }
        student.setStuId(UUID.randomUUID().toString());
        Date time1 = new Date();
        student.setCreateDate(time1);
        //未绑定微信
        student.setIsBind("0");

        studentMapper.insertSelective(student);
    }

    @Transactional
    @Override
    public void setGuardian(Guardian guardian) {

        Student student = new Student();
        student.setStuId(guardian.getStuId());

        if(guardian.getWxNo()!=null&&guardian.getWxNo().length()>0){
            student.setIsBind("1");
        }else{
            //获取该学生所有家长信息
            List<Guardian> guadianList = guardianMapper.listByStuId(guardian.getStuId());
            for (Guardian guadian: guadianList) {
                if(guadian.getWxNo()!=null&&guadian.getWxNo().length()>0){
                    student.setIsBind("1");
                    break;
                }
            }

        }

        studentMapper.updateByPrimaryKeySelective(student);
        guardianMapper.insert(guardian);
    }


    @Transactional
    @Override
    public void delGuardian(Integer id, String gname) {
        //获取该学生所有家长信息
        Guardian dbguadian =  guardianMapper.getGuadianById(String.valueOf(id));
        Student student = new Student();
        student.setIsBind("0");
        List<Guardian> guadianList = guardianMapper.listByStuId(dbguadian.getStuId());
        for (Guardian guadian: guadianList) {
            if(guadian.getWxNo()!=null&&guadian.getWxNo().length()>0){
                student.setIsBind("1");
                break;
            }
        }
        studentMapper.updateByPrimaryKeySelective(student);
        guardianMapper.deleteGuardian(id, gname);

    }


    @Override
    public List<Guardian> getGuardianList(String stuId) {
        return guardianMapper.listByStuId(stuId);
    }


    @Transactional
    @Override
    public void update(Student Student) {
//        Student student = mStudent.toStudent();
//        Guardian guardian = mStudent.toGuardian();

        studentMapper.updateByPrimaryKeySelective(Student);
//        guardianMapper.updateByPrimaryKeySelective(guardian);
    }

    //学生人脸图片保存路径
    @Value("${config.student.img.path}")
    private String faceImgPath;


    @Transactional
    @Override
    public void del(String stuId) {

        //删除学生
        //不删除门禁，信息表信息[以便后续查询]


        //      1.删除学生监护人表信息
        guardianMapper.deleteByStuNo(stuId);

        //      2.删除学生表信息
        Student student = studentMapper.selectByPrimaryKey(stuId);
        if(student!=null){
            //      2.1 删除图片
            if(student.getImgPath()!=null&&student.getImgPath().length()>0){
                String resourcePath = "/" + student.getScCode() + "/";
                String tempFileName = student.getStuNo() + ".jpg";
                String imgFilePath = faceImgPath + resourcePath + tempFileName;//新生成的图片
                File destFile = new File(imgFilePath);
                if(!destFile.exists()) return;
                destFile.delete();
            }
        }
        studentMapper.deleteByPrimaryKey(stuId);



        //      3.删除该该学生门禁信息
//        List<AscNoticeLog> list = ascNoticeLogMapper.getRecordByStuId(stuId);
//        for (AscNoticeLog asc:list) {
//                if(asc.getLeaveId()!=null){
//                    //3.1 删除该学生请假信息
//                    leaveMapper.deleteByPrimaryKey(asc.getLeaveId());
//                    //3.2 删除门禁详情表信息
//                    ascNoticeDetailMapper.deleteByPrimaryKey(asc.getId());
//                }
//        }
//        ascNoticeLogMapper.deleteByStuId(stuId);


        //      4.删除信息详情表记录表
//        messageDetailMapper.deleteByStuId(stuId);

    }

    @Override
    public Student load(String stuId) throws Exception {
        Student student = studentMapper.selectByPrimaryKey(stuId);
        if (student == null)
            throw new Exception("学生记录不存在");
        return student;
    }

    @Override
    public Student loadByScCodeAndStuNo(String scCode, String stuNo) throws Exception {
        Student student = studentMapper.selectByScCodeAndStuNo(scCode, stuNo);
        if (student == null)
            throw new Exception("学生记录不存在");
        return student;
    }

    @Override
    public PageResult list(int pageIndex, int pageSize, List<String> clCodeList, String unitCode, String stuName,String isBind) {
        int listCount = studentMapper.listCount(clCodeList,unitCode,stuName,isBind);
        int offset = (pageIndex == -1) ? pageIndex : (pageIndex - 1) * pageSize;
        List<Student> list = studentMapper.list(offset, pageSize, clCodeList, unitCode, stuName,isBind);
        return new PageResult(listCount, list);
    }



    @Override
    public PageResult listStudent(int pageIndex, int pageSize, List<String> clCodeList, String unitCode, String stuName,String isBind) {
        int listCount = studentMapper.listStudentCount(clCodeList,unitCode,stuName,isBind);
        int offset = (pageIndex == -1) ? pageIndex : (pageIndex - 1) * pageSize;
        List<Student> list = studentMapper.listStudent(offset, pageSize, clCodeList, unitCode, stuName,isBind);
        return new PageResult(listCount, list);
    }





    @Override
    public ReturnT<Object> isBingList(String sc_code,String unitName,List<String> clCodeList, String unitCode,String isBind) throws Exception {
        Map<String, String> colMap = new HashMap<>();
        colMap.put("编号", "stuNo");
        colMap.put("姓名", "stuName");
        colMap.put("性别", "sex");
        colMap.put("头像", "imgPath");
        colMap.put("微信绑定", "isBind");
        colMap.put("班级","className");
        colMap.put("年级", "gradeName");
        colMap.put("学校", "schoolName");

        List<Student> stuList =  studentMapper.binglist(sc_code,clCodeList,unitCode,isBind);
        List<Object> excelList = new ArrayList<>();
        for(int i = 0; i<stuList.size(); i++){

            String imgPath = stuList.get(i).getImgPath()==null?"":stuList.get(i).getImgPath();
            String schName = stuList.get(i).getSchoolName()==null?"":stuList.get(i).getSchoolName();
            String graName = stuList.get(i).getGradeName()==null?"":stuList.get(i).getGradeName();
            String claName =stuList.get(i).getClassName()==null?"":stuList.get(i).getClassName();
            String stuNo = stuList.get(i).getStuNo()==null?"":stuList.get(i).getStuNo();
            String stuName = stuList.get(i).getStuName()==null?"":stuList.get(i).getStuName();
            String stuSex = stuList.get(i).getSex().equals("1") ? "男":"女";

            String stuIsBind = stuList.get(i).getIsBind()==null?"":stuList.get(i).getIsBind();
            stuIsBind = stuIsBind.equals("1") ? "是":"否";

            Student student = new Student();
            student.setSchoolName(schName);
            student.setGradeName(graName);
            student.setClassName(claName);
            student.setStuNo(stuNo);
            student.setStuName(stuName);
            student.setSex(stuSex);
            student.setImgPath(imgPath);
            student.setIsBind(stuIsBind);

            excelList.add(student);
        }
        if(deleteFile(downPath+sc_code+"学生名单.xls")){
            new Excel().writeData(downPath+unitName+"学生名单.xls", "学生名单", excelList, colMap);
        }
        return new ReturnT<>(200,downPath,unitName+"学生名单.xls");
    }


    /**
     * 删除单个文件
     *
     * @param fileName
     *            要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }



    @Transactional
    @Override
    public void changeClass(String stuNoStr, String targrtClCode) {
        String[] stuNoArr = null;
        if (StringUtils.isNotBlank(stuNoStr)) {
            stuNoArr = stuNoStr.split(",");
        }
        if (stuNoArr != null) {
            for (String stuNo : stuNoArr) {
                if (StringUtils.isNotBlank(stuNo))
                    studentMapper.updateClCode(stuNo, targrtClCode);
            }
        }
    }

    @Override
    public void importStudent(String fileName, String sheetName) {
        ImportStudent importStudent = new ImportStudent(fileName, sheetName);
        importStudent.start();
        //simportStudent.run();
    }

    public static int total = 0;
    public static int totalSuccess = 0;
    public static int totalFail = 0;
    public static boolean isImporting = false;
    public static boolean isImportEnd = false;
    public static List<Object> errList = new ArrayList<>();
    public static String errMessage = null;

    class ImportStudent extends Thread {
        private String fileName;
        private String sheetName;

        public ImportStudent(String fileName, String sheetName) {
            this.fileName = fileName;
            this.sheetName = sheetName;
        }

        public void run() {
            try {
                List<JsonObject> list = new Excel().getData(fileName, sheetName);
                //检查字段是否存在
                checkFieldExist(list.get(0));

                StudentServiceImpl.total = list.size();
                StudentServiceImpl.totalSuccess = 0;
                StudentServiceImpl.totalFail = 0;
                StudentServiceImpl.isImporting = true;
                StudentServiceImpl.isImportEnd = false;
                StudentServiceImpl.errList.clear();
                StudentServiceImpl.errMessage = null;

                list.forEach(jsonObject -> {

                    Student student = new Student();
                    student.setStuId(UUID.randomUUID().toString());
                    student.setStuNo(jsonObject.get("编号").getAsString().trim());
                    student.setStuName(jsonObject.get("姓名").getAsString().trim());
                    student.setSex(jsonObject.get("性别").getAsString().trim());
                    student.setClCode(jsonObject.get("班级代码").getAsString().trim());


                    //判断身份证
                    if (null != jsonObject.get("身份证号").getAsString()) {
                        if (jsonObject.get("身份证号").getAsString().trim().length() > 0) {
                            student.setIdCode(jsonObject.get("身份证号").getAsString().trim());
                        } else {
                            student.setIdCode(null);
                        }
                    }


                    //判断学生家长
                    Guardian guardian = new Guardian();
                    if (null != jsonObject.get("家长姓名").getAsString() && !jsonObject.get("家长姓名").getAsString().equals("")) {
                        guardian.setGuaName(jsonObject.get("家长姓名").getAsString().trim());
                        guardian.setPhone(jsonObject.get("联系电话").getAsString().trim());
                        guardian.setStuId(student.getStuId());
                    } else {
                        guardian.setStuId(null);
                    }


                    //判断绑定





                    try {
                        //检查字段是否为空
                        checkFieldConetentEmpty(jsonObject);
                        //查找数据库是否存在该学生记录
                        Student dbStudent = studentMapper.selectByClCodeAndStuNo(student.getClCode(), student.getStuNo());
                        //查找数据库是否存在该学生家长记录

                        if (dbStudent == null) {
                            student.setCreateDate(new Date());
                            studentMapper.insertSelective(student);
                            if (guardian.getStuId() != null) {
                                guardianMapper.insertSelective(guardian);
                            }

                            StudentServiceImpl.totalSuccess++;
                        } else {
                            //导入时更新操作不允许修改学生stuId以及创建时间
                            student.setStuId(dbStudent.getStuId());
                            student.setCreateDate(dbStudent.getCreateDate());
                            studentMapper.updateByPrimaryKeySelective(student);
                            Guardian dbguardian = guardianMapper.findByStuIdAndGuaName(dbStudent.getStuId(), guardian.getGuaName());

                            if (dbguardian != null) {
                                guardian.setId(dbguardian.getId());
                                guardianMapper.updateByPrimaryKeySelective(guardian);
                            } else {
                                if (guardian.getStuId() != null) {
                                    guardian.setStuId(dbStudent.getStuId());
                                    guardianMapper.insertSelective(guardian);
                                }


                            }


                            StudentServiceImpl.totalSuccess++;
                        }
                    } catch (Exception e) {
                        StudentServiceImpl.totalFail++;
                        errList.add(new ImportErrorResult(student.getStuNo(), student.getStuName(), e.getMessage()));
                    }
                });
            } catch (Exception ex) {
                StudentServiceImpl.errMessage = ex.getMessage();
            } finally {
                StudentServiceImpl.isImporting = false;
                StudentServiceImpl.isImportEnd = true;
            }
        }

        public void checkFieldExist(JsonObject jsonObject) throws Exception {
            if (null == jsonObject.get("编号"))
                throw new Exception("“编号”字段不存在");
            if (null == jsonObject.get("姓名"))
                throw new Exception("“姓名”字段不存在");
            if (null == jsonObject.get("性别"))
                throw new Exception("“性别”字段不存在");

//            if (null == jsonObject.get("家长姓名"))
//                throw new Exception("“家长姓名”字段不存在");
//            if (null == jsonObject.get("联系电话"))
//                throw new Exception("“联系电话”字段不存在");
//            if (null == jsonObject.get("身份证号"))
//                throw new Exception("“身份证号”字段不存在");
            if (null == jsonObject.get("班级代码"))
                throw new Exception("“班级代码”字段不存在");
        }

        public JsonObject checkFieldConetentEmpty(JsonObject jsonObject) throws Exception {
            if (jsonObject.get("编号").getAsString().trim().equals(""))
                throw new Exception("“编号”不能为空");
            if (jsonObject.get("姓名").getAsString().trim().equals(""))
                throw new Exception("“姓名”不能为空");
            if (jsonObject.get("性别").getAsString().trim().equals(""))
                throw new Exception("“性别”不能为空");
            if (jsonObject.get("班级代码").getAsString().trim().equals(""))
                throw new Exception("“班级代码”不能为空");
//            if (jsonObject.get("家长姓名").getAsString().trim().equals(""))
//                throw new Exception("“家长姓名”不能为空");

//            if (jsonObject.get("联系电话").getAsString().trim().equals(""))
//                throw new Exception("“联系电话”不能为空");
//            if (jsonObject.get("身份证号").getAsString().trim().equals(""))
//                throw new Exception("“身份证号”不能为空");

            return jsonObject;
        }
    }

    private String errXlsFileName = "import_stu_err.xls";
    @Value("${system.config.downpath}")
    private String downPath;

    @Override
    public ReturnT<Object> importMsg() {
        if (StudentServiceImpl.isImportEnd) {
            if (StringUtils.isNotBlank(StudentServiceImpl.errMessage)) {
                return ReturnT.getFail(StudentServiceImpl.errMessage);
            }
            if (StudentServiceImpl.errList.size() > 0) {
                Map<String, String> colMap = new HashMap<>();
                colMap.put("编号", "no");
                colMap.put("姓名", "name");
                colMap.put("错误原因", "description");
                try {
                    String xlsFile = downPath + errXlsFileName;
                    new Excel().writeData(xlsFile, "导入出错数据", StudentServiceImpl.errList, colMap);
                    return new ReturnT(200, xlsFile, "导入已结束，不过出现错误");
                } catch (Exception e) {
                    e.printStackTrace();
                    return ReturnT.getFail(e.getMessage());
                }
            } else {
                return new ReturnT<>();
            }
        } else {
            Map<String, Integer> map = new HashMap<>();
            map.put("total", StudentServiceImpl.total);
            map.put("success", StudentServiceImpl.totalSuccess);
            map.put("fail", StudentServiceImpl.totalFail);
            return new ReturnT(map);
        }
    }

    @Override
    public void studentHandle(String sc_code, String stuNo, String stuName, String sex, String idCode, String cl_uuidStr, String delete) {
        Student exitStudent = studentMapper.selectByScCodeAndStuNo(sc_code, stuNo);
        ClassEntity classEntity = classMapper.getClassByUUID(cl_uuidStr);
        //不存在插入
        if (exitStudent == null && "0".equals(delete)) {
            Student student = new Student();
            student.setStuName(stuName);
            student.setClCode(classEntity.getCl_code());
            student.setSex(sex);
            student.setIdCode(idCode);
            student.setStuNo(stuNo);
            student.setCreateDate(new Date());
            student.setStuId(UUID.randomUUID().toString());
            studentMapper.insertSelective(student);
            return;
        }
        //存在修改
        if (exitStudent != null && "0".equals(delete)) { //存在修改
            exitStudent.setStuNo(stuNo);
            exitStudent.setStuName(stuName);
            exitStudent.setSex(sex);
            exitStudent.setIdCode(idCode);
            exitStudent.setClCode(classEntity.getCl_code());
            studentMapper.updateByPrimaryKeySelective(exitStudent);
            return;
        }
        //存在删除
        if (exitStudent != null && "1".equals(delete)) {
            String stuId = exitStudent.getStuId();
            //      1.删除学生监护人表信息
            guardianMapper.deleteByStuNo(stuId);

            //      2.删除学生表信息
            Student student = studentMapper.selectByPrimaryKey(stuId);
            if(student!=null){
                //      2.1 删除图片
                if(student.getImgPath()!=null&&student.getImgPath().length()>0){
                    String resourcePath = "/" + student.getScCode() + "/";
                    String tempFileName = student.getStuNo() + ".jpg";
                    String imgFilePath = faceImgPath + resourcePath + tempFileName;//新生成的图片
                    File destFile = new File(imgFilePath);
                    if(!destFile.exists()) return;
                    destFile.delete();
                }
            }
            studentMapper.deleteByPrimaryKey(stuId);



            //      3.删除该该学生门禁信息
            List<AscNoticeLog> list = ascNoticeLogMapper.getRecordByStuId(stuId);
            for (AscNoticeLog asc:list) {
                if(asc.getLeaveId()!=null){
                    //3.1 删除该学生请假信息
                    leaveMapper.deleteByPrimaryKey(asc.getLeaveId());
                    //3.2 删除门禁详情表信息
                    ascNoticeDetailMapper.deleteByPrimaryKey(asc.getId());
                }
            }
            ascNoticeLogMapper.deleteByStuId(stuId);


            //      4.删除信息详情表记录表
            messageDetailMapper.deleteByStuId(stuId);
        }
    }

    @Override
    public List<Student> getStudentByScCode(String scCode) {
        return studentMapper.selectByScCode(scCode);
    }
}

