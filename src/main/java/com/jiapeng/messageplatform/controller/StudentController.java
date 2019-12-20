package com.jiapeng.messageplatform.controller;

import com.jiapeng.messageplatform.dao.StudentMapper;
import com.jiapeng.messageplatform.entity.ClassInfo;
import com.jiapeng.messageplatform.entity.Guardian;
import com.jiapeng.messageplatform.entity.Student;
import com.jiapeng.messageplatform.entity.Teacher;
import com.jiapeng.messageplatform.service.*;
import com.jiapeng.messageplatform.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

/**
 * Created by HZL on 2019/8/9.
 */
@RestController
@RequestMapping("/student")
public class StudentController{
    @Autowired
    StudentService studentService;
    @Autowired
    ClassService classService;
    @Autowired
    GradeService gradeService;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    TeacherService teacherService;
    @Autowired
    DataSyncService dataSyncService;



    /**
     * 添加学生
     *
     * @param student
     * @return
     */
    @PostMapping("/add")
    public ReturnT<Object> add(Student student) throws Exception {
        studentService.add(student);
        dataSyncService.studentSync(student,"0");
        //调用网络电话平台API【添加学生】

        return new ReturnT<>();
    }

    @PostMapping("/update")
    public ReturnT<Object> update(Student mstudent) {
        studentService.update(mstudent);
        dataSyncService.studentSync(mstudent,"0");
        return new ReturnT<>();
    }

    @PostMapping("/del")
    public ReturnT<Object> del(String stuId) {
        dataSyncService.studentDelSync(stuId);//删除同步需要放在删除方法之前
        studentService.del(stuId);
        return new ReturnT<>();
    }

    /**
     * @param stuNoStr    格式：“x001,x002” 批量选中的学生的stuNo几个字符串
     * @param targeClCode 要转到的班级的代码
     * @return
     */
    @PostMapping("/changeClass")
    public ReturnT<Object> changeClass(String stuNoStr, String targeClCode) {
        studentService.changeClass(stuNoStr, targeClCode);
        return new ReturnT<>();
    }

    /**
     * 导入学生
     *
     * @param @RequestParam(value="file") MultipartFile file, HttpServletRequest request
     * @param
     * @return
     */
    @PostMapping("/import")
    public ReturnT<Object> importStudent(String fileName, String sheetName, HttpServletRequest request) throws Exception {
        //todo 批量同步待做
        studentService.importStudent(fileName, sheetName);
        return new ReturnT<>();
    }


    /**
     * 查看导入学生进度信息，导入后通过轮询查看该方法是否导入完成
     *
     * @return
     */
    @PostMapping("/importMsg")
    public ReturnT<Object> importMsg() {
        return studentService.importMsg();
    }

    /**
     * 学生列表（供管理端使用）
     *
     * @param page
     * @param limit
     * @param clCode
     * @param stuName
     * @return
     */
    @PostMapping("/list")
    public ReturnT<Object> getList(HttpServletRequest request, Integer page, Integer limit,
                                   String clCode, String stuName,
                                   String isBind, String sc_code,
                                   String flag) {

        //判断是系统管理员还是学校管理员【学校代码为空，说明是系统管理员，不为空说明微学校管理员】
        List<Map<String, Object>> classList = new ArrayList<>();
        List<String> clCodeList = new ArrayList<>();
        String sessionscCode = request.getSession().getAttribute("scCode").toString();
        if (sessionscCode != null && sessionscCode.length() != 0) {
            //学校管理员
            classList = classService.list(sessionscCode, "allClassData", 10000, 1);
            if (classList.size() == 0) {
                return new ReturnT<>(new ReturnT<>());
            }
        }

        if ("school".equals(flag)) {
            classList = classService.list(sc_code, "allClassData", 10000, 1);
            if (classList.size() == 0) {
                return new ReturnT<>(new ReturnT<>());
            }

        } else if ("grade".equals(flag)) {
            String gr_code = request.getParameter("gr_code");
            classList = classService.list(sc_code, gr_code, 10000, 1);
            if (classList.size() == 0) {
                return new ReturnT<>(new ReturnT<>());
            }
        } else if ("class".equals(flag)) {
            clCodeList.add(request.getParameter("clCodeList"));
        }

        if(!"class".equals(flag)){
        for (Map<String, Object> m :
                classList) {
            clCodeList.add(m.get("cl_code").toString());
        }
        }

        if(clCodeList.size()==0&&StringUtils.isBlank(stuName)){
            return new ReturnT<>(new ReturnT<>());
        }

        PageResult pageResult = studentService.listStudent(page, limit, clCodeList, clCode, stuName,isBind);
        return new ReturnT<>(pageResult);
    }


    /**
     * 导出学生数据
     * @param request
     * @param response
     * @param flag
     * @param unitCode
     * @param scCode
     * @param unitName
     * @throws Exception
     */
    @RequestMapping(value = "/outStudentList")
    @ResponseBody
    public void downLoadList(HttpServletRequest request, HttpServletResponse response,String flag, String unitCode,String scCode,String unitName) throws Exception{

        List<String> clCodeList = new ArrayList<>();
        String clCode = "";
            List<Map<String, Object>> classList = new ArrayList<>();

            if ("school".equals(flag)) {
                classList = classService.list(scCode, "allClassData", 10000, 1);
            } else if ("grade".equals(flag)) {
                classList = classService.list(scCode, unitCode, 10000, 1);
            } else if ("class".equals(flag)) {
                clCode =unitCode;
            }
            for (Map<String, Object> m :
                    classList) {
                clCodeList.add(m.get("cl_code").toString());
            }

        //老师
        String sessionteNo = SessionUtil.getLoginTeacherNo(request);
        if(sessionteNo != null && sessionteNo.length() != 0){
           Teacher teacher =  teacherService.load(sessionteNo);
            scCode = teacher.getScCode();
        }
        ReturnT<Object>  result =  studentService.isBingList(scCode,unitName,clCodeList, clCode,null);//班级代码    是否绑定
        new  FileController().downloadFile(new File(result.getContent().toString(),result.getMsg()), response, false);
    }


