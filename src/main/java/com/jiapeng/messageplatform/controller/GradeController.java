package com.jiapeng.messageplatform.controller;

import com.jiapeng.messageplatform.entity.GradeEntity;
import com.jiapeng.messageplatform.service.DataSyncService;
import com.jiapeng.messageplatform.service.GradeService;
import com.jiapeng.messageplatform.utils.DataGridJson;
import com.jiapeng.messageplatform.utils.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/grade")
public class GradeController{
    @Autowired
    GradeService gradeService;
    @Autowired
    private DataSyncService dataSyncService;

    /**
     * 添加年级，参数需要：name,orderNumber,sc_code
     * @param entity
     * @return
     */
    @PostMapping("/addGrade")
    public ReturnT<Object> addGrade(HttpServletRequest request, GradeEntity entity)throws Exception{
        String UserscCode = request.getSession().getAttribute("scCode").toString();
        if(UserscCode == null && UserscCode.length() == 0){
            return new ReturnT<>(500,"权限不足，无法操作！");
        }
        gradeService.add(entity);
        dataSyncService.organizeSync(entity,"0");
        return new ReturnT<>();
    }



    /**
     * 修改年级，参数需要：gr_code,name,orderNumber
     * @param entity
     * @return
     */
    @PostMapping("/updateGrade")
    public ReturnT<Object> updateGRade(HttpServletRequest request,GradeEntity entity) throws Exception{
        String UserscCode = request.getSession().getAttribute("scCode").toString();
        if(UserscCode == null && UserscCode.length() == 0){
            return new ReturnT<>(500,"权限不足，无法操作！");
        }
        gradeService.update(entity);
        dataSyncService.organizeSync(entity,"0");
        return  new ReturnT<>();
    }

    @PostMapping("/delGrade")
    public ReturnT<Object> delGrade(HttpServletRequest request,String gr_code) throws Exception{
        String scCode = request.getSession().getAttribute("scCode").toString();
        if(scCode == null && scCode.length() == 0){
            return new ReturnT<>(500,"权限不足，无法操作！");
        }
        dataSyncService.organizeDelSync(gr_code,"5");
        gradeService.del(gr_code);
        return new ReturnT<>();
    }

    @PostMapping("/listGrade")
    public ReturnT<Object> listGrade(String sc_code){
        if (sc_code==null||"".equals(sc_code)){
            return new ReturnT<>(new DataGridJson(0,null));
        }

        List l = gradeService.list(sc_code);
        DataGridJson obj = new DataGridJson(l);
        return new ReturnT<>(obj);
    }

    @PostMapping("/listSchoolGrade")
    public ReturnT<Object> listSchoolGrade(String sc_code){
        List l = gradeService.list(sc_code);
        DataGridJson obj = new DataGridJson(l);
        return new ReturnT<>(obj);
    }
}
