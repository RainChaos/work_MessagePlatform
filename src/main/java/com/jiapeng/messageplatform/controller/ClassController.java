package com.jiapeng.messageplatform.controller;

import com.jiapeng.messageplatform.dao.ClassMapper;
import com.jiapeng.messageplatform.entity.ClassEntity;
import com.jiapeng.messageplatform.service.ClassService;
import com.jiapeng.messageplatform.service.DataSyncService;
import com.jiapeng.messageplatform.utils.DataGridJson;
import com.jiapeng.messageplatform.utils.ReturnT;
import jxl.read.biff.BiffException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/class")
public class ClassController{
    @Autowired
    ClassService classService;
    @Autowired
    ClassMapper classMapper;
    @Autowired
    DataSyncService dataSyncService;

    /**
     * 添加班级，参数需要：cl_number,name,sc_code,gr_code
     *
     * @param entity
     * @return
     */
    @PostMapping("/addClass")
    public ReturnT<Object> addClass(ClassEntity entity) {
        classService.add(entity);
        dataSyncService.organizeSync(entity,"0");
        return new ReturnT<>();
    }

    /**
     * 修改班级，参数需要：cl_code,cl_number,name,gr_code
     *
     * @param entity
     * @return
     */
    @PostMapping("/updateClass")
    public ReturnT<Object> updateClass(ClassEntity entity) {
        classService.update(entity);
        dataSyncService.organizeSync(entity,"0");
        return new ReturnT<>();
    }

    @PostMapping("/delClass")
    public ReturnT<Object> delClass(String cl_code) throws Exception {
        dataSyncService.organizeDelSync(cl_code,"6");
        classService.del(cl_code);
        return new ReturnT<>();
    }

    @PostMapping("/listClass")
    public ReturnT<Object> listClass(@RequestParam(value = "sc_code", defaultValue = "1") String sc_code, @RequestParam(value = "gr_code", defaultValue = "1") String gr_code, int limit, int page) {
        List l = classService.list(sc_code, gr_code, limit, page);
        int count = classMapper.count(sc_code, gr_code);
        DataGridJson obj = new DataGridJson(count, l);
        return new ReturnT<>(obj);
    }

    /**
     * 根据年级代码获取所有班级
     * by hzl 2019-10-29 18:06:58
     *
     * @param gr_code
     * @return
     */
    @PostMapping("/listByGrCode")
    public ReturnT<Object> listByGrCode(@RequestParam(value = "gr_code") String gr_code) {
        List<Map<String, Object>> list = classMapper.listByGradeCode(gr_code);
        return new ReturnT<>(list);
    }

    /**
     * 导入班级，需要参数：sc_code,file
     *
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/impClass")
    public ReturnT<Object> impClass(@RequestParam(value = "file") MultipartFile file, HttpServletRequest request) throws IOException, BiffException {
        String path = request.getSession().getServletContext().getRealPath("") + "xls";
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        path += "/" + file.getOriginalFilename();
        file.transferTo(new File(path));
        String sc_code = request.getParameter("sc_code");
        classService.imp(sc_code, path);
        return new ReturnT<>(path);
    }
}
