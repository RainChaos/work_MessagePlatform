package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.utils.DataGridJson;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public class SchoolProvider {
    private String listWhere(@Param("scCode") String scCode, @Param("key") String key){
        String where="";

        if(StringUtils.isNotBlank(key)){
            //key不为空
            where =" where name like CONCAT('%',#{key},'%') ";

            if(StringUtils.isNotBlank(scCode)){
                //scCode不为空
                where+=" and sc_code = #{scCode} ";
            }
        }else {
            //key为空
            where =" where sc_code like CONCAT('%',#{scCode},'%') ";
        }

        return where;
    }

    public String listSql(@Param("scCode") String scCode, @Param("key") String key,@Param("rows") int rows,@Param("page") int page){
        String where = listWhere(scCode,key);
        Map<String,Integer> pObj = DataGridJson.compuPage(rows,page);
        String sql =String.format("select * from " +
                "(select ROW_NUMBER() over(order by createTime desc) rowid,* " +
                "from school %s)t where rowid between %d and %d",where,pObj.get("start"),pObj.get("end"));
        if (page==-1||rows==-1){
             sql =String.format("select * from " +
                    "(select ROW_NUMBER() over(order by createTime desc) rowid,* " +
                    "from school %s)t ",where);
        }

        return sql;
    }

    public String countSql(@Param("scCode") String scCode, @Param("key") String key){
        String where = listWhere(scCode,key);
        String sql = String.format("select count(sc_code) from school %s",where);
        return sql;
    }

}
