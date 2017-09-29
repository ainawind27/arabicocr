import java.util.Scanner;

public class H_Min_Max_Sum_4 {

	public static void main (String[]args) {
		
		long [] array = new long [5];
		System.out.println("Masukkan nilai array : ");
		Scanner a = new Scanner (System.in);
		for (int i=0;i<5;i++)
		{
			System.out.print("Array ke [" +i+ "]");
			array[i]= a.nextLong();
		}	
		
		long minVal = 0;
		long maxVal = 0;
		
		for (int i = 0 ; i<5; i++)
		{
			long minf = Summinmax (i,array);
			long maxf = Summinmax (i,array);
			
			if (i==0)
			{
				minVal = minf;
				maxVal = maxf;
			}
			
			if (minf <minVal)
			{
				minVal = minf;
			}
			if (maxf>maxVal)
			{
				maxVal = maxf;
			}
		}
		System.out.print(minVal+ " " +maxVal );
		
	}
	
	static long Summinmax(int a, long [] array)
	{
		long jumlah = 0;
		
		for (int i = 0; i<5; i++)
		{
			if (a!= i){
				jumlah = jumlah + array[i];
			}
		}
		
		return jumlah;
	}

}
