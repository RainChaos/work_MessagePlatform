package com.jiapeng.messageplatform.service;

import com.jiapeng.messageplatform.entity.Student;
import com.jiapeng.messageplatform.entity.Teacher;

/**
 * 将数据发送到门户
 */
public interface DataSyncService {
    /**
     * 同步学生到门户
     * @author lc
     * @param student
     * @param delete 1表示删除
     */
    void studentSync(Student student ,String delete);

    void studentOneKeySync(Student student,String menhyKey);
    /**
     * 同步学生删除到门户
     * @author lc
     * @param stuId
     */
    void  studentDelSync(String stuId);
    /**
     * 同步老师添加/修改到门户
     * @author lc
     * @param teacher
     */
    void teacherSync(Teacher teacher,String delete);
    /**
     * 同步老师删除到门户
     * @author lc
     * @param teNo
     */
    void teacherDelSync(String teNo);

    /**
     * 同步组织结构到门户
     * @author lc
     * @param o
     */
    void organizeSync(Object o ,String delete);

    /**
     * 同步组织结构删除到门户
     * @author lc
     * @param code
     */
    void organizeDelSync(String code,String type) throws Exception;
}
