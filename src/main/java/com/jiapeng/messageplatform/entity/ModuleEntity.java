package com.jiapeng.messageplatform.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ModuleEntity implements Serializable {
    private String id;
    private String name;
    private String icon;
    private int orderNumber;
    private String parentId;
    private String path;
    private String idName;
    private String checked;


    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getChecked() {
        return checked;
    }

    public ModuleEntity(){super();}

    public Map<String, Object> toMap() {
        Map<String, Object> itemMap = new HashMap<String, Object>();
        itemMap.put("id", this.id);
        itemMap.put("name", this.name);
        itemMap.put("icon", this.icon);
        itemMap.put("parentid", this.parentId);
        itemMap.put("path", this.path);
        itemMap.put("sort", this.orderNumber);
        itemMap.put("idName", this.idName);
        itemMap.put("checked", this.checked);
        return itemMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }


}
