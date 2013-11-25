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
		start();
		
		RangeInfo range;
		long start = System.currentTimeMillis();
		while (!stopFlag) {
			range = distri.getNextRangeInfo();
			if (range == null) {
				// 如果当前为空，则检查是否有失效的资源
				if(p2sp.checkHasSlowThread()){
					continue;
				}else{
					break;
				}
			}
			
			int startLoc = range.getStart();
			int endLoc = range.getEnd();
			System.out.println("Start " + Thread.currentThread().getName() + " " + range);
			uriInfo.setNowRange(range);
			
			int len = HttpClientUtil.downloadFile(this, uriInfo.getUri(), distri,
					mbb, startLoc, endLoc);
			
			System.out.println(uriInfo.getIndex() + " rIndex:"
					+ range.getIndex() + " range:" + startLoc + "-"
					+ (startLoc + len) + " endLoc:" + endLoc);
			
			distri.removeEmpty(range);
		};
		// 该线程下载结束
		long end = System.currentTimeMillis();
		System.out.println("线程：" + this.getName() + " 结束， stopFlag:" + stopFlag + "，用时" + (end - start));
		
		// 关闭线程和文件句柄
		close();
	}
	
}
