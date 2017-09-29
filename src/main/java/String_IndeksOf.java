import java.util.Scanner;

public class String_IndeksOf {

	static String kata;
	
	static void CekIndeks(){
		
		int index = kata.indexOf('a',2);
		
		if (index != -1){
			System.out.println("Huruf a ada pada indeks ke " +index);
		}
	}
	
	public static void main (String[]args){
		Scanner a = new Scanner (System.in);
		System.out.println("Masukkan kata : " );
		kata = a.next();
		CekIndeks();
	}
}
