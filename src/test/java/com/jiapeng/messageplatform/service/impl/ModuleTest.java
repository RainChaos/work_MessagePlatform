package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.entity.ModuleEntity;
import com.jiapeng.messageplatform.service.ModuleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
//@SpringBootTest
// 这块需要引入依赖 mybatis的测试依赖jar
@MybatisTest
// 这个注解的意义是指定了默认数据源
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

@ComponentScan("com.jiapeng.messageplatform")
public class ModuleTest {
    @Autowired
    ModuleService moduleService;

    @Rollback(false)
    @Test
    public void testInsert(){
        ModuleEntity entity = new ModuleEntity();
        entity.setParentId("100");
        entity.setName("test1");
        entity.setPath("/path");

        moduleService.add(entity);
    }

    @Test
    public void testGetAll(){
        List<Map<String,Object>> list = moduleService.getAll();
        for (Map<String, Object> entry : list) {
            for (Map.Entry<String, Object> entry1 : entry.entrySet()) {
                String key = entry1.getKey();
                Object val = entry1.getValue();
                System.out.println("");
            }

        }
    }

}
