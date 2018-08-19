package yh.util.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 读Txt文件类
 * @author yh
 * 2011-12-16 11:00
 */
public class ReadTxt {
	
	/**
	 * 读取txt文件
	 * @param fileName 文件名
	 * @return 文件内容
	 */
	public static String readTxt(String fileName) {
		FileReader fr = null;
		try {
			fr = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		char[] cbuf = new char[1024];
		StringBuffer readStr = new StringBuffer();

		try {
			int len = 0;
			while ((len = fr.read(cbuf)) != -1) {
				readStr.append(cbuf, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return readStr.toString();
	}
	
	/**
	 * 读取txt文件
	 * @param fileName 文件名
	 * @param charSet 字符集
	 * @return 文件内容
	 */
	public static String readTxt(String fileName,String charSet) {
		InputStreamReader read = null;
		
		try {
			read = new InputStreamReader(new FileInputStream(fileName), charSet);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		BufferedReader br = new BufferedReader(read);
		
		char[] cbuf = new char[1024];
		StringBuffer readStr = new StringBuffer();

		try {
			int len = 0;
			while ((len = br.read(cbuf)) != -1) {
				readStr.append(cbuf, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return readStr.toString();
	}
	
	/**
	 * 读取txt以行区分返回字符串数组
	 * @param fileName 文件名
	 * @param charSet 字符集
	 * @return 结果数组
	 */
	public static String[] getListFromTxt(String fileName,String charSet){
		List<String> list=new ArrayList<String>();
		InputStreamReader read = null;
		
		try {
			read = new InputStreamReader(new FileInputStream(fileName), charSet);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		BufferedReader br = new BufferedReader(read);
		
		String str;
		try{
			while((str=br.readLine())!=null){
				list.add(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] strs =list.toArray(new String[]{});
		System.out.println("getListFromTxt 读取数据共"+strs.length+"条！");
		return strs;
	}

	/**
	 * 获取txt中指定行间的list，从开始行 到 结束行（包含开始行和结束行）
	 * @param fileName 文件名
	 * @param charSet 字符集
	 * @param rowStart 读取开始行 （从0开始）
	 * @param rowEnd 读取结束行
	 * @return
	 */
	public static String[] getListFromTxt(String fileName,String charSet,int rowStart,int rowEnd){
		List<String> list=new ArrayList<String>();
		InputStreamReader read = null;
		
		try {
			read = new InputStreamReader(new FileInputStream(fileName), charSet);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		BufferedReader br = new BufferedReader(read);
		
		String str;
		try {
			//br.readLine();//第一行不要，否则编码会有错误
			int i=0;
			while(i<rowStart){
				br.readLine();
				i++;
			}
			
			while((str=br.readLine())!=null && i++<=rowEnd){
				list.add(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] strs =list.toArray(new String[]{});
		System.out.println("getListFromTxt 读取数据共"+strs.length+"条！");
		return strs;
	}
	
	/**
	
	 * 判断str在list中是否存在
	 * @param list 需要排重的list
	 * @param str 比较字符串
	 * @param charStart 比较str的开始位置，0开始  
	 * @param charEnd 比较str结束位置 (-1表示原始长度）
	 * @return 是否重复
	 * @throws StringIndexOutOfBoundsException 如果charStart或charEnd超过字符串大小的话抛出异常
	 */
	public static boolean isRepeat(List<String> list,String str,int charStart,int charEnd) throws StringIndexOutOfBoundsException{
		if(charEnd==-1){
			for(int i=0;i<list.size();i++){
				if(list.get(i).substring(charStart).equals(str.substring(charStart))){
					return true;
				}
			}
		}else{
			for(int i=0;i<list.size();i++){
				if(list.get(i).substring(charStart,charEnd).equals(str.substring(charStart,charEnd))){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 从txt读取文件排重后的list
	 * @param fileName 文件名
	 * @param charSet 字符集
	 * @param charStart 比较str的开始位置，0开始  
	 * @param charEnd 比较str结束位置 (-1表示原始长度）
	 * @return 
	 * @throws StringIndexOutOfBoundsException 如果charStart或charEnd超过字符串大小的话抛出异常
	 */
	public static String[] getListFromTxtNotRepeat(String fileName,String charSet,int charStart,int charEnd)throws StringIndexOutOfBoundsException{
		List<String> list=new ArrayList<String>();
		InputStreamReader read = null;
		
		try {
			read = new InputStreamReader(new FileInputStream(fileName), charSet);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		BufferedReader br = new BufferedReader(read);
		
		String str;
		try{
			while((str=br.readLine())!=null && !str.equals("")){
				if(!isRepeat(list,str,charStart,charEnd)){
					list.add(str);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] strs =list.toArray(new String[]{});
		System.out.println("getListFromTxtNotRepeat 读取数据共"+strs.length+"条！");
		return strs;
	}
	
	/**
	 * 读取大文件txt加速
	 * @param fileName 文件名
	 * @param charSet 字符集
	 * @param bufSize 缓冲区大小如5*1024*1024
	 * @return
	 */
	public static String readBigFileTxt(String fileName, String charSet, int bufSize){
		StringBuffer content = new StringBuffer();
		try {
		File file = new File(fileName);    
		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));     

		BufferedReader reader = new BufferedReader(new InputStreamReader(fis,charSet),bufSize);// 用5M的缓冲读取文本文件   
		
		String line = ""; 
			while((line = reader.readLine()) != null){ 
				content.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return content.toString();
	}
}
