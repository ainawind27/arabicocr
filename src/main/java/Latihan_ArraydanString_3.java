import java.util.Scanner;

public class Latihan_ArraydanString_3 {

	static void Cekkata(String kata){
		boolean hasil = false;
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		
		int ukuran_alph = alphabet.length();
		int ukuran_kata = kata.length();
		
		int [] arr = new int [ukuran_kata];
		
		for (int i=0;i<ukuran_kata;i++)
		{
			for (int j=0;j<ukuran_alph;j++)
			{
				if (kata.charAt(i)==alphabet.charAt(j))
					arr [i] = j;			
			}
		}
		hasil = proses(arr);
		if (hasil)
			System.out.println("Kata termasuk abecedarian");
		else
			System.out.println("Kata bukan abecedarian");
	}
	
	static boolean proses (int [] arr){
		int ukuran = arr.length;
		boolean cek = true;
		
		for (int i=1;i<ukuran;i++)
		{
			if (arr[i]<arr[i-1])
			{
				cek = false;
				break;
			}
		}
		return cek;
	}
	
	public static void main (String[]args){
		Scanner a = new Scanner (System.in);
		System.out.println("Masukkan kata : ");
		String kata = a.next();
		Cekkata (kata);
	}

}
