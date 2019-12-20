package com.jiapeng.messageplatform.service;

import com.jiapeng.messageplatform.entity.ConfigDefine;
import com.jiapeng.messageplatform.model.KeyAndValue;

import java.util.List;

/**
 * Created by HZL on 2019/9/4.
 */
public interface ConfigDefineService {
    void add(ConfigDefine config) throws Exception;

    void update(ConfigDefine config) throws Exception;

    void del(Integer id) throws Exception;

    List<ConfigDefine> listAll();

    List<KeyAndValue> getSelectList(Integer cfDeId);
}
