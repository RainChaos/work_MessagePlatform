package com.jiapeng.messageplatform.model;

/**
 * Created by HZL on 2019/9/10.
 */
public class NameAndValue {
    private String name;
    private Object value;

    public NameAndValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
