package cn.yicha.tupo.http.file;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.Iterator;
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
	static HashMap<String, Vector<FileChannel>> allFcs = new HashMap<String, Vector<FileChannel>>();
	static HashMap<String, ConcurrentLinkedQueue<MappedByteBuffer>> freeMbbs = new HashMap<String, ConcurrentLinkedQueue<MappedByteBuffer>>();

	/**
	 * 获取MappedByteBuffer，加速IO读写
	 * 
	 * @param fileName
	 * @param fileSize
	 * @return
	 */
	public synchronized static MappedByteBuffer getMappedByteBuffer(String fileName,
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
		Vector<FileChannel> fcs = allFcs.get(fileName);
		if (fcs == null) {
			fcs = new Vector<FileChannel>();
			allFcs.put(fileName, fcs);
		}
		try {
			RandomAccessFile rf = new RandomAccessFile(fileName, "rw");
			files.add(rf);
			FileChannel fc = rf.getChannel();
			fcs.add(fc);
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
		Vector<FileChannel> fcs = allFcs.get(fileName);
		if(fcs != null){
			for (FileChannel fc : fcs) {
				try {
					fc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				allFcs.remove(fc);
				fc = null;
			}
		}
		Vector<RandomAccessFile> files = allRfs.get(fileName);
		if(files != null){
			for (RandomAccessFile file : files) {
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				allRfs.remove(file);
				file = null;
			}
		}
		ConcurrentLinkedQueue<MappedByteBuffer> mbbs = freeMbbs.get(fileName);
		if(mbbs != null){
			Iterator<MappedByteBuffer> iter = mbbs.iterator();
			while(iter.hasNext()){
				MappedByteBuffer mbb = iter.next();
				mbbs.remove(mbb);
				try {
					// 非安全的关闭
					unsafeUnmap(mbb);
					unsafeUnmap2(mbb);
				} catch (PrivilegedActionException e) {
					e.printStackTrace();
				}
				mbb = null;
			}
		}
		allRfs.remove(fileName);
		freeMbbs.remove(fileName);
		
		// 垃圾回收，关闭文件句柄
		System.gc();
	}

	/**
	 * 关于所有文件
	 */
	public static void closeAll() {
		for (String fileName : allRfs.keySet()) {
			closeFile(fileName);
		}
	}
	
	/**
	 * 关闭mbb
	 * @param mbb
	 * @throws PrivilegedActionException
	 */
    private static void unsafeUnmap(final MappedByteBuffer mbb) throws PrivilegedActionException {
        AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
            public Object run() throws Exception {
                Method getCleanerMethod = mbb.getClass().getMethod("cleaner");
                getCleanerMethod.setAccessible(true);
                Object cleaner = getCleanerMethod.invoke(mbb); // sun.misc.Cleaner instance
                Method cleanMethod = cleaner.getClass().getMethod("clean");
                cleanMethod.invoke(cleaner);
                return null;
            }
        });
    }
    
	private static void unsafeUnmap2(final MappedByteBuffer mbb) {
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			public Object run() {
				try {
					Method getCleanerMethod = mbb.getClass().getMethod(
							"cleaner", new Class[0]);
					getCleanerMethod.setAccessible(true);
					sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod
							.invoke(mbb, new Object[0]);
					cleaner.clean();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
    }

}
