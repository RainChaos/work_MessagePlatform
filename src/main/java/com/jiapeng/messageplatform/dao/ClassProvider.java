package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.utils.DataGridJson;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public class ClassProvider {
    public String listSql(@Param("sc_code")String sc_code, @Param("gr_code")String gr_code,@Param("limit") int rows, @Param("page") int page) throws Exception {
        if(StringUtils.isBlank(sc_code)){
            throw new Exception("需要参数：sc_code");
        }
        String where = " where cla.sc_code = #{sc_code} ";
        if("allClassData".equals(gr_code)){
            gr_code=null;
        }
        if(StringUtils.isNotBlank(gr_code)){
            where+=" and cla.gr_code = #{gr_code} ";
        }

        Map<String,Integer> pObj = DataGridJson.compuPage(rows,page);

// String sql = String.format("select cla.*,gra.name as graName from class  cla left join grade gra on gra.gr_code = cla.gr_code %s  order by cl_number",where);
//       elect cla.*,gra.name as graName from class  cla left join grade gra on gra.gr_code = cla.gr_code %s  order by cl_number",where
        String sql = String.format("select * from (select ROW_NUMBER() over(order by cl_number) rowid,cla.*,gra.name as graName from class  cla left join grade gra on gra.gr_code = cla.gr_code %s) as t" +
                " where rowid between %d and %d",where,pObj.get("start"),pObj.get("end"));

        return sql;

    }

    public String countSql(@Param("sc_code")String sc_code, @Param("gr_code")String gr_code){
        String where = " where sc_code = #{sc_code} ";
        if("allClassData".equals(gr_code)){
            gr_code=null;
        }
        if(StringUtils.isNotBlank(gr_code)){
            where+=" and gr_code = #{gr_code} ";
        }
        String sql = String.format("select count(0) from class %s",where);
        return sql;
    }

}
