package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.utils.DataGridJson;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public class ScoresProvider {
    public String listSql(@Param("tb_name") String tb_name,@Param("cl_code") List<String> cl_code, @Param("stuId") String stuId, @Param("term") String term, @Param("limit") int rows, @Param("page") int page) throws Exception {

        if(StringUtils.isBlank(tb_name)){
            throw new Exception("需要参数：tb_name");
        }

        String where = " where 1 = 1";
        if(cl_code!=null){
            String clList = String.join(",", cl_code);
            where+=" and cl_code in ( "+clList+")";
        }
        if(StringUtils.isNotBlank(stuId)){
            where+=" and  stuId = #{stuId} ";
        }

        if(StringUtils.isNotBlank(term)){
            where+=" and  term = #{term} ";
        }

        Map<String,Integer> pObj = DataGridJson.compuPage(rows,page);

// String sql = String.format("select cla.*,gra.name as graName from class  cla left join grade gra on gra.gr_code = cla.gr_code %s  order by cl_number",where);
//       elect cla.*,gra.name as graName from class  cla left join grade gra on gra.gr_code = cla.gr_code %s  order by cl_number",where
        String sql = String.format("select * from (select ROW_NUMBER() over(order by id) rowid,sch.name as schName,gra.name as graName ,cla.name as claName,tbsco.* from  "+ tb_name  +" tbsco " +
                " left join student stu on stu.stuId = tbsco.stuId " +
                " left join class cla  on tbsco.cl_code = cla.cl_code " +
                " left join grade gra on gra.gr_code = cla.gr_code" +
                " left join school sch on sch.sc_code = gra.sc_code %s) as t" +
                " where rowid between %d and %d",where,pObj.get("start"),pObj.get("end"));

        System.out.println("查询语句："+sql);
        return sql;

    }

    public String countSql(@Param("tb_name") String tb_name,@Param("cl_code") List<String> cl_code, @Param("stuId") String stuId, @Param("term") String term){
        String where = " where 1 = 1";
        if(cl_code.size()!=0){
            String clList = String.join(",", cl_code);
            where+=" and cl_code in ( "+clList+")";
        }
        if(StringUtils.isNotBlank(stuId)){
            where+=" and  stuId = #{stuId} ";
        }
        if(StringUtils.isNotBlank(term)){
            where+=" and  term = #{term} ";
        }
        String sql = String.format("select count(0) from "+tb_name+" %s",where);
        return sql;
    }

    public String getRows(@Param("tb_name") String tb_name){
        String sql = String.format("select rowsName=name from syscolumns where id=object_id(N'"+tb_name+"')");
        return sql;
    }


    public String getRowIsExist(@Param("tb_name") String tb_name,@Param("rows") String rows) {
        String sql = String.format("select   *   from   syscolumns   where   id=object_id('"+tb_name+"')   and   name='"+rows+"' ");
        return sql;
    }


}
