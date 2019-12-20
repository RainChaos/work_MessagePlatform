package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.dao.RoleMapper;
import com.jiapeng.messageplatform.entity.ModuleEntity;
import com.jiapeng.messageplatform.entity.RoleEntity;
import com.jiapeng.messageplatform.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoleImpl implements RoleService {
    @Autowired
    RoleMapper roleMapper;

    private final int initId = 100;
    @Override
    public String add(RoleEntity entity) {
        String lastId = roleMapper.getLastId();
        if (lastId == null) {
            lastId = String.valueOf(initId);
        } else {
            lastId = String.valueOf(Integer.valueOf(lastId) + 1);
        }
        entity.setId(lastId);
        roleMapper.insert(entity);
        return lastId;
    }

    @Override
    public void update(RoleEntity entity) {
        roleMapper.update(entity);
    }

    @Override
    public void del(String id) {
        roleMapper.del(id);
    }

    @Override
    public List<Map<String, Object>> getAll() {
        return roleMapper.getAll();
    }

    @Override
    public List<Map<String, Object>> getModules(String rid) {

        roleMapper.getModule(rid);
        return roleMapper.getModule(rid);
    }

    @Override
    public void setModules(String rid, String[] mids) {
        roleMapper.clearModule(rid);
        for (String mid :
                mids) {
            roleMapper.setModule(rid,mid);
        }
    }


}
