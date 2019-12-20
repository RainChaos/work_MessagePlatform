package com.jiapeng.messageplatform.controller;

import com.jiapeng.messageplatform.entity.ConfigDefine;
import com.jiapeng.messageplatform.model.KeyAndValue;
import com.jiapeng.messageplatform.service.ConfigDefineService;
import com.jiapeng.messageplatform.utils.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HZL on 2019/9/10.
 */
@RequestMapping("/configDefine")
@RestController
public class ConfigDefineController{
    @Autowired
    ConfigDefineService configDefineService;

    @PostMapping("/add")
    public ReturnT<Object> add(ConfigDefine configDefine) throws Exception {
        configDefineService.add(configDefine);
        return new ReturnT<>(200, "添加成功");
    }

    @PostMapping("/update")
    public ReturnT<Object> update(ConfigDefine configDefine) throws Exception {
        configDefineService.update(configDefine);
        return new ReturnT<>(200, "修改成功");
    }

    @PostMapping("/del")
    public ReturnT<Object> del(Integer id) throws Exception {
        configDefineService.del(id);
        return new ReturnT<>(200, "删除成功");
    }

    @PostMapping("/list")
    public ReturnT<Object> list() {
        List<ConfigDefine> list = configDefineService.listAll();
        return new ReturnT<>(list);
    }

    /**
     * 配置项类型
     *
     * @return
     */
    @PostMapping("/typeList")
    public ReturnT<Object> typeList() {
        List<KeyAndValue> list = new ArrayList<>();
        list.add(new KeyAndValue("String", "String"));//字符类型
        list.add(new KeyAndValue("Integer", "Integer"));//数字类型
        list.add(new KeyAndValue("Boolean", "Boolean"));//true\false单选类型
        list.add(new KeyAndValue("Date", "Date"));//时间类型
        list.add(new KeyAndValue("Select", "Select"));//下拉框类型
        return new ReturnT<>(list);
    }

    /**
     * select类型对应的选择值列表
     *
     * @param id
     * @return
     */
    @PostMapping("/selectValueList")
    public ReturnT<Object> selectValueList(Integer id) {
        List<KeyAndValue> list = new ArrayList<>();
        list = configDefineService.getSelectList(id);
        return new ReturnT<>(list);
    }
}
