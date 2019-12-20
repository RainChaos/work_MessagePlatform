package com.jiapeng.messageplatform.controller;

import com.jiapeng.messageplatform.utils.XjpResultUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Controller
@RequestMapping("file")
public class FileController{


	String upfile_path = "E:/msg/upload";

	/**
	 * 富文本图片上传
	 * @param file
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
	@ResponseBody
	public XjpResultUtil upload2(MultipartFile file, HttpServletRequest req) {
		if (file == null) {
			return XjpResultUtil.error("图片不能为空！");
		}
		String fileSub = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."))
				.toLowerCase();
		if (".jpg".equals(fileSub) || ".jpeg".equals(fileSub) || ".png".equals(fileSub) || ".gif".equals(fileSub)) {
			Random d = new Random();
			String img = UUID.randomUUID().toString().replace("-", "") + d.nextInt(10000) + "" + fileSub;
			try {
				File f=new File(upfile_path);
				if(!f.exists()){
					f.mkdirs();
				}
				file.transferTo(new File(f, img));
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Map<String, String> map = new HashMap<>();
			String ctx = req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort();
			map.put("src", ctx+"/file/showPic?fileName="+img);
			return XjpResultUtil.ok(map);
		} else {
			return XjpResultUtil.error("文件格式不支持,请重新选择！");
		}
	}


	/**
	 * 图片显示
	 *
	 * @param fileName
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/showPic")
	@ResponseBody
	public String showPic(String fileName, HttpServletResponse response) {
		// String fileName="4d857472b93b4b9bb04d65f1506b2a329707.jpeg";
		FileInputStream fis = null;
		OutputStream os = null;
		try {
			//图片的本地全路径
			fis = new FileInputStream(upfile_path+"/"+ fileName);
			os = response.getOutputStream();
			int count = 0;
			byte[] buffer = new byte[1024 * 8];
			while ((count = fis.read(buffer)) != -1) {
				os.write(buffer, 0, count);
				os.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			fis.close();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "ok";
	}
	
	/**
	 * 处理文件下载
	 * @param request
	 * @param response
	 * @param fileName
	 */
	@RequestMapping(value = "/downLoad")
	@ResponseBody
	public void downLoadList(HttpServletRequest request, HttpServletResponse response,
                             String fileName){
	        downloadFile(new File(upfile_path,fileName), response, false);
	}
	
	/**
	 * 文件下载
	 * @param file
	 * @param response
	 * @param isDelete
	 */
	public void downloadFile(File file, HttpServletResponse response, boolean isDelete) {
		try {
			// 以流的形式下载文件。
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=" + new String(file.getName().getBytes("UTF-8"),"ISO-8859-1"));
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
			if(isDelete)
			{
				file.delete();        //是否将生成的服务器端文件删除
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}


	/**
	 * 文件删除
	 * @param file
	 * @param
	 */
	public void deleteFile(File file) {
		if(!file.exists()) return;
		file.delete();        //是否将生成的服务器端文件删除
		System.out.println("删除了"+file.getName());


	}
}
