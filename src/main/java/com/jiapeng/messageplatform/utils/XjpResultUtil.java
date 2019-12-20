package com.jiapeng.messageplatform.utils;

import java.io.Serializable;

public class XjpResultUtil implements Serializable {
	private Integer code;
	private String msg="";
	private Long count=0L;
	private Object data;
	
	public XjpResultUtil() {
		super();
	}

	public XjpResultUtil(Integer code) {
		super();
		this.code = code;
	}

	public XjpResultUtil(Integer code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}


	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public static XjpResultUtil ok(){
		return new XjpResultUtil(0);
	}
	
	public static XjpResultUtil ok(Object list){
		XjpResultUtil result = new XjpResultUtil();
		result.setCode(0);
		result.setData(list);;
		return result;
	}
	public static XjpResultUtil ok(String msg){
		XjpResultUtil result = new XjpResultUtil();
		result.setCode(0);
		result.setMsg(msg);
		return result;
	}
	
	public static XjpResultUtil error(){
		return new XjpResultUtil(500,"没有此权限，请联系超管！");
	}
	public static XjpResultUtil error(String str){
		return new XjpResultUtil(500,str);
	}
}
