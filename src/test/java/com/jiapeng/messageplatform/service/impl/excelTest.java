package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.utils.Excel;
import jxl.read.biff.BiffException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class excelTest {
    @Test
    public void testexcel(){
        Excel excel = new Excel();
        try {
            List<Map<String,Object>> list = excel.getDataMap("D:\\副本课程信息.xlsx",null);
            System.out.println(list.size());
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getExcelCol(){
        Excel excel = new Excel();
        try {

            List<String> list = excel.getField("D:\\test.xlsx","sheet1");
            for (String aa: list
                 ) {
                System.out.println("行："+aa);
            }
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
    }











}
