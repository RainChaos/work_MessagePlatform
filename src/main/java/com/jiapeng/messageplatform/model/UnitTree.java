package com.jiapeng.messageplatform.model;

import java.util.List;

/**
 * Created by HZL on 2019/8/20.
 */
public class UnitTree {
    private String unitType;//结构类型：学校、班级、年级（school、class、grade）
    private String id;
    private String title;
    private List<UnitTree> children;

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<UnitTree> getChildren() {
        return children;
    }

    public void setChildren(List<UnitTree> children) {
        this.children = children;
    }
}
