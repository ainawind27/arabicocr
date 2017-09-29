import java.util.Scanner;

public class H_BirthdayCakeCandles {
	  
	static int birthdayCakeCandles(int n, int[] ar) {
	        
		int max = 0;
		int ukuran = ar.length;
		int result=0;
		for (int i =1; i<ukuran;i++)
		{
			if (ar[max]<ar[i])
				max = i;
		}
		
		for (int i=0;i<ukuran;i++)
		{
			if (ar[max]==ar[i])
				result ++;
			
		}
		
		return result;
	    }
	    
	  public static void main(String[] args) {
	        Scanner in = new Scanner(System.in);
	        int n = in.nextInt();
	        int[] ar = new int[n];
	        for(int ar_i = 0; ar_i < n; ar_i++){
	            ar[ar_i] = in.nextInt();
	        }
	        int result = birthdayCakeCandles(n, ar);
	        System.out.println(result);
	    }
	}