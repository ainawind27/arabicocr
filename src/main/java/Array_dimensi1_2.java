import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Array_dimensi1_2 {
	
	static int [] array;
	static int ukuran;
	
	static void Inputarray(){
		array = new int [ukuran];
		System.out.println("Masukkan nilai array yang diinginkan : ");
		for (int i = 0;i<ukuran;i++){
			System.out.println("Array ke " +i+ " adalah " );
			array[i] = Inputdata();
		}
	}
	
	static void Bacaarray(){
		System.out.println("Isi array yang diinputkan : ");
		for (int i =0;i<ukuran;i++){
			System.out.println("Array ke " +i+ " adalah " +array[i]);
		}
	}
	
	static void Rata2(){
		double rata=0;
		for (int i = 0;i<ukuran;i++){
			rata = rata + array[i];
		}
		System.out.println("Rata - rata array adalah " +rata/ukuran);
	}
	
	public static void main(String [] args) {
		System.out.println("Masukkan ukuran array yang diinginkan : ");
		ukuran = Inputdata();
		
		Inputarray();
		Bacaarray();
		Rata2();
	}
	
	static int Inputdata()
    {
        BufferedReader a=new BufferedReader(new InputStreamReader(System.in)); // nerima input dari user, bisa baca file txt
        String angka=null;
    
        try
        {
            angka=a.readLine();
        }catch(Exception e)
        {
            e.toString();
        }
        
        int nilai=Integer.valueOf(angka).intValue();
        
        return nilai;
    }
}
