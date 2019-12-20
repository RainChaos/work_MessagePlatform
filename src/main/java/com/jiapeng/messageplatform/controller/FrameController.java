package com.jiapeng.messageplatform.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.json.async.NonBlockingJsonParser;
import com.jiapeng.messageplatform.annotation.LoginLimit;
import com.jiapeng.messageplatform.dao.UserProvider;
import com.jiapeng.messageplatform.entity.ModuleEntity;
import com.jiapeng.messageplatform.entity.RoleEntity;
import com.jiapeng.messageplatform.entity.SchoolEntity;
import com.jiapeng.messageplatform.entity.UserEntity;
import com.jiapeng.messageplatform.service.*;
import com.jiapeng.messageplatform.utils.*;
import com.sun.org.apache.xpath.internal.objects.XObject;
import com.sun.org.apache.xpath.internal.objects.XObjectFactory;
import jdk.nashorn.internal.ir.BaseNode;
import jdk.nashorn.internal.ir.ReturnNode;
import org.apache.catalina.Session;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/frame")
public class FrameController{
    @Autowired
    ModuleService moduleService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserService userService;
    @Resource
    GetRootPath getRootPath;
   //为获取结构
    @Autowired
    SchoolService schoolService;
    @Autowired
    DBChangeService dbChangeService;


    //系统所有菜单列表【供系统所有菜单管理】
    @RequestMapping(value = "/getModuleTree")
    @ResponseBody
    public XjpResultUtil getModuleTree()  {
        List<ModuleEntity> list = moduleService.getAllList("");
        XjpResultUtil resultlist=new XjpResultUtil();
        resultlist.setCode(0);
        resultlist.setCount(list.size()+0L);
        resultlist.setData(list);
        return resultlist;
    }


    //给角色分配权限模块【展示所有的系统菜单】
    @PostMapping("/getModuleTree1")
    @ResponseBody
    public List<ModuleEntity> getModuleTree1(String uid)  {
        List<ModuleEntity> list = moduleService.getAllList(uid);
        return list;
    }


    /**
     * 添加模块信息
     * 参数为模块实例中的属性
     * @param entity
     * @return
     */
    @PostMapping("/addModule")
    @ResponseBody
    public ReturnT<Object> addModule(ModuleEntity entity){
        if("".equals(entity.getParentId())||entity.getParentId() == null){
            entity.setParentId(null);
        }
        String id = moduleService.add(entity);
        return new ReturnT<>(id);
    }



    @PostMapping("/updateModule")
    public ReturnT<Object> updateModule(ModuleEntity entity){
        moduleService.update(entity);
        return new ReturnT<>();
    }

    @PostMapping("/delModule")
    @ResponseBody
    public ReturnT<Object> delModule(String id){
        moduleService.del(id);
        return new ReturnT<>();
    }
    //#endregion

    //region 角色
    @PostMapping("/addRole")
    public ReturnT<Object> addRole(String name){
        RoleEntity entity = new RoleEntity();
        entity.setName(name);
        String id = roleService.add(entity);
        return new ReturnT<>(id);
    }

    @PostMapping("/updateRole")
    public ReturnT<Object> updateRole(String id,String name){
        RoleEntity entity = new RoleEntity();
        entity.setId(id);
        entity.setName(name);
        roleService.update(entity);
        return new ReturnT<>();
    }

    @PostMapping("/delRole")
    public ReturnT<Object> delRole(String id){
        roleService.del(id);
        return new ReturnT<>(id);
    }


    @PostMapping("/listRole")
    public ReturnT<Object> listRole(){
        List<Map<String,Object>> list = roleService.getAll();
        DataGridJson dataGridJson = new DataGridJson(list);
        return new ReturnT<>(0,dataGridJson,"");
    }

    //设置角色跟菜单相关联
    @PostMapping("/setRoleMoudle")
    public ReturnT<Object> setModules(String rid,String[] mids){
        roleService.setModules(rid,mids);
        return new ReturnT<>();
    }


    //获取指定角色的菜单树
    @PostMapping("/getRoleModule")
    public ReturnT<Object> getModules(String rid){
        DataGridJson dataGridJson = new DataGridJson( roleService.getModules(rid));
        return new ReturnT<>(dataGridJson);
    }

    //endregion
    //region 用户
    /**
     * 添加用户信息
     * 目前只添加用户名和密码
     * @return
     * @throws Exception
     */
    @PostMapping("/addUser")
    public ReturnT<Object> addUser(String name,String passWord,String scCode) throws Exception {

        UserEntity entity = new UserEntity();
        entity.setName(name);
        entity.setPassWord(passWord);
        if(scCode==null&&scCode.length()==0){
            scCode = null;
        }
        entity.setScCode(scCode);
        userService.add(entity);
        return new ReturnT<>();
    }

