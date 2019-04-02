package com.djcps.library.common;

import java.util.Random;

/**
 * @author djsxs
 *
 */
public class RandomUtil {
	public static final String ALLCHAR = "0123456789";

	/**
	 * 返回一个定长的随机字符串(只包含大小写字母、数字)
	 * 
	 * @param length 随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateString(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println("返回一个定长的随机字符串(只包含大小写字母、数字):" + generateString(16));
	}
}