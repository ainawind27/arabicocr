
public class exception_1 {

	 public static void main(String[] args) {
	        try
	        {
	            int a = 1 / 0; // berpotensi untuk menimbulkan kesalahan yaitu
	            // pembagian dengan bilangan 0
	            System.out.println("perintah selanjutnya");
	        }
	        catch (Exception kesalahan)
	        {
	            System.err.println(kesalahan);
	        }
	    }
	}