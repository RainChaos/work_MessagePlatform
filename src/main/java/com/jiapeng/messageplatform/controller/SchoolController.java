package com.jiapeng.messageplatform.controller;

import com.jiapeng.messageplatform.annotation.LoginLimit;
import com.jiapeng.messageplatform.dao.SchoolDBMapper;
import com.jiapeng.messageplatform.entity.SchoolDB;
import com.jiapeng.messageplatform.entity.SchoolEntity;
import com.jiapeng.messageplatform.entity.SchoolWX;
import com.jiapeng.messageplatform.service.SchoolService;
import com.jiapeng.messageplatform.utils.DataGridJson;
import com.jiapeng.messageplatform.utils.ReturnT;
import com.jiapeng.messageplatform.utils.SessionUtil;
import jdk.nashorn.internal.ir.ReturnNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/school")
public class SchoolController{
    @Autowired
    SchoolService schoolService;

    /**
     * 添加学校，参数需要：name,address,contacts,contactPhone
     *
     * @param entity
     * @return
     */
    @PostMapping("/addSchool")
    public ReturnT<Object> addSchool(HttpServletRequest request, SchoolEntity entity) {

        //判断是系统管理员还是学校管理员【学校代码为空，说明是系统管理员，不为空说明微学校管理员】
        String scCode = request.getSession().getAttribute("scCode").toString();
        if (scCode != null && scCode.length() != 0) {
            return new ReturnT<>(500, "权限不足，无法操作！");
        }

        //系统超管
        schoolService.add(entity);
        return new ReturnT<>();

    }

    /**
     * 修改学校，参数需要：sc_code，name,address,contacts,contactPhone
     *
     * @param entity
     * @return
     */
    @RequestMapping("/updateSchool")
    public ReturnT<Object> updateSchool(SchoolEntity entity) {
        schoolService.update(entity);
        return new ReturnT<>();
    }

    @PostMapping("/setMenhuKey")
    public ReturnT<Object> setMenhuKey(SchoolEntity entity) {
        schoolService.setMenhuKey(entity);
        return new ReturnT<>();
    }


    @PostMapping("/delSchool")
    public ReturnT<Object> delSchool(HttpServletRequest request, String sc_code) throws Exception {
        //判断是系统管理员还是学校管理员【学校代码为空，说明是系统管理员，不为空说明微学校管理员】
        String scCode = request.getSession().getAttribute("scCode").toString();
        if (!"".equals(scCode) && sc_code.equals(scCode)) {
            return new ReturnT<>(500, "权限不足，无法操作！");
        }
        schoolService.del(sc_code);
        return new ReturnT<>();
    }

    /**
     * @param status true为可用，false为不可用
     * @return
     */
    @PostMapping("/setStatus")
    public ReturnT<Object> setStatus(HttpServletRequest request, String sc_code, boolean status, String reason) {
        //判断是系统管理员还是学校管理员【学校代码为空，说明是系统管理员，不为空说明微学校管理员】
        String scCode = request.getSession().getAttribute("scCode").toString();
        if (scCode != null && scCode.length() != 0) {
            return new ReturnT<>(500, "权限不足，无法操作！");
        }
        SchoolEntity entity = new SchoolEntity();
        entity.setSc_code(sc_code);
        entity.setStatus(status);
        entity.setStatusReason(reason);
        entity.setStatusTime(new Date());
        schoolService.setStatus(entity);
        return new ReturnT<>();
    }


    @PostMapping("/listSchool")
    @ResponseBody
    public ReturnT<Object> listSchool(HttpServletRequest request, String key, int limit, int page) {
        //判断是系统管理员还是学校管理员【学校代码为空，说明是系统管理员，不为空说明微学校管理员】
        String scCode = request.getSession().getAttribute("scCode").toString();
        DataGridJson obj = schoolService.list(scCode, key, limit, page);
        return new ReturnT<>(obj);
    }

    @PostMapping("/changeToken")
    public ReturnT<Object> changeToken(String sc_code) {
        schoolService.updateToken(sc_code);
        return new ReturnT<>();
    }

    @PostMapping("/listGradeTree")
    @ResponseBody
    public List listGradeTree(String sc_code, String teNo, String requestType) {
        //获取班级的
        List list = schoolService.listGradeTree(sc_code, teNo, requestType);
        return list;
    }


    //学校的白名单ip地址设置

    /**
     * @param sc_code
     * @return
     */
    @PostMapping("/setIP")
    public ReturnT<Object> setIP(String sc_code, String ipAddr) {
        schoolService.setIP(sc_code, ipAddr);
        return new ReturnT<>();
    }

    @PostMapping("/getIPList")
    public ReturnT<Object> getIPList(String sc_code) {
        return new ReturnT<>(schoolService.getIPListByScCode(sc_code));
    }


    //ip地址白名单检查
    @PostMapping("/checkIpAddr")
    public ReturnT<Object> checkIpAddr(String sc_code, String ipAddr) throws Exception {
        schoolService.whiteListCheck(sc_code, ipAddr);
        return new ReturnT<>();
    }


    /**
     * 修改学校微信配置
     * by hzl 2019-9-6 15:40:57
     *
     * @param schoolWX
     * @return
     * @throws Exception
     */
    @PostMapping("/updateSchoolWx")
    public ReturnT<Object> updateSchoolWX(SchoolWX schoolWX) throws Exception {
        schoolService.updateSchoolWx(schoolWX);
        return new ReturnT<>();
    }

    /**
     * 获取学校微信配置
     * by hzl 2019-9-6 15:40:57
     *
     * @param scCode
     * @return
     * @throws Exception
     */
    @PostMapping("/getSchoolWx")
    public ReturnT<Object> getSchoolWx(String scCode) throws Exception {
        SchoolWX schoolWX = schoolService.loadSchoolWX(scCode);
        return new ReturnT<>(schoolWX);
    }



    /**
     * 修改学校数据库配置
     * by hzl 2019-11-6 17:42:36
     *
     * @param schoolDB
     * @return
     * @throws Exception
     */
    @LoginLimit(limit = false)
    @PostMapping("/updateSchoolDB")
    public ReturnT<Object> updateSchoolDB(SchoolDB schoolDB) throws Exception {
        schoolService.updateSchoolDB(schoolDB);
        return new ReturnT<>();
    }

    /**
     * 获取学校数据库配置
     * by hzl  2019-11-6 17:42:36
     *
     * @param scCode
     * @return
     * @throws Exception
     */
    @LoginLimit(limit = false)
    @PostMapping("/getSchoolDB")
    public ReturnT<Object> getSchoolDB(String scCode) throws Exception {
        SchoolDB SchoolDB = schoolService.loadSchoolDB(scCode);
        return new ReturnT<>(SchoolDB);
    }


    @PostMapping("/addSchoolDB")
    public ReturnT<Object> addSchoolDB(HttpServletRequest request, SchoolDB entity) {
        schoolService.addSchollDB(entity);
        return new ReturnT<>();

    }


    @PostMapping("/listSchoolDB")
    public ReturnT<Object> listSchoolDB(HttpServletRequest request,int limit, int page) {
        DataGridJson obj = schoolService.listSchoolDB(limit, page);
        return new ReturnT<>(obj);
    }
}
