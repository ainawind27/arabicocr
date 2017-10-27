import java.util.Scanner;

public class H_Breaking_the_Records {
	
	public static void main (String [] srgs) {
		
		Scanner a = new Scanner (System.in);
		//System.out.println("Masukkan jumlah game");
		int gamecount = a.nextInt();
		
		int [] score = new int [gamecount];
		
	//	System.out.println("Masukkan score");
		for (int i = 0;i<gamecount; i++) {
			score[i]= a.nextInt();
		}
		
		
		int jumlahmax = 0;
		int jumlahmin = 0;
		int max = score[0];
		int min = score [0];
		
		for (int i = 0 ; i<score.length; i++) {
			
			if (score[i]>max) {		
				max = score[i];
				jumlahmax++;
				
		//		System.out.println("rekor maksimal " +score[i]);
			}
			
			if (score[i]<min) {
				min = score[i];
				jumlahmin++;
		//		System.out.println("rekor minimal " +score[i]);
			}
		}
	//	System.out.println(jumlahmax);
	//	System.out.println(jumlahmin);
	}

}
