import java.util.Scanner;

public class Array_2D_Buku {

	static int [][] array2d;
	static int baris;
	static int kolom;
	
	static void Inputarray(){
		array2d = new int [baris][kolom];
		Scanner a = new Scanner (System.in);
		System.out.println("Masukkan nilai array yang diinginkan : ");
		for (int i = 0; i<baris;i++){
			for (int j = 0; j<kolom; j++){
				System.out.print("Array ke : " +i+ " " + j + " ");
				array2d[i][j] = a.nextInt();
			}
		}
	}
	
	static void Bacaarray () {
		System.out.println(" Isi array adalah : ");
		for (int i=0;i<baris;i++){
			for (int j=0; j<kolom; j++){
				System.out.println(" Array ["+i+"],["+j+"] " + array2d[i][j]);
			}
			System.out.println("");
		}
	}
	
	static void minimum(){
		int min = array2d[0][0];
		for (int i=0; i<baris;i++){
			for (int j=0; j<kolom;j++){
				
				if (array2d[i][j]<min){
					min = array2d[i][j];
				}
			}	
		}
		System.out.println("Nilai minumum dari array 2 Dimensi adalah : " +min);
	}
	
	public static void main(String[]args) {
		System.out.println("Masukkan nilai matrix dari array 2 dimensi");
		Scanner b = new Scanner(System.in);
		System.out.print("Baris : ");
		baris = b.nextInt();
		System.out.print("");
		System.out.print("Kolom : ");
		kolom = b.nextInt();
		
		Inputarray();
		Bacaarray();
		minimum();
	}

}
