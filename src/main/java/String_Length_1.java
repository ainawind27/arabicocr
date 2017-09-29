import java.util.Scanner;

public class String_Length_1 {

	 public static void main(String[]args) {
		
		Scanner a = new Scanner(System.in);
		System.out.println("Masukkan kata : ");
		String kata = a.next();
		
		int panjang = kata.length();
		System.out.println("Panjang kata : " +kata+ " adalah " +panjang);
		
	}

}
