package cn.yicha.tupo.http.file;

import cn.yicha.tupo.http.HttpClientUtil;
import cn.yicha.tupo.http.RandomDown;
import cn.yicha.tupo.p2sp.distribute.bisect.BisectDistribute;
import cn.yicha.tupo.p2sp.P2SPDownload;
import cn.yicha.tupo.p2sp.entity.RangeInfo;
import cn.yicha.tupo.p2sp.entity.UriInfo;

/**
 * 基本分区域下载类，完成基本的断点下载
 * @author gudh
 * @date 2013-11-25
 */
public class SimpleRandomDown extends RandomDown {

	public SimpleRandomDown(BisectDistribute distribute, UriInfo uriInfo,
			P2SPDownload p2sp) {
		super(distribute, uriInfo, p2sp);
	}

	@Override
	public void run() {
		// 开始线程时执行
		start();
		
		RangeInfo range;
		long start = System.currentTimeMillis();
		while (!stopFlag) {
			range = distri.getNextRangeInfo();
			uriInfo.setNowRange(range);
			if (range == null) {
				// 如果当前为空，则检查是否有失效的资源
				if(p2sp.checkHasSlowThread()){
					continue;
				}else{
					break;
				}
			}
			int len = HttpClientUtil.downloadFile(this, uriInfo.getUri(), mbb,
					range.getStart(), range.getEnd());
			System.out.println(uriInfo.getIndex() + "  : " + range.getIndex() + " " + len);
		};
		
		// 该线程下载结束
		long end = System.currentTimeMillis();
		System.out.println(this.getThreadName() + " END -- StopFlag:"
				+ stopFlag + " Time:" + (end - start) + " " + uriInfo.getUri());

		// 关闭线程和文件句柄
		close();
	}
}
