package com.jiapeng.messageplatform.service;

import com.jiapeng.messageplatform.entity.ClassEntity;
import jxl.read.biff.BiffException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ScoresService {
    void update(String tableName,String sheetName,String filePath) throws Exception;
    void initTable(String tableName,List<String> listRows) throws Exception;
    List<Map<String,Object>> list(String tb_name,List<String> cl_code,String stuId,String term,int rows,int page);
    void insert(String tableName, List <Map<String, String>> list);
    List<String> getRows(String tb_name);
    void delRows(String tb_name,String rowName);
    void insRows(String tb_name,String rowName);
    

}
