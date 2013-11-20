package cn.yicha.tupo.p2sp.entity;  

/**
 * 资源信息
 * 
 *@author:gudaihui
 *@date:2013-11-20
 */
public class UriInfo {

	private String uri;
	private String index;
	private boolean isAvailable;
	private RangeInfo nowRange;
	
	public UriInfo(String uri){
		this.uri = uri;
	}
	
	public boolean equals(UriInfo uri){
		if(this.uri.equals(uri.uri) && this.index == uri.index){
			return true;
		}
		return false;
	}
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public boolean isAvailable() {
		return isAvailable;
	}
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	public RangeInfo getNowRange() {
		return nowRange;
	}
	public void setNowRange(RangeInfo nowRange) {
		this.nowRange = nowRange;
	}

}
