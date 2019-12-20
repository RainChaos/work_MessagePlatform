package com.jiapeng.messageplatform.controller;
import com.jiapeng.messageplatform.service.ScoresService;
import com.jiapeng.messageplatform.utils.DataGridJson;
import com.jiapeng.messageplatform.utils.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.security.KeyManagementException;
import java.util.*;

@RestController
@RequestMapping("/score")
public class ScoreController {
    @Autowired
    private ScoresService scoresService;



    @RequestMapping("/initTable")
    @ResponseBody
    public ReturnT<Object> initTable(String kemu) throws Exception {
        List<String> kemuList = Arrays.asList(kemu.split(","));
        scoresService.initTable("score_100001",kemuList);
        return new ReturnT<>();
    }

    @RequestMapping("/delRows")
    @ResponseBody
    public ReturnT<Object> delRows(HttpServletRequest request,String scCode,String rows) throws Exception {
        scoresService.delRows("score_100001",rows);
        return new ReturnT<>();
    }

    @RequestMapping("/insRows")
    @ResponseBody
    public ReturnT<Object> insRows(HttpServletRequest request,String scCode,String rows) throws Exception {
        scoresService.insRows("score_100001",rows);
        return new ReturnT<>();
    }


    @RequestMapping("/getRowsList")
    @ResponseBody
    public ReturnT<Object> getRowsList(HttpServletRequest request,String scCode) throws Exception {
//        String sessionscCode = request.getSession().getAttribute("scCode").toString();
//        if (scCode == null && scCode.length() == 0) {
//            //超级管理员
//            scCode = "100001";
//        }
        List<String> list = scoresService.getRows("score_100001");
        return new ReturnT<>(list);
    }







    @RequestMapping("/testTable")
    @ResponseBody
    public void testTable() throws Exception {
        scoresService.update("score_100001","sheet1","D:\\test.xlsx");
    }

    @RequestMapping("/testList")
    @ResponseBody
    public ReturnT<Object> testList() throws Exception {
        List<Map<String,Object>> list = scoresService.list("score_100001",null,null,null,10,1);
        return  new ReturnT<>(list);
    }

    @RequestMapping("/testInsert")
    @ResponseBody
    public void testInsert() throws Exception {

        List <Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();

        List<String> listRows = scoresService.getRows("score_100001");
        for (String row: listRows
                ) {
            map.put(row,row);
        }
        list.add(map);
        scoresService.insert("score_100001",list);
    }



}
