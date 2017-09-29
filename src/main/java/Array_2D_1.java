
public class Array_2D_1 {

	public static void main(String[]args) {
		
		String [][] Kota = {
				{"No" , "Nama" , "Kota"},
				{"1" , "Aina" , "Bandung"},
				{"2" , "Husnul" , "Depok"},
				{"3" , "Aulia" , "Jakarta"},
				{"4" , "Desfaharni" , "Bandung"}
				};
		for (int i = 0; i<5; i++){
			for (int j = 0;j<3; j++){
				System.out.print(Kota[i][j] + " " );
			}
			System.out.println("");
		}
		
	}

}
