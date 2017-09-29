import java.util.Scanner;

public class Array_1dimensi_Menjumlahkan_Jika1 {

	public static void main (String[]args) {
		
		int [] array = new int [5];
		int Jumlah1=0;
		Scanner a = new Scanner(System.in);
		
		for (int i = 0;  i<array.length;i++){
			System.out.print("Masukkan nilai array ke " +i+ "  : ");
			array[i]= a.nextInt();
		}
		
		for (int i=0;i<array.length;i++){
			if (array[i] == 1){
				Jumlah1 = Jumlah1+array[i];
		}
		}
		System.out.println("Array yang bernilai 1 jumlahnya adalah : " +Jumlah1 );
	}

}
