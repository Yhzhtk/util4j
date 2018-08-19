package yh.util.alg;

public class Sort {

	public static void main(String[] args) {
		int [] arr = new int[]{4,8,96,2,3,84,34,5,96,4,6,5,3,2,7,851,654,2,8,13,846,21,58,85,1,68,54,4,8,96,2,3,84,34,5,96,4,6,5,3,2,7,851,654,2,8,13,846,21,58,85,1,68,54,4,8,96,2,3,84,34,5,96,4,6,5,3,2,7,851,654,2,8,13,846,21,58,85,1,68,54,};
		
		System.out.println("排序数量：" + arr.length);
		
		long s = System.currentTimeMillis();
		quickSort(arr,0,arr.length - 1);
		long e = System.currentTimeMillis();
		System.out.println("qucikSortTime:" + (e - s));
		for(int i = 0; i < arr.length; i++){
			System.out.print(arr[i]);
		}
		System.out.println();
		
		s = System.currentTimeMillis();
		quickSort(arr,0,arr.length - 1);
		e = System.currentTimeMillis();
		for(int i = 0; i < arr.length; i++){
			System.out.print(arr[i]);
		}
		System.out.println("bubbleSortTime:" + (e - s));
	}
	
	/**
	 * 快速排序
	 * @param arr
	 * @param left
	 * @param right
	 */
	public static void quickSort(int[] arr, int left, int right){
		if (left >= right)
			return;
		
		int xx = left, yy = right;
		int key = arr[left];
		
		while (xx != yy) {
			while (xx < yy && arr[yy] >= key)
				yy--;
			arr[xx] = arr[yy];
			
			while (xx < yy && arr[xx] <= key)
				xx++;
			arr[yy] = arr[xx];
		}
		
		arr[xx] = key;
		quickSort(arr, left, xx - 1);
		quickSort(arr, xx + 1, right);
	}
	
	/**
	 * 冒泡排序
	 * @param arr
	 * @param left
	 * @param right
	 */
	public static void bubbleSort(int[] arr, int left, int right){
		for(int i = left; i < right - 1; i++){
			for(int j = i + 1; j < right; j++){
				if(arr[i] > arr[j]){
					swap(arr, i, j);
				}
			}
		}
	}
	
	public static void swap(int[] arr, int i1, int i2){
		int t = arr[i1];
		arr[i1] = arr[i2];
		arr[i2] = t;
	}
}
