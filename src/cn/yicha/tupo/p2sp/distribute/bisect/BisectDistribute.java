package cn.yicha.tupo.p2sp.distribute.bisect;

import java.util.ArrayList;
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
	private SortedSet<RangeInfo> sortedEmptyRanges;

	private List<RangeInfo> notUsedRanges;
	
	private ReentrantLock lock = new ReentrantLock();

	public BisectDistribute(int fileSize) {
		this(fileSize, new ArrayList<UriInfo>());
	}

	public BisectDistribute(int fileSize, List<UriInfo> uris) {
		fillRanges = new TreeSet<RangeInfo>(new StartComparator());
		emptyRanges = new TreeSet<RangeInfo>(new StartComparator());
		sortedEmptyRanges = new TreeSet<RangeInfo>(new RangeComparator());

		notUsedRanges = new ArrayList<RangeInfo>();

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
			notUsedRanges.add(r);
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
				notUsedRanges.add(r);
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
	}

	@Override
	public RangeInfo getNextRangeInfo() {
		lock.lock();
		RangeInfo r = null;
		// 如果有未使用的直接返回
		if (notUsedRanges.size() > 0) {
			r = notUsedRanges.get(0);
			notUsedRanges.remove(r);
			return r;
		}

		// 取最大空闲的一半
		r = getMaxEmptyRange();
		if (r == null) {
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
		Iterator<RangeInfo> iter = fillRanges.iterator();
		while (iter.hasNext()) {
			RangeInfo r = iter.next();
			// 不能等于end
			if (loc >= r.getStart() && loc < r.getEnd()) {
				return true;
			}
		}
		lock.unlock();
		return false;
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
				r.setEnd(e);
				// TODO 是否需要刷新

			}
		}
	}

	private void addEmpty(RangeInfo r) {
		emptyRanges.add(r);
		sortedEmptyRanges.add(r);
	}

	private void removeEmpty(RangeInfo r) {
		emptyRanges.remove(r);
		sortedEmptyRanges.remove(r);
		RangeFactory.releaseRangeInfo(r);
	}

	private RangeInfo getMaxEmptyRange() {
		if (sortedEmptyRanges.size() > 0) {
			return sortedEmptyRanges.first();
		}
		return null;
	}
}
