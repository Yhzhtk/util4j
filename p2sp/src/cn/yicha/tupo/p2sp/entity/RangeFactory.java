package cn.yicha.tupo.p2sp.entity;  

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 产生Range的工厂
 *@author:gudaihui
 *@date:2013-11-20
 */
public class RangeFactory {
	
	static ConcurrentLinkedQueue<RangeInfo> ranges = new ConcurrentLinkedQueue<RangeInfo>();
	
	/**
	 * 获取一个RangeInfo对象
	 * @param index
	 * @return
	 */
	public static RangeInfo getRangeInstance(int index){
		RangeInfo range = ranges.poll();
		if(range == null){
			range = new RangeInfo(index); 
		}else{
			range.setIndex(index);
			range.reset();
		}
		return range;
	}

	/**
	 * 释放
	 * 
	 * @param mbb
	 * @date:2013-11-20
	 * @author:gudaihui
	 */
	public static void releaseRangeInfo(RangeInfo range) {
		ranges.offer(range);
	}

	/**
	 * 获取r的克隆
	 * @param r
	 * @return
	 */
	public static RangeInfo getClone(RangeInfo r) {
		RangeInfo range = getRangeInstance(r.getIndex());
		range.setStart(r.getStart());
		range.setEnd(r.getEnd());
		range.setUsed(r.isUsed());
		return range;
	}
}
