package yh.util.test;

public class RecIterTest {

	static int COUNT = 0;

	public static void main(String[] args) {
		int n = 300;
		long res = 0;
		long start, end;

		start = System.currentTimeMillis();
		res = getIter(n);
		end = System.currentTimeMillis();
		System.out.println("Iter:" + res + "  time:" + (end - start));
		
		start = System.currentTimeMillis();
		res = getRec(n);
		end = System.currentTimeMillis();
		
		System.out.println("Rec:" + res + "  time:" + (end - start)+"   COUNT:"+COUNT);
	}

	public static long getRec(int n) {
		if (n == 3)
			COUNT++;
		if (n <= 2)
			return 1;
		return getRec(n - 1) + getRec(n - 2);
	}

	public static long getIter(int n) {
		long old = 1;
		long now = 1;
		long result = 1;
		while (n > 2) {
			result = old + now;
			old = now;
			now = result;
			n--;
		}
		return result;
	}
}
