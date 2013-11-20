package cn.yicha.tupo.p2sp.entity;  

/**
 * 产生Uri的工厂
 *@author:gudaihui
 *@date:2013-11-20
 */
public class UriFactory {
	
	/**
	 * 获取一个RangeInfo对象
	 * @param uri
	 * @return
	 * @date:2013-11-20
	 * @author:gudaihui
	 */
	public static UriInfo getUriInstance(String uri){
		return new UriInfo(uri);
	}
	
	/**
	 * 释放
	 * 
	 * @param mbb
	 * @date:2013-11-20
	 * @author:gudaihui
	 */
	public static void releaseUriInfo(UriInfo uri) {

	}

}
