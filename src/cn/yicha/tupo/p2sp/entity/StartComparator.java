package cn.yicha.tupo.p2sp.entity;

import java.util.Comparator;

public class StartComparator implements Comparator<RangeInfo> {
	@Override
	public int compare(RangeInfo o1, RangeInfo o2) {
		return o1.getStart() - o2.getStart();
	}
}
