package com.jiapeng.messageplatform.service.impl;

/**
 * Created by Administrator on 2019/9/4.
 */

import com.jiapeng.messageplatform.dao.*;
import com.jiapeng.messageplatform.entity.ConfigDefine;
import com.jiapeng.messageplatform.entity.ModuleEntity;
import com.jiapeng.messageplatform.service.ScoresService;
import com.jiapeng.messageplatform.service.UpdateService;
import com.jiapeng.messageplatform.utils.Excel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by LLJ on 2019/7/8.
 */
@Service
public class ScoresServiceImpl implements ScoresService {
    @Autowired
    private UpdateDao updateDao;

    @Autowired
    private ScoresMapper scoresMapper;


    @Override
    public List<Map<String,Object>> list(String tb_name,List<String> cl_code,String stuId,String term,int rows,int page) {
        return scoresMapper.list(tb_name,cl_code,stuId,term,rows,page);
    }


    public void delRows(String tb_name,String rowName){
        String sql = deleteColStr(tb_name,rowName);
        updateDao.executeSql(sql);

    };
    public void insRows(String tb_name,String rowName){
        String sql = createColStr(tb_name, rowName, "nvarchar(50)", null);
        updateDao.executeSql(sql);
    };















    @Override
    public List<String> getRows(String tableName){
        List<String> resultList = new ArrayList<>();
      List<String> list = scoresMapper.getRows(tableName);
        for (String rowStr:list){
            if(!"id".equals(rowStr)){
                resultList.add(rowStr);
            }
        }
        return resultList;
    }

    @Override
    public void insert(String tableName, List <Map<String, String>> list){

        String sql = "";
        for (int i=0; i < list.size();i++){
            Map<String, String> map = list.get(i);
            Collection<String> valueCollection = map.values();
            String colValues = valueCollection.toString().replace(",","],[").replace("[","'").replace("]","'").replace("[ id],","");
            String columnValues = StringUtils.deleteWhitespace(colValues);

            System.out.println("列的值："+columnValues);
            Set<String> keySet = map.keySet();
            String column = StringUtils.deleteWhitespace(keySet.toString().replace(",","],["));
            System.out.println("列："+column);
            sql += "INSERT INTO [dbo].["+tableName+"] ("+column+") VALUES ("+columnValues+");";
        }
       System.out.println("SQL执行："+sql);

        try {
            updateDao.executeSql(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
        //根据文件获取文件列名
    }


    @Override
    public void update(String tableName,String sheetName,String filePath) throws Exception{
        //根据文件获取文件列名
        List<String> listRows = new Excel().getField(filePath,sheetName);
        if(listRows.size()==0){
            throw new Exception("科目总数不能空！");
        }
        List<String> sqlList  = createTable(tableName,listRows);
        for (int i = 0; i < sqlList.size(); i++) {
            try {
                updateDao.executeSql(sqlList.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initTable(String tableName,List<String> listRows) throws Exception{

        //创建表
        List<String> sqlList  = createTable(tableName,listRows);
        for (int i = 0; i < sqlList.size(); i++) {
            try {
                String rowItem = sqlList.get(i).toString();
                if("cl_code".equals(rowItem)||"id".equals(rowItem)||"term".equals(rowItem)||"stuId".equals(rowItem)){
                }else {
                    updateDao.executeSql(sqlList.get(i));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //根据表已经存在，判断列是否存在，如果不存在则新增
        for (String str: listRows
                ) {
            String isExist = scoresMapper.getRowIsExist(tableName,str);
            if(isExist==null){
                String sql = createColStr(tableName, str, "nvarchar(50)", null);
                updateDao.executeSql(sql);
            }
        }

        //查询初始化列中是否有
        //数据库中有该字段，但是用户提交的数据没有他，就需要删除
        List<String> dbListRows = scoresMapper.getRows(tableName);

        //遍历数据库字段
        for (String  dbStr: dbListRows
                ) {
                    if(dbStr.equals("cl_code")||dbStr.equals("id")||dbStr.equals("term")||dbStr.equals("stuId")){
                    }else {
                        if(!listRows.contains(dbStr)){
                            String sql = deleteColStr(tableName,dbStr);
                            updateDao.executeSql(sql);
                        }
                    }
        }
    }







    /**
     * 创建成绩表
     * @param tableName    表名称
     * @param list          表列名集合
     * @return
     */
    public List<String> createTable(String tableName,List<String> list) {
        List<String> sqlList = new ArrayList<String>();

        String sqlStr="";
        for(int i = 0; i < list.size();i++){

            if(list.size()-1==i){
                sqlStr += "\t["+list.get(i).toString()+"] [nvarchar](20) NULL,\n"+"";
            }else {
                sqlStr += "\t["+list.get(i).toString()+"] [nvarchar](20) NULL,\n"+"";
            }
        }

        sqlList.add("if not exists (SELECT   name   FROM   sysobjects   where   name="+"'"+tableName +"'" +")" +
                "CREATE TABLE [dbo].["+tableName+"](\n" +
                "\t[id] [int] IDENTITY(1,1) NOT NULL,\n" +
                "\t[cl_code] [nvarchar](50) NULL,\n"+
                "\t[term] [nvarchar](50) NULL,\n"+
                "\t[stuId] [nvarchar](50) NULL,\n"
                + sqlStr +
                " CONSTRAINT [PK_"+tableName+"] PRIMARY KEY CLUSTERED \n" +
                "(\n" +
                "\t[id] ASC\n" +
                ")WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\n" +
                ") ON [PRIMARY]");
        return sqlList;
    }



    /**
     * 创建列的sql
     *
     * @param tableName    表名
     * @param colName      列明
     * @param type         字段类型
     * @param defaultValue 默认值
     * @return
     */
    private String createColStr(String tableName, String colName, String type, String defaultValue) {
        String sql = "IF not EXISTS(SELECT 1 FROM SYSOBJECTS T1  \n" +
                "  INNER JOIN SYSCOLUMNS T2 ON T1.ID=T2.ID  \n" +
                " WHERE T1.NAME='" + tableName + "' AND T2.NAME='" + colName + "'  \n" +
                ")  \n" +
                "begin\n" +
                "ALTER TABLE " + tableName + " ADD " + colName + " " + type + "\n";
        if (defaultValue != null) {
            sql += "ALTER TABLE " + tableName + " ADD CONSTRAINT DF_" + tableName + "_" + colName + " DEFAULT " + defaultValue + " FOR " + colName + "\n";
        }
        sql += "end\n";
        return sql;
    }


    /**
     *
     * @param tableName
     * @param colName
     * @return
     */
    private String deleteColStr(String tableName, String colName) {
        String sql = "alter table "+tableName+" drop column "+colName+";";
        return sql;
    }




    /**
     * 创建索引
     *
     * @param tableName
     * @param colName
     * @param indexName
     * @return
     */
    private String createIndexSql(String tableName, String colName, String indexName) {
        String sql = "if exists (SELECT   name   FROM   sysobjects   where   name= '" + tableName + "')\n" +
                "begin\n" +
                "IF EXISTS (SELECT * FROM SYSINDEXES WHERE NAME='" + indexName + "')\n" +
                "DROP INDEX " + tableName + "." + indexName + "\n" +
                "CREATE NONCLUSTERED INDEX " + indexName + "\n" +
                "ON " + tableName + "([" + colName + "]asc)  \n" +
                "WITH  (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]\n" +
                "end\n";
        return sql;
    }

}
