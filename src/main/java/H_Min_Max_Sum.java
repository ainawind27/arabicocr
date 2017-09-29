
	import java.io.*;
	import java.util.*;
	import java.text.*;
	import java.math.*;
	import java.util.regex.*;

	public class H_Min_Max_Sum {

	    public static void main(String[] args) {
	        Scanner in = new Scanner(System.in);
	        
	        int[] arr = new int[5];
	        System.out.println("Masukkan nilai array ");
	        for(int arr_i=0; arr_i < 5; arr_i++){
	        	System.out.print("Array ke ["+arr_i+"]");
	            arr[arr_i] = in.nextInt();
	        }
	        int j = 0;
	        int jumlah = 0;
	        while (j<arr.length)
	        {
	        	int jumlah1=0;
	        	for (int i = 0 ; i<arr.length;i++)
		        {
		        	jumlah = jumlah + arr[i];
		        }
	        	 jumlah1 = jumlah - arr[j];
	        	System.out.println("Jumlah adalah  : " +jumlah1); 
	        	j++;
	        }       	
	        
	       
	        
	    }
	}
