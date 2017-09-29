import java.util.Scanner;

public class String_Length {

	static String kata;
	
	static void Cekpanjangkata (){
		int panjang = kata.length();
		System.out.println("panjang kata " +kata+ " adalah " +panjang);
	}
	
	public static void main(String[]args) {
		Scanner a = new Scanner(System.in);
		System.out.println("Masukkan kata :");
		kata = a.next();
		
		Cekpanjangkata();
	}

}
