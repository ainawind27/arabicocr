import java.util.Scanner;



public class Pencarian_Interpolation_Search_1 {

	
	static int [] array;
	static int found;
	
	static void Inputarray ()
	{
		Scanner a = new Scanner (System.in);
		int n = a.nextInt();
		array = new int [n]	;
		for (int i = 0; i<n;i++)
		{
			System.out.print("array ke ["+i+"] : ");
			array [i] = a.nextInt();
		}
	}
	
	static void Bacaarray()
	{
		for (int i=0;i<array.length;i++)
		{
			System.out.println("Isi array [" +i+ "]"+array[i]);
		}
	}
	
	static int [] pengurutan (int [] array)
	{
		int ukuran = array.length;
		
		
		while (ukuran>0)
		{
			int max = 0;
			for (int i=1;i<ukuran;i++)
			{
				if (array[max]<array[i])
					max = i;
			}
			
			int temp = array[max];
			array[max]=array[ukuran-1];
			array[ukuran-1]=temp;
			
			ukuran--;
		}
		return array;
	}

	
static class Interpolation_search 
	{
	boolean hasil = false;
	int found;
	
	boolean pencarian_interpolasi(int [] array, int find)
	{

		int high = array.length-1;
		int low = 0;
		int mid;		
		
		while (low<high)
		{
			mid = low + (high - low) * ((find-array[low])/(array[high]-array[low]));
			
			if (find == array[mid])
			{
				found = mid;
				hasil = true;
				break;
			}
			
			if (find > array[mid])
			{
				low = mid+1;
			} else
			{
				high = mid-1;
			}
		}
		return hasil;
		
	}
		 int getfound()
		{
			return found;
		}

	
	}
		public static void main(String[]args) 
		{
			System.out.println("Masukkan nilai array yang diinginkan : ");
			Inputarray();
			Bacaarray();
			pengurutan(array);
			
			System.out.println("Array yang sudah terurut adalah : ");
			for (int i = 0; i<array.length;i++)
			{
				System.out.println(array[i]);
			}
			
			System.out.println("Masukkan nilai yang dicari : ");
			Scanner b = new Scanner (System.in);
			int find = b.nextInt();
			boolean hasil=false;
			Interpolation_search is = new Interpolation_search();
	       
			hasil = is.pencarian_interpolasi(array, find);
	        
	        if(hasil)
	            System.out.println("data berhasil ditemukan pada indeks ke "+is.found);
	        else
	            System.out.println("data tidak ditemukan"); 
			
		
	}

}
