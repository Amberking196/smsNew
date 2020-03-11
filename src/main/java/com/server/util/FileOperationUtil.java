package com.server.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.LocalDate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import sun.misc.BASE64Decoder;
@RestController
public class FileOperationUtil {

	private static int fileSize = 1024*1024*10;
	private static String[] imageType = {"image/jpg","image/jpeg","image/png"};
	/**
	 * 使用base64上传文件
	 * @param base64
	 */
	@PostMapping("/uploadImage")
	public ReturnDataUtil uploadFile(@RequestBody Map<String,String> param,HttpServletRequest request){
		String base64 = param.get("base64");
		ReturnDataUtil returnData = new ReturnDataUtil();
		if(base64==null || base64.length()==0){
			returnData.setStatus(0);
			returnData.setMessage("图片未接受到");
			return returnData;
		}
		
		if(allowType(base64.substring(base64.indexOf(":")+1, base64.indexOf(";")))){
			returnData.setStatus(0);
			returnData.setMessage("图片格式不正确");
			return returnData;
		}
		if(imageSize(base64)>fileSize){
			returnData.setStatus(0);
			returnData.setMessage("图片大小超出范围");
			return returnData;
		}
		StringBuffer uploadFilePath = new StringBuffer(request.getServletContext().getRealPath("/"));
		LocalDate date = LocalDate.now();
		uploadFilePath.append("image/"+date.getYear()+"/"+date.getMonthOfYear()+"/"+date.getDayOfMonth()+"/");
		String uploadFileName = UUIDUtil.getUUID()+".png";
		File file = new File(uploadFilePath.toString());
		if(!file.exists()){
			file.mkdirs();
		}
		BASE64Decoder decoder = new BASE64Decoder();
		File imageFile = new File(uploadFilePath+uploadFileName);
		OutputStream out = null;
		try {
			out = new FileOutputStream(imageFile);
			base64 = base64.substring(base64.indexOf(",")+1, base64.length());
			byte[] imgByte = decoder.decodeBuffer(base64);
			for (int i = 0; i < imgByte.length; ++i) {      
                if (imgByte[i] < 0) {// 调整异常数据      
                	imgByte[i] += 256;      
                }      
            }  
			out.write(imgByte);
			out.flush();
			returnData.setStatus(1);
			returnData.setMessage("成功");
			returnData.setReturnObject(uploadFilePath+uploadFileName);
		} catch (IOException e) {
			e.printStackTrace();
			returnData.setStatus(0);
			returnData.setMessage("上传失败");
		}finally{
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}			
			}
		}
		return returnData;
	}
	
	public Integer imageSize(String image){
		String str = image.substring(22);
		Integer equalIndex = str.indexOf("=");
		if(equalIndex>0){
			str = str.substring(0, equalIndex);
		}
		Integer strLength = str.length();
		Integer size = strLength-(strLength/8)*2;
		return size;
	}
	
	public boolean allowType(String type){
		for (String image : imageType) {
			if(type.equals(image)){
				return false;
			}
		}
		return true;
	}
	
}
