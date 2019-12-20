package com.jiapeng.messageplatform.model;

/**
 * Created by HZL on 2019/9/10.
 */
public class KeyAndValue {
    private String key;
    private Object value;

    public KeyAndValue(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
