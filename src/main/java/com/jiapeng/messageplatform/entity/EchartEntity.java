package com.jiapeng.messageplatform.entity;

import java.util.List;

/**
 * Created by Administrator on 2019/9/30.
 */
public class EchartEntity {


    private String code;
    private String name;
    private Integer value;

    private List<LeaveTotal> dataList;

    public void setDataList(List<LeaveTotal> dataList) {
        this.dataList = dataList;
    }

    public List<LeaveTotal> getDataList() {
        return dataList;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}


