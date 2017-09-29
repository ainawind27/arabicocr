import java.util.Scanner;

public class bilpangkat4 {

	public static void main(String[]args) {
		int hasil = 1;
		int angka, pangkat ;
		
		Scanner a = new Scanner(System.in);
		System.out.print("Masukkan angka : ");
		angka = a.nextInt();
		
		Scanner b = new Scanner(System.in);
		System.out.print("masukkan pangkat : ");
		pangkat = b.nextInt();
		
		for (int i = 1; i<=pangkat; i++){
			
			hasil = hasil * angka;
		}
		System.out.println("Angka " +angka+ " pangkat " +pangkat+ " = " +hasil);
		
	}

}
