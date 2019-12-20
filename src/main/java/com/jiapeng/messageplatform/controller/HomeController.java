package com.jiapeng.messageplatform.controller;

import com.alibaba.fastjson.JSONObject;
import com.jiapeng.messageplatform.annotation.LoginLimit;
import com.jiapeng.messageplatform.service.SchoolService;
import com.jiapeng.messageplatform.service.WxService;
import com.jiapeng.messageplatform.utils.*;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

/**
 * Created by HZL on 2019/5/9.
 */
@Controller
public class HomeController{
    @Autowired
    SchoolService schoolService;

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("/")
    @LoginLimit(limit = false)
    public String defaultPage() {
        return "forward:/login";
    }

    @RequestMapping("/index")
    public String indexPage(HttpServletRequest request, Model model) {
        return "classic/index";
    }

    @RequestMapping("/teacher/index")
    @LoginLimit(type = "teacher")
    public String teacherIndexPage(HttpServletRequest request, Model model) {
        model.addAttribute("openid",request.getParameter("wxNo"));
        return "classic/teacherIndex";
    }

    @RequestMapping("/main")
    public String mainPage(HttpServletRequest request, Model model) {
        return "classic/main";
    }

    //登录页
    @RequestMapping("/login")
    @LoginLimit(limit = false)
    public String loginPage(HttpServletRequest request,Model model) throws Exception {
        String openId = RequestHelper.getRequest(request, "openid", true);
        SessionUtil.setSession(request, Constants.OPENID, openId);
        model.addAttribute("openid",openId);
        DataGridJson obj = schoolService.list(null, "", 10000, 1);
        model.addAttribute("schoollist", new ReturnT<>(obj));
        return "classic/login";
    }

