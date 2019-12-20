package com.jiapeng.messageplatform.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 图片通用工具类
 * Created by HZL on 2019/10/29.
 */
public class ImageUtils {
    /**
     * 将网络图片转成Base64码，此方法可以解决解码后图片显示不完整的问题
     *
     * @param imgURL 图片地址。 例如：http://***.com/271025191524034.jpg
     * @return
     */
    public static String networkImgToBase64(String imgURL) {
        ByteArrayOutputStream outPut = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        try {
            // 创建URL
            URL url = new URL(imgURL);
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10 * 1000);

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "fail";//连接失败/链接失效/图片不存在
            }
            InputStream inStream = conn.getInputStream();
            int len = -1;
            while ((len = inStream.read(data)) != -1) {
                outPut.write(data, 0, len);
            }
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(outPut.toByteArray());
    }

    /**
     * 本地图片转base64
     *
     * @param imgFile
     * @return
     */
    public static String localImageToBase64(String imgFile) {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
//读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);//返回Base64编码过的字节数组字符串
    }


    /**
     * Base64解码并生成图片
     *
     * @param base64str
     * @param savepath
     * @return
     */
    public static boolean GenerateImage(String base64str, String savepath) { //对字节数组字符串进行Base64解码并生成图片
        if (base64str == null) //图像数据为空
            return false;
// System.out.println("开始解码");
        BASE64Decoder decoder = new BASE64Decoder();
        try {
//Base64解码
            byte[] b = decoder.decodeBuffer(base64str);
// System.out.println("解码完成");
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
// System.out.println("开始生成图片");
//生成jpeg图片
            OutputStream out = new FileOutputStream(savepath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
