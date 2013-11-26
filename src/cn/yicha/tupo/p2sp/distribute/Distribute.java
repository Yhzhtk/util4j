package cn.yicha.tupo.p2sp.distribute;  

import java.util.List;

import cn.yicha.tupo.p2sp.entity.RangeInfo;
import cn.yicha.tupo.p2sp.entity.UriInfo;

/**
 * 分发算法接口
 * 封装资源增加删除
 * 
 *@author:gudaihui
 *@date:2013-11-20
 */
public interface Distribute {

	public long getFileSize();
	
	public void addUri(UriInfo uri);
	
	public void deleteUri(UriInfo uri);
	
	public RangeInfo getNextRangeInfo();
	
	public void collectRangeInfo(RangeInfo range);
	
	public boolean isCompleted();
	
	public List<UriInfo> getAllUriInfos();
	
}
