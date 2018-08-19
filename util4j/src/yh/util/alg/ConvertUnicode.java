package yh.util.alg;

/**
 * 转换\\u的字符串为中文
 * @author yh
 *
 */
public class ConvertUnicode {
	
	public static void main(String[] args){
		
		String str = "\\u4e1c\\u57ce\\u533a\\u7f8e\\u672f\\u9986\\u540e\\u885788\\u53f7(\\u9686\\u798f\\u5bfa\\u5317\\u4fa720\\u7c73)";
		System.out.println(getUnicode(str));
	}
	
	static String getUnicode(String str){
		String[] infos = str.split("\\\\u");
		StringBuffer res = new StringBuffer();
		String suf = "";
		boolean haveSuf = false;
		boolean isFirst = true;
		for(String info : infos){
			if(isFirst){
				res.append(info);
				isFirst = false;
				continue;
			}
			if(info.length() > 4){
				suf = info.substring(4);
				info = info.substring(0, 4);
				haveSuf = true;
			}
			int n = Integer.parseInt(info, 16);
			res.append((char)n);
			if(haveSuf){
				res.append(suf);
				haveSuf = false;
			}
		}
		return res.toString();
	}
	
	/**
	 * 参见java.util.Properties.loadConvert() u 转码
	 * 
	 * @param in
	 * @return
	 */
	public static String decode(CharSequence in) {
		StringBuilder out = new StringBuilder();
		char aChar;
		for (int i = 0, n = in.length(); i < n; i++) {
			aChar = in.charAt(i);
			if (aChar == '\\') {
				if(in.length() > i + 1){
					aChar = in.charAt(++i);
					if (aChar == 'u') {
						// Read the xxxx
						int value = 0;
						for (int inner = 0; inner < 4; inner++) {
							aChar = in.charAt(++i);
							switch (aChar) {
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								value = (value << 4) + aChar - '0';
								break;
							case 'a':
							case 'b':
							case 'c':
							case 'd':
							case 'e':
							case 'f':
								value = (value << 4) + 10 + aChar - 'a';
								break;
							case 'A':
							case 'B':
							case 'C':
							case 'D':
							case 'E':
							case 'F':
								value = (value << 4) + 10 + aChar - 'A';
								break;
							default:
								throw new IllegalArgumentException(
										"Malformed \\uxxxx encoding.");
							}
						}
						out.append((char) value);
					} else {
						if (aChar == 't')
							aChar = '\t';
						else if (aChar == 'r')
							aChar = '\r';
						else if (aChar == 'n')
							aChar = '\n';
						else if (aChar == 'f')
							aChar = '\f';
						out.append(aChar);
					}
				}else{
					out.append(aChar);
				}
			} else {
				out.append((char) aChar);
			}
		}
		return out.toString();
	}
}
