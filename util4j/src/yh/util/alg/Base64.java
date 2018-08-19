package yh.util.alg;
import java.io.UnsupportedEncodingException;

/**
 * Base64编码转换
 * @author yh
 * 时间：2012-3-13 14:50
 */
public class Base64 {

    private static char[] base64EncodeChars = new char[] {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
        'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
        'w', 'x', 'y', 'z', '0', '1', '2', '3',
        '4', '5', '6', '7', '8', '9', '+', '/' };

    private static byte[] base64DecodeChars = new byte[] {
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
    52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
    -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
    15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
    -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
    41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1 };

    
    /**
     * 加密为Base64码
     * @param Str 加密字符串
     * @param charSet 输入串编码
     * @return
     */
    public static String encode(String Str,String charSet) {
    	try {
			byte[] bytes=Str.getBytes(charSet);
			return encode(bytes);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * 加密为Base64码
     * @param data 输入的字节
     * @return 输出加密文本
     */
    public static String encode(byte[] data) {
        StringBuffer sb = new StringBuffer();
        int len = data.length;
        int i = 0;
        int b1, b2, b3;
        while (i < len) {
            b1 = data[i++] & 0xff;
            if (i == len)
            {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
                sb.append("==");
                break;
            }
            b2 = data[i++] & 0xff;
            if (i == len)
            {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
                sb.append("=");
                break;
            }
            b3 = data[i++] & 0xff;
            sb.append(base64EncodeChars[b1 >>> 2]);
            sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
            sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
            sb.append(base64EncodeChars[b3 & 0x3f]);
        }
        return sb.toString();
    }
    
    /**
     * 解密Base64内容
     * @param str 解密字符串
     * @param charSet 返回流转换编码
     * @return
     */
    public static String decode(String str,String charSet) {
    	byte[] bytes=decode(str);
    	String res=null;
    	try {
			res=new String(bytes,charSet);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return res;
    }
    /**
     * 解密Base64内容
     * @param str Base64密文
     * @return 解密后的字节流
     * @throws UnsupportedEncodingException
     */
    public static byte[] decode(String str){
        StringBuffer sb = new StringBuffer();
        byte[] data = null;
		try {
			data = str.getBytes("US-ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        int len = data.length;
        int i = 0;
        int b1, b2, b3, b4;
        while (i < len) {
            /* b1 */
            do {
                b1 = base64DecodeChars[data[i++]];
            } while (i < len && b1 == -1);
            if (b1 == -1) break;
            /* b2 */
            do {
                b2 = base64DecodeChars[data[i++]];
            } while (i < len && b2 == -1);
            if (b2 == -1) break;
            sb.append((char)((b1 << 2) | ((b2 & 0x30) >>> 4)));
            /* b3 */
            do {
                b3 = data[i++];
                if (b3 == 61)
					try {
						return sb.toString().getBytes("iso8859-1");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
                b3 = base64DecodeChars[b3];
            } while (i < len && b3 == -1);
            if (b3 == -1) break;
            sb.append((char)(((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));
            /* b4 */
            do {
                b4 = data[i++];
                if (b4 == 61)
					try {
						return sb.toString().getBytes("iso8859-1");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
                b4 = base64DecodeChars[b4];
            } while (i < len && b4 == -1);
            if (b4 == -1) break;
            sb.append((char)(((b3 & 0x03) << 6) | b4));
        }
        try {
			return sb.toString().getBytes("iso8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
    }
}

//public static void main(String[] args) throws UnsupportedEncodingException {
////	String temp = new String(Base64.decode("QUFodHRwOi8vbS45MS5jb20vU29mdC9Db250cm9sbGVyLmFzaHg/YWN0aW9uPVJlYWRGaWxlJlBsYXRmb3JtPWlQaG9uZSZmX2lkPTEwMDg3MjBaWg=="));
////	System.out.println(temp);
////	
//	String str="Base64要求把每三个8Bit的字节转换为四个6Bit的字节（3*8 = 4*6 = 24），然后把6Bit再添两位高位0，组成四个8Bit的字节，也就是说，转换后的字符串理论上将要比原来的长1/3。";
//	
//	str+="http://www.baidu.com/baidu?tn=monline_5_dg&ie=utf-8&wd=Base64++//";
//	System.out.println(str);
//	String date=encode(str,"gbk");
//	
//	System.out.println(date);
//	
//	
//	String res=decode(date,"gbk");
//	
//	System.out.println(res);
//	
//}