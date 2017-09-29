import java.util.Scanner;

public class pengulanganwhilebooloean4 {

	public static void main(String[]args) {
		
		boolean running = true;
		int counter = 0;
		String jawab;
		Scanner a = new Scanner(System.in);
		
		while (running){
			System.out.println("apakah anda ingin keluar ? ");
			System.out.print("jawab <ya/tidak> ? ");
			
			jawab = a.nextLine();
			
			if (jawab.equalsIgnoreCase("ya")){
				running = false;
			}
			counter++;
		}
		System.out.println("anda sudah melakukan pengulangan sebanyak " +counter);
	}

}
