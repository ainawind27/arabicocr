import java.util.Scanner;

public class Array2d_Sum_Diagonal {

	public static void main(String[]args) {
		
		int [][] array2d = new int [3][2];
		Scanner a = new Scanner (System.in);
		
		for (int i=0;i<3;i++) {
			for (int j=0;j<2;j++){
				System.out.print("Isi array ke "+i+""+j+": ");
				array2d[i][j] = a.nextInt();
			}
		}
		
		for (int i=0;i<3;i++){
			for (int j=0;j<2;j++){
				System.out.print(array2d[i][j]+" ");
			}
			System.out.println("");
		}
		System.out.println("");
		
		for (int i=0;i<array2d.length;i++){
			for (int j=0;j<array2d[1].length;j++){
				System.out.print(array2d[i][j]+" ");
			}
			System.out.println("");
		}
		System.out.println("");
		System.out.println(array2d.length);
		System.out.println(array2d.length-1);
		
		System.out.println("");
		
		System.out.println(array2d[0].length); // 5
		System.out.println(array2d[1].length); // 3
		System.out.println(array2d[2].length);
	}

}
