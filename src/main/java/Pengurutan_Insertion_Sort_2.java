import java.util.Scanner;

public class Pengurutan_Insertion_Sort_2 {
	
	static int [] array;
	static int [] UrutArray (int [] array)
	{
		int ukuran = array.length;
		int i = 1;
		int max,j;
		while (i<ukuran)
		{
			max = array[i];
			
			for ( j=i ; j>0; j--)
			{
				if (max <array[j-1])
					array[j] = array[j-1];
				else 
					break;
			}
			array [j] = max;
			i++;
		}
		return array;
	}
	public static void main(String[]args) 
	{
		Scanner a = new Scanner (System.in);
		System.out.println("Masukkan jumlah array : ");
		int jumlaharray = a.nextInt();
		array = new int [jumlaharray];
		for (int k =0 ; k<jumlaharray;k++)
		{
			System.out.println("Array ke [" +k+ "] adalah : ");
			array[k] = a.nextInt();
		}
		UrutArray(array);
		
		System.out.println("Array yang sudah terurut adalah : ");
		
		for (int k = 0 ; k<jumlaharray;k++)
		{
			System.out.println(array[k]);
		}
	}

}
