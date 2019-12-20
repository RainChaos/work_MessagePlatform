package com.jiapeng.messageplatform.utils;

import java.io.Serializable;

/**
 * Created by HZL on 2018/12/18.
 */


public class ReturnT<T> implements Serializable {

    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 500;

    public static final ReturnT<String> SUCCESS = new ReturnT<String>(null);
    public static final ReturnT<String> FAIL = new ReturnT<String>(FAIL_CODE, null);

    public static ReturnT getFail(String msg) {
        return new ReturnT(FAIL_CODE, msg);
    }

    private int code;
    private String msg;
    private T content;

    public ReturnT() {
        this.code =SUCCESS_CODE;
    }

    public ReturnT(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ReturnT(int code,T content, String msg) {
        this.code = code;
        this.content=content;
        this.msg = msg;
    }

    public ReturnT(T content) {
        this.code = SUCCESS_CODE;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ReturnT [code=" + code + ", msg=" + msg + ", content=" + content + "]";
    }

}
