package com.jiapeng.messageplatform.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by HZL on 2019/5/30.
 */
public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * GET提交
     *
     * @return
     */
    public static String doGet(String url) {
        String strResult = "";
        // 1. 创建一个默认的client实例
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            // 2. 创建一个httpget对象
            HttpGet httpGet = new HttpGet(url);
            System.out.println("executing GET request " + httpGet.getURI());

            // 3. 执行GET请求并获取响应对象
            CloseableHttpResponse resp = client.execute(httpGet);
            try {
                // 6. 打印响应长度和响应内容
                if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    // 4. 获取响应体
                    HttpEntity entity = resp.getEntity();
                    System.out.println("Response content length = "
                            + entity.getContentLength());
                    System.out.println("------");
                    strResult = EntityUtils.toString(resp.getEntity());
                }
            } finally {
                //无论请求成功与否都要关闭resp
                resp.close();
            }
        } catch (ClientProtocolException e) {
            logger.error("get请求失败:", e);
            // e.printStackTrace();
        } catch (ParseException e) {
            logger.error("get请求解析出错:", e);
            // e.printStackTrace();
        } catch (IOException e) {
            logger.error("get请求IO出错:", e);
            // e.printStackTrace();
        } finally {
            // 8. 最终要关闭连接，释放资源
            try {
                client.close();
            } catch (Exception e) {
                logger.error("get请求完毕关闭连接出错:", e);
                // e.printStackTrace();
            }
        }
        return strResult;
    }

    /**
     * 普通POST提交
     *
     * @param url
     * @param map
     * @return
     */
    public static String doPost(String url, Map<String, Object> map) throws Exception {
        String strResult = "";
        // 1. 获取默认的client实例
        CloseableHttpClient client = HttpClients.createDefault();
        // 2. 创建httppost实例
        HttpPost httpPost = new HttpPost(url);
        // 3. 创建参数队列（键值对列表）
        List<NameValuePair> paramPairs = new ArrayList<>();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            Object val = map.get(key);
            if (null != val) {
                paramPairs.add(new BasicNameValuePair(key, val.toString()));
            }
        }
        UrlEncodedFormEntity entity;
        try {
            // 4. 将参数设置到entity对象中
            entity = new UrlEncodedFormEntity(paramPairs, "UTF-8");
            // 5. 将entity对象设置到httppost对象中
            httpPost.setEntity(entity);
            // 6. 发送请求并回去响应
            CloseableHttpResponse resp = client.execute(httpPost);
            try {
                // 7. 获取响应entity
                HttpEntity respEntity = resp.getEntity();
                strResult = EntityUtils.toString(respEntity, "UTF-8");
            } finally {
                // 9. 关闭响应对象
                resp.close();
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw e;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            // 10. 关闭连接，释放资源
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        return strResult;
    }

    /**
     * json参数方式POST提交
     *
     * @param url
     * @param params
     * @return
     */
    public static String doPost(String url, String params) {
        String strResult = "";
        // 1. 获取默认的client实例
        CloseableHttpClient client = HttpClients.createDefault();
        // 2. 创建httppost实例
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json;charset=UTF-8"); //添加请求头
        try {
            httpPost.setEntity(new StringEntity(params, "UTF-8"));
            CloseableHttpResponse resp = client.execute(httpPost);
            try {
                // 7. 获取响应entity
                HttpEntity respEntity = resp.getEntity();
                strResult = EntityUtils.toString(respEntity, "UTF-8");
            } finally {
                resp.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }

    public static String doPost1(String url, String json) {
        HttpPost post = new HttpPost(url);
        String response = null;
        try {
            System.out.println(json);
            StringEntity s = new StringEntity(json, "UTF-8"); // 中文乱码在此解决
            s.setContentType("application/json");
            post.setEntity(s);
            HttpResponse res = HttpClients.createDefault().execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(res.getEntity());// 返回json格式：
                response = result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}