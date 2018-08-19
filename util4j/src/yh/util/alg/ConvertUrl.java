package yh.util.alg;

import java.io.UnsupportedEncodingException;

/**
 * 转换URL地址
 * @author yh
 * 2011-11-16 9:30
 */
public class ConvertUrl {

	/**
	 * 转换为URL地址
	 * @param str 需要转换的字符串
	 * @return 得到的URL如%e6%b2%88
	 */
	public synchronized static String encodeUrl(String str){
		try {
			str=java.net.URLEncoder.encode(str,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * 解释URL地址
	 * @param str 需要转换的字符串如%e6%b2%88
	 * @return 得到的字符串“沈”
	 */
	public synchronized static String decodeUrl(String str){
		try {
			str=java.net.URLDecoder.decode(str,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(str);
		return str;
	}

	/**
	 *  转换为URL地址
	 * @param str 需要转换的字符串
	 * @param enc 转换字符编码
	 * @return
	 */
	public synchronized static String encodeUrl(String str,String enc){
		try {
			str=java.net.URLEncoder.encode(str,enc);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 *  解释URL地址
	 * @param str 需要转换的字符串
	 * @param enc 转换字符编码
	 * @return
	 */
	public synchronized static String decodeUrl(String str,String enc){
		try {
			str=java.net.URLDecoder.decode(str,enc);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(str);
		return str;
	}
}
