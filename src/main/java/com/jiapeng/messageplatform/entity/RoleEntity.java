package com.jiapeng.messageplatform.entity;

import java.io.Serializable;
import java.util.List;

public class RoleEntity implements Serializable {
    private String id;
    private String name;
    private List<ModuleEntity> modules;

    public RoleEntity(){super();}

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

    public List<ModuleEntity> getModules() {
        return modules;
    }

    public void setModules(List<ModuleEntity> modules) {
        this.modules = modules;
    }


}
