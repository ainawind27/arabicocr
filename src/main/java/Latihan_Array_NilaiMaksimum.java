import java.util.Scanner;

public class Latihan_Array_NilaiMaksimum {

	static int [] array ;
	static int ukuran;
	static void InputArray(){
		array = new int [ukuran];
		
		System.out.println("Masukkan nilai array yang diinginkan : ");
		Scanner b = new Scanner (System.in);
		for (int i = 0; i<ukuran ;i++){
			System.out.print("Masukkan Array ke ["+i+"] : " );
			array[i] = b.nextInt();
		}
	}
	
	static void Cekmaksimum(){
		int max = array[0];
		
		for (int i = 0 ; i<ukuran;i++){
			if (array[i]>max){
				System.out.println("Nilai maksimum adalah Array ke " +i+ " " +array[i]);
			}					
		}
		System.out.println("Nilai maksimum adalah : " +max);
	}

	public static void main (String[]args){
		Scanner a = new Scanner(System.in);
		System.out.println("Masukkan ukuran array : ");
		ukuran = a.nextInt();
		InputArray();
		Cekmaksimum();
	}
}
