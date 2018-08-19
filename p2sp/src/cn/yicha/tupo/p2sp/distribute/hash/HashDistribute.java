package cn.yicha.tupo.p2sp.distribute.hash;  

import java.util.List;

import cn.yicha.tupo.p2sp.distribute.Distribute;
import cn.yicha.tupo.p2sp.entity.RangeInfo;
import cn.yicha.tupo.p2sp.entity.UriInfo;

/**
 * 以一致性hash的方式处理分发
 * 
 *@author:gudaihui
 *@date:2013-11-20
 */
public class HashDistribute implements Distribute {

	@Override
	public void addUri(UriInfo uri) {
		// TODO Auto-generated method stub  
		
	}

	@Override
	public void deleteUri(UriInfo uri) {
		// TODO Auto-generated method stub  
		
	}

	@Override
	public RangeInfo getNextRangeInfo() {
		// TODO Auto-generated method stub  
		return null;
	}

	@Override
	public List<UriInfo> getAllUriInfos() {
		// TODO Auto-generated method stub  
		return null;
	}

	@Override
	public boolean isCompleted() {
		// TODO Auto-generated method stub  
		return false;
	}

	@Override
	public long getFileSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void collectRangeInfo(RangeInfo range) {
		// TODO Auto-generated method stub
		
	}

}
