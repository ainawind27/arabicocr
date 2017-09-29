
public class Kelasmobil {
	private String merek;
	private String warna;
	
	public Kelasmobil (String merek, String warna){
		this.merek = merek;
		this.warna = warna;
	}
	
	public void print(){
		System.out.println("Mobil saya mereknya " +this.merek+ " dan warnanya " +this.warna);
	}

}
