class Pewarisan_Orang {

	 protected String nama;
	 protected String alamat;
	 
	 protected Pewarisan_Orang(String nama, String alamat){
		 super();
		 this.nama = nama;
		 this.alamat = alamat;
	 }
	 protected void hello(){
		 System.out.println("Saya orang Indonesia");
	 }
}
 
 class mahasiswa1 extends Pewarisan_Orang {
	 public String nim;
	 
	 public mahasiswa1(String nama, String alamat){
		 super (nama, alamat);
	}
	 public void Setnim(String nim){
		 this.nim = nim;
	 }
	 
	 public void datadiri (){
		 System.out.println("Nama " + nama);
		 System.out.println("Alamat " + alamat);
		 System.out.println("Nim " + nim);
	 }
 }
 
 public class Pewarisan_Orang_tester{
	 public static void main (String[]args){
		 mahasiswa1 mahasiswa2 = new mahasiswa1 ("Aina", "Jl.Sangkurinag");
		 
		 mahasiswa2.Setnim("23215145");
		 mahasiswa2.hello();
		 mahasiswa2.datadiri();
		 
	 }
 }
