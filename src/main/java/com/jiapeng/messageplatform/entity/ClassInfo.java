package com.jiapeng.messageplatform.entity;

/**
 * Created by HZL on 2019/8/7.
 */
public class ClassInfo {
    private String classCode;//代码
    private String classNo;//编号
    private String className;
    private String gradeName;
    private String schoolName;
    private boolean classMaster;//是否班主任

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassNo() {
        return classNo;
    }

    public void setClassNo(String classNo) {
        this.classNo = classNo;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public boolean isClassMaster() {
        return classMaster;
    }

    public void setClassMaster(boolean classMaster) {
        this.classMaster = classMaster;
    }

}
