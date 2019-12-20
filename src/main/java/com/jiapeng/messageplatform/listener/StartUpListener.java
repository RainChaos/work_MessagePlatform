package com.jiapeng.messageplatform.listener;

import com.jiapeng.messageplatform.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by LLJ on 2019/7/8.
 */
@Component
public class StartUpListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    UpdateService updateService;
//    Logger log = LoggerFactory.getLogger(Test.class);
//        log.info("info");
//        log.error("ERROR");

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        System.out.println("程序启动");
//        //初始化数据表
//        updateService.update();
//
//        //初始化管理端菜单 by hzl 2019-9-3 14:33:52
//        updateService.initStuPage();
    }
}
