
public class arrray2d_indeks {

public static void main(String[]args) {
    	
        int [][] gambar ={
           
        	{0,0,0,0,0,0,0,0}, 
            {0,1,0,0,0,0,0,0},    
            {0,0,1,1,1,1,0,0}, 
            {0,1,0,0,0,0,1,0}, 
            {0,0,1,1,1,1,0,0}, 
            {0,0,0,0,0,0,0,0}, 
            };
        	int i, j;   
        	for ( i=0;i<6; i++){        
        		for ( j=0;j<8; j++){    
        			System.out.print(gambar[i][j]+" ");
        			//System.out.print("isi" +i+ "" + j + " : " +gambar[j][i]+" "); 
            // System.out.print(i + "" + j + " ");
            }
        System.out.println("");         
        }
//        	System.out.println("");
//        	
//        	for ( i=0;i<gambar.length; i++){        
//        		for ( j=0;j<gambar.length; j++){    
//        			System.out.print(gambar[i][j]+" ");
//        		}
//        		 System.out.println("");
   //     	}
        	System.out.println("array input "); 
        	for ( i=0;i<gambar.length; i++){        
        		for ( j=0;j<gambar[i].length; j++){    
        			//System.out.print(gambar[i][j]+ " ");
        			  System.out.print(i + "" + j + " ");
        			//System.out.print(i+j+" ");
        			//System.out.print("isi" +i+ "" + j + " : " +gambar[j][i]+" "); 
            // System.out.print(i + "" + j + " ");
            }
        System.out.println("");         
        }
        	
//        	System.out.println(""); 
//        	for ( i=1;i<gambar.length; i++){        
//        		for ( j=1;j<gambar.length; j++){    
//        			  System.out.print(i + "" + j + " ");
//        			//System.out.print(i+j+" ");
//        			//System.out.print("isi" +i+ "" + j + " : " +gambar[j][i]+" "); 
//            // System.out.print(i + "" + j + " ");
//            }
//        System.out.println("");         
//        }
	}
}