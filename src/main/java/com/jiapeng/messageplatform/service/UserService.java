package com.jiapeng.messageplatform.service;

import com.jiapeng.messageplatform.entity.ModuleEntity;
import com.jiapeng.messageplatform.entity.UserEntity;
import com.jiapeng.messageplatform.utils.DataGridJson;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface UserService {
    /**
     * 添加用户
     * 目前只添加用户名和密码
     *
     * @param entity
     * @throws Exception
     */
    void add(UserEntity entity) throws Exception;

    /**
     * 修改用户信息，如果密码不为空，则一起修改
     * 用于管理员对用户的修改
     * 目前只修改用户名和密码
     *
     * @param entity
     */
    void update(UserEntity entity);

    UserEntity get(int id);

    void del(int id);

    void setDisLog(UserEntity entity);

    DataGridJson listUser(String key, int rows, int page);

    /**
     * 登录用户自主修改密码
     *
     * @param name
     * @param passWord
     */
    void updatePass(String name, String passWord) throws Exception;

    void setRoles(int id, String[] rids);

    List<Map<String, Object>> getRoles(int id);

    UserEntity logon(HttpServletRequest request, String name, String passWord, String verifyCode) throws Exception;

    List<Map<String, Object>> getModuleTree(int id);

}
