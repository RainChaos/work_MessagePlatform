package com.jiapeng.messageplatform.service;

import com.jiapeng.messageplatform.entity.GradeEntity;

import java.util.List;
import java.util.Map;

public interface GradeService {
    void add(GradeEntity entity) throws Exception;
    void update(GradeEntity entity) throws Exception;
    void del(String gr_code)throws Exception;
    List<Map<String,Object>> list(String sc_code);

    /**
     * 指定学校的年级列表
     * @param sc_code
     */
    List<Map<String,Object>> listSchoolGrade(String sc_code);

    GradeEntity getGradeByGrCode(String gr_code);

    GradeEntity getByName(String scCode,String name);

    void gradeHandle(String sc_code,String uuidStr, String name, String delete) throws Exception;

    GradeEntity getByUuidStr(String uuid);

}
