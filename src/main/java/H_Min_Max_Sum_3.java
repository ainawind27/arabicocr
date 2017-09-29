import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class H_Min_Max_Sum_3 {
	
	public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        long arr[] = new long[5];
        for (int arr_i = 0; arr_i < 5; arr_i++) {
            arr[arr_i] = in.nextLong();
        }
        
        long minVal = 0, maxVal = 0;
       
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                minVal = sumOfNumbers(i, arr);
                maxVal = sumOfNumbers(i, arr);
            }
            if (sumOfNumbers(i, arr) < minVal) {
                minVal = sumOfNumbers(i, arr);
            }
            if (sumOfNumbers(i, arr) > maxVal) {
                maxVal = sumOfNumbers(i, arr);
            }
        }
        System.out.println(minVal + " " + maxVal);
    }
 
    static long sumOfNumbers(int a, long[] arr) {
        long sum = 0;
        for (int i = 0; i < 5; i++) {
            if (a != i) {
                sum += arr[i];
            }           
        }
        return sum;
    }
        
    
}