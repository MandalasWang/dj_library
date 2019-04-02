package com.djcps.library.common;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author djsxs
 *
 */
public class FileUpLoadUtil {

	public static File fileUpLoad(MultipartFile file) {
		String filePath = "D:\\work\\java\\eclipse-workspace\\dj_library\\src\\main\\resources\\static\\upload\\img\\";
		// 获取文件名
		String fileName = file.getOriginalFilename();
		// 获取文件的后缀名,比如图片的jpeg,png
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		// 文件上传后的路径
		fileName = UUID.randomUUID() + suffixName;
		File dest = new File(filePath + fileName);
		try {
			file.transferTo(dest);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dest;
	}

}
