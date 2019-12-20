package com.jiapeng.messageplatform.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jiapeng.messageplatform.dao.GuardianMapper;
import com.jiapeng.messageplatform.dao.StudentMapper;
import com.jiapeng.messageplatform.dao.TeacherMapper;
import com.jiapeng.messageplatform.entity.Guardian;
import com.jiapeng.messageplatform.entity.Leave;
import com.jiapeng.messageplatform.entity.Student;
import com.jiapeng.messageplatform.gate.code.GateRequest;
import com.jiapeng.messageplatform.gate.code.GateResponse;
import com.jiapeng.messageplatform.model.UnitTree;
import com.jiapeng.messageplatform.service.LeaveService;
import com.jiapeng.messageplatform.service.StudentService;
import com.jiapeng.messageplatform.utils.VeDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HZL on 2019/8/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentTest {
    @Autowired
    StudentService studentService;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    GuardianMapper guardianMapper;
    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    LeaveService leaveService;

    @Test
    public void add() {
        try {
            Student student = new Student();
            student.setClCode("001");
            student.setStuNo("test001");
            student.setStuName("小明");
            student.setSex("1");
            student.setIdCode("123456798123165");
            studentService.add(student);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void leasveAdd() {
        try {
            Leave leave = new Leave();
            leave.setTeNo("001");
            leave.setStuNo("007");
            leave.setReason("测试");
//            String startDate="2019-8-25 10:00:00";
//            String endDate="2019-8-27 10:00:00";
            String startDate = "2019-8-27 11:00:00";
            String endDate = "2019-8-28 9:00:00";
            leave.setStartDate(VeDate.strToDateLong(startDate));
            leave.setEndDate(VeDate.strToDateLong(endDate));
            leaveService.add(leave);
            System.out.println("ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void list() {
        try {
//            List<Student> list = studentMapper.listAll();
            System.out.println("25_plyoJfqOIE0BNKVixVvJ8mdKiX3ZmxAgzjsv6C-bI29OFrmKcviz5FU3yciqxE7JiZujZaVVVeVLLOC--O3i_SvOkGc1gLMoy6rIZNA96Z6ad4O4_AzszT-3WupyM0vgUsnlPIgnEMiN8daNTWXfADAKNO".length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void load() {
        try {
            Student student = studentMapper.selectByPrimaryKey("test001");
            List<Guardian> list = student.getGuardianList();
            for (Guardian guardian : list) {
                System.out.println(guardian.getGuaName());
            }
            System.out.println(student);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void pageList() {
        try {
//            List<Student> list = studentMapper.list(-1, 0, null, null, null);
//            Integer count = studentMapper.listCount(null, null, null);
//            System.out.println(list.size());
//            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void guardianLoadByStuNo() {
        try {
//            Guardian guardian = guardianMapper.find("test001", null);
//            System.out.println(guardian);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getTteacherClass() {
        try {
            List<Map<String, Object>> list = teacherMapper.getClassAndInfoList("xie");
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
            System.out.println(unitList);
        } catch (Exception e) {
            e.printStackTrace();
        }
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


}
