package cn.yicha.tupo.http.file;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工厂，产生文件句柄
 * 
 * @author:gudaihui
 * @date:2013-11-20
 */
public class FileFactory {

	static List<RandomAccessFile> rfs = new ArrayList<RandomAccessFile>();
	
	/**
	 * 获取MappedByteBuffer，加速IO读写
	 * 
	 * @param fileName
	 * @param fileSize
	 * @return
	 * @date:2013-11-20
	 * @author:gudaihui
	 */
	public static MappedByteBuffer getMappedByteBuffer(String fileName,
			long fileSize) {
		/**
		 * 工厂方法暂未实现
		 */
		MappedByteBuffer mbb = null;
		try {
			RandomAccessFile rf = new RandomAccessFile(fileName, "rw");
			rfs.add(rf);
			FileChannel fc = rf.getChannel();
			mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mbb;
	}

	/**
	 * 释放
	 * 
	 * @param mbb
	 * @date:2013-11-20
	 * @author:gudaihui
	 */
	public static void releaseMappedByteBuffer(MappedByteBuffer mbb) {

	}

	/**
	 * 关闭
	 * 
	 * @param mbb
	 * @date:2013-11-20
	 * @author:gudaihui
	 */
	public static void closeMappedByteBuffer(MappedByteBuffer mbb) {
	}
	
	/**
	关于所有文件
	 */
	public static void closeAll(){
		for(RandomAccessFile rf : rfs){
			try {
				rf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
