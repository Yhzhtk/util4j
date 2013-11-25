package cn.yicha.tupo.http.file;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 文件工厂，产生文件句柄
 * 
 * @author:gudaihui
 * @date:2013-11-20
 */
public class FileFactory {

	static final int QUEUESIZE = 50;

	// 使用vector保证线程安全性
	static HashMap<String, Vector<RandomAccessFile>> allRfs = new HashMap<String, Vector<RandomAccessFile>>();
	static HashMap<String, ConcurrentLinkedQueue<MappedByteBuffer>> freeMbbs = new HashMap<String, ConcurrentLinkedQueue<MappedByteBuffer>>();

	/**
	 * 获取MappedByteBuffer，加速IO读写
	 * 
	 * @param fileName
	 * @param fileSize
	 * @return
	 */
	public static MappedByteBuffer getMappedByteBuffer(String fileName,
			long fileSize) {
		MappedByteBuffer mbb = null;

		// 先从队列中获取，如果没有再重新创建
		Queue<MappedByteBuffer> mbbs = freeMbbs.get(fileName);
		if (mbbs != null) {
			mbb = mbbs.poll();
			if (mbb != null) {
				return mbb;
			}
		}

		Vector<RandomAccessFile> files = allRfs.get(fileName);
		if (files == null) {
			files = new Vector<RandomAccessFile>();
			allRfs.put(fileName, files);
		}
		try {
			RandomAccessFile rf = new RandomAccessFile(fileName, "rw");
			files.add(rf);
			FileChannel fc = rf.getChannel();
			mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mbb;
	}

	/**
	 * 释放不用的MappedByteBuffer
	 * 
	 * @param fileName
	 * @param mbb
	 */
	public static void releaseMappedByteBuffer(String fileName,
			MappedByteBuffer mbb) {
		ConcurrentLinkedQueue<MappedByteBuffer> mbbs = freeMbbs.get(fileName);
		if (mbbs == null) {
			mbbs = new ConcurrentLinkedQueue<MappedByteBuffer>();
			freeMbbs.put(fileName, mbbs);
		}
		// 如果队列已满，则返回false
		mbbs.offer(mbb);
	}

	/**
	 * 关闭文件
	 * 
	 * @param fileName
	 */
	public static void closeFile(String fileName) {
		allRfs.remove(fileName);
		freeMbbs.remove(fileName);
		Vector<RandomAccessFile> files = allRfs.get(fileName);
		if(files != null){
			for (RandomAccessFile file : files) {
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				file = null;
			}
		}
	}

	/**
	 * 关于所有文件
	 */
	public static void closeAll() {
		for (String fileName : allRfs.keySet()) {
			closeFile(fileName);
		}
	}
}
