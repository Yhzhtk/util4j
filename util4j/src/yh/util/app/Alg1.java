package yh.util.app;

public class Alg1 {

	public static void main(String[] args) {
		int num = getNum(2,3,4,48);
		System.out.println(num);
	}
	
	public static int getNum(int a, int b, int c, int m) throws java.lang.ArithmeticException{
		int al = m/a + 1;
		int bl = m/b + 1;
		int cl = m/c + 1;
		
		int n = 0;
		int num = 0;
		for(int i = 0; i < al; i++){
			for(int j = 0; j < bl; j++){
				for(int k = 0; k < cl; k++){
					if(a * i + b * j + c * k == m){
						num++;
					}else if(a * i + b * j + c * k > m){
						break;
					}
					n++;
				}
			}
		}
		System.out.println(n);
		return num;
	}
}
