package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.dao.ConfigDefineMapper;
import com.jiapeng.messageplatform.dao.ConfigMapper;
import com.jiapeng.messageplatform.entity.Config;
import com.jiapeng.messageplatform.entity.ConfigDefine;
import com.jiapeng.messageplatform.model.KeyAndValue;
import com.jiapeng.messageplatform.service.ConfigDefineService;
import com.jiapeng.messageplatform.service.ConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HZL on 2019/9/4.
 */
@Service
public class ConfigDefineServiceImpl implements ConfigDefineService {
    @Autowired
    ConfigDefineMapper configDefineMapper;
    @Autowired
    ConfigService configService;

    @Override
    public void add(ConfigDefine config) throws Exception {
        String code = config.getCode();
        ConfigDefine dbConfig = configDefineMapper.selectByCode(code);
        if (dbConfig != null)
            throw new Exception("已经存在代码为：" + code + "的配置");
        configDefineMapper.insertSelective(config);
    }

    @Override
    public void update(ConfigDefine config) throws Exception {
        configDefineMapper.updateByPrimaryKeySelective(config);
    }

    @Override
    public void del(Integer id) throws Exception {
        List<Config> configList = configService.list(null, id);
        if (configList.size() > 0) {
            throw new Exception("已经存在对应的配置值，要删除请先删除配置值");
        }
        configDefineMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<ConfigDefine> listAll() {
        List<ConfigDefine> list = configDefineMapper.list();
        return list;
    }

    @Override
    public List<KeyAndValue> getSelectList(Integer cfDeId) {
        List<KeyAndValue> list = new ArrayList<>();
        ConfigDefine configDefine = configDefineMapper.selectByPrimaryKey(cfDeId);
        String selectValue = configDefine.getSelectValue();
        String[] valuesArr = selectValue.split(",");
        for (String value : valuesArr) {
            if (StringUtils.isNotBlank(value)) {
                list.add(new KeyAndValue(value, value));
            }
        }
        return list;
    }
}
