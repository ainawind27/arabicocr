
public class Pewarisan_Hewan_Tester {

	public static void main (String[]args) {
		Pewarisan_subclass_burung burung = new Pewarisan_subclass_burung("Merpati", 2 , "Terbang");
		Pewarisan_subclass_kucing kucing = new Pewarisan_subclass_kucing("Kucing", 3 , "Berlari");
		
		burung.print();
		kucing.print();
	}

}
