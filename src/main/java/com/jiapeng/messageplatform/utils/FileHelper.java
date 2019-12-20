package com.jiapeng.messageplatform.utils;

import org.apache.commons.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class FileHelper {
//    public static void main(String args[]) throws IOException {
//        try {
//            String url1 = "C:/Users/Administrator/Desktop/imdb_crop/imdb_crop"; // 源文件夹
//            String url2 = "d:/FaceData/"; // 目标文件夹
//            (new File(url2)).mkdirs(); // 创建目标文件夹
//
//            File[] file = (new File(url1)).listFiles(); // 获取源文件夹当前下的文件或目录
//            for (int i = 0; i < file.length; i++) {
//                if (file[i].isDirectory()) // 复制目录
//                {
//                    File[] file1 = file[i].listFiles(); // 获取源文件夹当前下的文件或目录
//                    for (int j = 0; j < file1.length; j++) {
//                        if (file1[j].isFile()) // 复制文件
//                        {
//                            String type = file1[j].getName().substring(file1[j].getName().lastIndexOf(".") + 1);
//                            copyFile(file1[j], new File(url2 + file1[j].getName()));
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    public static void main(String args[]) throws IOException {
        try {
            String url1 = "D:/FaceData"; // 源文件夹

            File[] file = (new File(url1)).listFiles(); // 获取源文件夹当前下的文件或目录
            for (int i = 0; i < file.length; i++) {
                if (file[i].isFile()) // 复制文件
                {
                    String type = file[i].getName().substring(file[i].getName().lastIndexOf(".") + 1);
                    String fileName = "D:/FaceData/" + i + "." + type;
                    file[i].renameTo(new File(fileName));
                }
                System.out.println(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 复制文件
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }

            outBuff.flush();
        } finally {

            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }


    // 复制文件夹
    public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
        (new File(targetDir)).mkdirs();
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                File sourceFile = file[i];
                File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
                copyFile(sourceFile, targetFile);//递归调用
            }
            if (file[i].isDirectory()) {
                String dir1 = sourceDir + "/" + file[i].getName();
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }


    /**
     * 将InputStream转化为base64
     * by hzl 2019-10-29 10:25:03
     *
     * @param in
     * @return
     */
    public static String getBase64FromInputStream(InputStream in) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;
        // 读取图片字节数组
        try {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = in.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            data = swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new String(Base64.encodeBase64(data));
    }


    //传入对应的需要打包的file 集合对象`
//文件打包下载
    public static HttpServletResponse downLoadFiles(List<File> files, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            /**这个集合就是你想要打包的所有文件，
             * 这里假设已经准备好了所要打包的文件
             */
            //List<File> files = new ArrayList<File>();
            /**创建一个临时压缩文件，
             * 我们会把文件流全部注入到这个文件中
             * 这里的文件你可以自定义是.rar还是.zip
             * 这里的file路径发布到生产环境时可以改为
             */
            String fileName = "导出学生照片" + VeDate.getLocalDate("yyyyMMddHHmmss") + ".zip";
            File file = new File(request.getSession().getServletContext().getRealPath("/" + fileName));
            if (!file.exists()) {
                file.createNewFile();
            }
            response.reset();
            //response.getWriter()
            //创建文件输出流
            FileOutputStream fous = new FileOutputStream(file);
            /**打包的方法我们会用到ZipOutputStream这样一个输出流,
             * 所以这里我们把输出流转换一下*/
            //  org.apache.tools.zip.ZipOutputStream zipOut= new org.apache.tools.zip.ZipOutputStream(fous);
            ZipOutputStream zipOut = new ZipOutputStream(fous);
            /**这个方法接受的就是一个所要打包文件的集合，
             * 还有一个ZipOutputStream
             */
            zipFile(files, zipOut);
            zipOut.close();
            fous.close();
            return downloadZip(file, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**直到文件的打包已经成功了，
         * 文件的打包过程被我封装在FileUtil.zipFile这个静态方法中，
         * 稍后会呈现出来，接下来的就是往客户端写数据了
         */
        // OutputStream out = response.getOutputStream();
        return response;
    }

    /**
     * 把接受的全部文件打成压缩包
     *
     * @param files
     * @param outputStream
     */
    public static void zipFile(List files, ZipOutputStream outputStream) {
        int size = files.size();
        for (int i = 0; i < size; i++) {
            File file = (File) files.get(i);
            zipFile(file, outputStream);
        }
    }

    public static HttpServletResponse downloadZip(File file, HttpServletResponse response) {
        try {
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();

            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(file.getName().getBytes(), "iso-8859-1"));
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                File f = new File(file.getPath());
                f.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    /**
     * 根据输入的文件与输出流对文件进行打包
     *
     * @param inputFile
     * @param ouputStream
     */
    public static void zipFile(File inputFile, ZipOutputStream ouputStream) {
        try {
            if (inputFile.exists()) {
                /**如果是目录的话这里是不采取操作的，
                 * 至于目录的打包正在研究中
                 */
                if (inputFile.isFile()) {
                    FileInputStream IN = new FileInputStream(inputFile);
                    BufferedInputStream bins = new BufferedInputStream(IN, 512);
                    //org.apache.tools.zip.ZipEntry
                    ZipEntry entry = new ZipEntry(inputFile.getName());
                    ouputStream.putNextEntry(entry);
                    // 向压缩文件中输出数据
                    int nNumber;
                    byte[] buffer = new byte[512];
                    while ((nNumber = bins.read(buffer)) != -1) {
                        ouputStream.write(buffer, 0, nNumber);
                    }
                    // 关闭创建的流对象
                    bins.close();
                    IN.close();
                } else {
                    try {
                        File[] files = inputFile.listFiles();
                        for (int i = 0; i < files.length; i++) {
                            zipFile(files[i], ouputStream);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}