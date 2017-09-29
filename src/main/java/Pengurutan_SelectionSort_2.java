import java.util.Scanner;

public class Pengurutan_SelectionSort_2 {

	public static int [] selec (int [] array) {
			
		int ukuran = array.length;
		
		while (ukuran>0)
		{
			int max = 0;
			int i ;
				
			for ( i = 1; i< ukuran; i++)
			{
				if (array[max]<array[i])
				max = i;
			}
		
			int temp = array[max];
			array[max] = array [ukuran-1];
			array[ukuran-1]= temp;
			
			ukuran--;
		}
		return array;	
	}

	public static void main (String[]args){
		Scanner a = new Scanner (System.in);
		System.out.println("Masukkan nilai array : ");
		int[] array = new int [5];
		
			for (int i = 0 ; i<5; i++)
			{
				System.out.print("Array ke ["+i+"] adalah : "  );
				array[i] = a.nextInt();
			}
		
		selec(array);
			for (int j = 0 ; j<5;j++)
			{
				System.out.print(array[j]);	
			}
		
	}
}
