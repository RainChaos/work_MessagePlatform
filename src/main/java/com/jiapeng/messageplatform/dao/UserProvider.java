package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.entity.UserEntity;
import com.jiapeng.messageplatform.utils.DataGridJson;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class UserProvider {
    public String updateSql(UserEntity entity){
        return new SQL(){{
            UPDATE("sys_user");
            SET("name=#{name}");
            if(StringUtils.isNotBlank( entity.getPassWord())){
                SET("passWord=#{passWord}");
            }
            WHERE("id=#{id}");
        }}.toString();
    }

    private String listWhere(String key){
        String where=" ";

        if(StringUtils.isNotBlank(key)){
            where =" where name like CONCAT('%',#{key},'%') ";
        }
        return where;
    }

    public String listSql(@Param("key") String key, @Param("rows") int rows, @Param("page") int page){
        String where = listWhere(key);
        Map<String,Integer> pObj = DataGridJson.compuPage(rows,page);
        String sql =String.format("select * from " +
                "(select ROW_NUMBER() over(order by id) rowid,id,userdb.name,dislog,dislogreason,dislogtime,userdb.scCode,sch.name as details " +
                "from sys_user userdb left join school sch on sch.sc_code = userdb.scCode %s)t where rowid between %d and %d",where,pObj.get("start"),pObj.get("end"));
        return sql;
//        return new SQL(){{
//            SELECT("ROW_NUMBER() over(order by id) rowid,id,name,dislog,dislogreason,dislogtime");
//            FROM("sys_user");
//            if(StringUtils.isNotBlank(key)){
//                WHERE("name like CONCAT('%',#{key},'%')");
//            }
//        }}.toString();
    }

    public String countSql(String key){
        String where = listWhere(key);
        String sql = String.format("select count(id) from sys_user %s",where);
        return sql;
    }

}
