import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class H_Time_Conversion_1 {

	public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String time = in.next();
        String []listTime = time.split(":");
        String hour = listTime[0];
        String minutes = listTime[1];
        String secounds = listTime[2].substring(0, 2);
        String caser = listTime[2].substring(2, 4);
        System.out.println(secounds);
       
        
        if(caser.equals("AM"))
        	
        	{
            if(hour.equals("12"))
                   hour="00";

            System.out.println(hour+":"+minutes+":"+secounds);
        	}
        
        else
        	{
            if(!hour.equals("12")){
                int h = Integer.parseInt(hour); 
                h = h +12; 
                hour =""+h; 
            }
            System.out.println(hour+":"+minutes+":"+secounds);
        }
        
    }
}