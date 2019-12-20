package com.jiapeng.messageplatform.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiapeng.messageplatform.dao.MessageLogMapper;
import com.jiapeng.messageplatform.dao.StudentMapper;
import com.jiapeng.messageplatform.dao.TeacherMapper;
import com.jiapeng.messageplatform.entity.*;
import com.jiapeng.messageplatform.gate.entity.AutoRecord;
import com.jiapeng.messageplatform.model.ComeInOutRecord;
import com.jiapeng.messageplatform.model.NameAndValue;
import com.jiapeng.messageplatform.service.*;
import com.jiapeng.messageplatform.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by HZL on 2019/8/24.
 */

@RequestMapping("/api")
@RestController
public class ApiController{
    @Autowired
    SchoolService schoolService;
    @Autowired
    MessageService messageService;
    @Autowired
    LeaveService leaveService;
    @Autowired
    ConfigService configService;
    @Autowired
    AscCountService ascCountService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    MessageLogMapper messageLogMapper;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    StudentService studentService;

    @Autowired
    private GradeService gradeService;
    @Autowired
    private ClassService classService;

    @Autowired
    private DataSyncService dataSyncService;

    //api接口加密秘钥
    @Value("${config.api.key}")
    public String apiKey;

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    /**
     * 提供给门禁系统查询学生请假信息接口
     * by hzl 2019-8-26 16:54:01
     *
     * @param request
     * @return
     */
    @RequestMapping("/getleaveinfo")
    public ReturnT<Object> getStuLeaveInfo(HttpServletRequest request) {
        try {
            String ipAddr = RequestHelper.getIpAddress(request);
            logger.info("/getleaveinfo ip:" + ipAddr);
            String scCode = RequestHelper.getRequest(request, "sc_code");
            String stuNo = RequestHelper.getRequest(request, "stu_no");
            String actionTimeStr = RequestHelper.getRequest(request, "action_time");//出入时间
            String nonceStr = RequestHelper.getRequest(request, "nonce_str");//随机字符串
            String sign = RequestHelper.getRequest(request, "sign");
            Date actionTime = VeDate.strToDateLong(actionTimeStr);

            //0.白名单检查
            // schoolService.whiteListCheck(scCode, ipAddr);
            //1.把传递过来的参数转为Map
            Map<String, Object> paramsMap = RequestHelper.getParameterMap(request);
            //2.获取对Map进行加密的结果
            String localSign = Md5Util.generateSignature(paramsMap, apiKey);
            if (!localSign.equals(sign)) {
                throw new Exception("sign error");
            }
            //3.获取学生请假信息,如果有则返回，没有则抛出异常
            Map<String, Object> map = leaveService.getLeaveInfo(scCode,stuNo, actionTime);
            return new ReturnT<>(map);
        } catch (Exception e) {
            return ReturnT.getFail(e.getMessage());
        }
    }

    /**
     * 提供给门禁系统发送信息通知接口
     * by hzl 2019-8-24 15:03:23
     *
     * @param request
     * @return
     */
    @PostMapping("/ascnotice")
    public ReturnT<Object> ascNotice(HttpServletRequest request) {
        try {
            String ipAddr = RequestHelper.getIpAddress(request);
            logger.info("/ascnotice ip:" + ipAddr);
            String scCode = RequestHelper.getRequest(request, "sc_code");
            String stuNo = RequestHelper.getRequest(request, "stu_no");
            String leaveId = RequestHelper.getRequest(request, "leave_id", true);
            String imgName = RequestHelper.getRequest(request, "image_name", true);
            Integer ascType = Integer.valueOf(RequestHelper.getRequest(request, "asc_type"));
            String ascDeviceNo = RequestHelper.getRequest(request, "device_no");
            Integer actionType = Integer.valueOf(RequestHelper.getRequest(request, "action_type"));
            String actionTime = RequestHelper.getRequest(request, "action_time");
            String nonceStr = RequestHelper.getRequest(request, "nonce_str");//随机字符串
            String sign = RequestHelper.getRequest(request, "sign");
            String ascRemark = "";

            //0.白名单检查
//            schoolService.whiteListCheck(scCode, ipAddr);
            //1.把传递过来的参数转为Map
            Map<String, Object> paramsMap = RequestHelper.getParameterMap(request);
            logger.info("INFO：" + JSONObject.toJSONString(paramsMap));
            //2.获取对Map进行加密的结果
            String localSign = Md5Util.generateSignature(paramsMap, apiKey);
            if (!localSign.equals(sign)) {
                throw new Exception("sign error");
            }
            //3.调用门禁消息通知接口
            messageService.ascNoticeSendInterface(scCode, stuNo, leaveId, imgName, ascDeviceNo, ascType, actionType, actionTime, ascRemark);
            return new ReturnT<>("success");
        } catch (Exception e) {
            return ReturnT.getFail(e.getMessage());
        }
    }

    @Value("${system.config.downpath}")
    private String downPath;

    @Value("${config.api.ascimagemaxsize}")
    private String ascImageMaxSize;

