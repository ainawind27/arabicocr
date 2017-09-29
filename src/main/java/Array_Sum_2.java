import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Array_Sum_2 {

    static int simpleArraySum(int n, int[] ar) {
        int jumlah = 0;      
        for (int i = 0 ; i<n; i++)
        {
            jumlah = jumlah + ar[i];
        }
        return jumlah;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Masukkan jumlah array : ");
        int n = in.nextInt();
        int[] ar = new int[n];
        for(int ar_i = 0; ar_i < n; ar_i++){
        	System.out.print("Array ke " +ar_i+" : ");
            ar[ar_i] = in.nextInt();
        }
        int result = simpleArraySum(n, ar);
        System.out.println(result);
    }
}