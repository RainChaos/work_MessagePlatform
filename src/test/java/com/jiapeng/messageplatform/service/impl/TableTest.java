package com.jiapeng.messageplatform.service.impl;

import com.jiapeng.messageplatform.dao.UserMapper;
import com.jiapeng.messageplatform.entity.UserEntity;
import com.jiapeng.messageplatform.service.ScoresService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@RunWith(SpringRunner.class)
//@SpringBootTest
// 这块需要引入依赖 mybatis的测试依赖jar
@MybatisTest
// 这个注解的意义是指定了默认数据源
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan("com.jiapeng.messageplatform")
public class TableTest {
    @Autowired
    private ScoresService scoresService;
    @RequestMapping("/testTable")
    @ResponseBody
    public void testTable() throws Exception {
        scoresService.update("tb_score_100001","sheet1","D:\\test.xlsx");
    }

    @Test
    public List<Map<String,Object>> testList() throws Exception {
        return scoresService.list("tb_score_100001",null,null,null,10,1);
    }

    @Test
    public void testInsert() throws Exception {

        List <Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();

        List<String> listRows = scoresService.getRows("tb_score_100001");
        for (String row: listRows
                ) {
            map.put(row,"111");
        }
        list.add(map);
         scoresService.insert("tb_score_100001",list);
    }


}
