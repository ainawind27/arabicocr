
public class pengulanganwhileboolean1 {

	public static void main(String[]args) {
		boolean hasil = false;
		int i = 0;
		
		while (!hasil) //batas perulagan jika hasil sudah bernilai true berhenti
		{
			if(i==5)
				hasil=true; //berhenti 
			System.out.println("while dengan boolean");
			System.out.println(i);
			i++;
		}
		
		System.out.print("i setelah keluar loop " +i);
	}

}
