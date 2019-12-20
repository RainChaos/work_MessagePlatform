package com.jiapeng.messageplatform.service;

import com.jiapeng.messageplatform.entity.Guardian;
import com.jiapeng.messageplatform.entity.Student;
import com.jiapeng.messageplatform.utils.PageResult;
import com.jiapeng.messageplatform.utils.ReturnT;

import java.util.List;

/**
 * Created by HZL on 2019/8/2.
 */
public interface StudentService {
    void add(Student student) throws Exception;

    void update(Student student);

    void del(String stuId);

    Student load(String stuId) throws Exception;

    Student loadByScCodeAndStuNo(String scCode, String stuNo) throws Exception;

    /**
     * @param pageIndex  起始页
     * @param pageSize   每页大小
     * @param clCodeList 教师拥有的班级代码集合
     * @param unitCode   选择要查找的单位代码
     * @param stuName
     * @param isBind
     * @return
     */
    PageResult list(int pageIndex, int pageSize, List<String> clCodeList, String unitCode, String stuName,String isBind);


    PageResult listStudent(int pageIndex, int pageSize, List<String> clCodeList, String unitCode, String stuName,String isBind);

    ReturnT<Object> isBingList(String sc_code,String unitName,List<String> clCodeList, String unitCode,String isBind) throws Exception;

    void changeClass(String stuNoStr, String targrtClCode);

    void importStudent(String fileName, String sheetName);

    ReturnT<Object> importMsg();

    List<Guardian> getGuardianList(String stuId);

    void setGuardian(Guardian guardian);

    void delGuardian(Integer id, String gname);

    /**
     * created by lc 同步学生
     * @param sc_code
     * @param stuNo
     * @param stuName
     * @param sex
     * @param idCode
     * @param cl_uuidStr
     * @param delete
     */
    void studentHandle(String sc_code,String stuNo,String stuName,String sex,String idCode,String cl_uuidStr,String delete);


    /**
     * 根据学校编码获取学生
     * @param scCode
     * @return
     */
    List<Student> getStudentByScCode(String scCode);


}