    /**
     * 修改用户信息，如果密码不为空，则一起修改密码
     * 目前修改的用户信息只有用户名和密码
     * @return
     */
    @PostMapping("/updateUser")
    public ReturnT<Object> updateUser(int id,String name,String passWord){
        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setPassWord(passWord);
        userService.update(entity);
        return new ReturnT<>();
    }

    @PostMapping("/getUser")
    public ReturnT<Object> getUser(int id){
        UserEntity userEntity = userService.get(id);
        return new ReturnT<>(userEntity);
    }

    @PostMapping("/delUser")
    public ReturnT<Object> delUser(int id){
        userService.del(id);
        return new ReturnT<>();
    }

    /**
     * 禁止/允许登录
     * @param id
     * @param disLog
     * @param reason
     * @return
     */
    @PostMapping("/setUserDis")
    public ReturnT<Object> setUserDis(int id,boolean disLog,String reason){
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setDislog(disLog);
        userEntity.setDislogReason(reason);
        userEntity.setDisTime(new Date());
        userService.setDisLog(userEntity);
        return new ReturnT<>();
    }

    @PostMapping("/listUser")
    @ResponseBody
    public ReturnT<Object> listUser(String key,int limit,int page){
        DataGridJson obj = userService.listUser(key,limit,page);
        return new ReturnT<>(obj);
    }

    @PostMapping("/getUserRoles")
    @ResponseBody
    public ReturnT<Object> getUserRole(int id){
        List<Map<String,Object>> list = userService.getRoles(id);
        DataGridJson obj = new DataGridJson(list.size(),list);
        return new ReturnT<>(obj);
    }

    @PostMapping("/setUserRoles")
    public ReturnT<Object> setUserRole(int uid,String[] rids){
        userService.setRoles(uid,rids);
        return  new ReturnT<>();
    }


    @PostMapping("/logon")
    @LoginLimit(limit = false)
    @ResponseBody
    public ReturnT<Object> logon(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String name = request.getParameter("name");
        String passWord = request.getParameter("passWord");
        String verifyCode  = request.getParameter("verifyCode");

        String scCode = request.getParameter("scCode");
        if(!"admin".equals(scCode)){
            //学校管理员账号
            dbChangeService.changeDb(scCode);
        }else {
            //超级管理员账号
            scCode = null;
        }
        UserEntity user= userService.logon(request,name,passWord,verifyCode);
        request.getSession().setAttribute("id", user.getId());//设置session
        request.getSession().setAttribute("name", user.getName());//设置session

//        String scCode ="";
//        if(user.getScCode()!=null){
//            scCode = user.getScCode();
//        }
        SessionUtil.setSession(request, "scCode", scCode);
        request.getSession().setAttribute("tree",userService.getModuleTree(user.getId()));//用户权限树

        Cookie cName = new Cookie("name", URLEncoder.encode(user.getName(), "utf-8"));
        Cookie cId = new Cookie("id", String.valueOf(user.getId()));
        cName.setPath("/");
        cId.setPath("/");
        response.addCookie(cName);
        response.addCookie(cId);
        return new ReturnT<>();
    }

    /**
     * 登录用户修改自己密码
     * @return
     */
    @PostMapping("/updatePass")
    public ReturnT<Object> updatePass(HttpServletRequest request) throws Exception {
        String passWord = request.getParameter("passWord");
        String name = request.getSession().getAttribute("name").toString();
        userService.updatePass(name,passWord);
        return new ReturnT<>();
    }

    /**
     * 当前用户的权限树
     * @param request
     * @return
     */
    @RequestMapping("/getUserModuleTree")
    @ResponseBody
    public List<Map<String,Object>> getUserModuleTree(HttpServletRequest request){
        int uid = Integer.valueOf( request.getSession().getAttribute("id").toString());
        List<Map<String,Object>> tree = userService.getModuleTree(uid);
        return tree;
    }
    //endregion



    @PostMapping("/listSchool")

    public List listSchool(HttpServletRequest request,String sc_code,String teNo,String requestType){
        if ("".equals(sc_code)||sc_code==null){
            //获取所有学校
            String scCode = request.getSession().getAttribute("scCode").toString();
            DataGridJson obj = schoolService.list(scCode,"",100,1);
            List<SchoolEntity> list =(List<SchoolEntity>)obj.getRows();

            return list;
        } else{
            //根据学校代码获取该学校的年级
            List list = schoolService.listGradeTree(sc_code,teNo,requestType);
            return  list;
        }
    }
}


