package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.dao.*;
import com.jiapeng.messageplatform.entity.*;
import com.jiapeng.messageplatform.service.ClassService;
import com.jiapeng.messageplatform.utils.Excel;
import jxl.read.biff.BiffException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ClassImpl implements ClassService {
    @Autowired
    ClassMapper classMapper;
    @Autowired
    GradeMapper gradeMapper;
    @Autowired
    StudentMapper  studentMapper;
    @Autowired
    ClassTeacherMapper classTeacherMapper;
    @Autowired
    SchoolMapper schoolMapper;
    @Autowired
    TeacherWxInfoMapper teacherWxInfoMapper;




    private int initId=10000000;

    @Override
    public void add(ClassEntity entity) {
        String lastId = classMapper.getLastId();
        if (lastId == null) {
            lastId = String.valueOf(initId);
        } else {
            lastId = String.valueOf(Integer.valueOf(lastId) + 1);
        }
        entity.setCl_code(lastId);
        entity.setUuidStr(UUID.randomUUID().toString());
        classMapper.insert(entity);
    }

    @Override
    public void update(ClassEntity entity) {
        classMapper.update(entity);
    }

    @Override
    public void del(String cl_code) throws Exception{

        //查看该班级是否有学生
        List<Student>  list = studentMapper.listByClCode(cl_code);
        if(list.size()!=0){
            //说明该班级有学生
            throw new Exception("请先删除该班级的学生！");
        }
        classTeacherMapper.delClassTeacherByClCode(cl_code);
        teacherWxInfoMapper.deleteByClCode(cl_code);
        classMapper.del(cl_code);
    }

    @Override
    public List<Map<String,Object>> list(String sc_code, String gr_code,int limit,int page) {
        return classMapper.list(sc_code,gr_code,limit,page);
    }

    @Override
    public List<String> listByCodeAndTeNo(String sc_code, String gr_code,String teNo) {
        return classMapper.listByCodeAndTeNo(sc_code,gr_code,teNo);
    }



    @Override
    public List<String> imp(String sc_code,String file) throws IOException, BiffException {
        Excel excel = new Excel();
        List<Map<String,Object>> list = excel.getDataMap(file,null);
        List<String> errList = new ArrayList<>();
        for (Map<String, Object> row :
                list) {
            String gradeName = row.get("年级").toString();
            String cl_number = row.get("班级编码").toString();
            String name = row.get("班级名称").toString();
            GradeEntity gradeEntity = gradeMapper.getByName(gradeName,sc_code);
            if(gradeEntity==null){
                errList.add(String.format("年级记录 %s 不存在，请在年级管理中添加",gradeName));
            }else{
                ClassEntity classEntity  = classMapper.getByName(sc_code,name);
                if(classEntity==null){
                    classEntity = new ClassEntity();
                    classEntity.setSc_code(sc_code);
                    classEntity.setGr_code(gradeEntity.getGr_code());
                    classEntity.setCl_number(cl_number);
                    classEntity.setName(name);
                    add(classEntity);
                }
            }
        }
        return errList;
    }

    @Override
    public void classHandle(String sc_code, String gr_uuidStr, String cl_uuidStr, String name, String delete) throws Exception{

        if(delete.equals("1")){
            classMapper.deleteClassByUUID(cl_uuidStr);
            return;
        }

        SchoolEntity dbSchool = schoolMapper.load(sc_code);
        if(dbSchool==null){throw new Exception("没有该学校！");}


        GradeEntity dbGrade = gradeMapper.getGradeByUUID(gr_uuidStr);
        if(dbGrade==null){throw new Exception("没有该年级！");}
        String gr_code = dbGrade.getGr_code();


        //根据uuid值获取班级代码
        ClassEntity dbClass = classMapper.getClassByUUID(cl_uuidStr);
        if(dbClass!=null){
            dbClass.setName(name);
            classMapper.update(dbClass);
        }else{
            String lastId = classMapper.getLastId();
            if (lastId == null) {
                lastId = String.valueOf(initId);
            } else {
                lastId = String.valueOf(Integer.valueOf(lastId) + 1);
            }
            ClassEntity classEntity = new ClassEntity();
            classEntity.setCl_code(lastId);
            classEntity.setName(name);
            classEntity.setSc_code(sc_code);
            classEntity.setGr_code(gr_code);
            classEntity.setUuidStr(cl_uuidStr);
            classMapper.insert(classEntity);
        }
    }
}
