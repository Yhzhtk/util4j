package cn.yicha.tupo.p2sp.distribute.block;  

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import cn.yicha.tupo.p2sp.distribute.Distribute;
import cn.yicha.tupo.p2sp.entity.RangeFactory;
import cn.yicha.tupo.p2sp.entity.RangeInfo;
import cn.yicha.tupo.p2sp.entity.UriInfo;

/**
 * 以递增块的方式处理分发
 * 
 * 还需考虑线程安全性
 * 
 *@author:gudaihui
 *@date:2013-11-20
 */
public class BlockDistribute implements Distribute{

	private long fileSize;
	private int blockSize;
	private int blockCount;
	
	private List<UriInfo> uris = new ArrayList<UriInfo>();
	
	private int nowIndex;
	private List<RangeInfo> failRanges = new ArrayList<RangeInfo>();
	
	private boolean init = false;
	
	private ReentrantLock lock = new ReentrantLock();
	
	public BlockDistribute(){
	}
	
	public BlockDistribute(long fileSize){
		try {
			initFileSize(fileSize);
		} catch (Exception e) {
		}
	}
	
	public void initFileSize(long fileSize) throws Exception{
		if(init){
			throw new Exception("Distribute 只能初始化一次");
		}
		this.fileSize = fileSize;
		this.blockSize = 1 << 12; // 默认块大小为2k
		this.blockCount = (int) ((this.fileSize - 1) / blockSize + 1); // 块个数
		
		// 当前是块索引是-1
		this.nowIndex = -1;
		init = true;
	}

	@Override
	public void addUri(UriInfo uri) {
		lock.lock();
		uris.add(uri);
		lock.unlock();
	}

	@Override
	public void deleteUri(UriInfo uri) {
		lock.lock();
		RangeInfo range = uri.getNowRange();
		if(range != null && range.isAvailable()){
			failRanges.add(range);
		}
		int i = 0;
		for(i = 0; i < uris.size(); i++){
			if(uris.get(i).equals(uri)){
				break;
			}
		}
		if(i < uris.size()){
			uris.remove(i);
		}
		lock.unlock();
	}

	@Override
	public RangeInfo getNextRangeInfo() {
		lock.lock();
		RangeInfo r = null;
		if(failRanges.size() > 0){
			r = failRanges.get(0);
			failRanges.remove(0);
		}
		if(r == null && nowIndex < blockCount){
			nowIndex++;
			if(nowIndex < blockCount){
				r =  RangeFactory.getRangeInstance(nowIndex);
				int loc = nowIndex * blockSize;
				r.setStart(loc);
				loc += blockSize;
				loc = loc > fileSize ? -1 : loc; // 大于文件长度时设为-1,表示下载到最末尾
				r.setEnd(loc);
			}
		}
		lock.unlock();
		return r;
	}
	

	@Override
	public void collectRangeInfo(RangeInfo range) {
		
	}

	@Override
	public List<UriInfo> getAllUriInfos() {  
		return uris;
	}

	@Override
	public boolean isCompleted() {
		if(failRanges.size() == 0){
			if(nowIndex == blockCount){
				return true;
			}
		}
		return false;
	}

	public long getFileSize() {
		return fileSize;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public int getBlockCount() {
		return blockCount;
	}
}