    /**
     * 提供门禁系统调用的图片上传接口
     *
     * @param request
     * @param base64Data
     * @return
     */
    @PostMapping("/imgupload")
    public ReturnT<Object> imgUpload(HttpServletRequest request, @RequestParam("base64Data") String base64Data) {
        try {
            String dataPrix = ""; //base64格式前头
            String data = "";//实体部分数据
            if (base64Data == null || "".equals(base64Data)) {
                throw new Exception("上传失败，上传图片数据为空");
            } else {
                String[] d = base64Data.split("base64,");//将字符串分成数组
                if (d != null && d.length == 2) {
                    dataPrix = d[0];
                    data = d[1];
                } else {
                    throw new Exception("上传失败，数据不合法");
                }
            }
            String suffix = "";//图片后缀，用以识别哪种格式数据
            //data:image/jpeg;base64,base64编码的jpeg图片数据
            if ("data:image/jpeg;".equalsIgnoreCase(dataPrix)) {
                suffix = ".jpg";
            } else if ("data:image/x-icon;".equalsIgnoreCase(dataPrix)) {
                //data:image/x-icon;base64,base64编码的icon图片数据
                suffix = ".ico";
            } else if ("data:image/gif;".equalsIgnoreCase(dataPrix)) {
                //data:image/gif;base64,base64编码的gif图片数据
                suffix = ".gif";
            } else if ("data:image/png;".equalsIgnoreCase(dataPrix)) {
                //data:image/png;base64,base64编码的png图片数据
                suffix = ".png";
            } else {
                throw new Exception("上传图片格式不合法");
            }

            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String tempFileName = uuid + suffix;
            String imgFilePath = downPath + Constants.ASC_IMG_UPLOAD_PATH_NAME + tempFileName;//新生成的图片
            File destFile = new File(imgFilePath);
            destFile.getParentFile().mkdirs();

            BASE64Decoder decoder = new BASE64Decoder();
            try {
                //Base64解码
                byte[] b = decoder.decodeBuffer(data);
                for (int i = 0; i < b.length; ++i) {
                    if (b[i] < 0) {
                        //调整异常数据
                        b[i] += 256;
                    }
                }
                OutputStream out = new FileOutputStream(imgFilePath);
                out.write(b);
                out.flush();
                out.close();
                //imageService.save(imgurl);
                return new ReturnT<>(tempFileName);
            } catch (IOException e) {
                throw new Exception("上传图片失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnT.getFail(e.getMessage());
        }
    }

    /**
     * 获取门禁出入记录接口
     * by hzl 2019-10-9 15:40:19
     *
     * @param request
     * @return
     */
    @PostMapping("/ascrecord")
    public ReturnT<Object> getAscRecord(HttpServletRequest request) {
        try {
            String ipAddr = RequestHelper.getIpAddress(request);
            logger.info("/ascrecord ip:" + ipAddr);
            String scCode = RequestHelper.getRequest(request, "sc_code", true);
            String stuNo = RequestHelper.getRequest(request, "stu_no", true);
            String startDate = RequestHelper.getRequest(request, "start_date", true);
            String endDate = RequestHelper.getRequest(request, "end_date", true);
            String ascType = RequestHelper.getRequest(request, "asc_type", true);
            String actionType = RequestHelper.getRequest(request, "action_type", true);
            String actionState = RequestHelper.getRequest(request, "action_state", true);
            String nonceStr = RequestHelper.getRequest(request, "nonce_str");//随机字符串
            String sign = RequestHelper.getRequest(request, "sign");//签名

            //1.把传递过来的参数转为Map
            Map<String, Object> paramsMap = RequestHelper.getParameterMap(request);
            //2.获取对Map进行加密的结果
            String localSign = Md5Util.generateSignature(paramsMap, apiKey);
            if (!localSign.equals(sign)) {
                throw new Exception("sign error");
            }
            List<ComeInOutRecord> list = new ArrayList<>();
            //如果时间为空则默认显示前10条数据
            if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
                list = ascCountService.getRecord(-1, -1, scCode, null, stuNo, startDate, endDate, ascType, actionType, actionState, null);
            } else {
                list = ascCountService.getRecord(1, 10, scCode, null, stuNo, startDate, endDate, ascType, actionType, actionState, null);
            }
            return new ReturnT<>(list);
        } catch (Exception e) {
            return ReturnT.getFail(e.getMessage());
        }
    }

    /**
     * 门禁数据饼图统计接口
     * by hzl 2019-10-10 17:38:13
     *
     * @param request
     * @return
     */
    @PostMapping("/ascpiecount")
    public ReturnT<Object> AscPieCount(HttpServletRequest request) {
        try {
            String ipAddr = RequestHelper.getIpAddress(request);
            logger.info("/ascpiecount ip:" + ipAddr);
            String scCode = RequestHelper.getRequest(request, "sc_code", true);
            String stuNo = RequestHelper.getRequest(request, "stu_no", true);
            String startDate = RequestHelper.getRequest(request, "start_date", true);
            String endDate = RequestHelper.getRequest(request, "end_date", true);
            String nonceStr = RequestHelper.getRequest(request, "nonce_str");//随机字符串
            String sign = RequestHelper.getRequest(request, "sign");//签名

            //1.把传递过来的参数转为Map
            Map<String, Object> paramsMap = RequestHelper.getParameterMap(request);
            //2.获取对Map进行加密的结果
            String localSign = Md5Util.generateSignature(paramsMap, apiKey);
            if (!localSign.equals(sign)) {
                throw new Exception("sign error");
            }
            Map<String, Object> result = ascCountService.ascPieCountGroupByState(scCode, stuNo, startDate, endDate, null);
            return new ReturnT<>(result);
        } catch (Exception e) {
            return ReturnT.getFail(e.getMessage());
        }
    }


    /**
     * 门禁数据折线图统计接口
     * by hzl 2019-10-10 17:38:13
     *
     * @param request
     * @return
     */
    @PostMapping("/asclinecount")
    public ReturnT<Object> AscLineCount(HttpServletRequest request) {
        try {
            String ipAddr = RequestHelper.getIpAddress(request);
            logger.info("/ascpiecount ip:" + ipAddr);
            String scCode = RequestHelper.getRequest(request, "sc_code", true);
            String stuNo = RequestHelper.getRequest(request, "stu_no", true);
            String startDate = RequestHelper.getRequest(request, "start_date", true);//该时间为月初第一刻
            String endDate = RequestHelper.getRequest(request, "end_date", true);//该时间为月末最后一刻
            String nonceStr = RequestHelper.getRequest(request, "nonce_str");//随机字符串
            String sign = RequestHelper.getRequest(request, "sign");//签名

            //1.把传递过来的参数转为Map
            Map<String, Object> paramsMap = RequestHelper.getParameterMap(request);
            //2.获取对Map进行加密的结果
            String localSign = Md5Util.generateSignature(paramsMap, apiKey);
            if (!localSign.equals(sign)) {
                throw new Exception("sign error");
            }
            Map<String, Object> result = ascCountService.ascLineCountGroupByState(scCode, stuNo, startDate, endDate, null);
            return new ReturnT<>(result);
        } catch (Exception e) {
            return ReturnT.getFail(e.getMessage());
        }
    }


    //    学生人脸图片保存路径 by hzl 2019-10-22 11:01:29
    @Value("${config.student.img.path}")
    private String faceImgPath;
    //    学生人脸图片访问路径 by hzl 2019-10-22 11:01:29
    @Value("${config.student.img.url}")
    private String faceImgUrl;

    /**
     * 人脸采集图片上传接口（可裁剪）
     * by hzl 2019-10-22 09:23:56
     *
     * @param imageFile
     * @param cutData
     * @param request
     * @return
     */
    @PostMapping("/faceImgCutUpload")
    public ReturnT<String> faceImgUpload(@RequestParam("avatar_file") MultipartFile imageFile, @RequestParam("sid") String sId,
                                         @RequestParam("sc_code") String scCode, @RequestParam("avatar_data") String cutData,
                                         HttpServletRequest request) {
        Map map = (Map) JSON.parse(cutData);
        //每个学校照片保存到对应用学校代码命名的文件夹中
        String resourcePath = "/" + scCode + "/";
        String imgUrl = "";
        try {
            // 判断文件是否为空
            if (!imageFile.isEmpty()) {
                if (FileUploadUtil.allowImgUpload(imageFile.getContentType())) {
                    String fileName = imageFile.getOriginalFilename();
                    //"_1"在裁剪图片重新保存的图片命名中会去掉
                    fileName = sId + "_1" + fileName.substring(fileName.lastIndexOf("."));
                    File dir = new File(faceImgPath + resourcePath);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File file = new File(dir, fileName);
                    imageFile.transferTo(file);
                    String srcImagePath = faceImgPath + resourcePath + fileName;

                    int imageX = stringNumToInt(map.get("x").toString());
                    int imageY = stringNumToInt(map.get("y").toString());
                    int imageH = stringNumToInt(map.get("height").toString());
                    int imageW = stringNumToInt(map.get("width").toString());
                    //这里开始截取操作
                    ImgCut.imgCut(srcImagePath, imageX, imageY, imageW, imageH);
                    //写入到数据库
                    imgUrl = faceImgUrl + resourcePath + fileName.replace("_1", "");
                    try {
                        studentMapper.UpdateImgPath(scCode, sId, imgUrl);
                    } catch (Exception e) {
                        throw new Exception("图片路径写入数据库失败");
                    }
                } else {
                    throw new Exception("上传的图片格式不正确");
                }
            } else {
                throw new Exception("请选择图片");
            }
            return new ReturnT(imgUrl);
        } catch (Exception e) {
            return new ReturnT().getFail(e.getMessage());
        }
    }

    private int stringNumToInt(String num) {
        return (int) Double.parseDouble(num);
    }

    /**
     * 人脸上传接口（前端压缩好后，通过FormData提交base64字符串）
     * by hzl 2019-10-28 15:16:43
     *
     * @param request
     * @return
     */
    @PostMapping("/faceImgCmpUpload")
    public ReturnT<Object> uploadFile(HttpServletRequest request) {
        try {
            String base64Data = RequestHelper.getRequest(request, "base64_data");
            String scCode = RequestHelper.getRequest(request, "sc_code");
            String sId = RequestHelper.getRequest(request, "stu_no");
            String nonceStr = RequestHelper.getRequest(request, "nonce_str");//随机字符串
            String sign = RequestHelper.getRequest(request, "sign");//签名

            //1.把传递过来的参数转为Map
            Map<String, Object> paramsMap = RequestHelper.getParameterMap(request);
            //2.获取对Map进行加密的结果
            String localSign = Md5Util.generateSignature(paramsMap, apiKey);
            if (!localSign.equals(sign)) {
                throw new Exception("sign error");
            }

            String dataPrix = ""; //base64格式前头
            String data = "";//实体部分数据
            if (base64Data == null || "".equals(base64Data)) {
                throw new Exception("上传失败，上传图片数据为空");
            } else {
                String[] d = base64Data.split("base64,");//将字符串分成数组
                if (d != null && d.length == 2) {
                    dataPrix = d[0];
                    data = d[1];
                } else {
                    throw new Exception("上传失败，数据不合法");
                }
            }
            String suffix = "";//图片后缀，用以识别哪种格式数据
            //data:image/jpeg;base64,base64编码的jpeg图片数据
            if ("data:image/jpeg;".equalsIgnoreCase(dataPrix)) {
                suffix = ".jpg";
            } else if ("data:image/png;".equalsIgnoreCase(dataPrix)) {
                //data:image/png;base64,base64编码的png图片数据
                suffix = ".png";
            } else {
                throw new Exception("上传图片格式不合法，只允许上传png或jpg格式照片");
            }

            //每个学校照片保存到对应用学校代码命名的文件夹中
            String resourcePath = "/" + scCode + "/";
            String tempFileName = sId + suffix;
            String imgFilePath = faceImgPath + resourcePath + tempFileName;//新生成的图片
            String imgUrl = faceImgUrl + resourcePath + tempFileName;
            File destFile = new File(imgFilePath);
            destFile.getParentFile().mkdirs();

            BASE64Decoder decoder = new BASE64Decoder();
            OutputStream out = null;
            try {
                //Base64解码
                byte[] b = decoder.decodeBuffer(data);
                for (int i = 0; i < b.length; ++i) {
                    if (b[i] < 0) {
                        //调整异常数据
                        b[i] += 256;
                    }
                }
                out = new FileOutputStream(imgFilePath);
                out.write(b);
                out.flush();
            } catch (IOException e) {
                throw new Exception("Base64解码失败：" + e.getMessage());
            } finally {
                out.close();
            }

            try {
                studentMapper.UpdateImgPath(scCode, sId, imgUrl);
            } catch (Exception e) {
                throw new Exception("图片路径写入数据库失败");
            }
            return new ReturnT<>(imgUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnT.getFail(e.getMessage());
        }
    }


    /**  后端图片压缩方法
     * by hzl 2019-10-28 17:49:384
     * @param asd
     */
//    public static void main(String[] asd) {
//        //拼接后台文件名称
//        String thumbnailPathName = "C:\\Users\\Administrator\\Desktop\\IMG_20190517_184720.jpg";
//        File file = new File(thumbnailPathName);
//        if (file == null || !file.exists()) {
//            return;
//        }
//        long size = file.length();
//        double scale = 1.0d;
//        if (size >= 200 * 1024) {
//            scale = (200 * 1024f) / size;
//            System.out.println(scale);
//        }
//        //拼接文件路劲
//        String thumbnailFilePathName = thumbnailPathName.substring(0, thumbnailPathName.lastIndexOf(".")) + "_min.jpg";
//        try {
//            if (size > 200 * 1024) {
//                Thumbnails.of(thumbnailPathName).size(413, 626).toFile(thumbnailFilePathName);//变为400*300,遵循原图比例缩或放到400*某个高度
//            }
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//    }

    /***
     *获取学生信息接口
     * by hzl 2019-10-22 17:06:38
     * @param request
     * @return
     */
    @PostMapping("/getStuInfo")
    public ReturnT<Object> getStuInfo(HttpServletRequest request) {
        try {
            String ipAddr = RequestHelper.getIpAddress(request);
            logger.info("/getStuInfo ip:" + ipAddr);
            String scCode = RequestHelper.getRequest(request, "sc_code", true);
            String stuNo = RequestHelper.getRequest(request, "stu_no", true);
            String nonceStr = RequestHelper.getRequest(request, "nonce_str");//随机字符串
            String sign = RequestHelper.getRequest(request, "sign");//签名

            //1.把传递过来的参数转为Map
            Map<String, Object> paramsMap = RequestHelper.getParameterMap(request);
            //2.获取对Map进行加密的结果
            String localSign = Md5Util.generateSignature(paramsMap, apiKey);
            if (!localSign.equals(sign)) {
                throw new Exception("sign error");
            }
            Student student = studentService.loadByScCodeAndStuNo(scCode, stuNo);
            return new ReturnT<>(student);
        } catch (Exception e) {
            return ReturnT.getFail(e.getMessage());
        }
    }

    /**
     * 获取学生图片（base64字符串）
     *
     * @param request
     * @return
     */
    @PostMapping("/getStuImgBase64")
    public ReturnT<Object> getStuImgBase64(HttpServletRequest request) {
        try {
            String ipAddr = RequestHelper.getIpAddress(request);
            logger.info("/getStuImg ip:" + ipAddr);
            String scCode = RequestHelper.getRequest(request, "sc_code", true);
            String stuNo = RequestHelper.getRequest(request, "stu_no", true);
            String nonceStr = RequestHelper.getRequest(request, "nonce_str");//随机字符串
            String sign = RequestHelper.getRequest(request, "sign");//签名

            //1.把传递过来的参数转为Map
            Map<String, Object> paramsMap = RequestHelper.getParameterMap(request);
            //2.获取对Map进行加密的结果
            String localSign = Md5Util.generateSignature(paramsMap, apiKey);
            if (!localSign.equals(sign)) {
                throw new Exception("sign error");
            }
            Student student = studentService.loadByScCodeAndStuNo(scCode, stuNo);
            String imgPath = student.getImgPath();
            String base64Str = "";
            if (StringUtils.isNotBlank(imgPath)) {
                imgPath = imgPath.replace(faceImgPath, faceImgUrl);
                base64Str = ImageUtils.localImageToBase64(imgPath);
            } else {
                InputStream inputStream = ApiController.class.getResourceAsStream("/static/image/header/facetemplate0.jpg");
                base64Str = FileHelper.getBase64FromInputStream(inputStream);
            }
            return new ReturnT<>(base64Str);
        } catch (Exception e) {
            return ReturnT.getFail(e.getMessage());
        }
    }

    /**
     * 参数签名sign值接口
     * by hzl
     *
     * @param request
     * @return
     */
    @RequestMapping("/getsigntest")
    public String getParamsSignTest(HttpServletRequest request) {
        try {
            //1.把传递过来的参数转为Map
            Map<String, Object> paramsMap = RequestHelper.getParameterMap(request);
            //2.获取对Map进行加密的结果
            String localSign = Md5Util.generateSignature(paramsMap, apiKey);
            return localSign;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

//
//    String rsaPrivateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANsT+SYDEaiyFuwS\n" +
//            "uH0uqYWZoaXGjontkRce3D8Y2aYbTIwankAQoQiy6aZMyTKPewjNTGAOI7w9DLP3\n" +
//            "hYHmjEckMU5jFQRT3dccG/7rYb51eQUrny5aRtZ2IVts0N7wH4jg/ZAkfd9dcUki\n" +
//            "PWEhDTxtnrd1eMJEzJnK8aZtiVQtAgMBAAECgYBJRI7I1bHsN1NPNJua0iZD1jOn\n" +
//            "m5ZELr2de+aw+4Ce+vZIzQYr6uPQYvWddoS7chz1vqNSZKgy5j//WELARPiYm6fA\n" +
//            "09146S2/yASl1Dd2DbdU+VBgQh+jR1IuFkhohHX2AY3XjYdmWv28180+xRpVrLJT\n" +
//            "J++CVPzPmmywn4j3wQJBAPr+kAyWxzvKHv14xzjesJUoQwabHtLYEXqHrlLFd4cV\n" +
//            "M8wJvIfl+UX2J42kVNekiLKDSq7/iIYHgaJSM3ipaYUCQQDfcnagtCTcwkCdGjzN\n" +
//            "kiQ3wiOGJ0rPc8CIg03ts622kOEbKv1Q93ET89tF2ddlZ4xhhba0vFXLZOpJ+iqP\n" +
//            "2SyJAkEAoTo4af3IyT1SntugjYVuxMFyHKI0MJge0iip7/1shFc4SI7QjjNJ5ojy\n" +
//            "a2fuH/9O1+zCYps+pWnY+d33tkCMyQJBAMgHA8MhAM7O5mC9GCk+5kUz+fEiQ4oq\n" +
//            "qPSL9rsP2jZQw/M6LMLKf+PIrUZ+mKAibjzvInvxDSyjDkljNa7IT2ECQQDJBB3O\n" +
//            "WckhqwULstec/rKl/U+SmaJelDM6RsmPfeNm8H+YchL2hfOmD5gyRfUaBlMDGLEy\n" +
//            "KBzxQeGg8Ab/64m2";
//
//    String rsaPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDbE/kmAxGoshbsErh9LqmFmaGl\n" +
//            "xo6J7ZEXHtw/GNmmG0yMGp5AEKEIsummTMkyj3sIzUxgDiO8PQyz94WB5oxHJDFO\n" +
//            "YxUEU93XHBv+62G+dXkFK58uWkbWdiFbbNDe8B+I4P2QJH3fXXFJIj1hIQ08bZ63\n" +
//            "dXjCRMyZyvGmbYlULQIDAQAB";
//
//    @RequestMapping("/getrsasigntest")
//    public String getrsasigntest(HttpServletRequest request) {
//        try {
//            //1.把传递过来的参数转为Map
//            Map<String, Object> paramsMap = RequestHelper.getParameterMap(request);
//            //2.获取对Map进行加密的结果
//            String localSign = RSAUtil.generateSignature(paramsMap, rsaPrivateKey);
//            return URLEncoder.encode(localSign);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return e.getMessage();
//        }
//    }
//
//    @RequestMapping("/checkrsasign")
//    public String checkrsasign(HttpServletRequest request) {
//        try {
//            //1.把传递过来的参数转为Map
//            Map<String, Object> paramsMap = RequestHelper.getParameterMap(request);
//            //2.获取对Map进行加密的结果
//            String localSign = RSAUtil.generateSignature(paramsMap, rsaPrivateKey);
//            String contentStr = RSAUtil.getSignaStr(paramsMap);
//            String signStr = RequestHelper.getRequest(request, "sign");
//            boolean result = RSAUtil.verify(contentStr, signStr, rsaPublicKey, "UTF-8");
//
//            return String.valueOf(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return e.getMessage();
//        }
//    }

    /**
     * 门户单点登录接口
     * author 刘超
     * time 2019-10-14 14:28
     */
    @Value("${portal.login.url}")
    private String ssoLoginUrl;
    @Value("${portal.casweb.url}")
    private String casWebUrl;

    @RequestMapping("/ssologin")
    public void ssoLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session, String token) throws IOException {
        Map user = new HashMap();
        if (token != null) {
            user = restTemplate.getForObject(casWebUrl + "?token=" + token, Map.class);
        } else {
            response.sendRedirect(ssoLoginUrl);
            return;
        }
        if (user == null || user.size() == 0 || "".equals(user.get("userId"))) {
            response.sendRedirect(ssoLoginUrl);
            return;
        }

        Teacher teacher = teacherMapper.findOneByLoginName(user.get("userId").toString());
        if (teacher != null) {
            //设置登录教师编号Session
            SessionUtil.setSession(request, Constants.LOGIN_TEACHER_NO, teacher.getTeNo());
            SessionUtil.setSession(request, Constants.LOGIN_TEACHER_SCCODE, teacher.getScCode());
            SessionUtil.setSession(request, Constants.LOGIN_TEACHER_LOGINNAME, teacher.getLoginname());
            SessionUtil.setSession(request, Constants.OPENID, teacher.getIswxlogin());
            response.sendRedirect("/teacher/index");
            return;
        }
        response.sendRedirect(ssoLoginUrl);
        return;
    }

    /**
     * 获取我的学生（分页查询）
     * author 刘超
     * time 2019-10-14 14:28
     */
    @RequestMapping("/getMyStudent")
    public Map getMyStudent(@RequestBody Map<String, Object> map) throws Exception {
        Map result = new HashMap();
        String teNo = map.get("teNo").toString();
        String limit = map.get("limit").toString();
        String page = map.get("page").toString();
        String sign = map.get("sign").toString();
        String localSign = Md5Util.generateSignature(map, apiKey);
        if (!localSign.equals(sign)) {
            result.put("code", 500);
            result.put("msg", "签名错误");
            return result;
        }
        PageHelper.startPage(Integer.valueOf(page), Integer.valueOf(limit));
        try {
            List<Student> students = teacherMapper.getMyStudent(teNo);
            PageInfo<Student> pageInfo = new PageInfo<>(students);
            result.put("code", 200);
            result.put("content", students);
            result.put("count", pageInfo.getTotal());
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "接口错误");
        }
        return result;
    }


    /**
     * 发送消息
     * author 刘超
     * time 2019-10-14 14:28
     */
    @RequestMapping("/sendMsg")
    public ReturnT<Object> sendMsg(@RequestBody Map<String, Object> map) throws Exception {
        String msgType = map.get("msgType").toString();
        String sccode = map.get("sccode").toString();
        String teNo = map.get("teNo").toString();
        String sendDate = map.get("sendDate").toString();
        String stuNo = map.get("stuNo").toString();
        String content = map.get("content").toString();
        String sign = map.get("sign").toString();
        String localSign = Md5Util.generateSignature(map, apiKey);
        if (!localSign.equals(sign)) {
            return new ReturnT<>(500, "签名错误");
        }
        try {
            Student student = studentMapper.selectByScCodeAndStuNo(sccode, stuNo);
            List<String> stuIdsList = new ArrayList<>();
            stuIdsList.add(student.getStuId());
            messageService.sendMsgByOne(teNo, msgType, content, VeDate.strToDateLong(sendDate), null, stuIdsList);
            return new ReturnT<>();
        } catch (Exception e) {
            return new ReturnT<>(500, "接口出错");
        }
    }

    /**
     * 获取老师发送消息的记录
     * author 刘超
     * time 2019-10-14 14:28
     */
    @RequestMapping("/getMyMsgHistory")
    public Map getMyMsgHistory(@RequestBody Map<String, Object> map) throws Exception {
        String teNo = map.get("teNo").toString();
        String sign = map.get("sign").toString();
        String page = map.get("page").toString();
        String limit = map.get("limit").toString();
        Map result = new HashMap();
        String localSign = Md5Util.generateSignature(map, apiKey);
        if (!localSign.equals(sign)) {
            result.put("code", 500);
            result.put("msg", "签名错误");
            return result;
        }
        //分页插件
        PageHelper.startPage(Integer.valueOf(page), Integer.valueOf(limit));
        try {
            List<MessageLog> messageLogs = messageLogMapper.selectMessageByStuNo(teNo);
            PageInfo<MessageLog> pageInfo = new PageInfo<>(messageLogs);
            result.put("code", 200);
            result.put("content", messageLogs);
            result.put("count", pageInfo.getTotal());
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "接口错误");
        }
        return result;
    }

    /**
     * 请假
     * author 刘超
     * time 2019-10-14 14:28
     */
    @RequestMapping("/leave")
    public ReturnT leave(@RequestBody Map<String, Object> map) throws Exception {
        String teNo = map.get("teNo").toString();
        String stuNo = map.get("stuNo").toString();
        String reason = map.get("reason").toString();
        String sign = map.get("sign").toString();
        String localSign = Md5Util.generateSignature(map, apiKey);
        if (!localSign.equals(sign)) {
            return new ReturnT(500, "签名错误");
        }
        Leave leave = new Leave();
        leave.setTeNo(teNo);
        leave.setStuNo(stuNo);
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //加上时间
        Date startDate = sDateFormat.parse(map.get("startDate").toString());
        Date endDate = sDateFormat.parse(map.get("endDate").toString());
        leave.setStartDate(startDate);
        leave.setEndDate(endDate);
        leave.setReason(reason);
        try {
            leaveService.add(leave);
            return new ReturnT();
        } catch (Exception e) {
            return new ReturnT(500, e.getMessage());
        }
    }

    /**
     * 门户门禁考勤统计接口
     * by hzl 2019-10-25 15:16:26
     *
     * @param request
     * @return
     */
    @PostMapping("/getCheckOnWarkCount")
    public Map getMenhuAscCount(HttpServletRequest request) {
        Map result = new HashMap();
        try {
            String scCode = RequestHelper.getRequest(request, "sc_code");
            String unitId = RequestHelper.getRequest(request, "unitId");
            String grCode = gradeService.getByUuidStr(unitId).getGr_code();
            String type = RequestHelper.getRequest(request, "type");
            String startDate = RequestHelper.getRequest(request, "startDate");
            String endDate = RequestHelper.getRequest(request, "endDate");
            String sign = RequestHelper.getRequest(request, "sign");

            Map<String, Object> paramsMap = RequestHelper.getParameterMap(request);
            String localSign = Md5Util.generateSignature(paramsMap, apiKey);
            if (!localSign.equals(sign)) {
                result.put("code", 500);
                result.put("msg", "签名错误");
                return result;
            }

            List<NameAndValue> list = ascCountService.menhuAscCount(scCode, grCode, type, startDate, endDate);
            result.put("code", 200);
            result.put("data", list);
            result.put("msg", "");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "接口错误:" + e.getMessage());
        }
        return result;
    }

