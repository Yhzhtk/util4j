package yh.util.alg;

/**
 * 字符串相似度匹配
 * 
 * 假设两个字符串ABCDEFG和ABECDFG有这样的一种匹配方式：
 * AB-CDEFG匹配ABECD-FG（字符串1包含一个-称为插入，字符串2包含一个-称为缺失，合称插缺）， 则此例包含6个匹配和2个插缺（没有替换）。
 * 如果为字符串的每个位置编号，则匹配路线成为（11）（22）（23）（34）（45）（55）（66）（77）。
 * 每一种候选的匹配方式都对应一个匹配路线，可以定义一个得分函数来比较各种匹配方式的优劣。 最简单的一种方式是给匹配记一个+1分、给不匹配记一个0分，
 * 这种得分函数最终计算出两个字符串的最长共同子序列。
 * 
 * 最后除以两个字符串的平均个数，即为得分
 * 
 * @author:gudaihui
 * @date:2013-11-19
 */
public class SimilarMatch {

	private int scores[][] = new int[100][200];

	int fetchScore(char x, char y) {
		return x == y ? 1 : 0;
	}

	/**
	 * 返回src和dest的匹配得分
	 * @param src
	 * @param dest
	 * @return
	 * @date:2013-11-19
	 * @author:gudaihui
	 */
	public float similarRate(String src, String dest) {
		// 长的放后面
		if (src.length() > dest.length()) {
			String temp = src;
			src = dest;
			dest = temp;
		}
		if (scores.length < src.length() + 1
				|| scores[0].length < dest.length() + 1) {
			scores = new int[src.length() + 1][dest.length() + 1];
		}

		for (int i = 1; i <= src.length(); i++) {
			for (int j = 1; j <= dest.length(); j++) {
				int score = fetchScore(src.charAt(i - 1), dest.charAt(j - 1));
				scores[i][j] = Math.max(
						Math.max(scores[i - 1][j], scores[i][j - 1]),
						scores[i - 1][j - 1] + score);
			}
		}
		return scores[src.length()][dest.length()] * 2f
				/ (src.length() + dest.length());
	}

}
