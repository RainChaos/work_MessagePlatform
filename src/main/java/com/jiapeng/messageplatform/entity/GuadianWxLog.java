package com.jiapeng.messageplatform.entity;

/**
 * Created by Administrator on 2019/11/12.
 */
public class GuadianWxLog {

    private Integer id;

    private String sc_code;

    private String stuid;

    private String stuno;

    private String stuname;

    private String guaname;

    private String operate;

    private String schname;

    private String wxno;

    private String operatetime;

    public String getWxno() {
        return wxno;
    }

    public void setWxno(String wxno) {
        this.wxno = wxno;
    }

    public String getSchname() {
        return schname;
    }

    public void setSchname(String schname) {
        this.schname = schname;
    }

    public String getSc_code() {
        return sc_code;
    }

    public void setSc_code(String sc_code) {
        this.sc_code = sc_code;
    }

    public String getStuid() {
        return stuid;
    }

    public void setStuid(String stuid) {
        this.stuid = stuid;
    }

    public String getStuno() {
        return stuno;
    }

    public void setStuno(String stuno) {
        this.stuno = stuno;
    }

    public String getStuname() {
        return stuname;
    }

    public void setStuname(String stuname) {
        this.stuname = stuname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public String getGuaname() {
        return guaname;
    }

    public void setGuaname(String guaname) {
        this.guaname = guaname;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getOperatetime() {
        return operatetime;
    }

    public void setOperatetime(String operatetime) {
        this.operatetime = operatetime;
    }
}