    /**
     * author lc
     *
     * @param map
     */
    @PostMapping("/studentSync")
    public void studentSync(@RequestBody Map<String, String> map) {
        //学号
        String studentid = map.get("studentid") == null ? null : map.get("studentid").toString();
        //学号为空后面不用执行了
        if (studentid == null) {
            return;
        }
        //学校编码
        String schoolcode = map.get("sc_code") == null ? "" : map.get("sc_code").toString();
        //姓名
        String name = map.get("name") == null ? null : map.get("name").toString();
        //班级
        String unitid = map.get("unitid") == null ? null : map.get("unitid").toString();
        //性别
        String sex = map.get("sex") == null ? null : map.get("sex").toString();
        if ("1".equals(sex)) {
            sex = "1";
        }
        if ("2".equals(sex)) {
            sex = "0";
        }
        //身份证
        String idcard = map.get("idcard") == null ? null : map.get("idcard").toString();

        //是否删除
        String delete = map.get("delete") == null ? null : map.get("delete").toString();
        //更新时间
        studentService.studentHandle(schoolcode, studentid, name, sex, idcard, unitid, delete);
    }

    /**
     * @param map
     * @author lc
     * @date 2019-11-25
     */
    @PostMapping("/studentOneKeySync")
    public void studentOneKeySync(@RequestBody Map<String, Object> map) throws Exception {
        String scCode = map.get("sc_code").toString();
        if (StringUtils.isBlank(scCode)) {
            return;
        }
        String sign = map.get("sign").toString();
        String localSign = Md5Util.generateSignature(map, apiKey);
        if (!localSign.equals(sign)) {
            return;
        }
        SchoolEntity schoolEntity = schoolService.getByScCode(scCode);
        List<Student> students = studentService.getStudentByScCode(scCode);
        String menhukey = schoolEntity.getMenhuKey();
        if (schoolEntity == null || StringUtils.isBlank(menhukey)) {
            return;
        }
        students.stream().forEach(student -> dataSyncService.studentOneKeySync(student, menhukey));
    }

