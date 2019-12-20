package com.jiapeng.messageplatform.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataGridJson {
    int total;
    Object rows;

    public int getTotal() {
        return total;
    }

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public DataGridJson(List list) {
        this.total = list.size();
        rows = list;
    }

    public DataGridJson(int total,List list){
        this.total = total;
        rows = list;
    }

    /**
     * 计算分页
     * @param rows
     * @param page
     * @return
     */
    public static Map<String, Integer> compuPage(int rows, int page){
        if (page == 0)
        {
            page = 1;
        }
        if (rows == 0)
        {
            rows = 20;
        }
        int start = (page - 1) * rows;
        int end = start + rows;
        start = page == 1 ? start : start + 1;
        Map<String,Integer> result = new HashMap<String,Integer>();
        result.put("start",start);
        result.put("end",end);
        return result;
    }
}
