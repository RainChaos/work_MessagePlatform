package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.Student;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface StudentMapper {
    int deleteByPrimaryKey(String stuId);

    int insert(Student record);

    int insertSelective(Student record);

    Student selectByPrimaryKey(String stuId);

    Student selectByScCodeAndStuNo(@Param("scCode")String scCode,@Param("stuNo")String stuNo);

    @Select("select sch.name as schoolName,cla.name as className,stu.* from student stu\n" +
            "left join class cla on cla.cl_code = stu.cl_code\n" +
            "left join school sch on sch.sc_code = cla.sc_code" +
            " where stuId = #{stuId}")
    Student selectBystuId(@Param("stuId")String stuId);


    @Delete("delect * from student where cl_code = #{cl_code} ")
    void delByClCode(@Param("cl_code")String cl_code);


//    //根据身份证号查找学生
//    @Select("select * from student where idCode = #{idCode} ")
//    Student selectByIdCode(@Param("idCode")String idCode);

    //根据班级代码和学生编号查找学生
    @Select("select * from student where cl_code = #{clCode} and stuNo = #{stuNo}")
    Student selectByClCodeAndStuNo(@Param("clCode")String clCode,@Param("stuNo")String stuNo);

    @Select("select * from student s " +
            "left join class cla on cla.cl_code = s.cl_code " +
            "where stuNo = #{stuNo} and stuName = #{stuName} and cla.sc_code = #{scCode} ")
    Student findByScCodeAndStuNo(@Param("scCode")String scCode,@Param("stuNo")String stuNo,@Param("stuName")String stuName);

    int updateByPrimaryKeySelective(Student record);

    int updateByPrimaryKey(Student record);

//    @Select("select * from student")
//    List<Student> listAll();

    @Select("select * from student where cl_code = #{clCode}")
    List<Student> listByClCode(String clCode);


    List<Student> list(@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize, @Param("clCodeList") List<String> clCodeList, @Param("changeUnitCode") String changeUnitCode, @Param("stuName") String stuName,@Param("isBind") String isBind);

    List<Student> listStudent(@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize, @Param("clCodeList") List<String> clCodeList, @Param("changeUnitCode") String changeUnitCode, @Param("stuName") String stuName,@Param("isBind") String isBind);


    int listStudentCount(@Param("clCodeList") List<String> clCodeList,@Param("changeUnitCode") String changeUnitCode, @Param("stuName") String stuName,@Param("isBind") String isBind);

    List<Student> binglist( @Param("sc_code")String sc_code, @Param("clCodeList") List<String> clCodeList, @Param("changeUnitCode") String changeUnitCode,@Param("isBind")String isBind);

    int listCount(@Param("clCodeList") List<String> clCodeList,@Param("changeUnitCode") String changeUnitCode, @Param("stuName") String stuName,@Param("isBind") String isBind);

    @Update("update student set cl_code = #{clCode} where stuNo = #{clCode}")
    void updateClCode(String stuNo, String clCode);

    @Select("SELECT stu.* from student stu \n" +
            "left join class_teacher ct on ct.cl_code = stu.cl_code\n" +
            "where ct.te_no = #{teNo}")
    List<Student> getStuListByTeNo(String teNo);


    List<Student> listByStuId(@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize,@Param("stuIdsList") List<String> stuIdsList);



    /**
     * 更新学生人脸图片语句
     * by hzl 2019-10-22 11:32:41
     * @param scCode
     * @param stuNo
     * @param imgPath
     */
    @Update("update student set faceImage1 = #{imgPath} from student,class,school " +
            " where stuNo = #{stuNo} and school.sc_code = #{scCode}" +
            " and student.cl_code=class.cl_code and class.sc_code=school.sc_code  ")
    void UpdateImgPath(@Param("scCode") String scCode, @Param("stuNo") String stuNo, @Param("imgPath") String imgPath);

    /**
     *
     * @param scCode
     * @return
     */
    List<Student> selectByScCode(String scCode);
}
