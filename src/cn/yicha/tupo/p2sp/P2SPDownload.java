package cn.yicha.tupo.p2sp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.yicha.tupo.http.HttpClientUtil;
import cn.yicha.tupo.http.RandomDown;
import cn.yicha.tupo.http.file.FileFactory;
import cn.yicha.tupo.http.file.JudgeRandomDown;
import cn.yicha.tupo.p2sp.distribute.bisect.BisectDistribute;
import cn.yicha.tupo.p2sp.entity.UriFactory;
import cn.yicha.tupo.p2sp.entity.UriInfo;

/**
 * P2SP下载，使用二分最大块分发算法
 */
public class P2SPDownload {
	
	private ExecutorService threadPool; // 线程池
	
	private long maxAllowStopTime = 3000; // 最大允许没有下载的时间，单位毫秒
	private long minAllowSpeed = 1024; // 最小允许的下载速度，单位B/s
	
	private BisectDistribute distribute;
	private List<JudgeRandomDown> threads;
	private String fileName;

	private volatile int rCount;
	
	// 监控线程
	public static P2SPManager pmanager;
	static{
		// 开启监控
		pmanager = new P2SPManager();
		pmanager.start();
	}

	public P2SPDownload(List<String> urls, String fileName, int maxThread) {
		threadPool = Executors.newFixedThreadPool(maxThread);
		
		this.fileName = fileName;
		int fileSize = HttpClientUtil.getFileSize(urls.get(0));
		System.out.println("fileSize:" + fileSize);
		
		List<UriInfo> uris = new ArrayList<UriInfo>();
		for (int i = 0; i < urls.size(); i++) {
			UriInfo uri = UriFactory.getUriInstance(urls.get(i));
			uris.add(uri);
		}
		distribute = new BisectDistribute(fileSize, uris);

		/**
		 * 初始化线程
		 */
		threads = new ArrayList<JudgeRandomDown>(urls.size());
		for (int i = 0; i < urls.size(); i++) {
			JudgeRandomDown rd = new JudgeRandomDown(distribute,
					uris.get(i), this);
			threads.add(rd);
		}
		
		// 添加到监控
		pmanager.addP2SPDownload(this);
	}

	/**
	 * 开始下载
	 */
	public void start() {
		for (RandomDown t : threads) {
			threadPool.execute(t);
		}
		// 启动之后延时1秒
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 停止下载
	 */
	public void stop(){
		for (RandomDown t : threads) {
			t.stopFlag();
		}
		close();
	}
	
	/**
	 * 检查是否有资源以及不可用
	 * 
	 * 如果不可用则删除资源
	 * @return
	 */
	public synchronized boolean checkHasSlowThread(){
		boolean hasDead = false;
		for (RandomDown t : threads) {
			// 资源失效
			if(!t.isAvailable(maxAllowStopTime, minAllowSpeed)){
				t.stopFlag();
				distribute.deleteUri(t.getUriInfo());
				hasDead = true;
			}
		}
		return hasDead;
	}
	
	/**
	 * 添加新源
	 * @param uri
	 */
	public synchronized void addUrl(String uri){
		UriInfo uriInfo = UriFactory.getUriInstance(uri);
		distribute.addUri(uriInfo);
		JudgeRandomDown rd = new JudgeRandomDown(distribute, uriInfo, this);
		threads.add(rd);
		threadPool.execute(rd);
	}
	
	/**
	 * 删除资源
	 * @param url
	 */
	public synchronized void removeUrl(String url){
		List<RandomDown> removes = new ArrayList<RandomDown>();
		for (RandomDown t : threads) {
			if(t.getUriInfo().getUri().equals(url)){
				removes.add(t);
			}
		}
		for(RandomDown t : removes){
			t.stopFlag();
			distribute.deleteUri(t.getUriInfo());
			threads.remove(t);
			System.out.println("Remove：" + t.getUriInfo().getUri());
		}
	}

	/**
	 * 提供子线程通信的接口
	 * @param tIndex
	 */
	public synchronized void subCompleteOne(String tIndex) {
		rCount--;
	}

	/**
	 * 添加一个新的运行实例
	 * @param index
	 */
	public synchronized void addStartOne(String index) {
		rCount++;
	}
	
	/**
	 * 判断下载是否完成
	 * @return
	 */
	public boolean checkComplete() {
		boolean res = false;
		res = (rCount == 0);
		if(res){
			// 结束后关闭文件
			close();
		}
		return res;
	}
	
	/**
	 * 下载结束时关闭文件等
	 */
	private void close(){
		FileFactory.closeFile(fileName);
		if(threadPool != null){
			threadPool.shutdownNow();
			threadPool = null;
		}
	}
	
	/**
	 * 关闭监控
	 */
	public static void closeManager(){
		if(pmanager != null && pmanager.isAlive()){
			pmanager.stopManager();
			pmanager = null;
		}
	}

	public BisectDistribute getDistribute() {
		return distribute;
	}

	public List<JudgeRandomDown> getThreads() {
		return threads;
	}

	public String getFileName() {
		return fileName;
	}
}
