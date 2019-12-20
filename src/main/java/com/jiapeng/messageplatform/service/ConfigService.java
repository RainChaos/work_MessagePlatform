package com.jiapeng.messageplatform.service;

import com.jiapeng.messageplatform.entity.Config;

import java.util.List;

/**
 * Created by HZL on 2019/9/10.
 */
public interface ConfigService {
    void add(Config config) throws Exception;

    /**
     * 更新配置（只允许更新value）
     * @param config
     * @throws Exception
     */
    void update(Config config) throws Exception;

    void del(Integer id);

    Config findByScCodeAndCfCode(String scCode, String cfCode) throws Exception;

    List<Config> list(String scCode, Integer cfDeId);
}
