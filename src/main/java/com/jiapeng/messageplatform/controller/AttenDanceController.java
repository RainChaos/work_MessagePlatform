package com.jiapeng.messageplatform.controller;

import com.jiapeng.messageplatform.annotation.LoginLimit;
import com.jiapeng.messageplatform.service.AscCountService;
import com.jiapeng.messageplatform.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2019/11/21.
 */
@Controller
@RequestMapping("/att")
public class AttenDanceController{
    @Autowired
    private AscCountService ascCountService;

    /**
     * 获取考勤记录
     * by xie 2019-11-20 10:56:43
     * @param
     * @return
     */
    @LoginLimit(limit = false)
    @PostMapping("/list")
    @ResponseBody
    public Map getAttendList(String page,String limit,String startDate,String endDate,String sc_code,String gr_code,String cl_code,String stuNo)throws Exception {
            String flag = "school";
            String code = sc_code;

            if(gr_code!=null&&cl_code==null){
                flag = "grade";
                code = gr_code;
            }
            if(gr_code!=null&&cl_code!=null){
                flag = "class";
                code=cl_code;
            }
            if(sc_code==null){
                throw new  Exception("请先选择学校！");
            }
            PageResult list = ascCountService.studentAttendCount( Integer.valueOf(page),  Integer.valueOf(limit), startDate, endDate,sc_code, code,flag,stuNo);
            Map result = new HashMap();
            result.put("code", 200);
            result.put("data", list.getData());
            result.put("total", list.getTotal());
            result.put("msg", "");
            return result;
    }
}
