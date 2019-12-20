package com.jiapeng.messageplatform.gate.code;

import java.security.MessageDigest;


public class MD5 {

    public static String md5(String text) {
        try {

            MessageDigest md5 = MessageDigest.getInstance("MD5");
            sun.misc.BASE64Encoder baseEncoder = new sun.misc.BASE64Encoder();
            byte[] ssd = md5.digest(text.getBytes("utf-8"));

            return byteArrayToHexString(ssd);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    //下面这个函数用于将字节数组换成成16进制的字符串

    public static String byteArrayToHex(byte[] byteArray) {

        // 首先初始化一个字符数组，用来存放每个16进制字符

        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};


        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））

        char[] resultCharArray = new char[byteArray.length * 2];


        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去

        int index = 0;

        for (byte b : byteArray) {

            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];

            resultCharArray[index++] = hexDigits[b & 0xf];

        }


        // 字符数组组合成字符串返回

        return new String(resultCharArray);
    }

    /**
     * 字节转十六进制
     *
     * @param b 需要进行转换的byte字节
     * @return 转换后的Hex字符串
     */
    public static String byteToHex(byte[] byteMd5) {
        StringBuilder reluststr = new StringBuilder();
        for (int i = 0; i < byteMd5.length; i++) {
            String hex = Integer.toHexString(byteMd5[i] & 0xFF);
            if (hex.length() < 2) {
                hex = "0" + hex;
            }

            reluststr.append(hex);
        }

        return reluststr.toString();
    }


    /**
     * @param b byte[]
     * @return String
     */
    public static String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += byteToHexString(b[i]);
        }
        return result;
    }

    private static String[] hexDigits =
            {
                    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b",
                    "c", "d", "e", "f"};

    /**
     * 将字节转换为对应�?16进制明文
     *
     * @param b byte
     * @return String
     */
    public static String byteToHexString(byte b) {

        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static void main(String[] args) {
        //System.out.println(byteToHexString((byte)-99));
        String str =
                "appid=mjce55eba54da18029&deviceno=&personnelnoes=null&timestemp=20190416112516142&key=80af998940d64e529522efd0d7fbbd43";
        String ened = MD5.md5(str);
        System.out.println("xx--" + ened.length());
        System.out.println(ened);
    }
}

