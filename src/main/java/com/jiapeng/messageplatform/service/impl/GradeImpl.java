package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.dao.ClassMapper;
import com.jiapeng.messageplatform.dao.GradeMapper;
import com.jiapeng.messageplatform.dao.SchoolMapper;
import com.jiapeng.messageplatform.entity.ClassEntity;
import com.jiapeng.messageplatform.entity.ClassInfo;
import com.jiapeng.messageplatform.entity.GradeEntity;
import com.jiapeng.messageplatform.entity.SchoolEntity;
import com.jiapeng.messageplatform.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class GradeImpl implements GradeService {
    @Autowired
    GradeMapper gradeMapper;
    @Autowired
    ClassMapper classMapper;

    @Autowired
    SchoolMapper schoolMapper;

    private int initId=100000;
    @Override
    public void add(GradeEntity entity) throws Exception {
        String lastId = gradeMapper.getLastId();
        if (lastId == null) {
            lastId = String.valueOf(initId);
        } else {
            lastId = String.valueOf(Integer.valueOf(lastId) + 1);
        }
        entity.setGr_code(lastId);
        entity.setUuidStr(UUID.randomUUID().toString());

        try {
            gradeMapper.insert(entity);
        }catch (Exception e){
            throw  new Exception("该年级名称已经存在");
    }

    }

    @Override
    public void update(GradeEntity entity) throws Exception {
        try{
            gradeMapper.update(entity);
        }catch (Exception e){
            throw  new Exception("该年级名称已经存在");
        }

    }

    @Override
    public void del(String gr_code) throws Exception{

        //先查看与之相关的表是否存在记录
        //班级表
       ClassEntity classEntity = classMapper.findByGradeCode(gr_code);
        if(classEntity!=null){
            throw new Exception("请先删除与该年级相关联的班级！");
        }
        gradeMapper.del(gr_code);
    }

    @Override
    public List<Map<String,Object>> list(String sc_code) {
        return gradeMapper.list(sc_code);
    }

    @Override
    public List<Map<String,Object>> listSchoolGrade(String sc_code) {
        return gradeMapper.listSchoolGrade(sc_code);
    }

    @Override
    public GradeEntity getGradeByGrCode(String gr_code) {
        return gradeMapper.getGradeByGrCode(gr_code);
    }

    @Override
    public GradeEntity getByName(String scCode,String name){
        return gradeMapper.getByName(name,scCode);
    }

    @Override
    public void gradeHandle(String sc_code,String uuidStr, String name, String delete) throws Exception {

        if(delete.equals("1")){
            gradeMapper.deleteGradeByUUID(uuidStr);
            return;
        }

        SchoolEntity dbSchool = schoolMapper.load(sc_code);
        if(dbSchool==null){throw new Exception("没有该学校！");}

        //根据uuid值获取年级代码
        GradeEntity dbGrade = gradeMapper.getGradeByUUID(uuidStr);
        if(dbGrade!=null){
            dbGrade.setSc_code(sc_code);
            dbGrade.setName(name);
            gradeMapper.update(dbGrade);
        }else{
            Integer lastOrderNumber = gradeMapper.getLastOrderNumber();
            String lastId = gradeMapper.getLastId();
            if (lastId == null) {
                lastId = String.valueOf(initId);
            } else {
                lastId = String.valueOf(Integer.valueOf(lastId) + 1);
            }

            GradeEntity gradeEntity = new GradeEntity();
            gradeEntity.setGr_code(lastId);
            gradeEntity.setName(name);
            gradeEntity.setSc_code(sc_code);
            gradeEntity.setUuidStr(uuidStr);
            gradeEntity.setOrderNumber(lastOrderNumber + 1);
            gradeMapper.insert(gradeEntity);
        }
    }

    @Override
    public GradeEntity getByUuidStr(String uuid) {
        return gradeMapper.getGradeByUUID(uuid);
    }
}
