package com.jiapeng.messageplatform.gate.code;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jiapeng.messageplatform.gate.entity.Device;
import com.jiapeng.messageplatform.gate.entity.Personnel;
import com.jiapeng.messageplatform.utils.HttpUtil;
import com.jiapeng.messageplatform.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 闸机接口请求方法类
 * Created by HZL on 2019/12/9.
 */
@Component
public class GateRequest {
    @Autowired
    GateResponse gateResponse;

    @Value("${gate.appid}")
    private String appid;
    @Value("${gate.appsecret}")
    private String appSecret;
    @Value("${gate.apiurl}")
    private String apiUrl;

    /**
     * 新增设备方法
     * by hzl 2019-12-9 16:46:47
     *
     * @param device
     * @return
     * @throws Exception
     */
    public GateResponse insertDevice(Device device) throws Exception {
        String method = "/device/insert.do";
        String deviceNo = device.getDevice_No();
        GateRequestParams param = new GateRequestParams(appid);
        param.setValue("deviceno", deviceNo);
        param.setValue("data", device.toJsonStr());
        param.setValue("timestemp", "20190416112516142");
        param.setValue("sign", param.makeSign(appSecret));
        try {
            String apiAddr = apiUrl + method;
            String result = HttpUtil.doPost(apiAddr, param.getJsonStr());
            GateResponse response = gateResponse.getResponse(result);
            return response;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 新增人员方法
     * by hzl 2019-12-10 09:33:32
     *
     * @param personnel
     * @return
     * @throws Exception
     */
    public GateResponse insertPersonnel(Personnel personnel) throws Exception {
        String method = "/Personnel/insert.do";
        GateRequestParams param = new GateRequestParams(appid);
        param.setValue("data", personnel.toJsonStr());
      param.setValue("sign", param.makeSign(appSecret));
        try {
            String apiAddr = apiUrl + method;
            String result = HttpUtil.doPost(apiAddr, param.getJsonStr());
            GateResponse response = gateResponse.getResponse(result);
            return response;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 批量新增人员方法
     * by hzl 2019-12-12 09:49:11
     *
     * @param personnels
     * @return
     * @throws Exception
     */
    public GateResponse insertPersonnelList(List<Personnel> personnels) throws Exception {
        String method = "/Personnel/insertlist.do";
        GateRequestParams param = new GateRequestParams(appid);
        param.setValue("data", JSON.toJSONString(personnels));
        param.setValue("sign", param.makeSign(appSecret));
        try {
            String apiAddr = apiUrl + method;
            String result = HttpUtil.doPost(apiAddr, param.getJsonStr());
            GateResponse response = gateResponse.getResponse(result);
            return response;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 编辑人员方法
     * by hzl 2019-12-11 09:03:23
     *
     * @param personnel
     * @return
     * @throws Exception
     */
    public GateResponse updatePersonnel(Personnel personnel) throws Exception {
        String method = "/Personnel/update.do";
        GateRequestParams param = new GateRequestParams(appid);
        param.setValue("data", personnel.toJsonStr());
        param.setValue("sign", param.makeSign(appSecret));
        try {
            String apiAddr = apiUrl + method;
            String result = HttpUtil.doPost(apiAddr, param.getJsonStr());
            GateResponse response = gateResponse.getResponse(result);
            return response;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 批量编辑人员方法
     * by hzl 2019-12-12 10:00:39
     *
     * @param personnels
     * @return
     * @throws Exception
     */
    public GateResponse updatePersonnelList(List<Personnel> personnels) throws Exception {
        String method = "/Personnel/updatelist.do";
        GateRequestParams param = new GateRequestParams(appid);
        param.setValue("data", JSONArray.toJSONString(personnels));
        param.setValue("sign", param.makeSign(appSecret));
        try {
            String apiAddr = apiUrl + method;
            String result = HttpUtil.doPost(apiAddr, param.getJsonStr());
            GateResponse response = gateResponse.getResponse(result);
            return response;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取人员列表方法
     * by hzl 2019-12-12 09:08:50
     *
     * @param appid
     * @return
     * @throws Exception
     */
    public List<Personnel> getPersonnelList(String appid) throws Exception {
        String method = "/Personnel/list.do";
        GateRequestParams param = new GateRequestParams(appid);
        param.setValue("appid", appid);
        param.setValue("sign", param.makeSign(appSecret));
        try {
            String apiAddr = apiUrl + method;
            String result = HttpUtil.doPost(apiAddr, param.getJsonStr());
            GateResponse response = gateResponse.getResponse(result);
            Object data = response.getData();
            if (null == data) {
                return null;
            } else {
                List<Personnel> personnelsList = new ArrayList<>();
                personnelsList = JSON.parseArray(data.toString(), Personnel.class);
                return personnelsList;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 获取人员方法
     * by hzl 2019-12-10 10:59:42
     *
     * @param personnelNo
     * @return
     * @throws Exception
     */
    public Personnel getPersonnel(String personnelNo) throws Exception {
        String method = "/Personnel/get.do";
        GateRequestParams param = new GateRequestParams(appid);
        param.setValue("personnelno", personnelNo);
        param.setValue("sign", param.makeSign(appSecret));
        try {
            String apiAddr = apiUrl + method;
            String result = HttpUtil.doPost(apiAddr, param.getJsonStr());
            GateResponse response = gateResponse.getResponse(result);
            Object data = response.getData();
            if (null == data) {
                return null;
            } else {
                Personnel personnel = null;
                personnel = JSON.parseObject(data.toString(), Personnel.class);
                return personnel;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 删除人员方法
     * by hzl 2019-12-10 17:01:23
     *
     * @param personnelNo
     * @return
     * @throws Exception
     */
    public GateResponse deletePersonnel(String personnelNo) throws Exception {
        String method = "/Personnel/delete.do";
        GateRequestParams param = new GateRequestParams(appid);
        param.setValue("personnelno", personnelNo);
        param.setValue("sign", param.makeSign(appSecret));
        try {
            String apiAddr = apiUrl + method;
            String result = HttpUtil.doPost(apiAddr, param.getJsonStr());
            GateResponse response = gateResponse.getResponse(result);
            return response;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 批量删除人员方法
     * by hzl 2019-12-11 08:58:07
     *
     * @param noLists
     * @return
     * @throws Exception
     */
    public GateResponse deletePersonnelList(List<String> noLists) throws Exception {
        String method = "/Personnel/deletelist.do";
        GateRequestParams param = new GateRequestParams(appid);
        param.setValue("personnelno", noLists.toString());
        param.setValue("sign", param.makeSign(appSecret));
        try {
            String apiAddr = apiUrl + method;
            String result = HttpUtil.doPost(apiAddr, param.getJsonStr());
            GateResponse response = gateResponse.getResponse(result);
            return response;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 上传人员照片方法
     * by hzl 2019-12-10 17:31:10
     *
     * @param personnelNo
     * @param imgBase64Str 需要经过URLEncoder.encode 例如：URLEncoder.encode ("/9j/4AAQSkZJRgABAQEAYABgAAD/....../5ZSAe6HIaaWiipKP/9k=","UTF-8")
     * @return
     * @throws Exception
     */
    public GateResponse imgUploadPersonnel(String personnelNo, String imgBase64Str) throws Exception {
        String method = "/Personnel/upload.do";
        GateRequestParams param = new GateRequestParams(appid);
        param.setValue("personnelno", personnelNo);
        param.setValue("photo", imgBase64Str);
        param.setValue("sign", param.makeSign(appSecret));
        try {
            String apiAddr = apiUrl + method;
            String result = HttpUtil.doPost(apiAddr, param.getJsonStr());
            GateResponse response = gateResponse.getResponse(result);
            return response;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 人员授权方法
     * by hzl 2019-12-10 17:25:13
     *
     * @param personnelNo
     * @param deviceNo
     * @return
     * @throws Exception
     */
    public GateResponse authPersonnel(String personnelNo, String deviceNo) throws Exception {
        String method = "/Personnel/auth.do";
        GateRequestParams param = new GateRequestParams(appid);
        param.setValue("personnelno", personnelNo);
        param.setValue("deviceno", deviceNo);
        param.setValue("sign", param.makeSign(appSecret));
        try {
            String apiAddr = apiUrl + method;
            String result = HttpUtil.doPost(apiAddr, param.getJsonStr());
            GateResponse response = gateResponse.getResponse(result);
            return response;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 删除人员授权方法
     * by hzl 2019-12-11 08:46:04
     *
     * @param personnelNo
     * @param deviceNo
     * @return
     * @throws Exception
     */
    public GateResponse deleteAuthPersonnel(String personnelNo, String deviceNo) throws Exception {
        String method = "/Personnel/deleteauth.do";
        GateRequestParams param = new GateRequestParams(appid);
        param.setValue("personnelno", personnelNo);
        param.setValue("deviceno", deviceNo);
        param.setValue("sign", param.makeSign(appSecret));
        try {
            String apiAddr = apiUrl + method;
            String result = HttpUtil.doPost(apiAddr, param.getJsonStr());
            GateResponse response = gateResponse.getResponse(result);
            return response;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 查询人员授权
     * by hzl 2019-12-11 08:50:01
     *
     * @param personnelNos 人员编号数组字符串
     * @param deviceNo
     * @return
     * @throws Exception
     */
    public GateResponse getAuthPersonnel(List<String> personnelNos, String deviceNo) throws Exception {
        String method = "/Personnel/getauth.do";
        GateRequestParams param = new GateRequestParams(appid);
        param.setValue("personnelnoes", personnelNos == null ? null : personnelNos.toString());
        param.setValue("deviceno", deviceNo);
        param.setValue("sign", param.makeSign(appSecret));
        try {
            String apiAddr = apiUrl + method;
            String result = HttpUtil.doPost(apiAddr, param.getJsonStr());
            GateResponse response = gateResponse.getResponse(result);
            return response;
        } catch (Exception e) {
            throw e;
        }
    }

}
