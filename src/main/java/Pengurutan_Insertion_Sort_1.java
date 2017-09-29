import java.util.Scanner;

public class Pengurutan_Insertion_Sort_1 {
   
	    static int[] insertion(int[] array)
	    {
	     int ukuran=array.length;  
	     int max,j;
	     int i=1;
	     
	     while(i<ukuran)
	     {
	         max=array[i];
	      
	         for(j=i;j>0;j--)
	         {
	             if(max<array[j-1])
	                 array[j]=array[j-1];
	             else
	                 break;
	         }         
	         array[j]=max;         
	         i++;
	     }     
	     return array;     
	    }
	    
	    public static void main (String [] args){
			 Scanner a = new Scanner (System.in);
			 System.out.println("Masukkan isi array : ");
			 int[] array = new int [5];
			 
			 for (int i = 0 ; i<5 ; i++)
			 {
				 System.out.print("Array ke ["+i+"] : ");
				 array [i] = a.nextInt();
			 }
			 
			 insertion(array);
			 
			 for (int j = 0; j<5 ;j++){
				 System.out.print(array[j]);
			 }
		 }
	}
