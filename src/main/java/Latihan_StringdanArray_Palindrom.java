import java.util.Scanner;

public class Latihan_StringdanArray_Palindrom {

	static String kata;
	static void Cekkata(){
		
		String balik = "";
		int ukuran = kata.length();
		
		for (int i=ukuran-1;i>=0;i--){
			balik = balik + kata.charAt(i);
		}
		
		System.out.println(balik);
		if (kata.equals(balik)){
			System.out.println("Kata ini merukapan palindrom");
		}else{
			System.out.println("kata ini bukan palindrom");
		}
	}
	
	public static void main(String[]args) {
		Scanner a = new Scanner(System.in);
		System.out.println("Masukkan kata : ");
		kata = a.next();
		Cekkata ();
	}

}
