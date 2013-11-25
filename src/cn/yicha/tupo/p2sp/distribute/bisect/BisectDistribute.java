package cn.yicha.tupo.p2sp.distribute.bisect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantLock;

import cn.yicha.tupo.p2sp.distribute.Distribute;
import cn.yicha.tupo.p2sp.entity.RangeComparator;
import cn.yicha.tupo.p2sp.entity.RangeFactory;
import cn.yicha.tupo.p2sp.entity.RangeInfo;
import cn.yicha.tupo.p2sp.entity.StartComparator;
import cn.yicha.tupo.p2sp.entity.UriInfo;

/**
 * 剩余最大块取二分法操作
 * 
 * @author yicha
 * 
 */
public class BisectDistribute implements Distribute {

	private int baseSize; // 下载判断块大小
	private int fileSize;
	private List<UriInfo> uris;

	private int rangeIndex = 0;
	private SortedSet<RangeInfo> fillRanges;
	private SortedSet<RangeInfo> emptyRanges;
	
	private int minRangeSize = 2048;
	private Comparator<RangeInfo> rangeComparator;
	private List<RangeInfo> sortedEmptyRanges;
	
	private ReentrantLock lock = new ReentrantLock();

	public BisectDistribute(int fileSize) {
		this(fileSize, new ArrayList<UriInfo>());
	}

	public BisectDistribute(int fileSize, List<UriInfo> uris) {
		fillRanges = new TreeSet<RangeInfo>(new StartComparator());
		emptyRanges = new TreeSet<RangeInfo>(new StartComparator());
		
		rangeComparator = new RangeComparator();
		sortedEmptyRanges = new ArrayList<RangeInfo>();

		this.baseSize = 1024;
		this.fileSize = fileSize;
		this.uris = uris;

		// // 添加一个空的已完成
		// RangeInfo r = RangeFactory.getRangeInstance(rangeIndex++);
		// r.setStart(0);
		// r.setEnd(0);
		// this.fillRanges.add(r);

		// 初始时按线程平分
		if (uris.size() == 0) {
			RangeInfo r = RangeFactory.getRangeInstance(rangeIndex++);
			r.setStart(0);
			r.setEnd(fileSize);
			addEmpty(r);
		} else {
			int len = uris.size();
			int esize = fileSize / len;
			for (int i = 0; i < len; i++) {
				int s = i * esize;
				int e = s + esize;
				if (i == len - 1) {
					e = fileSize;
				}
				RangeInfo r = RangeFactory.getRangeInstance(rangeIndex++);
				r.setStart(s);
				r.setEnd(e);
				addEmpty(r);
			}
		}
	}

	@Override
	public void addUri(UriInfo uri) {
		this.uris.add(uri);
	}

	@Override
	public void deleteUri(UriInfo uri) {
		this.uris.remove(uri);
		if(uri != null && uri.getNowRange() != null){
			// 当前正在下载的设为未使用
			RangeInfo r = uri.getNowRange();
			r.setUsed(false);
		}
	}

	@Override
	public RangeInfo getNextRangeInfo() {
		lock.lock();
		RangeInfo r = getMaxEmptyRange();
		if (r == null) {
			lock.unlock();
			return null;
		}
		// 未使用直接返回
		if(!r.isUsed()){
			r.setUsed(true);
			lock.unlock();
			return r;
		}
		
		// 小于最小允许的大小则不需要再分了
		if(r.getEnd() - r.getStart() < minRangeSize){
			lock.unlock();
			return null;
		}
		
		int s = r.getStart();
		int e = r.getEnd();
		int m = (s + e) / 2;
		r.setEnd(m);

		RangeInfo n = RangeFactory.getRangeInstance(rangeIndex++);
		n.setStart(m);
		n.setEnd(e);
		addEmpty(n);

		n.setUsed(true);
		lock.unlock();
		return n;
	}

	@Override
	public boolean isCompleted() {
		return emptyRanges.size() == 0;
	}

	@Override
	public List<UriInfo> getAllUriInfos() {
		return uris;
	}

	public int getBaseSize() {
		return baseSize;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setBytesOk(int loc, int blen) {
		lock.lock();
		// 设置填充信息
		setFillInfo(loc, blen);
		// 设置未下载信息
		setEmptyInfo(loc, blen);
		lock.unlock();
	}

	public boolean hasBytesDown(int loc) {
		lock.lock();
		boolean res = false;
		Iterator<RangeInfo> iter = fillRanges.iterator();
		while (iter.hasNext()) {
			RangeInfo r = iter.next();
			// 不能等于end
			if (loc >= r.getStart() && loc < r.getEnd()) {
				res = true;
			}
		}
		lock.unlock();
		return res;
	}

	private void setFillInfo(int loc, int blen) {
		Iterator<RangeInfo> iter = fillRanges.iterator();
		RangeInfo r = null;
		while (iter.hasNext()) {
			r = iter.next();
			if (loc >= r.getStart() && loc <= r.getEnd()) {
				break;
			}
			r = null;
		}
		if (r == null) {
			r = RangeFactory.getRangeInstance(rangeIndex++);
			r.setStart(loc);
			fillRanges.add(r);
		}
		// 设置结尾处
		r.setEnd(loc + blen);
	}

	private void setEmptyInfo(int loc, int blen) {
		Iterator<RangeInfo> iter = emptyRanges.iterator();
		RangeInfo r = null;
		while (iter.hasNext()) {
			r = iter.next();
			if (loc == r.getStart()) {
				break;
			}
			r = null;
		}
		if (r != null) {
			int e = loc + blen;
			if (e >= r.getEnd()) {
				removeEmpty(r);
			} else {
				r.setStart(e);
			}
		}
	}

	private void addEmpty(RangeInfo r) {
		emptyRanges.add(r);
		sortedEmptyRanges.add(r);
	}

	public void removeEmpty(RangeInfo r) {
		emptyRanges.remove(r);
		sortedEmptyRanges.remove(r);
		RangeFactory.releaseRangeInfo(r);
	}

	private RangeInfo getMaxEmptyRange() {
		Collections.sort(sortedEmptyRanges, rangeComparator);
		
		if (sortedEmptyRanges.size() > 0) {
			return sortedEmptyRanges.get(0);
		}
		return null;
	}
}