    /**
     * author lc
     *
     * @param map
     */
    @PostMapping("/teacherSync")
    public void teacherSync(@RequestBody Map<String, String> map) {
        String name = map.get("name") == null ? null : map.get("name").toString();
        //教师号
        String teacherid = map.get("teacherid") == null ? "" : map.get("teacherid").toString();
        //教师号为空后面不用执行了
        if (teacherid == null) {
            return;
        }
        //性别
        String sex = map.get("sex") == null ? null : map.get("sex").toString();
        if ("1".equals(sex)) {
            sex = "0";
        }
        if ("2".equals(sex)) {
            sex = "1";
        }
        //身份证
        String idcard = map.get("idcard") == null ? null : map.get("idcard").toString();
        //手机号
        String phone = map.get("phone") == null ? null : map.get("phone").toString();
        //密码
        String password = map.get("password") == null ? null : map.get("password").toString();
        //学校编码
        String sc_code = map.get("sc_code") == null ? null : map.get("sc_code").toString();
        //使用否
        String used = map.get("used") == null ? null : map.get("used").toString();
        //删除否
        String delete = map.get("delete") == null ? null : map.get("delete").toString();
        teacherService.teacherHandle(teacherid, password, name, sex, idcard, phone, sc_code, used, delete);
    }

    /**
     * author lc
     *
     * @param map
     */
    @PostMapping("/organizeSync")
    public void organizeSync(@RequestBody Map<String, String> map) throws Exception {
        String type = map.get("type") == null ? "" : map.get("type").toString();
        String uuidStr = map.get("unitid") == null ? "" : map.get("unitid").toString();
        String name = map.get("name") == null ? "" : map.get("name").toString();
        String sc_code = map.get("sc_code") == null ? "" : map.get("sc_code").toString();
        String gr_code = map.get("punitid") == null ? "" : map.get("punitid").toString();
        String delete = map.get("delete") == null ? "" : map.get("delete").toString();

        switch (type) {
            case "1":
                //类型为学校
                System.out.println("修改学校信息");
                schoolService.schoolHandle(sc_code, name, delete);
                break;
            case "5":
                //类型为年级
                System.out.println("修改年级信息");
                gradeService.gradeHandle(sc_code, uuidStr, name, delete);
                break;
            case "6":
                //类型为班级
                System.out.println("修改班级信息");
                classService.classHandle(sc_code, gr_code, uuidStr, name, delete);
                break;
            default:
                break;
        }
    }

