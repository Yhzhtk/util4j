package cn.yicha.tupo.p2sp;

import java.util.ArrayList;
import java.util.List;

import cn.yicha.tupo.http.HttpClientUtil;
import cn.yicha.tupo.http.RandomDownJT;
import cn.yicha.tupo.p2sp.distribute.Distribute;
import cn.yicha.tupo.p2sp.distribute.bisect.BisectDistribute;
import cn.yicha.tupo.p2sp.entity.UriFactory;
import cn.yicha.tupo.p2sp.entity.UriInfo;

/**
 * P2SP下载
 */
public class P2SPDownload {

	private Distribute distribute;
	private Thread[] threads;
	private String fileName;

	private int rCount;

	public P2SPDownload(List<String> urls, String fileName) {
		this.fileName = fileName;
		int fileSize = HttpClientUtil.getFileSize(urls.get(0));
		System.out.println("fileSize:" + fileSize);
		
		List<UriInfo> uris = new ArrayList<UriInfo>();
		for (int i = 0; i < urls.size(); i++) {
			UriInfo uri = UriFactory.getUriInstance(urls.get(i));
			uri.setIndex("t" + i);
			uris.add(uri);
		}
		distribute = new BisectDistribute(fileSize, uris);

		threads = new RandomDownJT[urls.size()];
		for (int i = 0; i < urls.size(); i++) {
			threads[i] = new RandomDownJT((BisectDistribute) distribute,
					uris.get(i), this);
			threads[i].setName("T" + i);
		}
	}

	public void start() {
		for (Thread t : threads) {
			t.start();
			rCount++;
		}
	}

	public void completeOne(String tIndex) {
		rCount--;
	}

	public boolean isComplete() {
		return rCount == 0;
	}

	public Distribute getDistribute() {
		return distribute;
	}

	public Thread[] getThreads() {
		return threads;
	}

	public String getFileName() {
		return fileName;
	}
}
