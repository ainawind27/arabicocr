public class Normalisasi_Chaincode3 {

	public static void main (String[]args){
		int [] chaincode = new int[]{4,4,5,4,5,5,5,5,6,6,6,7,7,7,7,8,8,8,8,8,8,
				1,2,1,2,2,2,3,2,3,3,2,0};
		int[][] normalized = new int[2][100];
		
		System.out.println("Nilai Chaincode adalah : ");
		for (int i = 0; i<chaincode.length;i++){
			System.out.print(chaincode[i]);
		}
		System.out.println("");
		int indeks_cek =0;
		int sama = 0;

		int normalizedpos = 0;
		
		System.out.println("Frekuensi kemunculain setiap angka dalam ChainCode :");
		for (int i =0 ; i<chaincode.length; i++)
		{
			if (chaincode[indeks_cek]== chaincode[i]){
				sama++;			
				normalized[0][normalizedpos] = chaincode[i];  
				normalized[1][normalizedpos] = sama; 
			}else {
				System.out.print(sama);	
				indeks_cek = i;
				sama = 1;
				normalizedpos++;
				normalized[0][normalizedpos] = chaincode[i];  
				normalized[1][normalizedpos] = sama;  
			}
		} 
		System.out.print(sama);
		
		
		System.out.println("");
		System.out.println("");
		
		System.out.println("Chain Code dirubah kedalam bentuk sebagai berikut dari  2 x 9 matrix");

		int normalizedlen = normalizedpos + 1;
		for (int i =0;i<normalized.length;i++){
			for (int j = 0 ; j<normalizedlen;j++){
				System.out.print(normalized[i][j]);
			}
			System.out.println("");
		}
		
		int [][] normalized2 = new int [2][50];
		int normalizedpos2 =0;
		
		for (int i = 0; i<normalizedlen;i++){
				if(normalized[1][i]!=1){
					
					if (normalizedpos2>0 && normalized[0][i]==normalized2[0][normalizedpos2-1]){
						normalized2[1][normalizedpos2-1] = normalized2[1][normalizedpos2-1]  +  normalized[1][i];
					}else{
						normalized2[0][normalizedpos2]= normalized[0][i];
						normalized2[1][normalizedpos2] = normalized[1][i];
						normalizedpos2++;
					}
				} 
				
		}
		
		System.out.println("");
		System.out.println("semua nilai yang mana frequensinya bukan 1 :");
		int normalized2len = normalizedpos2 ;
		for (int i =0; i<normalized2.length;i++){
			for (int j = 0 ;j<normalized2len;j++){
				System.out.print(normalized2[i][j]);
			}
			System.out.println("");
		}
		System.out.println("");
		int sum = 0;
		for (int i = 0; i < normalized2len; i++) {
			sum += normalized2[1][i];
		}
		System.out.println("Sum Fi: " + sum);
		System.out.println("");
		int[] normalized3 = new int[100];
		int normalized3pos = 0;
		for (int i = 0; i < normalized2len; i++) {
			double fin = normalized2[1][i] * 10.0 / sum;
			int finRounded = (int) Math.round(fin);
			for (int j = 0; j < finRounded; j++) {
				normalized3[normalized3pos] = normalized2[0][i];
				normalized3pos++;
			}
		}
		
		int normalized3len = normalized3pos;
		
		System.out.print("Hasil Normalisasi ChainCode : ");
		
		int sum2 = 0;
		for (int i = 0; i < normalized3len; i++) {
//			sum2 = sum2 + i;
			System.out.print(normalized3[i]);
		}
		
		System.out.println("");
		//if (normalized3len > 10) normalized3len--;
        for (int i = 0; i < normalized3len; i++) {
			System.out.print(normalized3[i]);
		}
    
		System.out.println("");
	
		System.out.println(normalized3len);
//		System.out.println("");
		
//		int sum1 = 0;
//		int i;
//		for ( i = 0; i<normalized3len;i++){
//			sum1 = sum+i;
//			System.out.print(i);
//		}
	
		
		
	}
}



