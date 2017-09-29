import java.util.Scanner;

public class String_Equals_2 {
 
 static String nama1;
 static String nama2;
 	static void Ceksama(){
 		if (nama1.equals(nama2)){
 			System.out.println("Kedua kata tersebut sama");
 		}else{
 			System.out.println("Kedua kata tersebut tidak sama");
 		}
 		
 	}
	public static void main(String[]args) {
		Scanner a = new Scanner (System.in);
		System.out.println("Masukkan dua nama berturut - turut");
		System.out.println("Masukkan nama 1 : ");
		nama1 = a.next();
		System.out.println("Masukan nama 2 : ");
		nama2 = a.next();
		
		Ceksama();
	}

}
