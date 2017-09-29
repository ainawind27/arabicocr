class Bentuk{
	protected double luas;
	
	protected void Hitungluas(){
	}
}

class Segitiga extends Bentuk{
	double sisi;
	public Segitiga(){
		super();
	}
	public void Hitungluas(){
		luas = 0.5*(sisi*sisi);
		System.out.println("Luas segitiga adalah : " +luas);
	}
	public void Setsisi(int sisi){
		this.sisi = sisi;
	}
	public void Setsisi (double sisi){
		this.sisi = sisi;
	}
}

class Persegi extends Bentuk{
	int sisi;
	public Persegi(int sisi){
		super();
		this.sisi= sisi;
	}
	public void Hitungluas(){
		luas = sisi*sisi;
		System.out.println("Luas persegi adalah : " +luas);
	}
}

public class kelas_Polimorfisme1 {

	public static void main(String [] args) {
		
		Segitiga segitiga = new Segitiga();
		Persegi persegi = new Persegi (5);
		
		segitiga.Setsisi(5);
		segitiga.Hitungluas();
		
		System.out.println("");
		
		persegi.Hitungluas();
	}

}
