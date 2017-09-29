import java.util.Scanner;

public class Digit_hingga_satuan_1 {

	public static void main(String[]args) {
		int Angka;
		int temp=0;
		
		Scanner a = new Scanner(System.in);
		System.out.println("Masukkan nilai Angka ");
		Angka = a.nextInt();
		while (Angka>1){
			temp = Angka%10;
			System.out.println("Satuannya adalah " +temp);
			Angka = Angka/10;
		}
		
		
	}
	
	

}
