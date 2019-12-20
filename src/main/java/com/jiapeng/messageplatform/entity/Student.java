package com.jiapeng.messageplatform.entity;

import java.util.Date;
import java.util.List;

public class Student {

    private String stuId;

    private String stuNo;

    private String stuName;

    private String sex;

    private String idCode;

    private String clCode;

    private Date createDate;

    private Date updateDate;

    private String className;

    private String gradeName;

    private String schoolName;

    private List<Guardian> guardianList;

    private String imgPath; //头像路径 by hzl 2019-10-22 17:03:17
    private String grCode; //年级代码 by hzl 2019-10-30 10:54:14
    private String scCode; //学校代码 by hzl 2019-10-30 10:54:14

    private String isBind;


    public String getIsBind() {
        return isBind;
    }

    public void setIsBind(String isBind) {
        this.isBind = isBind;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public String getClCode() {
        return clCode;
    }

    public void setClCode(String clCode) {
        this.clCode = clCode;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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

    public List<Guardian> getGuardianList() {
        return guardianList;
    }

    public void setGuardianList(List<Guardian> guardianList) {
        this.guardianList = guardianList;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getGrCode() {
        return grCode;
    }

    public void setGrCode(String grCode) {
        this.grCode = grCode;
    }

    public String getScCode() {
        return scCode;
    }

    public void setScCode(String scCode) {
        this.scCode = scCode;
    }
}