    /**
     * 学生监护人列表
     * @param page
     * @param limit
     * @param stuId
     * @return
     */
    @PostMapping("/listGuardian")
    @ResponseBody
    public ReturnT<Object> getList(Integer page, Integer limit, String stuId) {
        DataGridJson obj = new DataGridJson(studentService.getGuardianList(stuId));
        return new ReturnT<>(obj);
    }

    /**
     *删除监护人
     * @param guaId
     * @param gname
     * @return
     */
    @PostMapping("/delGuardian")
    @ResponseBody
    public ReturnT<Object> delGuardian(Integer guaId, String gname) {
        studentService.delGuardian(guaId, gname);
        return new ReturnT<>();
    }

    /**
     * 新增学生监护人
     * @param guardian
     * @return
     */
    @PostMapping("/setGuardian")
    @ResponseBody
    public ReturnT<Object> setGuardian(Guardian guardian) {
        studentService.setGuardian(guardian);
        return new ReturnT<>();
    }

    /**
     * 获取学生列表（教师用户调用）
     * @param
     * @return
     */
    @PostMapping("/getStuListByclCode")
    public ReturnT<Object> getStudentList(HttpServletRequest request, String clCodeList, Integer page, Integer limit, String clCode, String stuName, String isBind) {
        PageResult pageResult = studentService.list(page, limit, null, clCode, stuName, isBind);
        return new ReturnT<>(pageResult);
    }

    //学生人脸图片保存路径
    @Value("${config.student.img.path}")
    private String faceImgPath;
    //学生人脸图片访问路径
    @Value("${config.student.img.url}")
    private String faceImgUrl;

    /**
     * 学生头像图片上传接口
     * by hzl 2019-10-30 10:16:52
     *
     * @param request
     * @param stuId
     * @return
     */
    @PostMapping("/imgUpload")
    public ReturnT<Object> getStudentList(HttpServletRequest request, String stuId) {
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile multipartFile = multipartRequest.getFile("imagefile");//file是form-data中二进制字段对应的name
            Student student = studentService.load(stuId);
            String scCode = student.getScCode();
            String stuNo = student.getStuNo();

            //每个学校照片保存到对应用学校代码命名的文件夹中
            String resourcePath = "/" + scCode + "/";
            String tempFileName = stuNo + ".jpg";
            String imgFilePath = faceImgPath + resourcePath + tempFileName;//新生成的图片
            String imgUrl = faceImgUrl + resourcePath + tempFileName;
            File destFile = new File(imgFilePath);
            destFile.getParentFile().mkdirs();
            multipartFile.transferTo(destFile);

            try {
                studentMapper.UpdateImgPath(scCode, stuNo, imgUrl);
            } catch (Exception e) {
                throw new Exception("图片路径写入数据库失败");
            }

            return new ReturnT<>("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnT.getFail(e.getMessage());
        }
    }

    /**
     * 学生照片导出
     * by hzl 2019-10-30 16:43:01
     *
     * @param request
     * @param response
     * @param flag     选择单位结构的类型：school、grade、class
     * @param unitCode 所选择单位结构对应的代码
     * @return
     * @throws Exception
     */
    @RequestMapping("/exportImg")
    public HttpServletResponse exportImg(HttpServletRequest request, HttpServletResponse response, String flag, String unitCode,String scCode) throws Exception {
        //1.根据flag传递的类型、已经对应的代码unitCode 获取clCodeList（请补充完相关代码）
        List<String> clCodeList = new ArrayList<>();
        List<Map<String, Object>> classList = new ArrayList<>();
        String clCode = "";
        if ("school".equals(flag)) {
            classList = classService.list(scCode, "allClassData", 10000, 1);
        } else if ("grade".equals(flag)) {
            classList = classService.list(scCode, unitCode, 10000, 1);
        } else if ("class".equals(flag)) {
            clCode =unitCode;
        }
        for (Map<String, Object> m :
                classList) {
            clCodeList.add(m.get("cl_code").toString());
        }

        if(classList.size()==0){
            clCodeList.add(clCode);
        }
        if(scCode.length()==0){
            scCode = request.getSession().getAttribute("scCode").toString();
        }


        PageResult pageResult = studentService.list(-1, -1, clCodeList, clCode, null, null);

        List<Student> stuList = pageResult.getData();
        String imgSavePath = faceImgPath + "/" + scCode + "/";

        List<File> files = new ArrayList<>();
        for (Student stu : stuList) {
            if (stu.getImgPath()!=null) {
                String stuImgName = stu.getStuNo() + ".jpg";
                File file = new File(imgSavePath + stuImgName);
                if (null != file) {
                    files.add(file);
                }
            }
        }
        HttpServletResponse downResponse = FileHelper.downLoadFiles(files, request, response);
        return downResponse;
    }

}
