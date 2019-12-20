package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.dao.*;
import com.jiapeng.messageplatform.entity.*;
import com.jiapeng.messageplatform.service.DataSyncService;
import com.jiapeng.messageplatform.service.SchoolService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DataSyncServiceImpl implements DataSyncService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSyncServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private SchoolMapper schoolMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private GradeMapper gradeMapper;

    @Autowired
    private ClassMapper classMapper;
    @Override
    @Async//异步调用
    public void studentSync(Student student,String delete) {
        //查询学生属于哪个学校
        SchoolEntity schoolEntity = schoolMapper.selectByClassCode(student.getClCode());
        //获取学校门户地址
        String menhukey=schoolEntity.getMenhuKey();
        //接口调用
        String name = student.getStuName();
        String studentid = student.getStuNo();
        String clCode = student.getClCode();
        ClassEntity classEntity=classMapper.getClassByClCode(clCode);
        String unitid = classEntity.getUuidStr();
        String sex = student.getSex()=="0"?"2":"1";
        String idcard = student.getIdCode();
        String data ="{\"topic\":\"student\",\"data\":{\"name\":\""+name+"\",\"studentid\":\""+ studentid+
                "\",\"unitid\":\""+unitid+"\",\"sex\":\""+sex+
                "\",\"idcard\":\""+idcard+"\",\"delete\":\""+delete+"\"}}";
        HttpHeaders headers = new HttpHeaders();
        //设置类型
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        HttpEntity<String> formEntity = new HttpEntity<String>(data, headers);
        try {
            restTemplate.postForObject(menhukey+"/DataCenter/sendMessage", formEntity, String.class);
        } catch (Exception e) {
            LOGGER.error("同步数据到{}出错：{}", schoolEntity.getName(), data);
        }
    }

    @Override
    public void studentOneKeySync(Student student,String menhuKey){
        //接口调用
        String name = student.getStuName();
        String studentid = student.getStuNo();
        String clCode = student.getClCode();
        ClassEntity classEntity=classMapper.getClassByClCode(clCode);
        String unitid = classEntity.getUuidStr();
        String sex = student.getSex()=="0"?"2":"1";
        String idcard = student.getIdCode();
        String data ="{\"topic\":\"student\",\"data\":{\"name\":\""+name+"\",\"studentid\":\""+ studentid+
                "\",\"unitid\":\""+unitid+"\",\"sex\":\""+sex+
                "\",\"idcard\":\""+idcard+"\",\"delete\":\"0\"}}";
        HttpHeaders headers = new HttpHeaders();
        //设置类型
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        HttpEntity<String> formEntity = new HttpEntity<String>(data, headers);
        try {
            restTemplate.postForObject(menhuKey+"/DataCenter/sendMessage", formEntity, String.class);
        } catch (Exception e) {
            LOGGER.error("同步数据到{}出错：{}", menhuKey, data);
        }
    }

    @Override
    public void studentDelSync(String stuid){
        Student student = studentMapper.selectByPrimaryKey(stuid);
        //查询学生属于哪个学校
        SchoolEntity schoolEntity = schoolMapper.selectByClassCode(student.getClCode());
        //获取学校门户地址
        String menhukey=schoolEntity.getMenhuKey();
        String data ="{\"topic\":\"student\",\"data\":{\"studentid\":\""+ student.getStuNo()+
                "\",\"delete\":\"1\"}}";
        HttpHeaders headers = new HttpHeaders();
        //设置类型
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        HttpEntity<String> formEntity = new HttpEntity<String>(data, headers);
        try {
            restTemplate.postForObject(menhukey+"/DataCenter/sendMessage", formEntity, String.class);
        } catch (Exception e) {
            LOGGER.error("同步数据到{}出错：{}", schoolEntity.getName(), data);
        }
    }
    @Override
    @Async
    public void teacherSync(Teacher teacher,String delete) {
        String scCode = teacher.getScCode();
        if (scCode == null) {
            scCode=teacherMapper.selectByPrimaryKey(teacher.getTeNo()).getScCode();
        }
        //获取学校门户地址
        SchoolEntity byScCode = schoolMapper.load(scCode);
        String idcard = teacher.getIdcode();
        String sex = teacher.getSex()=="0"?"2":"1";
        String phone = teacher.getPhoneno();
        String teacherid = teacher.getTeNo();
        String isuse = teacher.getEnable();
        String name = teacher.getName();
        String data ="{\"topic\":\"teacher\",\"data\":{\"name\":\""+name+"\",\"teacherid\":\""+ teacherid+
                "\",\"sex\":\""+sex+"\",\"idcard\":\""+idcard+
                "\",\"phone\":\""+phone+
                "\",\"used\":\""+isuse+"\",\"delete\":\""+delete+"\"}}";
        HttpHeaders headers = new HttpHeaders();
        //设置类型
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        HttpEntity<String> formEntity = new HttpEntity<String>(data, headers);
        try {
            restTemplate.postForObject(byScCode.getMenhuKey()+"/DataCenter/sendMessage", formEntity, String.class);
        } catch (Exception e) {
            LOGGER.error("同步数据到{}出错：{}", byScCode.getName(), data);
        }
    }

    @Override
    public void teacherDelSync(String teNo) {
        Teacher teacher =teacherMapper.selectByPrimaryKey(teNo);
        //获取学校门户地址
        SchoolEntity byScCode = schoolMapper.load(teacher.getScCode());
        String data ="{\"topic\":\"teacher\",\"data\":{\"teacherid\":\""+teNo+"\",\"delete\":\"1\"}}";
        HttpHeaders headers = new HttpHeaders();
        //设置类型
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        HttpEntity<String> formEntity = new HttpEntity<String>(data, headers);
        try {
            restTemplate.postForObject(byScCode.getMenhuKey()+"/DataCenter/sendMessage", formEntity, String.class);
        } catch (Exception e) {
            LOGGER.error("同步数据到{}出错：{}", byScCode.getName(), data);
        }
    }

    @Override
    @Async
    public void organizeSync(Object o,String delete) {
        //班级
        if (o instanceof ClassEntity){
            ClassEntity classEntity = (ClassEntity) o;
            String name =classEntity.getName();
            String cl_code = classEntity.getCl_code();
            String gr_code = classEntity.getGr_code();
            String type = "6";
            SchoolEntity schoolEntity = schoolMapper.selectByClassCode(cl_code);
            GradeEntity gradeEntity = gradeMapper.getGradeByGrCode(gr_code);
            String menhuKey = schoolEntity.getMenhuKey();
            String punitid = gradeEntity.getUuidStr();
            String unitid = classEntity.getUuidStr()==null?classMapper.getClassByClCode(cl_code).getUuidStr():classEntity.getUuidStr();
            String data ="{\"topic\":\"organize\",\"data\":{\"name\":\""+name+"\",\"unitid\":\""+unitid+
                    "\",\"punitid\":\""+punitid+"\",\"type\":\""+type+"\",\"delete\":\""+delete+"\"}}";
            HttpHeaders headers = new HttpHeaders();
            //设置类型
            MediaType mediaType = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(mediaType);
            HttpEntity<String> formEntity = new HttpEntity<String>(data, headers);
            try {
                restTemplate.postForObject(menhuKey+"/DataCenter/sendMessage", formEntity, String.class);
            } catch (Exception e) {
                LOGGER.error("同步数据到{}出错：{}", schoolEntity.getName(), data);
            }

        }
        //年级
        if (o instanceof GradeEntity){
            GradeEntity gradeEntity = (GradeEntity) o;
            String name =gradeEntity.getName();
            String gr_code = gradeEntity.getGr_code();
            SchoolEntity schoolEntity = null;
            String unitid = gradeEntity.getUuidStr();
            if (gradeEntity.getSc_code()==null){
                GradeEntity exit = gradeMapper.getGradeByGrCode(gr_code);
                schoolEntity=schoolMapper.load(exit.getSc_code());
                unitid=exit.getUuidStr();
            }else {
                schoolEntity = schoolMapper.load(gradeEntity.getSc_code());
            }
            String menhuKey = schoolEntity.getMenhuKey();
            String type = "5";

            String punitid = schoolEntity.getUuidStr();
            String data ="{\"topic\":\"organize\",\"data\":{\"name\":\""+name+"\",\"unitid\":\""+unitid+
                    "\",\"punitid\":\""+punitid+"\",\"type\":\""+type+"\",\"delete\":\""+delete+"\"}}";
            HttpHeaders headers = new HttpHeaders();
            //设置类型
            MediaType mediaType = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(mediaType);
            HttpEntity<String> formEntity = new HttpEntity<String>(data, headers);
            try {
                restTemplate.postForObject(menhuKey+"/DataCenter/sendMessage", formEntity, String.class);
            } catch (Exception e) {
                LOGGER.error("同步数据到{}出错：{}",schoolEntity.getName(), data);
            }
        }

        //学校
        if (o instanceof SchoolEntity){
            SchoolEntity schoolEntity = (SchoolEntity) o;
            String menhuKey = schoolEntity.getMenhuKey();
            String name =schoolEntity.getName();
            String unitid = schoolEntity.getUuidStr();
            String type = "1";
            String data ="{\"topic\":\"organize\",\"data\":{\"name\":\""+name+"\",\"unitid\":\""+unitid+
                    "\",\"type\":\""+type+"\",\"delete\":\""+delete+"\"}}";
            HttpHeaders headers = new HttpHeaders();
            //设置类型
            MediaType mediaType = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(mediaType);
            HttpEntity<String> formEntity = new HttpEntity<String>(data, headers);
            try {
                restTemplate.postForObject(menhuKey+"/DataCenter/sendMessage", formEntity, String.class);
            } catch (Exception e) {
                LOGGER.error("同步数据到{}出错：{}", schoolEntity.getName(), data);
            }

        }
    }

    @Override
    public void organizeDelSync(String code,String type) throws Exception {
        //学校
        String menhuKey="";
        String unitid="";
        if ("1".equals(type)){
            SchoolEntity schoolEntity = schoolMapper.load(code);
             menhuKey = schoolEntity.getMenhuKey();
             unitid = schoolEntity.getUuidStr();
        }

        if ("5".equals(type)){
            //先查看与之相关的表是否存在记录
            //班级表
            ClassEntity classEntity = classMapper.findByGradeCode(code);
            if(classEntity!=null){
                throw new Exception("请先删除与该年级相关联的班级！");
            }
            GradeEntity gradeEntity = gradeMapper.getGradeByGrCode(code);
            SchoolEntity schoolEntity = schoolMapper.load(gradeEntity.getSc_code());
             menhuKey = schoolEntity.getMenhuKey();
            unitid = gradeEntity.getUuidStr();
        }

        if ("6".equals(type)){
            ClassEntity classEntity = classMapper.getClassByClCode(code);
            SchoolEntity schoolEntity = schoolMapper.selectByClassCode(code);
             menhuKey = schoolEntity.getMenhuKey();
            unitid =classEntity.getUuidStr();
        }
        String data ="{\"topic\":\"organize\",\"data\":{\"unitid\":\""+ unitid+
                "\",\"delete\":\"1\"}}";
        HttpHeaders headers = new HttpHeaders();
        //设置类型
        MediaType mediaType = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(mediaType);
        HttpEntity<String> formEntity = new HttpEntity<String>(data, headers);
        try {
            restTemplate.postForObject(menhuKey+"/DataCenter/sendMessage", formEntity, String.class);
        } catch (Exception e) {
            LOGGER.error("同步数据到{}出错：{}", "", data);
        }
    }
}
