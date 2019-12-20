package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.dao.ModuleMapper;
import com.jiapeng.messageplatform.dao.RoleMapper;
import com.jiapeng.messageplatform.dao.UserMapper;
import com.jiapeng.messageplatform.entity.ModuleEntity;
import com.jiapeng.messageplatform.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ModuleImpl implements ModuleService {
    @Autowired
    ModuleMapper moduleMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    UserMapper userMapper;

    private int initId=100;

    @Override
    public String add(ModuleEntity entity) {
        String lastId = moduleMapper.getLastId(entity.getParentId());
        if(entity.getParentId()==null){
            if(lastId==null){
                lastId=String.valueOf(initId);
            }else{
                lastId=String.valueOf(Integer.valueOf(lastId)+1);
            }
        }else{
            if(lastId==null){
                lastId=entity.getParentId()+String.valueOf(initId);
            }else{
                lastId=String.valueOf(Integer.valueOf(lastId)+1);
            }
        }
        entity.setId(lastId);
        moduleMapper.insert(entity);
        return lastId;
    }

    @Override
    public void update(ModuleEntity entity) {
        moduleMapper.update(entity);
    }

    @Override
    public void del(String id) {
        moduleMapper.del(id);
    }

    @Override
    public List<Map<String, Object>> getAll() {
        List<ModuleEntity> list = moduleMapper.getAll();
        List<Map<String,Object>> resultList = getTree(list, null, null);

        return resultList;
    }

    //by谢江鹏
    @Override
    public List<ModuleEntity> getAllList(String rid) {
        //获取所有的菜单列表
        List<ModuleEntity> allMenus = moduleMapper.getAll();
            //根据角色id获取所有的菜单列表
        if (!"".equals(rid)&&rid!=null){
            List<ModuleEntity> userMenu = roleMapper.getModuleByRid(rid);
            for (ModuleEntity allmenu : allMenus) {
                for (ModuleEntity usermenu : userMenu) {
                    if (usermenu.getId().equals(allmenu.getId())) {
                        allmenu.setChecked("true");
                    }
                }
            }


        }

        return allMenus;
    }





    @Override
    public ModuleEntity getModuleById(String id) {
        return moduleMapper.getModuleById(id);
    }


    public List<Map<String,Object>> getTree(List<ModuleEntity> list,Map<String,Object> parentMap,List<Map<String,Object>> resultList){
        String parentId = null;
        if(parentMap==null){
            resultList=new ArrayList<Map<String,Object>>();
        }else{
            parentId = parentMap.get("id").toString();
        }
        final String tmparentid = parentId;
        List<ModuleEntity> childsList = list.stream()
                .filter(p->{
                    boolean have= p.getParentId()==null?tmparentid==null:
                            p.getParentId().equals(tmparentid);
                    return have;
                })
                .collect(Collectors.toList());

        for (ModuleEntity itemEntity : childsList) {
            Map<String,Object> itemMap = itemEntity.toMap();
            itemMap.put("children", new ArrayList<Map<String,Object>>());

            final String itemid = itemEntity.getId();
            List<ModuleEntity> tmplist = list.stream()
                    .filter(f->f.getParentId()==null?false:f.getParentId().equals(itemid))
                    .collect(Collectors.toList());
            if(tmplist.size()>0){
                getTree(list, itemMap, resultList);
            }
            if(parentId==null){
                resultList.add(itemMap);
            }else{
                ((List<Map<String,Object>>)parentMap.get("children")).add(itemMap);
            }
        }
        return resultList;
    }
}
