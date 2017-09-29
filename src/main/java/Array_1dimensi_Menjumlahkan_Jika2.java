import java.util.Scanner;

public class Array_1dimensi_Menjumlahkan_Jika2 {

public static void main (String[]args) {
		
		int ukuran ;
		Scanner b = new Scanner(System.in);
		System.out.println("Masukkan jumlah ukuran array : ");
		ukuran = b.nextInt();
		int [] array = new int [ukuran];
		int Jumlah1=0;
		Scanner a = new Scanner(System.in);
		
	
		
		
		for (int i = 0;  i<array.length;i++){
			System.out.print("Masukkan nilai array ke " +i+ "  : ");
			array[i]= a.nextInt();
		}
		
		for (int i=0;i<array.length;i++){
			if (array[i] == 2){
				Jumlah1 = Jumlah1+array[i];
		}
		}
		System.out.println("Array yang bernilai 2 jumlahnya adalah : " +Jumlah1 );
	}

}
