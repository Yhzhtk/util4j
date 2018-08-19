package yh.util.js;

import java.math.BigInteger;

public class ParseJs {
	//转换类
	public static String parseJs(String p, int a, int c, String[] k, int e, Object d) {
		while(c-- > 0) {
			if(k[c] != null) {
				String eac = e(a, c);
				int eaci = toDecimal(eac, 36);
				if(k[eaci] == null || k[eaci].equals("")) {
					k[eaci] = eac.toLowerCase();
				}
				p = p.replaceAll("\\b" + eac.toLowerCase() + "\\b", k[eaci]);
			}
		}
		System.out.println(p);
		return p;
	}
	
	private static String e(int a, int c) {
		String result = ((c < a) ? "" : e(a, c/a)) + ((c = c % a) > 35 ? "" + (char) (c + 29) : toAnyConversion(new BigInteger(c + ""), new BigInteger("36")));
		return result;
	}
	
	public static void main(String[] args) {
		String p = "g 4(){3 5=\"f://e.k.j.h/7/8/6\";3 2=[\"/c.1\",\"/9.1\",\"/a.1\",\"/b.1\",\"/r.1\",\"/q.1\",\"/s.1\",\"/t.1\",\"/p.1\",\"/m.1\"];l(3 i=0;i<2.n;i++){2[i]=5+2[i]}o 2}3 d=4();";
		int a = 30;
		int c = 30;
		String[] k = "|png|pvalue|var|dm5imagefun|pix|111894||6974|5_8627|6_7086|7_9292|4_7947||tel|http|function|com||yourour|manhua21|for|13_3579|length|return|12_7096|9_6717|8_4330|10_3437|11_1813".split("\\|");
		int e = 0;
		Object d = new Object();
		parseJs(p, a, c, k, e, d);
	}
	
	//数字转换为字符
    static char changToNum(BigInteger temp)
    {
    	int n = temp.intValue();
        if(n >= 10 && n <= 35)
            return (char) (n - 10 + 'A');

        else if(n >= 36 && n <= 61)
            return (char)(n - 36 + 'a');

        else
            return (char)(n + '0');
    }
    
  //十进制转换为任意进制
    public static String toAnyConversion(BigInteger Bigtemp, BigInteger base)
    {
        String ans = "";
        while(Bigtemp.compareTo(BigInteger.ZERO) != 0)
        {
            BigInteger temp = Bigtemp.mod(base);
            Bigtemp = Bigtemp.divide(base);
            char ch = changToNum(temp);
            ans = ch + ans;
        }
        return ans;
    }
    
  //任意进制转换为10进制
    public static int toDecimal(String input, int base)
    {
        BigInteger Bigtemp = BigInteger.ZERO, temp = BigInteger.ONE;
        int len = input.length();
        for(int i = len - 1; i >= 0; i--)
        {
            if(i != len - 1)
                temp = temp.multiply(BigInteger.valueOf(base));
            int num = changeDec(input.charAt(i));
            Bigtemp = Bigtemp.add(temp.multiply(BigInteger.valueOf(num)));
        }
        return Bigtemp.intValue();
    }
    
  //十进制转换中把字符转换为数
    static int changeDec(char ch)
        {
            int num = 0;
            if(ch >= 'A' && ch <= 'Z')
                num = ch - 'A' + 10;
            else if(ch >= 'a' && ch <= 'z')
                num = ch - 'a' + 36;
            else
                num = ch - '0';
            return num;
        }
}
