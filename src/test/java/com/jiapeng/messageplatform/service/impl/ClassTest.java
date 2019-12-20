package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.service.ClassService;
import jxl.read.biff.BiffException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.security.PublicKey;

@RunWith(SpringRunner.class)
//@SpringBootTest
// 这块需要引入依赖 mybatis的测试依赖jar
@MybatisTest
// 这个注解的意义是指定了默认数据源
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan("com.jiapeng.messageplatform")
public class ClassTest {
    @Autowired
    ClassService classService;

    @Rollback(false)
    @Test
    public  void testimp(){
        String file = "E:\\work\\MessagePlatform\\src\\main\\resources\\templates\\classic\\mb\\班级导入模版.xlsx";
        try {
            classService.imp("100000",file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }
}
