import java.util.Scanner;

public class segiempat1 {

	public static void main (String[]args) {
		int panjang;
		Scanner a = new Scanner (System.in);
		System.out.print("masukkan panjang : ");
		panjang = a.nextInt();
		
		int lebar;
		Scanner b = new Scanner (System.in);
		System.out.print("masukkan lebar : ");
		lebar = b.nextInt();
		
		for (int i = 1; i<= panjang;i++){
			for (int j = 1; j<= lebar; j++){
				System.out.print("*");
			}
			System.out.println();
		}
	}

}
