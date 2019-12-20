package com.jiapeng.messageplatform.service;

import com.jiapeng.messageplatform.entity.ClassEntity;
import jxl.read.biff.BiffException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ClassService {
    void add(ClassEntity entity);
    void update(ClassEntity entity);
    void del(String cl_code) throws Exception;
    List<Map<String,Object>> list(String sc_code,String gr_code,int limit,int page);

    List<String> listByCodeAndTeNo(String sc_code,String gr_code,String teNo);


    /**
     * 导入班级，返回错误列表
     * @param sc_code
     * @param file
     * @return
     * @throws IOException
     * @throws BiffException
     */
    List<String> imp(String sc_code,String file) throws IOException, BiffException;

    void classHandle(String sc_code, String gr_uuidStr, String cl_uuidStr, String name, String delete)throws Exception;

}
