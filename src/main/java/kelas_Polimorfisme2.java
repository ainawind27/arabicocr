class Bentuk1{
	protected double luas;
	public void Hitungluas(){		
	}
}

class Segitiga1 extends Bentuk1{
	double sisi;
	public Segitiga1(){
		super();
	}
	public void Hitungluas(){
		luas = 0.5*(sisi*sisi);
		System.out.println("Luas Segitiga adalah : " +luas);
	}
	public void Setsisi (int sisi){
		this.sisi = sisi;
	}
	public void Setsisi (double sisi){
		this.sisi= sisi;
	}
}

class Persegi1 extends Bentuk1{
	int sisi;
	public Persegi1 (int sisi){
		super();
		this.sisi = sisi;
	}
	
	public void Hitungluas(){
		luas = sisi*sisi;
		System.out.println("Luas Persegi adalah : " +luas);
	}
}

public class kelas_Polimorfisme2 {

	public static void main(String[]args) {
		
		Segitiga1 segitiga = new Segitiga1();
		Persegi1 persegi = new Persegi1(5);
		
		segitiga.Setsisi(5);
		segitiga.Hitungluas();
		
		System.out.println("");
		
		persegi.Hitungluas();
	}

}
