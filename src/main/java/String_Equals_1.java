import java.util.Scanner;

public class String_Equals_1 {

	static String kata1;
	static String kata2;
	
	static void CekEquals(){
		System.out.println("Cek kesamaan kata : ");
		
		if (kata1.equals(kata2)){
			System.out.println("Kedua kata tersebut sama");
		} else {
			System.out.println("Kedua kata tersebut berbeda");
		}
	}
	
	public static void main (String[]args){
		System.out.println("Masukkan kedua kata berturut - turut ");
		Scanner a= new Scanner (System.in);
		System.out.println("Masukkan kata pertama : " );
		kata1 = a.next();
		System.out.println("Masukkan kata kedua : ");
		kata2 = a.next();
		
		CekEquals();
	}
}
