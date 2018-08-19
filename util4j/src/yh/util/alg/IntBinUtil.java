package yh.util.alg;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 整型的二进制文件读写和转换
 *@author: yh
 *@date:2013-1-31
 */
public class IntBinUtil {

	public static void main(String[] args) throws IOException{
		int i = 0x6aCa1aa2;
		System.out.println(i);
		
		byte s = (byte) (1992603 & 0xFF);
		System.out.println(s);
		
		byte[] bytes = getBytes(i);
		write("out", bytes);
		
		byte[] b2 = read("out");
		System.out.println(getInteger(b2));
		
		writeTxt("out1", i + "");
	}
	
	/**
	 * 将整数转为连续的二进制，最左边的二进制数据时bytes[0]
	 * @param i
	 * @return
	 * @date:2013-2-1
	 * @author:yh
	 */
	public static byte[] getBytes(int i){
		byte[] bytes = new byte[4];
		
		for(int m = 3; m >= 0; m--){
			bytes[m] = (byte) (i & 0xff);
			i >>= 8;
		}
		
		return bytes;
	}
	
	/**
	 * 将连续的二进制转为整数, bytes[0]将写入最左边
	 * @param bytes
	 * @return
	 * @date:2013-2-1
	 * @author:yh
	 */
	public static int getInteger(byte[] bytes){
		int n = 0;
		int s = 0;
		for(int i = 0; i < 4; i++){
			n = n << 8;
			s = bytes[i];
			if(bytes[i] < 0){
				s += 256;
			}
			n = n + s;
		}
		return n;
	}
	
	/**
	 * 读二进制数据
	 * @param fileName
	 * @return
	 * @throws IOException
	 * @date:2013-2-1
	 * @author:yh
	 */
	public static byte[] read(String fileName) throws IOException {
		File file = new File(fileName);
		DataInputStream in = new DataInputStream(new FileInputStream(file ));
		
		byte[] bytes = new byte[4];
		
		in.read(bytes);
		in.close();
		
		return bytes;
	}
	
	/**
	 * 写二进制数据
	 * @param fileName
	 * @param bytes
	 * @throws IOException
	 * @date:2013-2-1
	 * @author:yh
	 */
	public static void write(String fileName, byte[] bytes) throws IOException {
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
		for(byte b : bytes){
			out.write(b);
		}
		out.close();
	}
	
	/**
	 * 写文本数据
	 * @param fileName
	 * @param content
	 * @date:2013-2-1
	 * @author:yh
	 */
	public static void writeTxt(String fileName, String content) {
		FileWriter fw;
		try {
			fw = new FileWriter(fileName);

			fw.write(content);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
