package cn.yicha.tupo.p2sp.entity;  

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 产生Uri的工厂
 *@author:gudaihui
 *@date:2013-11-20
 */
public class UriFactory {
	
	static ConcurrentLinkedQueue<UriInfo> uris = new ConcurrentLinkedQueue<UriInfo>();
	
	/**
	 * 获取一个RangeInfo对象
	 * @param uri
	 * @return
	 */
	public static UriInfo getUriInstance(String uri){
		UriInfo uriInfo = uris.poll();
		if(uriInfo == null){
			uriInfo = new UriInfo(uri); 
		}else{
			uriInfo.reset();
			uriInfo.setIndex(uri);
			uriInfo.setUri(uri);
		}
		return uriInfo;
	}
	
	/**
	 * 释放
	 * 
	 * @param mbb
	 * @date:2013-11-20
	 * @author:gudaihui
	 */
	public static void releaseUriInfo(UriInfo uri) {
		uris.offer(uri);
	}

}
