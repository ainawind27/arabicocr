import java.util.Scanner;

public class H_Grading_Students {

public static void main (String[]args) {
		
		Scanner a = new Scanner (System.in);
	
		int n = a.nextInt();
		
		int [] nilai = new int [n];
		
		for (int i = 0 ; i<n ;i++){
			nilai[i] = a.nextInt();
		}
		int temp = 0;
		int temp2 = 0;
		int batas5 = 5;
		
		for (int i=0;i<n;i++){
			temp = nilai[i]%5;
			temp2 =  batas5-temp;
			
			if (temp2<3){
				if (nilai [i] >37){
					while ( temp <5){
						nilai [i] ++;
						temp++;
					} 
				}
							
				System.out.println(nilai[i]);
			} else {
				
				System.out.println(nilai[i]);
			}
				
			
		}
		
		
	}

}
