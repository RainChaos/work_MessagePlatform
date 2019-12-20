package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.dao.SchoolDBMapper;
import com.jiapeng.messageplatform.dao.SchoolIpMapper;
import com.jiapeng.messageplatform.dao.SchoolMapper;
import com.jiapeng.messageplatform.dao.SchoolWXMapper;
import com.jiapeng.messageplatform.entity.*;
import com.jiapeng.messageplatform.service.ClassService;
import com.jiapeng.messageplatform.service.GradeService;
import com.jiapeng.messageplatform.service.SchoolService;
import com.jiapeng.messageplatform.service.TeacherService;
import com.jiapeng.messageplatform.utils.DataGridJson;
import com.jiapeng.messageplatform.utils.TokenProccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class SchoolImpl implements SchoolService {
    @Autowired
    SchoolMapper schoolMapper;
    @Autowired
    ClassService classService;
    @Autowired
    GradeService gradeService;
    @Autowired
    TeacherService teacherService;
    @Autowired
    SchoolIpMapper schoolIpMapper;
    @Autowired
    SchoolWXMapper schoolWXMapper;
    @Autowired
    SchoolDBMapper schoolDBMapper;

    private int initId = 100000;

    @Override
    public void add(SchoolEntity entity) {
        String lastId = schoolMapper.getLastId();
        if (lastId == null) {
            lastId = String.valueOf(initId);
        } else {
            lastId = String.valueOf(Integer.valueOf(lastId) + 1);
        }
        if (entity.getMenhuKey() == null) {
            entity.setMenhuKey("");
        }
        entity.setUuidStr(UUID.randomUUID().toString());
        entity.setSc_code(lastId);
        schoolMapper.insert(entity);
    }


    public  void  addSchollDB(SchoolDB entity){

        SchoolDB dbschoolDB = schoolDBMapper.selectByPrimaryKey(entity.getScCode());
        if(dbschoolDB!=null){
            schoolDBMapper.updateByPrimaryKey(entity);
        }else {
            schoolDBMapper.insert(entity);
        }


    };



    @Override
    public void update(SchoolEntity entity) {
        schoolMapper.update(entity);
    }

    @Override
    public void setMenhuKey(SchoolEntity entity) {
        schoolMapper.setMenhuKey(entity);
    }

    @Override
    public void del(String sc_code) throws Exception {
        //删除学校

        //1.班级含有sc_code
        //2.config表含有
        //3.ip表
        //4.学校微信表
        try {
            schoolMapper.del(sc_code);
        } catch (Exception e) {
            throw new Exception("请先删除与该学校相关的数据！");
        }

    }

    @Override
    public DataGridJson list(String scCode, String key, int rows, int page) {
        List list = schoolMapper.list(scCode, key, rows, page);
        int count = schoolMapper.count(scCode, key);
        DataGridJson obj = new DataGridJson(count, list);
        return obj;
    }

    @Override
    public DataGridJson listSchoolDB(int rows, int page) {
        int offset = (page - 1) * rows;
        List list = schoolDBMapper.list(offset, rows);
        int count = schoolDBMapper.listCount();
        DataGridJson obj = new DataGridJson(count, list);
        return obj;
    }





    @Override
    public void setStatus(SchoolEntity entity) {
        schoolMapper.setStatus(entity);
    }

    @Override
    public void updateToken(String sc_code) {
        String token = TokenProccessor.getInstance().makeToken();
        schoolMapper.updateToken(sc_code, token);
    }

    @Override
    public List<Map<String, Object>> listGradeTree(String sc_code, String teNo, String requestType) {
        //sc_code学校代码的所有年级
        List<Map<String, Object>> gradList = gradeService.list(sc_code);


//        if("classHeader".equals(requestType)){
//            //根据老师编号获取该教师所关联的年级
//            gradList = gradeService.listByTeNo(sc_code,teNo);
//        }


        //教师原来拥有的班级
        List<ClassInfo> list = new ArrayList<>();
        if (teNo != null && !teNo.equals("")) {
            list = teacherService.getClassList(teNo);
        }


        for (Map<String, Object> grade :
                gradList) {
            grade.put("id", grade.get("gr_code").toString());
            grade.put("title", grade.get("name").toString());
            //当teNo不为空的时候
            if (teNo != null && !teNo.equals("")) {
                for (int h = 0; h < list.size(); h++) {
                    if ("classHeader".equals(requestType)) {
                        if (list.get(h).getGradeName().equals(grade.get("name").toString())) {
                            grade.put("checked", "true");
                        }
                    } else {
                        if (list.get(h).getGradeName().equals(grade.get("name").toString())) {
                            grade.put("checked", "true");
                        }
                    }
                }
            }

            //获取sc_code学校代码所有年级下的代码
            List<Map<String, Object>> classList = classService.list(sc_code, grade.get("gr_code").toString(), 1000, 1);
            for (Map<String, Object> ban :
                    classList) {
                if (teNo != null && !teNo.equals("")) {
                    for (int h = 0; h < list.size(); h++) {
                        if ("classHeader".equals(requestType)) {
                            if (list.get(h).isClassMaster()) {
                                if (list.get(h).getClassCode().equals(ban.get("cl_code").toString())) {
                                    ban.put("checked", "true");
                                }
                            }
                        } else {
                            if (list.get(h).getClassCode().equals(ban.get("cl_code").toString())) {
                                ban.put("checked", "true");
                            }
                        }
                    }
                }

                ban.put("id", ban.get("cl_code").toString());
                ban.put("title", ban.get("name").toString());
                grade.put("children", classList);
            }


        }
        return gradList;
    }

    // by xie
    public void whiteListCheck(String scCode, String ipAddr) throws Exception {
        SchoolEntity schoolEntity = schoolMapper.load(scCode);
        if (schoolEntity == null) {
            throw new Exception("invalid sc_code");
        }
        SchoolIP ipdata = schoolIpMapper.whiteListCheck(scCode, ipAddr);
        if (ipdata == null) {
            throw new Exception("ip not in school white list");
        }

    }

    // by xie
    public void setIP(String scCode, String ipAddr) {
        schoolIpMapper.del(scCode);
        schoolIpMapper.insert(scCode, ipAddr);
    }

    ;

    public List<SchoolIP> getIPListByScCode(String scCode) {
        return schoolIpMapper.getIPListByScCode(scCode);
    }

    @Override
    public void addSchoolWx(SchoolWX schoolWX) throws Exception {
        SchoolWX dbSchoolWX = schoolWXMapper.selectByPrimaryKey(schoolWX.getScCode());
        if (null == dbSchoolWX) {
            schoolWXMapper.insertSelective(schoolWX);
        } else {
            throw new Exception("不能添加重复的scCode");
        }
    }

    @Transactional
    @Override
    public void updateSchoolWx(SchoolWX schoolWX) throws Exception {
        schoolWXMapper.deleteByPrimaryKey(schoolWX.getScCode());
        schoolWXMapper.insertSelective(schoolWX);
    }

    @Override
    public SchoolWX loadSchoolWX(String scCode) throws Exception {
        SchoolWX schoolWX = schoolWXMapper.selectByPrimaryKey(scCode);
        if (null == schoolWX) {
            throw new Exception("未找到SchoolWX相关数据");
        }
        return schoolWX;
    }

    @Override
    public void schoolHandle(String sc_code, String name, String delete) {

        if (delete.equals("1")) {
            schoolMapper.del(sc_code);
            return;
        }

        //根据uuid值获取学校代码
        SchoolEntity dbSchool = schoolMapper.load(sc_code);

        if (dbSchool != null) {
            dbSchool.setSc_code(sc_code);
            dbSchool.setName(name);
            schoolMapper.update(dbSchool);
        } else {
            //添加
            String lastId = schoolMapper.getLastId();
            if (lastId == null) {
                lastId = String.valueOf(initId);
            } else {
                lastId = String.valueOf(Integer.valueOf(lastId) + 1);
            }
            SchoolEntity schoolEntity = new SchoolEntity();
            schoolEntity.setSc_code(lastId);
            schoolEntity.setUuidStr(sc_code);
            schoolEntity.setName(name);
            schoolMapper.insert(schoolEntity);
        }
    }

    @Transactional
    @Override
    public void updateSchoolDB(SchoolDB schoolDB) throws Exception {
        schoolDBMapper.deleteByPrimaryKey(schoolDB.getScCode());
        schoolDBMapper.insertSelective(schoolDB);
    }

    @Override
    public SchoolDB loadSchoolDB(String scCode) throws Exception {
        SchoolDB schoolDB = schoolDBMapper.selectByPrimaryKey(scCode);
        return schoolDB;
    }

    @Override
    public SchoolEntity getByScCode(String scCode) {
        return schoolMapper.load(scCode);
    }
}
