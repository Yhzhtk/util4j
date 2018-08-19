package yh.util.dbpool;

/**
 * 数据库处理的小方法类
 * @author yh
 * 创建时间：2012-12-5
 */
public class DbEscape {

	/**
	 * 转义字符串，用于数据库插入或者判断
	 * @param str 需要转义的字符串
	 * @return 转义的结果
	 */
	public static String getEscape(String str) {
		if (str == null) {
			return null;
		} else {
			str = str.replace("\\", "\\\\").replace("'", "\\'")
			.replace("\"", "\\\"");
		}
		return str;
	}
	
	/**
	 * 转义字符串，用于数据库查询
	 * @param str 需要转义的字符串
	 * @return 转义的结果
	 */
	public static String getSearchEscape(String str) {
		if (str == null) {
			return null;
		} else {
			str = getEscape(str).replace("_", "\\_").replace("%", "\\%");
		}
		return str;
	}
}
