package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.entity.SchoolEntity;
import com.jiapeng.messageplatform.service.SchoolService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
//@SpringBootTest
// 这块需要引入依赖 mybatis的测试依赖jar
@MybatisTest
// 这个注解的意义是指定了默认数据源
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan("com.jiapeng.messageplatform")
public class SchoolTest {
    @Autowired
    SchoolService schoolService;

    @Rollback(false)
    @Test
    public void testadd(){
        SchoolEntity entity = new SchoolEntity();
        entity.setName("桂林一中");

        schoolService.add(entity);
    }
}
