package cn.yicha.tupo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cn.yicha.tupo.p2sp.P2SPDownload;

/**
 * p2sp的shell运行，通过命令形式控制p2sp下载
 * 
 * @author gudh
 * @date 2013-11-25
 */
public class P2SPShell {

	static String helpStr = "";

	static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {

		int n = getNextInt("请输入初始URL数量：", 1, 10);

		List<String> urls = new ArrayList<String>(n);
		printlnMsg("请输入" + n + "行URL");
		for (int i = 0; i < n; i++) {
			urls.add(getNextString(i + ":", "http.*"));
		}

		String fileName = getNextString("请输入文件名：", "[c-fC-F]:/.*");

		int i = getNextInt("请输入最大允许的线程数：", 1, 10);
		P2SPDownload p2sp = new P2SPDownload(urls, fileName, i);

		long start = System.currentTimeMillis();
		p2sp.start();

		while (true) {
			String l = getNext();
			// 检测是否完成
			if (l.trim().equals("check")) {
				if (p2sp.isComplete()) {
					break;
				} else {
					printMsg("\nCheck Not Complete\n");
				}
			} else if (l.trim().equals("add")) {
				String uri = getNextString("请输入新增URL：", "http.*");
				if (uri.equals("http")) {
					continue;
				}
				p2sp.addUrl(uri);
			} else if (l.trim().equals("remove")) {
				String uri = getNextString("请输入删除URL：", "http.*");
				if (uri.equals("http")) {
					continue;
				}
				p2sp.removeUrl(uri);
			} else if (l.trim().equals("stop")) {
				p2sp.stop();
			}
		}
		long end = System.currentTimeMillis();
		System.out.print("Uri Count：" + urls.size() + "  All Time：" + (end - start));
	}

	/**
	 * 获取在指定范围的整型
	 * 
	 * @param inputMsg
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getNextInt(String inputMsg, int start, int end) {
		int i = Integer.MIN_VALUE;
		do {
			printlnMsg(inputMsg);
			try {
				i = Integer.parseInt(getNext());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			if (i >= start && i <= end) {
				break;
			}
			printMsg("输入不合法！");
		} while (true);

		return i;
	}

	/**
	 * 获取匹配正则的字符串
	 * 
	 * @param inputMsg
	 * @param regex
	 * @return
	 */
	public static String getNextString(String inputMsg, String regex) {
		String str = "";
		do {
			printlnMsg(inputMsg);
			str = getNext();
			if (str.matches(regex)) {
				break;
			}
			printMsg("输入不合法！");
		} while (true);
		return str;
	}

	public static String getNext() {
		String l = scanner.nextLine();
		if (l.equals("exit")) {
			System.exit(0);
		}
		return l;
	}

	public static void printMsg(String msg) {
		System.out.print(msg);
	}

	public static void printlnMsg(String msg) {
		System.out.println(msg);
	}

}
