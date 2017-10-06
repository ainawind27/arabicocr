import java.util.Scanner;

public class FriendsWhileApp {

	public static void main (String[] args){
		Scanner input = new Scanner (System.in);
		 int friendCount = 0;
	        String friend;
	        System.out.print("Who is your friend? ");
	        friend = input.nextLine();
	        while (!friend.isEmpty()) {
	        	System.out.println(friend + " is your friend");
	        	friendCount++;
	        	System.out.println("Now you have " + friendCount + " friend(s)");
	        	
	        	System.out.print("Who is your friend? ");
	            friend = input.nextLine();
	        }
	        System.out.println("Thank you :)");
	        }
	}
