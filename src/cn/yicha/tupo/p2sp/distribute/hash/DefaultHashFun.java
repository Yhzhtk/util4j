package cn.yicha.tupo.p2sp.distribute.hash;  

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class DefaultHashFun implements HashFunction{

	static MessageDigest md = null;
	
	public DefaultHashFun(){
		try {
			md = MessageDigest.getInstance("md5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Integer hash(String str) {
		return digestToInt(str);
	}

	@Override
	public Integer hash(Object obj) {
		return digestToInt(obj.toString());
	}
	
	static int digestToInt(String str){
		 md.reset();
         md.update(str.getBytes());
         byte[] digest = md.digest();
         int v = 0;
         for(int i = 0; i < 4; i++){
        	 v |= ((digest[i] & 0xff) << ((3 - i) * 8));
         }
         return v;
	}
	
	/**
     * 获取字符串的md5
     * @param str
     * @return
     */
    static String digestToStr(String str){
            md.reset();
            md.update(str.getBytes());
            byte[] digest = md.digest();
            return bin2Hex(digest);
    }
    
    /**
     * 将buf转为字符串
     * @param buff
     * @return
     */
    static String bin2Hex(byte[] buff) {
            StringBuilder sb = new StringBuilder();
            String temp;
            for (byte bt : buff) {
                    temp = Integer.toHexString(bt & 0xff);
                    sb.append(temp.length() == 1 ? ('0' + temp) : temp);
            }
            return sb.toString();
    }
}