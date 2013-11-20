package cn.yicha.tupo.p2sp.distribute.hash;  

public interface HashFunction {
	
	public Integer hash(String str);
	
	public Integer hash(Object obj);
	
}