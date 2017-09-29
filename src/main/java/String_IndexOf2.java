import java.util.Scanner;

public class String_IndexOf2 {

	static String kata;
	
	static void Cekindex(){
		int index = kata.indexOf('n');
		if (index!= - 1){
			System.out.println("Huruf n ada di index ke " +index);
		}else {
			System.out.println("Index tidak ditemukan");
		}
		
	}
	public static void main (String[]args) {
		Scanner a = new Scanner (System.in);
		System.out.println("Masukkan kata : ");
		kata = a.next();
		Cekindex();
	}

}
