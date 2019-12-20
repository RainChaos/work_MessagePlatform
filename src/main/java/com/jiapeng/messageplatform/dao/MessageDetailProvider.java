package com.jiapeng.messageplatform.dao;

import com.jiapeng.messageplatform.utils.DataGridJson;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public class MessageDetailProvider {
    public String listSql(@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize,
                          @Param("logId") String logId,
                          @Param("state") String state,
                          @Param("scCode")String scCode,
                          @Param("graCode")String graCode,
                          @Param("clCode")String clCode,
                          @Param("stuName")String stuName) throws Exception {
        if(StringUtils.isBlank(logId)){
            throw new Exception("需要参数：logId");
        }

        //根据logId查询
        String where = " where md.logId = #{logId} ";


        //发送状态
        if(StringUtils.isNotBlank(state)){
            where+=" and md.state = #{state} ";
        }

        //根据学校
        if(StringUtils.isNotBlank(scCode)){
            where+=" and sch.sc_code = #{scCode} ";
        }

        //根据年级
        if(StringUtils.isNotBlank(graCode)){
            where+=" and gra.gr_code = #{graCode} ";
        }
        //根据班级
        if(StringUtils.isNotBlank(clCode)){
            where+=" and cla.cl_code = #{clCode} ";
        }

        //根据姓名
        if(StringUtils.isNotBlank(stuName)){
            where+=" and stu.stuName = #{stuName} ";
        }

        Map<String,Integer> pObj = DataGridJson.compuPage(pageSize,pageIndex);

        String sql = String.format("select * from (" +
                "select ROW_NUMBER() over(order by md.id) " +
                " rowid,sch.name as scName,gra.name as graName,cla.name as claName,md.*,stu.stuNo,stu.stuName from message_detail md " +
                "LEFT JOIN student stu on stu.stuId = md.stuId  \n" +
                "LEFT JOIN class cla on cla.cl_code = stu.cl_code\n" +
                "LEFT JOIN school sch  on cla.sc_code= sch.sc_code\n" +
                "LEFT JOIN grade gra  on cla.gr_code= gra.gr_code" +
                "%s) as t" +
                " where rowid between %d and %d",where,pObj.get("start"),pObj.get("end"));

        return sql;

    }

    public String countSql(@Param("logId") String logId,
                           @Param("state") String state,
                           @Param("scCode")String scCode,
                           @Param("graCode")String graCode,
                           @Param("clCode")String clCode,
                           @Param("stuName")String stuName){
        String where = " where logId = #{logId} ";

        if(StringUtils.isNotBlank(logId)){
            where+=" and logId = #{logId} ";
        }
        String sql = String.format("select count(0) from message_detail %s",where);
        return sql;
    }
}
