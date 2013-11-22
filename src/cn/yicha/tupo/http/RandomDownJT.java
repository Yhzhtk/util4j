package cn.yicha.tupo.http;

import java.nio.MappedByteBuffer;

import cn.yicha.tupo.http.file.FileFactory;
import cn.yicha.tupo.p2sp.distribute.bisect.BisectDistribute;
import cn.yicha.tupo.p2sp.P2SPDownload;
import cn.yicha.tupo.p2sp.entity.RangeInfo;
import cn.yicha.tupo.p2sp.entity.UriInfo;

public class RandomDownJT extends Thread {

	private BisectDistribute distri;
	private UriInfo uriInfo;
	private P2SPDownload p2sp;

	static int i = 0;
	
	public RandomDownJT(BisectDistribute distribute, UriInfo uriInfo,
			P2SPDownload p2sp) {
		this.uriInfo = uriInfo;
		this.distri = distribute;
		this.p2sp = p2sp;
	}

	@Override
	public void run() {
		MappedByteBuffer mbb = FileFactory.getMappedByteBuffer(
				p2sp.getFileName(), distri.getFileSize());
		
		RangeInfo range;
		long start = System.currentTimeMillis();
		while (true) {
			range = distri.getNextRangeInfo();
			if (range == null) {
				break;
			}
			
			int startLoc = range.getStart();
			int endLoc = range.getEnd();
			System.out.println("Start " + Thread.currentThread().getName() + " " + range);
			uriInfo.setNowRange(range);
			
			int len = HttpClientUtil.downloadFileJ(uriInfo.getUri(), distri,
					mbb, startLoc, endLoc);
			
			System.out.println(uriInfo.getIndex() + " rIndex:"
					+ range.getIndex() + " range:" + startLoc + "-"
					+ (startLoc + len) + " endLoc:" + endLoc);
			
			distri.removeEmpty(range);
		}
		;
		// 该线程下载结束
		long end = System.currentTimeMillis();
		System.out.println("线程：" + uriInfo.getIndex() + " 完成，用时"
				+ (end - start));
		p2sp.completeOne(uriInfo.getIndex());
	}
}
