import java.util.Scanner;

public class Array_Sum_1 {

	static int jumlaharray;
	static int [] array;
	
	static void InputArray(){
		array = new int [jumlaharray];
		int jumlah = 0;
		Scanner b = new Scanner (System.in);
			System.out.println("Masukkan isi array yang diinginkan :");
		
			for (int i = 0 ; i<jumlaharray; i++)
			{
				System.out.print("Array ke [" +i+ "] : ");
				array[i] = b.nextInt();
			}
			
			for (int j = 0 ; j<jumlaharray; j++)
			{
				 jumlah = jumlah + array[j];
			}
			
			System.out.println("Jumlah array adalah : " +jumlah);
	}
	
	public static void main(String [] args) {
		Scanner a = new Scanner(System.in);
		System.out.println("Masukkan jumlah array yang diinginkan : ");
		jumlaharray = a.nextInt();
		
		InputArray();
	}

}
