package com.jiapeng.messageplatform.service;

import com.jiapeng.messageplatform.entity.ModuleEntity;
import com.jiapeng.messageplatform.entity.RoleEntity;

import java.util.List;
import java.util.Map;

public interface RoleService {
    String add(RoleEntity entity);
    void update(RoleEntity entity);
    void del(String id);
    List<Map<String,Object>> getAll();
    List<Map<String,Object>> getModules(String rid);
    void setModules(String rid,String[] mids);
}
