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
public class RandomDown extends Thread {
	// 控制线程结束标志
	protected boolean stopFlag;
	
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
		
		mbb = FileFactory.getMappedByteBuffer(
				p2sp.getFileName(), distri.getFileSize());
	}
	
	/**
	 * 结束下载
	 */
	public void stopFlag(){
		stopFlag = true;
	}
	
	/**
	 * 关闭线程结尾工作，包括线程通知和关闭文件句柄
	 */
	public void close(){
		p2sp.completeOne(uriInfo.getIndex());
		FileFactory.releaseMappedByteBuffer(p2sp.getFileName(), mbb);
	}

}
