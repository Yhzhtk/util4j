package cn.yicha.tupo.http;

import java.nio.MappedByteBuffer;

import cn.yicha.tupo.http.file.FileFactory;
import cn.yicha.tupo.p2sp.P2SPDownload;
import cn.yicha.tupo.p2sp.distribute.bisect.BisectDistribute;
import cn.yicha.tupo.p2sp.entity.UriInfo;

/**
 * 随机下载的父类
 * @author gudh
 * @date 2013-11-25
 */
public class RandomDown implements Runnable {
	// 控制线程结束标志
	protected boolean stopFlag;
	
	/**
	 * 记录下载速度，用于判断资源是否可用
	 */
	protected long lastTime;
	protected int lastSpeed;

	protected BisectDistribute distri;
	protected UriInfo uriInfo;
	protected P2SPDownload p2sp;
	protected MappedByteBuffer mbb;
	
	public RandomDown(BisectDistribute distribute, UriInfo uriInfo,
			P2SPDownload p2sp) {
		stopFlag = false;
		this.uriInfo = uriInfo;
		this.distri = distribute;
		this.p2sp = p2sp;
	}
	
	/**
	 * 以上次成功时间距现在小于等于time，并且上次速度大于等于speed来决定资源是否可用
	 * @param time
	 * @param speed
	 * @return
	 */
	public boolean isAvailable(long time, float speed){
		long t = System.currentTimeMillis() - lastTime;
		return t <= time && lastSpeed > speed;
	}
	
	/**
	 * 结束下载
	 */
	public void stopFlag(){
		stopFlag = true;
	}
	
	/**
	 * 开始线程时执行
	 */
	public void start(){
		p2sp.addNewOne(uriInfo.getIndex());
		mbb = FileFactory.getMappedByteBuffer(
				p2sp.getFileName(), distri.getFileSize());
	}
	
	/**
	 * 关闭线程结尾工作，包括线程通知和关闭文件句柄
	 */
	public void close(){
		p2sp.completeOne(uriInfo.getIndex());
		FileFactory.releaseMappedByteBuffer(p2sp.getFileName(), mbb);
	}

	public UriInfo getUriInfo(){
		return uriInfo;
	}

	@Override
	public void run() {
		System.out.println("RandomDown run method");
	}
	
	public String getThreadName() {
		return Thread.currentThread().getName();
	}
}
