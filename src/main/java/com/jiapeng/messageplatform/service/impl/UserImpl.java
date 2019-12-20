package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.dao.UserMapper;
import com.jiapeng.messageplatform.entity.ModuleEntity;
import com.jiapeng.messageplatform.entity.UserEntity;
import com.jiapeng.messageplatform.service.ModuleService;
import com.jiapeng.messageplatform.service.RoleService;
import com.jiapeng.messageplatform.service.UserService;
import com.jiapeng.messageplatform.utils.Constants;
import com.jiapeng.messageplatform.utils.DataGridJson;
import com.jiapeng.messageplatform.utils.Md5Util;
import com.jiapeng.messageplatform.utils.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;
import java.util.List;
import java.util.Map;

@Service
public class UserImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    ModuleService moduleService;



    @Override
    public void add(UserEntity entity) throws Exception {
        if(StringUtils.isBlank(entity.getPassWord())){
            throw new Exception("请输入密码");
        }
        entity.setPassWord(Md5Util.MD5(entity.getPassWord()));
        userMapper.add(entity);
    }

    /**
     * 修改用户名，如果密码不为空，则一起修改密码
     * @param entity
     */
    @Override
    public void update(UserEntity entity) {
        if(StringUtils.isNotBlank(entity.getPassWord())){
            entity.setPassWord(Md5Util.MD5(entity.getPassWord()));
        }
        userMapper.update(entity);
    }

    @Override
    public UserEntity get(int id) {
        UserEntity entity = userMapper.get(id);
        entity.setPassWord("");
        return  entity;
    }

    @Override
    public void del(int id) {
        userMapper.del(id);
    }

    @Override
    public void setDisLog(UserEntity entity) {
        userMapper.setDis(entity);
    }

    @Override
    public DataGridJson listUser(String key, int rows, int page) {
        List<Map<String,Object>> list = userMapper.list(key,rows,page);
        int count =userMapper.count(key);
        for (Map<String,Object> m :
                list) {
            List roles = getRoles(Integer.valueOf( m.get("id").toString()));
            m.put("roles",roles);
        }
        DataGridJson obj = new DataGridJson(count,list);
        return obj;
    }


    @Override
    public void updatePass(String name, String passWord) throws Exception {
        UserEntity entity = userMapper.getByName(name);
        if(entity==null){
            throw new Exception("用户名不存在");
        }
        entity.setPassWord(Md5Util.MD5(passWord));
        userMapper.update(entity);
    }

    @Override
    public void setRoles(int id, String[] rids) {
        userMapper.clearRole(id);
        for (String rid :
                rids) {
            userMapper.setRole(id, rid);
        }
    }

    @Override
    public List<Map<String, Object>> getRoles(int id) {
        return userMapper.getRoles(id);
    }

    @Override
    public UserEntity logon(HttpServletRequest request, String name, String passWord, String verifyCode) throws Exception {
        String sessionVerifyCode = SessionUtil.getVerifyCode(request).toLowerCase();
        if (StringUtils.isBlank(verifyCode.toLowerCase()))
            throw new Exception(Constants.VERIFY_CODE_EMPTY);
        if (!sessionVerifyCode.equals(verifyCode.toLowerCase()))
            throw new Exception(Constants.VERIFY_CODE_ERROR);

        UserEntity entity = userMapper.getByName(name);
        if(entity==null){
            throw new Exception("用户名不存在");
        }
        if(!entity.getPassWord().equals(Md5Util.MD5(passWord))){
            throw new Exception("密码不正确");
        }
        if(entity.isDislog()){
            throw new Exception("已禁止登录");
        }

        return entity;
    }

    @Override
    public List<Map<String, Object>> getModuleTree(int id) {
        List<ModuleEntity> list = userMapper.getModules(id);
        List<Map<String,Object>> tree = moduleService.getTree(list,null,null);
        return tree;
    }






}
