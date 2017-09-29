
public class Kelasmotor {

	private String nomor_plat;
	private String warna;
	private String isi_silinder;
	private String kecepatan_maks;
	
	public Kelasmotor (String nomor_plat, String warna, String isi_silinder, String kecepatan_maks){
		this.nomor_plat = nomor_plat;
		this.warna = warna;
		this.isi_silinder = isi_silinder;
		this.kecepatan_maks = kecepatan_maks;
	}
	
	public void print(){
		System.out.println("Motor saya nomor platnya " +this.nomor_plat+ " berwarna " +this.warna + 
							" isi silindernya " +this.isi_silinder+ " Kecepatan maksimumnya " +this.kecepatan_maks);
	}

}
