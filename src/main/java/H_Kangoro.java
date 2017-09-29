import java.io.InputStream;
import java.util.Scanner;

public class H_Kangoro {
	public static void main (String[]args) {
		Scanner a = new Scanner (System.in);
		
		int x1 = a.nextInt();
		int v1 = a.nextInt();
		int x2 = a.nextInt();
		int v2 = a.nextInt();
		
		boolean hasil = cekt(x1, v1, x2, v2);
		
		if(hasil)
			System.out.print("YES");
		else
			System.out.print("NO");
		}

	static boolean cekt(int x1, int v1, int x2, int v2) {
		int v = v1 - v2;
		int s = x2 - x1;
		
		if(((v > 0) && (s > 0)) || ((v < 0) && (s < 0))) {
			v = Math.abs(v);
			s = Math.abs(s);
			return s % v == 0;
		} else {
			return false;
		}
	}			
}

	