    /**
     * 获取考勤记录
     *
     * @param request
     * @return
     */

    @PostMapping("/getAttendList")
    public Map getAttendList(HttpServletRequest request) {
        Map result = new HashMap();
        try {
            Integer page = Integer.valueOf(RequestHelper.getRequest(request, "page"));
            Integer limit = Integer.valueOf(RequestHelper.getRequest(request, "limit"));
            String startDate = RequestHelper.getRequest(request, "startDate");
            String endDate = RequestHelper.getRequest(request, "endDate");
            String sign = RequestHelper.getRequest(request, "sign", true);
            String sc_code = RequestHelper.getRequest(request, "sc_code");
            String teacherNo = RequestHelper.getRequest(request, "teacherNo");
            Map<String, Object> paramsMap = RequestHelper.getParameterMap(request);
            String localSign = Md5Util.generateSignature(paramsMap, apiKey);
            if (!localSign.equals(sign)) {
                result.put("code", 500);
                result.put("msg", "签名错误");
                return result;
            }

            PageResult list = ascCountService.menhuAttendCount(page, limit, startDate, endDate, null, teacherNo);
            result.put("code", 200);
            result.put("data", list.getData());
            result.put("total", list.getTotal());
            result.put("msg", "");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("total", 0);
            result.put("msg", "接口错误:" + e.getMessage());
        }
        return result;
    }

