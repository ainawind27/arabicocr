import java.util.Scanner;

public class String_CharAt_2 {

		static String kata;
		static void Cetak_Char(){
			int panjang = kata.length();
			System.out.println("Huruf yang ditulis : ");
			
			for (int i = 0; i<panjang; i++){
				System.out.println("Huruf ke " +i+ " adalah : " +kata.charAt(i));
			}
		}
		
		public static void main (String[]args){
			Scanner a = new Scanner (System.in);
			System.out.println("Masukkan tulisan kata : ");
			kata = a.next();
			
			Cetak_Char();
				
			}
		}
	

