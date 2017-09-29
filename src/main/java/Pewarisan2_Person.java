
class Person {
	protected  String nama;
	protected String alamat;
	
	public Person (String nama, String alamat){
		super();
		this.nama = nama;
		this.alamat = alamat;
	}
	public void hello(){
		System.out.println("Saya orang Indonesia");
	}
}

class mahasiswa extends Person{
	private String nim;
	private String profesi;
	public mahasiswa (String nama, String alamat){
		super (nama,alamat);
	}
	
	public void Setnim (String nim){
		this.nim = nim;
	}
	public void Setprofesi(String profesi){
		this.profesi = profesi;
	}
	public void Data_diri(){
		System.out.println("Nama : " +this.nama);
		System.out.println("Alamat : " +this.alamat);
		System.out.println("Nim : " +this.nim);
		System.out.println("Profesi : " +this.profesi);
	}
}

class Dosen extends Person{
	private String nip;
	private String profesi;
	
	public Dosen(String nama, String alamat){
		super (nama,alamat);
	}
	
	public void Setnip(String nip){
		this.nip = nip;
	}
	public void Setprofesi(String profesi){
		this.profesi = profesi;
	}
	public void Data_diri(){
		System.out.println("Nama : " +this.nama);
		System.out.println("Alamat : " +this.alamat);
		System.out.println("Nip :" +this.nip);
		System.out.println("Profesi : " +this.profesi);
		
	}
}


public class Pewarisan2_Person {

	public static void main(String[] args) {
		
		mahasiswa mahasiswa1 = new mahasiswa ("Aina", "Jl. Sangkuriang");
		mahasiswa1.Setnim("23215145");
		mahasiswa1.Setprofesi("Mahasiswa");
		mahasiswa1.hello();
		mahasiswa1.Data_diri();
		
		System.out.println("* ");
		
		Dosen dosen = new Dosen ("Ary Setijadi","Jl. Merdeka");
		dosen.Setnip("23116345");
		dosen.Setprofesi("Dosen");
		dosen.hello();
		dosen.Data_diri();
		
		
	}

}
