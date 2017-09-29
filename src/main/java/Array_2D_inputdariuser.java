import java.util.Scanner;

public class Array_2D_inputdariuser {

	public static void main(String[]args) {
		int [][] array2d = new int [2][3];
		Scanner a = new Scanner (System.in);
		System.out.println("Masukkan nilai Array 2 Dimensi");
		for (int i = 0; i<2;i++){
			for (int j=0; j<3; j++){
				System.out.print("Array ke " +i+ " " +j+ " : ");
				array2d[i][j]= a.nextInt();
			}
			System.out.println("");
		}
		System.out.println("Isi nilai Array 2 Dimensi adalah");
		for (int i=0;i<2;i++){
			for (int j=0;j<3;j++){
				System.out.print(array2d[i][j] + " ");
			}
			System.out.println("");
		}
	}

}