    //提供给网络电话平台接口
    /**
     * 获取单个学生信息
     *
     * @param scCode
     * @param stuNo
     * @return
     * @throws Exception
     */
    @RequestMapping("/netPhone/getStudent")
    @ResponseBody
    public ReturnT<Object> getStudent(String scCode,String stuNo) throws Exception{
        Student student = studentService.loadByScCodeAndStuNo(scCode,stuNo);
        return new ReturnT<>(student);
    }

    /**
     * 获取学生总数
     *
     * @param scCode
     * @return
     * @throws Exception
     */
    @RequestMapping("/netPhone/getStuCount")
    @ResponseBody
    public ReturnT<Object> getStuCount(String scCode) throws Exception{
        List<String> clCodeList = getClCode(scCode);
        int listCount = studentMapper.listStudentCount(clCodeList,null,null,null);
        Map<String,String> map = new HashMap<String,String>();
        map.put("scCode",scCode);
        map.put("stuTotals",String.valueOf(listCount));
        return new ReturnT<>(map);
    }

    /**
     * 获取学生列表
     * @param page
     * @param limit
     * @param scCode
     * @return
     */
    @RequestMapping("/netPhone/getStuList")
    @ResponseBody
    public ReturnT<Object> getList(Integer page, Integer limit,String scCode) {
        List<String> clCodeList = getClCode(scCode);
        Map<String,Object> map = new HashMap<String,Object>();
        if(clCodeList==null){
            map.put("scCode",scCode);
            map.put("total",0);
            map.put("data",null);
            map.put("dataSize",0);
            return new ReturnT<>(map);
        }
        int listCount = studentMapper.listStudentCount(clCodeList,null,null,null);
        page = (page <= 0) ? 1 : page;
        int offset = (page == 1) ? 0 : (page - 1) * limit;
        List<Student> list  = studentMapper.listStudent(offset,limit,clCodeList, null, null,null);
        map.put("scCode",scCode);
        map.put("total",listCount);
        map.put("data",list);
        map.put("dataSize",String.valueOf(list.size()));
        return new ReturnT<>(map);
    }

