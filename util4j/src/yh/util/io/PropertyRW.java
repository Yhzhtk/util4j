package yh.util.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * 资源文件读写类
 * @author yh
 * 时间：2012-3-13 13:49
 */
public class PropertyRW {
	
	private Properties properties;
	private FileInputStream inputFile;
	private FileOutputStream outputFile;

	public PropertyRW() {
		this.properties = new Properties();
	}

	/**
	 * 初始化资源文件读取类
	 * @param filePath 资源文件类
	 * @throws FileNotFoundException 未找到文件
	 */
	public PropertyRW(String filePath) throws FileNotFoundException {
		this.properties = new Properties();
		try {
			this.inputFile = new FileInputStream(filePath);
			this.properties.load(this.inputFile);
			this.inputFile.close();
		} catch (FileNotFoundException ex) {
			throw ex;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 获取关键字值
	 * @param key 关键字
	 * @return 关键字对应值
	 */
	public String getValue(String key) {
		if (this.properties.containsKey(key)) {
			String value = this.properties.getProperty(key);
			return value;
		}
		return "";
	}
	
	/**
	 *  获取关键字值
	 * @param key 关键字
	 * @param charSet 编码集
	 * @return 转换编码后的值，转换基本编码是ISO-8859-1，使用saveFile保存的\\u不用转码
	 */
	public String getValueConvert(String key,String charSet) {
		if (this.properties.containsKey(key)) {
			String value = this.properties.getProperty(key);
			try {
				value=new String(value.getBytes("ISO-8859-1"),charSet);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return value;
		}
		return "";
	}

	/**
	 * 从资源文件获取某个关键值
	 * @param fileName 资源文件名
	 * @param key 关键字
	 * @return
	 */
	public String getValue(String fileName, String key) {
		String value;
		try {
			value = "";
			this.inputFile = new FileInputStream(fileName);
			this.properties.load(this.inputFile);
			this.inputFile.close();
			if (this.properties.containsKey(key)) {
				value = this.properties.getProperty(key);
				return value;
			}
			return value;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	/**
	 * properties对象清空
	 */
	public void clear() {
		this.properties.clear();
	}

	/**
	 * 设置关键字值
	 * @param key 关键字
	 * @param value 值
	 */
	public void setValue(String key, String value) {
		this.properties.setProperty(key, value);
	}

	/**
	 * 保存资源文件
	 * @param fileName 资源文件名
	 * @param description 资源文件描述
	 */
	public void saveFile(String fileName, String description) {
		try {
			this.outputFile = new FileOutputStream(fileName);
			this.properties.store(this.outputFile, description);
			this.outputFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
