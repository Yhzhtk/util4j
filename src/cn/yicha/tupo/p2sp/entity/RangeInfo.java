package cn.yicha.tupo.p2sp.entity;  

/**
 * 存储区域信息
 * 
 *@author:gudaihui
 *@date:2013-11-20
 */
public class RangeInfo {
	
	private int index;
	private int start;
	private int end;
	private boolean used;
	
	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public RangeInfo(int index){
		this.index = index;
		reset();
	}
	
	public boolean equals(RangeInfo range){
		return this.index == range.index;
	}
	
	public void reset(){
		start = -1;
		end = -1;
		used = false;
	}
	
	public boolean isAvailable(){
		if(start == -1 || end == -1){
			return false;
		}
		return true;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	
	public String toString(){
		return index + ":" + start + "-" + end;
	}
}
