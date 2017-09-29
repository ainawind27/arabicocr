import java.util.Scanner;

public class Latihan_String_Palindrom_2 {

	static String kata1;
	
	static void Palindrom(){
		int panjang = kata1.length();
		String balik = "";
		for (int i = panjang-1; i>=0; i--){
			balik = balik + kata1.charAt(i);
		}
		
		if (kata1.equals(balik)){
			System.out.println("kata ini palindrom");
		}else {
			System.out.println("kata ini bukan palindrom");
		}
	}
	
	public static void main(String[]args) {
		Scanner a = new Scanner(System.in);
		System.out.println("Masukkan kata  :");
		kata1 = a.next();
		Palindrom();
	}

}
