package yh.util.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ConsistentHash {
	
	public static void main(String[] args){
		String key = "";
		System.out.println(md5HashingAlg(key));
		System.out.println(0xFF);
		System.out.println(0xaa);
		System.out.println(0xaa & 0xFF);
		
		System.out.println("long max value:" + Long.MAX_VALUE);
		System.out.println("byte min value:" + Byte.MIN_VALUE);
		System.out.println("byte max value:" + Byte.MAX_VALUE);
		
		ConsistentHash t = new ConsistentHash();
		  System.out.println(t.getClass().getResource(""));
		  System.out.println(t.getClass().getResource("."));
		  System.out.println(t.getClass().getResource("/"));
	}
	
	public static Long md5HashingAlg(String key) {
        MessageDigest md5 = null;
		if(md5==null) {
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException( "++++ no md5 algorythm found");            
            }
        }
        md5.reset();
        md5.update(key.getBytes());
        byte[] bKey = md5.digest();
        System.out.println(bKey.length);
        
        System.out.println("-------");
        System.out.println(bKey[3] + " " + (bKey[3]&0xFF));
        System.out.println(Integer.toBinaryString(bKey[3]));
        System.out.println(Integer.toBinaryString(bKey[3]&0xFF));
        System.out.println(bKey[2] + " " + (bKey[2]&0xFF));
        System.out.println(bKey[1] + " " + (bKey[1]&0xFF));
        System.out.println(bKey[0] + " " + (bKey[0]&0xFF));
        
        long res = ((long)(bKey[3]&0xFF) << 24) | ((long)(bKey[2]&0xFF) << 16) | ((long)(bKey[1]&0xFF) << 8) | (long)(bKey[0]&0xFF);
        return res;
    }
}