    /**
     * 获取学校列表
     *
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping("/netPhone/getSchoolList")
    @ResponseBody
    public ReturnT<Object> getSchoolList(Integer page, Integer limit) {
        DataGridJson dataGridJson = schoolService.list(null,null,limit,page);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total",dataGridJson.getTotal());
        map.put("data",dataGridJson.getRows());
        return new ReturnT<>(map);
    }

    /**
     * 根据学校代码获取学校班级
     *
     * @param scCode
     * @return
     */
    private List<String> getClCode(String scCode){
        List<Map<String, Object>> classList = null;
        List<String> clCodeList = null;

        if(scCode!=null){
            classList = classService.list(scCode, "allClassData", 10000, 1);
            for (Map<String, Object> m : classList) {
                clCodeList.add(m.get("cl_code").toString());
            }
        }
        return  clCodeList;
    }

    /**
     * 闸机识别上传记录接收接口
     * by hzl 2019-12-18 12:55:07
     *
     * @param request
     * @return
     */
    @RequestMapping("/faceRecRecord")
    public void faceRecRecord(HttpServletRequest request, @RequestBody AutoRecord autoRecord) {
        try {
            String ipAddr = RequestHelper.getIpAddress(request);
            logger.info("/ascnotice ip:" + ipAddr);
            logger.info("AutoRecord:" + autoRecord);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
