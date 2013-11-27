package cn.yicha.tupo.p2sp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 管理线程
 * @author gudh
 * @date 2013-11-27
 */
public class P2SPManager extends Thread {

	private boolean stopFlag;
	
	private ConcurrentLinkedQueue<InputStream> streams = new ConcurrentLinkedQueue<InputStream>();
	
	private Vector<P2SPDownload> p2sps = new Vector<P2SPDownload>();
	
	/**
	 * 停止管理，结束线程
	 */
	public void stopManager(){
		stopFlag = true;
	}
	
	@Override
	public void run() {
		while(!stopFlag){
			checkDownload();
			closeInputStream();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 添加需要关闭的InputStream，异步关闭
	 * @param stream
	 */
	public void addInputStream(InputStream stream){
		streams.add(stream);
	}
	
	/**
	 * 添加一个P2SPDownload
	 * @param p2sp
	 */
	public void addP2SPDownload(P2SPDownload p2sp){
		p2sps.add(p2sp);
	}
	
	/**
	 * 执行一次检查下载的操作
	 */
	private void checkDownload(){
		List<P2SPDownload> removes = new ArrayList<P2SPDownload>();
		for(P2SPDownload p2sp : p2sps){
			if(stopFlag){
				break;
			}
			if(p2sp.checkComplete()){
				System.out.println(p2sp.getFileName() + " checked download COMPLETE!");
				removes.add(p2sp);
			}
		}
		// 移除已经完成的
		for(P2SPDownload p2sp : removes){
			p2sps.remove(p2sp);
		}
	}
	
	/**
	 * 执行一次关闭流的操作
	 */
	private void closeInputStream(){
		do{
			InputStream s = streams.poll();
			if(s == null){
				break;
			}
			try {
				System.out.println("Close a InputStream : " + s.toString());
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		while(!stopFlag);
	}
}
