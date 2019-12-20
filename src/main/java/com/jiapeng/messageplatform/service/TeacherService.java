package com.jiapeng.messageplatform.service;

import com.jiapeng.messageplatform.entity.ClassInfo;
import com.jiapeng.messageplatform.entity.Student;
import com.jiapeng.messageplatform.entity.Teacher;
import com.jiapeng.messageplatform.model.UnitTree;
import com.jiapeng.messageplatform.utils.PageResult;
import com.jiapeng.messageplatform.utils.ReturnT;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by HZL on 2019/8/7.
 */
public interface TeacherService {

    void login(HttpServletRequest request, String loginName, String password,String verifyCode,String flag) throws Exception;

    Teacher getRecordByWxNo(String wxNo);

    void add(Teacher teacher)throws Exception;

    void update(Teacher teacher);

    void resetPwd(Teacher teacher);

    void updatePwd(String teNo, String oldPassword, String newPassword) throws Exception;

    void setClass(String teNo, List<String> clCodeList);

    void delClass(String teNo, String clCodeList);

    void enable(String teNo, String enableType, String reason) throws Exception;

    void del(String teNo);

    Teacher load(String teNo);

    PageResult pageList(int pageIndex, int pageSize, String scCode,String isdelete,String name);

    void importTeacher(String fileName, String sheetName);

    ReturnT<Object> importMsg();

//    //获取班级列表
    List<ClassInfo> getClassList(String teNo);

    //获取班级列表
    List<Map<String, Object>>  getClassAndInfoList(String teNo);

    List<UnitTree> getUnitTree(String teNo);

    //获取所有学生
    List<Student> getStuListByTeNo(String teNo);

    //根据学校获取该学校所有的教师
    PageResult getTeNoListByScCode(int pageIndex, int pageSize, String scCode,String isdelete);

    /**
     *
     * @param teNo 教师号
     * @param password 密码
     * @param name 姓名
     * @param sex 性别
     * @param idcode 身份证
     * @param phone 手机号
     * @param sccode 学校编码
     * @param enable 是否使用
     */
    void teacherHandle(String teNo,String password,String name,String sex,String idcode,String phone,String sccode,String enable,String delete);

}
