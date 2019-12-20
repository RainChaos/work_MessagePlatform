package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.Student;
import com.jiapeng.messageplatform.entity.Teacher;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface TeacherMapper {
    int deleteByPrimaryKey(String teNo);

    int insert(Teacher record);

    int insertSelective(Teacher record);

    Teacher selectByPrimaryKey(String teNo);

    int updateByPrimaryKeySelective(Teacher record);

    int updateByPrimaryKey(Teacher record);

    Teacher findOneByLoginName(String loginName);

    Teacher selectByWxNo(String wxNo);

    @Update("update teacher set password=#{password} where te_no=#{teNo} ")
    void updatePwd(@Param("teNo") String teNo, @Param("password") String password);

    List<Teacher> pageList(@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize, @Param("scCode") String scCode,@Param("isdelete") String isdelete,@Param("name") String name);

    Integer pageListCount(@Param("scCode") String scCode,@Param("isdelete") String isdelete,@Param("name") String name);

    //
    @Select("select t_info.id, s.sc_code,g.gr_code,c.cl_code,ct.te_no,tea.phoneNo,s.name as schoolName,g.name as gradeName,c.name as className,ct.isheadmaster,t_info.phoneNo as t_wx_phone, t_info.openId \n" +
            "from class_teacher ct \n" +
            "left join class c on ct.cl_code= c.cl_code \n" +
            "left join grade g on g.gr_code= c.gr_code \n" +
            "left join school s on s.sc_code=c.sc_code \n" +
            "LEFT JOIN teacher_wx_info t_info ON t_info.teNo=ct.te_no\n" +
            "left join teacher tea on tea.te_no=ct.te_no  \n" +
            "where ct.te_no in (#{teNo}) \n" +
            "order by sc_code,gr_code,cl_code asc")
    List<Map<String, Object>> getClassAndInfoList(@Param("teNo") String teNo);

    List<Teacher> getTeacherByScCode(@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize, @Param("scCode") String scCode,@Param("isdelete") String isdelete,@Param("name") String name);


    /**
     * 获取老师所在班级的学生
     * @param teNo
     * @return
     */
    List<Student> getMyStudent(String teNo);
}
