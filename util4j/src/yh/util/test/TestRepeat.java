package yh.util.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import yh.util.repeat.BloomFilter;
import yh.util.repeat.MD5;
import yh.util.repeat.TreeFilter;

public class TestRepeat {

	public static void main(String[] args) {
		int len = 5000000;
		List<String> check = new ArrayList<String>();
		
		check.add("1");
		check.add("1546464");
		check.add("s654646");
		check.add("13231a");
		check.add("adfa");
		check.add("10000000");
		check.add("9999999");
		
		//Md5Filter(len, check);
		//MapFilter(len, check);
		TreeFilter(len, check);
		//BloomFilter(len, check);
	}
	
	public static void Md5Filter(int len,  List<String> check){
		HashSet<String> hs = new HashSet<String>();
		long s = System.currentTimeMillis();
		for(int i = 0; i < len; i++){
			hs.add(MD5.digestString(i + ""));
			if(i % 1000000 == 0){
				System.out.println(i);
				System.gc();
			}
		}
		System.out.println(System.currentTimeMillis() - s);
		
		for(String c : check){
			s = System.currentTimeMillis();
			System.out.println(hs.contains(c));
			System.out.println(c + "    " + (System.currentTimeMillis() - s));
		}
		
		"".length();
	}
	
	public static void MapFilter(int len,  List<String> check){
		HashSet<String> hs = new HashSet<String>();
		long s = System.currentTimeMillis();
		for(int i = 0; i < len; i++){
			hs.add(i + "");
			if(i % 1000000 == 0){
				System.out.println(i);
				System.gc();
			}
		}
		System.out.println(System.currentTimeMillis() - s);
		
		for(String c : check){
			s = System.currentTimeMillis();
			System.out.println(hs.contains(c));
			System.out.println(c + "    " + (System.currentTimeMillis() - s));
		}
		
		"".length();
	}
	
	public static void TreeFilter(int len, List<String> check){
		TreeFilter tf = new TreeFilter();
		long s = System.currentTimeMillis();
		for(int i = 0; i < len; i++){
			tf.add(i + "");
			if(i % 1000000 == 0){
				System.out.println(i);
				System.gc();
			}
		}
		System.out.println(System.currentTimeMillis() - s);
		
		for(String c : check){
			s = System.currentTimeMillis();
			System.out.println(tf.contains(c));
			System.out.println(c + "    " + (System.currentTimeMillis() - s));
		}
		
		"".length();
	}
	
	public static void BloomFilter(int len, List<String> check){
		
		BloomFilter bf = new BloomFilter();	
		long s = System.currentTimeMillis();
		for(int i = 0; i < len; i++){
			bf.add(i + "");
		}
		System.out.println(System.currentTimeMillis() - s);
		
		for(String c : check){
			s = System.currentTimeMillis();
			System.out.println(bf.contains(c));
			System.out.println(c + "    " + (System.currentTimeMillis() - s));
		}
		
		"".length();
	}
}
