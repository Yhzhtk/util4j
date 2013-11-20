package cn.yicha.tupo.http;

import java.nio.MappedByteBuffer;

import cn.yicha.tupo.http.file.FileFactory;
import cn.yicha.tupo.p2sp.distribute.block.BlockDistribute;
import cn.yicha.tupo.p2sp.entity.P2SPDownload;
import cn.yicha.tupo.p2sp.entity.RangeInfo;
import cn.yicha.tupo.p2sp.entity.UriInfo;

public class RandomDownT extends Thread {

	private BlockDistribute distri;
	private UriInfo uriInfo;
	private P2SPDownload p2sp;

	public RandomDownT(BlockDistribute distribute, UriInfo uriInfo,
			P2SPDownload p2sp) {
		this.uriInfo = uriInfo;
		this.distri = distribute;
		this.p2sp = p2sp;
	}

	@Override
	public void run() {
		MappedByteBuffer mbb = FileFactory.getMappedByteBuffer(p2sp.getFileName(),
				distri.getFileSize());
		RangeInfo range;
		long start = System.currentTimeMillis();
		while (true) {
			range = distri.getNextRangeInfo();
			uriInfo.setNowRange(range);
			if (range == null) {
				break;
			}
			int len = HttpClientUtil.downloadFile(uriInfo.getUri(), mbb,
					range.getStart(), range.getEnd());
			System.out.println(uriInfo.getIndex() + "  : " + range.getIndex() + " " + len);
		};
		// 该线程下载结束
		long end = System.currentTimeMillis();
		System.out.println("线程：" + uriInfo.getIndex() + " 完成，用时" + (end - start));
		p2sp.completeOne(uriInfo.getIndex());
	}
}
