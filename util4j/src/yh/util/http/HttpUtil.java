package yh.util.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class HttpUtil {
	
	/**
	 * 获取对象
	 * @param request
	 * @param attname
	 * @return
	 */
	public static Object getObject(HttpServletRequest request, String attname) {
		Object result = request.getAttribute(attname);
		return result;
	}
	
	/**
	 * 获取request中的int类型参数,如果不存在或错误返回default参数
	 * 
	 * @param request
	 * @param paraname
	 * @param defaultVal
	 * @return
	 */
	public static int getInt(HttpServletRequest request, String paraname,
			int defaultVal) {
		try {
			return Integer.parseInt(request.getParameter(paraname));
		} catch (Exception e) {
			return defaultVal;
		}
	}

	/**
	 * 获取request中的double类型参数,如果不存在或错误返回default参数
	 * 
	 * @param request
	 * @param paraname
	 * @param defaultVal
	 * @return
	 */
	public static double getDouble(HttpServletRequest request, String paraname,
			double defaultVal) {
		try {
			return Double.parseDouble(request.getParameter(paraname));
		} catch (Exception e) {
			return defaultVal;
		}
	}

	/**
	 * 获取request中的long类型参数,如果不存在或错误返回default参数
	 * 
	 * @param request
	 * @param paraname
	 * @param defaultVal
	 * @return
	 */
	public static long getLong(HttpServletRequest request, String paraname,
			int defaultVal) {
		try {
			return Long.parseLong(request.getParameter(paraname));
		} catch (Exception e) {
			return defaultVal;
		}
	}

	/**
	 * 获取request中的字符串类�?
	 * 
	 * @param request
	 * @param paraname
	 * @return
	 */
	public static String getString(HttpServletRequest request, String paraname) {
		String result = request.getParameter(paraname) == null ? "" : request
				.getParameter(paraname).trim();
		return result;
	}

	/**
	 * 获取request中的字符串类
	 * 
	 * @param request
	 * @param paraname
	 * @return
	 */
	public static String getString(HttpServletRequest request, String paraname,
			String charSet) {
		String result = request.getParameter(paraname);
		result = (result == null) ? "" : request.getParameter(paraname);
		try {
			result = new String(result.getBytes("iso-8859-1"), charSet);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static short getShort(HttpServletRequest request, String paraname,
			short defaultVal) {
		try {
			return Short.parseShort(request.getParameter(paraname));
		} catch (Exception e) {
			return defaultVal;
		}
	}

	public static void write(HttpServletResponse response, String result) {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		if (result == null)
			result = "";
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.println(result);
		out.flush();
		out.close();
	}
}
