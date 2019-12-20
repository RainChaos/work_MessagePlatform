package com.jiapeng.messageplatform.utils;

/**
 * Created by HZL on 2019/2/27.
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;

public class ImgCut {
    /**
     * 截取图片
     *
     * @param srcImageFile 原图片地址
     * @param x            截取时的x坐标
     * @param y            截取时的y坐标
     * @param desWidth     截取的宽度
     * @param desHeight    截取的高度
     */
    public static String imgCut(String srcImageFile, int x, int y, int desWidth, int desHeight) {
        String url = "";
        try {
            Image img;
            ImageFilter cropFilter;
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getWidth();
            int srcHeight = bi.getHeight();
            if (srcWidth >= desWidth && srcHeight >= desHeight) {
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                cropFilter = new CropImageFilter(x, y, desWidth, desHeight);
                img = Toolkit.getDefaultToolkit().createImage(
                        new FilteredImageSource(image.getSource(), cropFilter));
                BufferedImage tag = new BufferedImage(desWidth, desHeight,
                        BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(img, 0, 0, null);
                g.dispose();
                //输出文件
                String newImgName = srcImageFile.replace("_1", "");

                ImageIO.write(tag, "JPEG", new File(newImgName));
                //删除原文件
                try {
                    File file = new File(srcImageFile);
                    file.delete();
                } catch (Exception e) {

                }
                url = newImgName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }
}