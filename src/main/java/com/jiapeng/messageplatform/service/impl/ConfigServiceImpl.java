package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.dao.ConfigMapper;
import com.jiapeng.messageplatform.entity.Config;
import com.jiapeng.messageplatform.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HZL on 2019/9/10.
 */
@Service
public class ConfigServiceImpl implements ConfigService {
    @Autowired
    ConfigMapper configMapper;

    @Override
    public void add(Config config) throws Exception {
        //1.判断是否存在
        Config dbConfig = configMapper.selectByScCodeAndCfCode(config.getScCode(), config.getCfCode());
        if (null != dbConfig) {
            throw new Exception("配置已存在，请勿重复添加");
        }
        configMapper.insertSelective(config);
    }


    @Override
    public void update(Config config) throws Exception {
        Integer id = config.getId();
        String value = config.getValue();
        config = new Config();
        config.setId(id);
        config.setValue(value);
        configMapper.updateByPrimaryKeySelective(config);
    }

    @Override
    public void del(Integer id) {
        configMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Config findByScCodeAndCfCode(String scCode, String cfCode) throws Exception {
        Config config = configMapper.selectByScCodeAndCfCode(scCode, cfCode);
        if (null == config) {
            throw new Exception("配置不存在");
        } else {
            return config;
        }
    }

    @Override
    public List<Config> list(String scCode, Integer cfDeId) {
        List<Config> list = new ArrayList<>();
        list = configMapper.list(scCode, cfDeId);
        return list;
    }
    
}
