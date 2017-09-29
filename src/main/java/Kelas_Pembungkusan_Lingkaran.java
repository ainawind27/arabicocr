class Lingkaran{
	int jari;
	public Lingkaran (){	
	}
	
	public void Setjari (int jari){
		this.jari = jari;
	}
	
	private double Getluas(){
		double luas = jari*jari*3.14;
		return luas;
	}
	public void Display(){
		System.out.println("Luas lingkaran dengan jari - jari " +jari+ " adalah : " +Getluas());
	}
}


public class Kelas_Pembungkusan_Lingkaran {

	public static void main(String[]args) {
		Lingkaran lingkaran = new Lingkaran();
		
		lingkaran.Setjari(7);
		lingkaran.Display();
	}

}