    //退出方法
    @RequestMapping("/logout")
    @LoginLimit(limit = false)
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String openid = SessionUtil.getSession(request,Constants.OPENID).toString();
        session.removeAttribute(Constants.LOGIN_TEACHER_NO);
        session.removeAttribute(Constants.OPENID);
        session.removeAttribute("scCode");
        return "forward:/login?openid="+openid;
    }


    //管理员退出方法
    @RequestMapping("/admin/logout")
    @LoginLimit(limit = false)
    public String adminLogout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("id");
        session.removeAttribute("name");
        session.removeAttribute("tree");
        session.removeAttribute("scCode");
        String openid = SessionUtil.getSession(request,Constants.OPENID).toString();
        session.removeAttribute(Constants.OPENID);
        return "forward:/login?openid="+openid;
    }


    //获取验证码
    @RequestMapping(value = "/getverify")
    @LoginLimit(limit = false)
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            //表明生成的响应是图片
            response.setContentType("image/jpeg");
            GraphicsUtil graphicsUtil = new GraphicsUtil();
            graphicsUtil.createImg(request, response);
        } catch (Exception e) {
            logger.error("获取验证码失败>>>> ", e);
            e.printStackTrace();
        }
    }

    @Value("${system.config.downpath}")
    private String downPath;

    /**
     * excel文件上传方法，上传成功后返回sheet列表名称
     * by hzl 2019-8-19 09:10:45
     *
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/uploadExcel")
    @ResponseBody
    public ReturnT<Object> getSheets(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new Exception("上传失败，请选择文件");
        }
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (!ext.equals("xls") && !ext.equals("xlsx")) {
            throw new Exception("只支持上传xls或xlsx格式的文件");
        }
        String filePath = downPath + "upload/";
        if (!new File(filePath).exists()) {
            new File(filePath).mkdirs();
        }

        File dest = new File(filePath + fileName);
        file.transferTo(dest);
        //解析出工作表
        List<String> list = new Excel().getSheets(filePath + fileName);
        Map<String, Object> map = new HashMap<>();
        map.put("fileName", filePath + fileName);
        map.put("sheetName", list);
        return new ReturnT<>(map);
    }


//    @RequestMapping("/test1")
//    @ResponseBody
//    @LoginLimit(limit = false)
//    public ReturnT<Object> test(HttpServletRequest request) throws Exception {
////        int bcs = Integer.valueOf(RequestHelper.getRequest(request, "bcs"));
////        int cs = Integer.valueOf(RequestHelper.getRequest(request, "cs"));
//        ApplicationHome h = new ApplicationHome(getClass());
//        File jarF = h.getSource();
//        String xlsFile = jarF.getParentFile().toString() + "/sda/ss.as";
//        return new ReturnT<>(xlsFile);
//    }
//
//    @RequestMapping("/band")
//    @LoginLimit(limit = false)
//    public String band(HttpServletRequest request) throws Exception {
//        return "classic/wxPage/band";
//    }
//
//    @Autowired
//    WxService wxService;
//    @Autowired
//    WxMpService wxMpService;
//
//    @GetMapping("/authorize")
//    public String getAuthorUrl(HttpServletRequest request) {
//
//        StringBuffer url = request.getRequestURL();
//        //项目域名
//        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append(request.getServletContext().getContextPath()).append("/").toString();
//        //回调地址
//        String redirectUrl = tempContextUrl + "home/wxuserinfo";
//        //回调state参数
//        String state = "";
//        return "redirect:" + wxMpService.oauth2buildAuthorizationUrl(redirectUrl, WxConsts.OAuth2Scope.SNSAPI_BASE, state);
//        //wxService.getAuthorUrl(redirectUrl, "");
//    }
//
//    @RequestMapping("/wxuserinfo")
//    @LoginLimit(limit = false)
//    public String wxuserinfo(HttpServletRequest request, @RequestParam("code") String code,
//                             @RequestParam("state") String returnUrl, Model model) throws Exception {
//        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
//        try {
//            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
//        } catch (WxErrorException e) {
//            logger.error("【微信网页授权】{}", e);
//            //抛出异常 自定义的  方便处理  可自己定义
//            throw new Exception("微信网页授权异常" + e.getError().getErrorMsg());
//        }
//
//        String openId = wxMpOAuth2AccessToken.getOpenId();
//        model.addAttribute("openId", openId);
//        //  return "redirect:" + returnUrl + "?openid=" + openId;
//
//
////        Map<String, String> map = new HashMap<String, String>();
////        Enumeration paramNames = request.getParameterNames();
////        while (paramNames.hasMoreElements()) {
////            String paramName = (String) paramNames.nextElement();
////            String[] paramValues = request.getParameterValues(paramName);
////            if (paramValues.length == 1) {
////                if (paramValues[0].length() != 0) {
////                    map.put(paramName, paramValues[0]);
////                }
////            }
////        }
////        //打印map所有值
////        Set<Map.Entry<String, String>> set = map.entrySet();
////        System.out.println("===========================");
////        for (Map.Entry entry : set) {
////            System.out.println(entry.getKey() + ":" + entry.getValue());
////        }
////        System.out.println("===========================");
//        return "classic/wxPage/wxuserinfo";
//    }
//
//    @RequestMapping("/sendmsg")
//    @ResponseBody
//    @LoginLimit(limit = false)
//    public String sendmsg(HttpServletRequest request) throws Exception {
//        try {
//            WxMpKefuMessage wxMpKefuMessage = new WxMpKefuMessage();
//            //     wxMpKefuMessage.setToUser("oIWhp1leHgRAjVEH8wWIF6RrFtqM");
//            wxMpKefuMessage.setToUser("oIWhp1uG2B9Grip1zOr7fQI0hvNw");
//            wxMpKefuMessage.setMsgType(WxConsts.KefuMsgType.TEXT);
//            wxMpKefuMessage.setContent("This is a test message！");
//            boolean result = wxMpService.getKefuService().sendKefuMessage(wxMpKefuMessage);
//            for (int i = 0; i < 20; i++) {
//                wxMpService.getKefuService().sendKefuMessage(wxMpKefuMessage);
//            }
//            return String.valueOf(result);
//        } catch (WxErrorException e) {
//            logger.error("【微信网页授权】{}", e);
//            //抛出异常 自定义的  方便处理  可自己定义
//            throw new Exception("微信网页授权异常" + e.getError().getJson());
//        }
//    }
//
//    @RequestMapping("/sendtempmsg")
//    @ResponseBody
//    @LoginLimit(limit = false)
//    public String orderStatus(HttpServletRequest request) {
//        //数据
//        List<WxMpTemplateData> data = Arrays.asList(
//                new WxMpTemplateData("first", "您关注的学生有一条校园通知\n"),
//                new WxMpTemplateData("keyword1", "桂林佳朋一中学生\n所在班级：三年级1班\n学生姓名：moon"),
//                new WxMpTemplateData("keyword2", "李老师"),
//                new WxMpTemplateData("keyword3", "2019-8-14 14:52:54\n"),
//                new WxMpTemplateData("keyword4", "您好！＂校讯通＂已成功在我校开通，我们会把贵子弟在校情况的信息发给您，也请您多与班主任、科任老师联系，我校的教育教学工作需要您的配合与支持，希望家长积极参与并多提宝贵意见，共同把贵子弟教育好。您好！＂校讯通＂已成功在我校开通，我们会把贵子弟在校情况的信息发给您，也请您多与班主任、科任老师联系，我校的教育教学工作需要您的配合与支持，希望家长积极参与并多提宝贵意见，共同把贵子弟教育好。您好！＂校讯通＂已成功在我校开通，我们会把贵子弟在校情况的信息发给您，也请您多与班主任、科任老师联系，我校的教育教学工作需要您的配合与支持，希望家长积极参与并多提宝贵意见，共同把贵子弟教育好。！\n"),
//                new WxMpTemplateData("remark", "点击查看详情")
//        );
//
//        //获取项目域名
//        StringBuffer url = request.getRequestURL();
//        String localDomain = url.delete(url.length() - request.getRequestURI().length(), url.length()).append(request.getServletContext().getContextPath()).append("/").toString();
//
//        //2,推送消息
//        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
//                .toUser("oIWhp1leHgRAjVEH8wWIF6RrFtqM")//要推送的用户openid
//                .data(data) //数据
//                .templateId("hVMN84iVwGI_TzMBoCk3UDq3uy3ow655xjvGHX3K7Jk")//模版id
//                .url("http://1e8x752468.iask.in:20731/page/messageDetails?id=33")//点击模版消息要访问的网址*/
//                .build();
//        //发起推送
//        try {
//            String msg = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
//            System.out.println("推送成功：" + msg);
//            return ("推送成功：" + msg);
//        } catch (Exception e) {
//            System.out.println("推送失败：" + e.getMessage());
//            e.printStackTrace();
//            return ("推送失败：" + e.getMessage());
//        }
//    }

}
