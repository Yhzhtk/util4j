package yh.util.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Test {

	public static void main(String[] args) {
		Set<Integer> set = new TreeSet<Integer>();
		List<Integer> list = new ArrayList<Integer>();
		
		for (int i = -3; i < 3; i++) {
			set.add(i+1);
			set.add(i);
			list.add(i);
		}
		System.out.println(set + " " + list);
		for (int i = 0; i < 3; i++) {
			set.remove(i);
			list.remove(i);
		}
		System.out.println(set + " " + list);
	}
}
