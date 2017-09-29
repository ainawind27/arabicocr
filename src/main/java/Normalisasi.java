public class Normalisasi {

	static int [] normalizedFinish (String bodyChain){
		// int [] chaincode = new int[]{7,7,7,7,3,1,1,1,2,2,2,
			//	2,2,5,8,3,3,5,3,3,3,3

//};
		int[][] normalized = new int[2][1000];
		
		System.out.println("Nilai Chaincode adalah : ");
	//	for (int i = 0; i<bodyChain.length();i++){
			System.out.print(bodyChain);
			
		//}
		System.out.println("");
		int indeks_cek =0;
		int sama = 0;

		int normalizedpos = 0;
		
		System.out.println("Frekuensi kemunculain setiap angka dalam ChainCode :");
		for (int i =0 ; i<bodyChain.length(); i++)
		{
			if (bodyChain.charAt(indeks_cek)== bodyChain.charAt(i)){
				sama++;
				
				normalized[0][normalizedpos] = Character.getNumericValue(bodyChain.charAt(i));  
				normalized[1][normalizedpos] = sama; 
			}else {
				System.out.print(sama);	
				indeks_cek = i;
				sama = 1;
				normalizedpos++;
				normalized[0][normalizedpos] = Character.getNumericValue(bodyChain.charAt(i));  
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
		
		int [][] normalized2 = new int [2][500];
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
		//System.out.println("Sum Fi: " + sum);
		
		int[] normalized3 = new int[1000];
		int normalized3pos = 0;
		for (int i = 0; i < normalized2len; i++) {
			for (int j = 0; j < normalized2[1][i]; j++) {
				normalized3[normalized3pos] = normalized2[0][i];
				normalized3pos++;
			}
		}
		
		int normalized3len = normalized3pos;
		
		System.out.print("Tampilkan Chaincode sesuai dengan frekuensi terjadinya");
		System.out.println("");
		for (int i = 0; i < normalized3len; i++) {
			System.out.print(normalized3[i]);
		}
		System.out.println("");
		System.out.println("");
		System.out.print("Hasil Normalisasi ChainCode : ");
		int[] normalized4 = new int[10];
		System.out.println("");
        for (int i = 0; i < 10; i++) {
        	normalized4[i] = normalized3[(int) Math.round((i/9.0)*(normalized3len-1))];
			System.out.print(normalized4[i]);
		}
    
		System.out.println("");
		return normalized4;
	}
	
}



