package cn.yicha.tupo.p2sp.entity;  

/**
 * 产生Range的工厂
 *@author:gudaihui
 *@date:2013-11-20
 */
public class RangeFactory {
	
	/**
	 * 获取一个RangeInfo对象
	 * @param index
	 * @return
	 * @date:2013-11-20
	 * @author:gudaihui
	 */
	public static RangeInfo getRangeInstance(int index){
		return new RangeInfo(index);
	}

	/**
	 * 释放
	 * 
	 * @param mbb
	 * @date:2013-11-20
	 * @author:gudaihui
	 */
	public static void releaseRangeInfo(RangeInfo uri) {

	}

}
