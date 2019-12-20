package com.jiapeng.messageplatform.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Teacher {
    private String teNo;

    private String loginname;

    private String name;

    private String sex;

    private String idcode;

    private String phoneno;

    private String email;

    private String scCode;

    private String scName;

    private String isadmin;


    //额外修改
    private String enable;
    private String statusreason;
    private String password;
    private String iswxlogin;

    private Date statusdate;

    private String isdelete;

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }

    private List<ClassInfo> classList;

    private List<String> clCodeList;

    public String getIswxlogin() {
        return iswxlogin;
    }

    public void setIswxlogin(String iswxlogin) {
        this.iswxlogin = iswxlogin;
    }

    public void setScName(String scName) {
        this.scName = scName;
    }

    public String getScName() {
        return scName;
    }

    public String getTeNo() {
        return teNo;
    }

    public void setTeNo(String teNo) {
        this.teNo = teNo;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdcode() {
        return idcode;
    }

    public void setIdcode(String idcode) {
        this.idcode = idcode;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getScCode() {
        return scCode;
    }

    public void setScCode(String scCode) {
        this.scCode = scCode;
    }

    public String getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(String isadmin) {
        this.isadmin = isadmin;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public Date getStatusdate() {
        return statusdate;
    }

    public void setStatusdate(Date statusdate) {
        this.statusdate = statusdate;
    }

    public String getStatusreason() {
        return statusreason;
    }

    public void setStatusreason(String statusreason) {
        this.statusreason = statusreason;
    }

    public List<ClassInfo> getClassList() {
        return classList;
    }

    public void setClassList(List<ClassInfo> classList) {
        this.classList = classList;
    }

    public List<String> getClCodeList() {
        List<String> list = new ArrayList<>();
        this.classList.forEach(classInfo -> {
            list.add(classInfo.getClassCode());
        });
        return list;
    }

    public void setClCodeList(List<String> clCodeList) {
        this.clCodeList = clCodeList;
    }

    public List<ClassTeacher> getClassTeacherList() {
        List<ClassTeacher> list = new ArrayList<>();
        for (ClassInfo classInfo : this.classList) {
            ClassTeacher classTeacher = new ClassTeacher();
            classTeacher.setTeNo(this.teNo);
            classTeacher.setClCode(classInfo.getClassCode());
            classTeacher.setHeadMaster(classInfo.isClassMaster());
            list.add(classTeacher);
        }
        return list;
    }
}