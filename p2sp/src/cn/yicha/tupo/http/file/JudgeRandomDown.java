package cn.yicha.tupo.http.file;

import cn.yicha.tupo.http.HttpClientUtil;
import cn.yicha.tupo.http.RandomDown;
import cn.yicha.tupo.p2sp.distribute.bisect.BisectDistribute;
import cn.yicha.tupo.p2sp.P2SPDownload;
import cn.yicha.tupo.p2sp.entity.RangeInfo;
import cn.yicha.tupo.p2sp.entity.UriInfo;

/**
 * 高级断点下载类，下载过程中实时判断下一块是否下载，如果已经下载则跳出
 * 
 * @author gudh
 * @date 2013-11-25
 */
public class JudgeRandomDown extends RandomDown {

	public JudgeRandomDown(BisectDistribute distribute, UriInfo uriInfo,
			P2SPDownload p2sp) {
		super(distribute, uriInfo, p2sp);
	}

	@Override
	public void run() {
		// 开始线程时执行
		boolean r = start();
		if (!r) {
			System.err.print("start error, check the free of disk or retry");
		} else {
			RangeInfo range;
			long start = System.currentTimeMillis();
			while (!stopFlag) {
				distri.showRange();
				range = distri.getNextRangeInfo();
				distri.showRange();
				if (range == null) {
					// 如果当前为空，则检查是否有失效的资源
					if (p2sp.checkHasSlowThread()) {
						continue;
					} else {
						break;
					}
				}

				int startLoc = range.getStart();
				int endLoc = range.getEnd();
				System.out.println(Thread.currentThread().getName()
						+ " START -- " + range + " " + uriInfo.getUri());
				uriInfo.setNowRange(range);

				try {
					int len = HttpClientUtil.downloadFile(this,
							uriInfo.getUri(), distri, mbb, range);
					System.out.println(Thread.currentThread().getName()
							+ " rIndex:" + range.getIndex() + " range:"
							+ startLoc + "-" + (startLoc + len) + " endLoc:"
							+ endLoc);
				} catch (Exception e) {
					e.printStackTrace();
					// 此处检测资源是否可以用，如果不可用直接结束当前线程
				}

				// 判断回收range
				distri.collectRangeInfo(range);
			}
			;
			// 该线程下载结束
			long end = System.currentTimeMillis();
			System.out.println(this.getThreadName() + " END -- StopFlag:"
					+ stopFlag + " Time:" + (end - start) + " "
					+ uriInfo.getUri());
		}
		// 关闭线程和文件句柄
		close();
	}

}
