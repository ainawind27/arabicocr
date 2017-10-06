import java.util.Scanner;

public class AgeLegal2App {
	
	public static void main(String... args) {
		Scanner input = new Scanner(System.in);
		System.out.print("Age: ");
		
		int count = 1;
        final int age = input.nextInt();
        final int legalText = age >= 17 ? 
        		count : count;  
        System.out.println(legalText);
	}
}
