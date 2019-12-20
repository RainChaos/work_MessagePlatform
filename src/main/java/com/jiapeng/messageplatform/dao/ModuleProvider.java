package com.jiapeng.messageplatform.dao;

import org.apache.ibatis.jdbc.SQL;

public class ModuleProvider {
    public String getLastId(String parentId){
        return new SQL(){{
            SELECT("max(id)");
            FROM("sys_module");
            if(parentId!=null){
                WHERE("parentid=#{parentId}");
            }else{
                WHERE("parentid is null");
            }
        }}.toString();
    }
}
