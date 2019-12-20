package com.jiapeng.messageplatform.utils;

/**
 * Created by HZL on 2019/2/27.
 */

import java.util.*;

public class FileUploadUtil {
    public static final List<String> ALLOW_TYPES_IMG = Arrays.asList(
            "image/jpg", "image/jpeg", "image/png", "image/gif"
    );

    //文件重命名
    public static String rename(String fileName) {
        int i = fileName.lastIndexOf(".");
        //文件后缀
        String suffixStr = fileName.substring(i);
        //uuid
        String uuidStr = UUID.randomUUID().toString();
        return uuidStr + suffixStr;
    }

    //校验文件类型是否是被允许的
    public static boolean allowImgUpload(String postfix) {
        return ALLOW_TYPES_IMG.contains(postfix);
    }

}