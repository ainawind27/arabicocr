import java.util.Scanner;

public class pengulanganwhileboolean3 {

	public static void main(String[]args) {
		
		boolean running = true;
		String jawab;
		int counter = 0;
		Scanner a = new Scanner (System.in);
		
		while (running){
			System.out.println("Apakah anda ingin keluar ?");
			System.out.println("Jawab <ya/tidak> ?");
			
			jawab = a.nextLine();
			
			if (jawab.equalsIgnoreCase("ya")){
				running = false;
			}
			counter++;
		}
		System.out.println("Anda sudah melakukan pengulangan sebanyak " +counter);
	}

}
