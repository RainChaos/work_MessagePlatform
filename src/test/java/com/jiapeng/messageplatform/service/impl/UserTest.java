package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.dao.UserMapper;
import com.jiapeng.messageplatform.entity.UserEntity;
import com.jiapeng.messageplatform.service.UserService;
import com.jiapeng.messageplatform.utils.DataGridJson;
import com.jiapeng.messageplatform.utils.TokenProccessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RunWith(SpringRunner.class)
//@SpringBootTest
// 这块需要引入依赖 mybatis的测试依赖jar
@MybatisTest
// 这个注解的意义是指定了默认数据源
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan("com.jiapeng.messageplatform")
public class UserTest {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;

    @Test
    public  void testUpdateSql(){
        UserEntity entity=new UserEntity();
        entity.setId(1);
        entity.setName("test");
        entity.setPassWord("tttt");
        userService.update(entity);
    }

    @Test
    public void testlist(){
        DataGridJson obj = userService.listUser("t",2,1);
        System.out.printf("%d",obj.getTotal());
    }

    @Test
    public void testtree(){
        List<Map<String,Object>> tree = userService.getModuleTree(1);
    }

    @Test
    public void testToken(){
        String token=  TokenProccessor.getInstance().makeToken();
        System.out.println(TokenProccessor.getInstance().makeToken());
        System.out.println(TokenProccessor.getInstance().makeToken());
        System.out.println(TokenProccessor.getInstance().makeToken());
        System.out.println(UUID.randomUUID().toString());
    }
}
