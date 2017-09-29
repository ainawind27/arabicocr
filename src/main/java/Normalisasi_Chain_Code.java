
public class Normalisasi_Chain_Code {

		
		public static void main (String[]args){
			int [] chaincode = new int[]{7,7,7,7,3,1,1,1,2,2,2,2,2,5,8,3,3,5,3,3,3,3};
			
			for (int i = 0; i<chaincode.length;i++){
				System.out.print(chaincode[i]);
			}
			System.out.println("");
			int indeks_cek =0;
			int sama = 0;
			int temp;
			//char [] array = new char [chaincode.length]; 
				//int [] array = new int [9];
				for (int i =0 ; i<chaincode.length; i++)
				{
					if (chaincode[indeks_cek]== chaincode[i]){
						sama++;
//						array[i]=(char) chaincode[i];
//						System.out.print(array[i]);
						}else {
							System.out.print(sama);	
							indeks_cek = i;
							sama = 1;
						}
				} System.out.print(sama);
			}
		
			
		}
