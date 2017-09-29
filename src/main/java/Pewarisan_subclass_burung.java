
public class Pewarisan_subclass_burung extends Pewarisan_Superclass_Hewan{
	private String sifat;
	
	public Pewarisan_subclass_burung(String nama, int umur , String sifat) {
		super (nama, umur);
		this.sifat=sifat;
	}
	
	public void print(){
		System.out.println("ini adalah hewan " +this.nama+ " Umurnya " +this.umur+ " tahun, sifatnya bisa " +this.sifat);
	}

}
