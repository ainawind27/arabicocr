import java.util.Scanner;

public class H_AppledanOrange {

	public static void main (String [] args){
		
		Scanner c = new Scanner(System.in);
		
	//	System.out.println("Masukkan nilai s dan t ");
		int s = c.nextInt();
		int t = c.nextInt();
		System.out.println("");
	//	System.out.println("Masukkan nilai a dan b ");
		int a = c.nextInt();
		int b = c.nextInt();
	//	System.out.println("");
	//	System.out.println("Masukkan nilai m dan n ");
		int m = c.nextInt();
		int n = c.nextInt();	
	//	System.out.println("");
	//	System.out.println("Masukkan nilai dApple ");
		int [] dApple = new int [m];
		int [] dOrange = new int [n];
		for (int i= 0; i<m;i++){
			dApple[i] = c.nextInt();
		}
	//	System.out.println("Masukkan nilai dOrange ");
	//	System.out.println("");
		for (int i = 0 ; i<n;i++){
			dOrange[i] = c.nextInt();
		}
		
		int fallApple = 0;
		int fallOrange = 0;
		int fallAppleInHouse = 0;
		int fallOrangeInHouse = 0;
		
		for (int i = 0; i<m;i++){
			fallApple = a + dApple[i];
			if (fallApple >= s && fallApple<= t){
				fallAppleInHouse++;
			}
		}
		
		for (int i = 0; i<n; i++){
			fallOrange = b+dOrange[i];
			if (fallOrange <=t && fallOrange >=s){
				fallOrangeInHouse++;
			}
		}
		
		System.out.println(fallAppleInHouse);
		System.out.println(fallOrangeInHouse);
	}
	

	
}
