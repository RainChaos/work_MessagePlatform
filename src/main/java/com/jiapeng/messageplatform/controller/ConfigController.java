package com.jiapeng.messageplatform.controller;

import com.jiapeng.messageplatform.entity.Config;
import com.jiapeng.messageplatform.service.ConfigService;
import com.jiapeng.messageplatform.utils.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by HZL on 2019/9/4.
 */
@RequestMapping("/config")
@RestController
public class ConfigController{
    @Autowired
    ConfigService configService;

    @PostMapping("/add")
    public ReturnT<Object> add(HttpServletRequest request, Config config) throws Exception {
        configService.add(config);
        return new ReturnT<>(200, "添加成功");
    }

    @PostMapping("/update")
    public ReturnT<Object> update(Config config) throws Exception {
        configService.update(config);
        return new ReturnT<>(200, "修改成功");
    }

    @PostMapping("/del")
    public ReturnT<Object> del(Integer configId) {
        configService.del(configId);
        return new ReturnT<>(200, "删除成功");
    }

    @PostMapping("/list")
    public ReturnT<Object> list(String scCode, Integer cfDeId) {
        List<Config> list = configService.list(scCode, cfDeId);
        return new ReturnT<>(list);
    }

}
