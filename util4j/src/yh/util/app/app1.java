package yh.util.app;

public class app1 {

	public static void main(String[] args) throws Exception {
		int a = 1558621646;
		int b = 55464151;
		int len = 1000000000;
		long s = 0;
		long e = 0;
		s = System.currentTimeMillis();
		
		s = System.currentTimeMillis();
		for(int i = 0; i < len; i++){
			a = 0;
		}
		e = System.currentTimeMillis();
		System.out.println(e - s);
		
		s = System.currentTimeMillis();
		for(int i = 0; i < len; i++){
			if(a > b){
				
			}
		}
		e = System.currentTimeMillis();
		System.out.println(e - s);
		
	}
	
	public static int test() throws Exception{
		try{
			System.out.println(1/0);
		}catch(Exception e){
			System.out.println(2);
			System.exit(0);
		}finally{
			System.out.println(3);
		}
		System.out.println(4);
		return 0;
	}
}
