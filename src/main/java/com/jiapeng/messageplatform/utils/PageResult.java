package com.jiapeng.messageplatform.utils;

import java.util.List;

/**
 * Created by HZL on 2018/12/18.
 */
public class PageResult {
    /**
     * 总数
     */
    private int total;
    /**
     * 当前页中的对象
     */
    private List data;

    public PageResult(int total, List data){
        this.total = total;
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
