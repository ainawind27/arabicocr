
public class Array_1dimensi_Menjumlahkan_Element {

	public static void main (String [] args) {
		int Jumlah = 0;
		
		int [] array = new int [5];
		array [0] = 1;
		array [1] = 2;
		array [2] = 3;
		array [3] = 4;
		array [4] = 5;
		
		for (int i = 0 ; i<array.length; i++ ){
			Jumlah = Jumlah + array[i];
		}
		for (int i = 0; i<array.length; i++){
			System.out.println("Array ke " +i+ " adalah " +array[i]);
		}
		System.out.println("Jumlah dari array adalah " + Jumlah);
	}

}
