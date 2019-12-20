package com.jiapeng.messageplatform.model;

/**
 * Created by HZL on 2019/8/8.
 */
public class ImportErrorResult {
    private String no;
    private String name;
    private String description;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImportErrorResult(String no, String name, String description) {
        this.no = no;
        this.name = name;
        this.description = description;
    }
}
