import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Array_dimensi1 {

	static int [] array;
	static int ukuran;
	

	static void Inputarray(){
		Scanner a = new Scanner (System.in);
		array = new int [ukuran];
		System.out.println("Masukkan nilai array yang diinginkan: ");
		
		for (int i=0;i<ukuran;i++){
			System.out.print("array ke " +i+ " :");
			array[i] = a.nextInt();
		}
	}
	 static void Bacaaray(){
		System.out.println("Isi array yang diinputkan : ");
		for (int i=0;i<ukuran;i++){
			System.out.println("array ke  " +i+ " : " +array[i]);
		}
	}
	static void rata2(){
		double rata=0;
		for (int i = 0;i<ukuran;i++){
			rata = rata + array[i];
		}
		System.out.println("rata - rata array adaah : "+rata/ukuran);
	}
	
	public static void main (String[]args){
		Scanner b = new Scanner(System.in);
		System.out.println("Masukkan ukuran array yang diinginkan ");
		ukuran = b.nextInt();
		Inputarray();
		Bacaaray();
		rata2();
	}
	
//	static int inputdata()
//    {
//        BufferedReader a=new BufferedReader(new InputStreamReader(System.in));
//        String angka=null;
//    
//        try
//        {
//            angka=a.readLine();
//        }catch(Exception e)
//        {
//            e.toString();
//        }
//        
//        int nilai=Integer.valueOf(angka).intValue();
//        
//        return nilai;
//    }
}
