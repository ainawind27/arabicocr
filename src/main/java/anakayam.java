import java.util.Scanner;

public class anakayam {

	public static void main(String[]args) {
		int ayam;
		Scanner a = new Scanner (System.in);
			System.out.print("Masukkan jumlah anak ayam : ");
		ayam = a.nextInt();
		
		for (int i =ayam ; i>=2;i--){
			System.out.println("Anak ayam turun " +i+ " mati satu tinggal " +(i-1));
		}
			System.out.println("Anak ayam turun 1 mati satu tinggal induknya");
	}

}
