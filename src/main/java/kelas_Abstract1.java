
abstract class kelas_Abstract {

	public void Bernafas (){
		System.out.println("Makhluk hidup bernafas");
	}
	abstract void Berjalan();
}

class Manusia extends kelas_Abstract{
	public void Berjalan(){
		System.out.println("Manusia berjalan dengan kaki");
	}
}

class Ikan extends kelas_Abstract{
	
	public void Berjalan(){
		System.out.println("Ikan berenang dengan sirip");
	}
}

public class kelas_Abstract1{
	public static void main (String[]args){
		Manusia manusia = new Manusia();
		Ikan ikan = new Ikan();
		
		manusia.Berjalan();
		manusia.Bernafas();
		
		System.out.println("");
		ikan.Berjalan();
		ikan.Bernafas();
	}
}
