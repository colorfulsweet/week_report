package com.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * md5加密算法工具类
 * @author Administrator
 *
 */
public class Md5 {
	/**
	 * 对字符串执行md5加密
	 * @param plainText
	 * @return 加密后的字符串
	 */
	public static String encrypt(String plainText) {
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
}

