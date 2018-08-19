package yh.util.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则基础类
 * @author yh
 * 2010-12-19 10:00
 */
public class RegexUtil {

	/**
	 * 正则匹配，返回匹配结果
	 * 
	 * @param source
	 *            匹配目标串
	 * @param regex
	 *            匹配模式串
	 * @param index
	 *            返回从0开始第index个
	 * @param group
	 *            返回第group个组
	 * @return 返回匹配结果，如果没有找到则返回null
	 */
	public static String Regex(CharSequence source, String regex, int index,
			int group) {

		if (source == null || regex == null)
			return null;

		Pattern pattern = Pattern.compile(regex,Pattern.DOTALL);
		Matcher matcher = pattern.matcher(source);

		for (int i = 0; i < index; i++)
			matcher.find();
		if (matcher.find()) {
			return matcher.group(group);
		} else {
			return null;
		}
	}

	/**
	 * 正则匹配，返回匹配结果
	 * 
	 * @param source
	 *            匹配目标串
	 * @param regex
	 *            匹配模式串
	 * @param index
	 *            返回从0开始第index个
	 * @param groupNum
	 *            返回group个组
	 * @return 返回匹配结果，如果没有找到则返回null
	 */
	public static String[] RegexAll(CharSequence source, String regex, int index,
			int groupNum) {

		if (source == null || regex == null)
			return null;

		String[] groups = new String[groupNum];
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(source);

		for (int i = 0; i < index; i++)
			matcher.find();
		if (matcher.find()) {
			for(int j = 0; j < groupNum; j++){
				groups[j] = matcher.group(j + 1);
			}
			return groups;
		} else {
			return null;
		}
	}
	
	/**
	 * 返回匹配结果集,其中有groupNum个结果
	 * 
	 * @param source
	 *            模式串
	 * @param regex
	 *            目标串
	 * @param groupNum
	 *            返回组数
	 * @return List<String>对象
	 * @throws Exception 
	 */
	public static List<String[]> RegexAll(CharSequence source, String regex,
			int groupNum) throws Exception {
		List<String[]> list = new ArrayList<String[]>();

		if (source == null || regex == null)
			return list;

		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(source);

		try {
			while (matcher.find()) {
				String[] groups = new String[groupNum];
				for (int i = 0; i < groupNum; i++) {
					groups[i] = matcher.group(i + 1);
				}
				list.add(groups);
			}
		} catch (Exception e) {
			throw e;
		}
		System.out.println("匹配" + regex + "有：" + list.size() + "个");
		return list;
	}
	
	/**
	 * 判断目标串中是否存在某个模式
	 * 
	 * @param source
	 *            目标串
	 * @param regex
	 *            正则模式串
	 * @return 存在结果
	 */
	public static boolean RegexExist(CharSequence source, String regex) {

		if (source == null || regex == null)
			return false;

		Pattern pattern = Pattern.compile(regex,Pattern.DOTALL);
		Matcher matcher = pattern.matcher(source);

		if (matcher.find()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 返回匹配结果集
	 * 
	 * @param source
	 *            模式串
	 * @param regex
	 *            目标串
	 * @param group
	 *            返回第group个组
	 * @return List<String>对象
	 */
	public static List<String> Regex(CharSequence source, String regex,
			int group) {
		List<String> list = new ArrayList<String>();

		if (source == null || regex == null)
			return list;

		Pattern pattern = Pattern.compile(regex,Pattern.DOTALL);
		Matcher matcher = pattern.matcher(source);

		while (matcher.find()) {
			list.add(matcher.group(group));
		}
		System.out.println("匹配" + regex + "有：" + list.size() + "个");
		return list;
	}
}
