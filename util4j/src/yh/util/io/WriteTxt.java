package yh.util.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * 写Txt文件类
 * @author yh
 * 2011-12-16 15:00
 */
public class WriteTxt {

	/**
	 * 写txt文件
	 * @param fileName 文件名
	 * @param str 文件内容
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
	
	/**
	 * 写txt文件
	 * @param fileName 文件名
	 * @param content 文件内容
	 * @param charSet 文件内容编码字符集
	 * @return
	 */
	public static boolean writeTxt(String fileName, String content, String charSet) {

		File file = new File(fileName);
		File path=new File(file.getParent());
		if(!path.exists()){
			path.mkdirs(); 
		}
		if (!file.exists()) {
			try {
				file.createNewFile();// 文本不存在,建立
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), charSet));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			out.write(content);//写文件内容
			out.flush();
			out.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 将字符串数组写入txt文件，以换行区分
	 * @param fileName 文件名
	 * @param contents 文件内容的字符串数组
	 * @return
	 */
	public static boolean writeTxt(String fileName, String[] contents) {

		String strTxt = "";
		for (int i = 0; i < contents.length; i++) {
			strTxt += contents[i] + "\r\n";
		}
		FileWriter fw;
		try {
			fw = new FileWriter(fileName);
			fw.write(strTxt);
			fw.flush();
			fw.close();

			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将字符串数组写入txt文件，以换行区分
	 * @param fileName 文件名
	 * @param contents 文件内容的字符串数组
	 * @param charSet 文本内容编码字符集
	 * @return
	 */
	public static boolean writeTxt(String fileName, String[] contents,String charSet) {

		File file = new File(fileName);
		File path=new File(file.getParent());
		if(!path.exists()){
			path.mkdirs(); 
		}
		if (!file.exists()) {
			try {
				file.createNewFile();// 文本不存在,建立
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), charSet));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String strTxt = "";
		for (int i = 0; i < contents.length; i++) {
			strTxt += contents[i] + "\r\n";
		}
		
		try {
			out.write(strTxt);//写文本内容
			out.flush();
			out.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 附加txt文本
	 * @param fileName 文本名
	 * @param content 附加的文本内容
	 */
	public static void appendTxt(String fileName, String content) {
		FileWriter fw;
		try {
			fw = new FileWriter(fileName,true);//第二个参数为true表示附加

			fw.write(content);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 使用RandomAccessFile方法追加文本UTF-8字符集
	 * @param fileName 文本名 
	 * @param content 追加的内容
	 */
	public static void appendUTFTxt(String fileName, String content) {
		try {
			// 打开一个随机访问文本流，按读写方式
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			// 文本长度，字节数
			long fileLength = randomFile.length();
			// 将写文本指针移到文本尾。
			randomFile.seek(fileLength);
			randomFile.writeUTF(content);
			randomFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
