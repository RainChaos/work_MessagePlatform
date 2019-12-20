package com.jiapeng.messageplatform.service;

import com.jiapeng.messageplatform.entity.ModuleEntity;

import java.util.List;
import java.util.Map;

public interface ModuleService {
    String add(ModuleEntity entity);

    void update(ModuleEntity entity);

    void del(String id);

    ModuleEntity getModuleById(String id);


    List<Map<String, Object>> getAll();


    //谢江鹏
    List<ModuleEntity> getAllList(String uid);


    List<Map<String, Object>> getTree(List<ModuleEntity> list,
                                      Map<String, Object> parentMap,
                                      List<Map<String, Object>> resultList);
}
