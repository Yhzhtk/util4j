package cn.yicha.tupo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cn.yicha.tupo.http.file.FileFactory;
import cn.yicha.tupo.p2sp.P2SPDownload;

/**
 * p2sp的shell运行，通过命令形式控制p2sp下载
 * 
 * @author gudh
 * @date 2013-11-25
 */
public class P2SPShell {

	/**
	 * http://mirror.astpage.ru/centos/6.4/updates/i386/repodata/
	 * edc53fcf6f3468f6cdd0af4dd68de92573e790724525c668c27a9d69858a0b7f
	 * -primary.xml.gz http://mirror.vilkam.ru/centos/6.4/updates/i386/repodata/
	 * edc53fcf6f3468f6cdd0af4dd68de92573e790724525c668c27a9d69858a0b7f
	 * -primary.xml.gz
	 * http://mirror.fairway.ne.jp/centos/6.4/updates/i386/repodata
	 * /edc53fcf6f3468f6cdd0af4dd68de92573e790724525c668c27a9d69858a0b7f
	 * -primary.xml.gz
	 */
	static String exampleStr = "======================================\nExample URLS:\n"
			+ "http://mirror.astpage.ru/centos/6.4/updates/i386/repodata/edc53fcf6f3468f6cdd0af4dd68de92573e790724525c668c27a9d69858a0b7f-primary.xml.gz\n"
			+ "http://mirror.vilkam.ru/centos/6.4/updates/i386/repodata/edc53fcf6f3468f6cdd0af4dd68de92573e790724525c668c27a9d69858a0b7f-primary.xml.gz\n"
			+ "http://mirror.fairway.ne.jp/centos/6.4/updates/i386/repodata/edc53fcf6f3468f6cdd0af4dd68de92573e790724525c668c27a9d69858a0b7f-primary.xml.gz"
			+ "\n======================================";

	static String descStr = "\n  P2SPShell(P2SP命令控制工具)\n"
			+ "--------------------------------------\n"
			+ "  description\tP2SP下载文件(多线程多资源地址，资源数量动态增减)。\n"
			+ "  version\t1.0\n  author\t顾代辉\n  date\t2013-11-26";

	static String helpStr = "======================================\n"
			+ "  help(h)\t查看帮助\n  example(e)\t示例地址\n  p2sp(p)\t开始P2SP下载\n  exit\t退出\n"
			+ "--------------------------------------\n"
			+ "  check(c)\t检测是否下载完成\n  add(a)\t在接下来的一行输入新增的URL地址\n"
			+ "  remove(r)\t在接下来的一行输入需要删除的URL地址\n  stop(s)\t停止下载\n  view(v)\t查看Range\n"
			+ "======================================";

	static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		printlnMsg(descStr);
		printlnMsg(helpStr);
		while (true) {
			String l = getNextString("> ", ".*").trim();
			if (l.equals("help") || l.equals("h")) {
				printlnMsg(helpStr);
			} else if (l.equals("example") || l.equals("e")) {
				printlnMsg(exampleStr);
			} else if (l.equals("p2sp") || l.equals("p")) {
				startP2SP();
			} else if (l.trim().equals("exit")) {
				System.exit(0);
			} else if (l.startsWith("gc")) {
				String[] infos = l.split(" +");
				if (infos.length > 1) {
					FileFactory.closeFile(infos[1]);
				}
				System.gc();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.gc();
			} else if (!l.trim().equals("")) {
				printlnMsg("没有命令：" + l);
			}
		}
	}

	/**
	 * 开始一次P2SP下载
	 */
	public static void startP2SP() {
		int n = getNextInt("请输入初始URL数量：", 0, 20);

		if (n == 0) {
			printlnMsg("资源数为0，返回不下载！");
			return;
		}

		List<String> urls = new ArrayList<String>(n);
		printlnMsg("请输入" + n + "行URL");
		for (int i = 0; i < n; i++) {
			urls.add(getNextString(i + ":", "http.*"));
		}

		String fileName = getNextString("请输入文件名：", "([c-fC-F]:)?/.*");

		int i = getNextInt("请输入最大允许的线程数：", 1, 100);
		P2SPDownload p2sp = new P2SPDownload(urls, fileName, i);

		long start = System.currentTimeMillis();
		p2sp.start();

		while (true) {
			String l = getNext().trim();
			try {
				// 检测是否完成
				if (l.equals("check") || l.equals("c")) {
					if (p2sp.checkComplete()) {
						System.gc();
						break;
					} else {
						printMsg("================\nCheck Not Complete\n================\n");
					}
				} else if (l.equals("add") || l.equals("a")) {
					String uri = getNextString("请输入新增URL：", "(|http.*)");
					if (uri.trim().equals("")) {
						continue;
					}
					p2sp.addUrl(uri);
				} else if (l.equals("remove") || l.equals("r")) {
					String uri = getNextString("请输入删除URL：", "(|http.*)");
					if (uri.trim().equals("")) {
						continue;
					}
					p2sp.removeUrl(uri);
				} else if (l.equals("stop") || l.equals("s")) {
					p2sp.stop();
					p2sp.checkComplete();
				} else if (l.equals("view") || l.equals("v")) {
					p2sp.showRange();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		long end = System.currentTimeMillis();
		printlnMsg("下载文件：" + fileName + "  共用时：" + (end - start));
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
			printMsg(inputMsg);
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
			printMsg(inputMsg);
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
