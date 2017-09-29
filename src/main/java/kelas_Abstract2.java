abstract class Abstract{
	
	public void Bernafas(){
		System.out.println("Makhluk hidup bernafas");
	}
	
	abstract  void Berjalan();
}

class Manusia1 extends Abstract{
	public void Berjalan(){
		System.out.println("Manusia berjalan dengan kaki");
	}
}

class Ikan1 extends Abstract{
	public void Berjalan(){
		System.out.println("Ikan berenang dengan sirip");
	}
}

public class kelas_Abstract2 {

	public static void main(String[]args) {
		Manusia1 manusia = new Manusia1();
		Ikan1 ikan = new Ikan1();
		
		manusia.Bernafas();
		manusia.Berjalan();
		System.out.println("");
		
		ikan.Bernafas();
		ikan.Berjalan();
	}

}
