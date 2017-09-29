
import model.Point;

public class Matrix2d_2 {

	static int[][] matrix() {
    	
        int [][] gambar ={
           
        	{0,0,0,0,0,0,}, 
            {0,1,0,1,0,0,},    
            {0,0,1,0,1,0,}, 
            {0,0,1,0,1,0,}, 
            {0,0,1,0,1,0,}, 
            {0,0,1,0,1,0,}, 
            {0,0,0,1,0,0,}, 
            {0,0,0,0,0,0,}, 
            };
        	int i, j;   
        	for ( i=0;i<8; i++){        
        		for ( j=0;j<6; j++){      
             System.out.print(gambar[i][j]+" ");
             
            // System.out.print(i + "" + j + " ");
            }
        System.out.println("");         
        }
        System.out.println("");
        return gambar;
    }
	
	static public void main(String args[]) throws Exception 
	   {
		   int [][]chaincode =  Matrix2d_2.matrix();
		   ChainCode c = new ChainCode ();
		   String chain_1 = c.chain(chaincode);
		  System.out.println("Hasil Chain Code adalah :");
		   System.out.println(chain_1);
	   }
 
 	static class ChainCode {

		     String chain(int[][] input) {
		        String result = "";
		        boolean done = false; //selagi masih ada yg ditelusuri dia lanjut terus
		        Point p = findFirstPixel(input); // mencari titik hitam pertama, buat objek p 
		        								//yang isinya hasil dari fungsi findFirstPixel
		        
		        if (p != null) {
		            Point next = p; //bikin titik namanya next isinya sama dengan p
		            int x = p.getX(), y = p.getY();//ambil titik koordinat p

		            while (!done) { // ketika done true maka berhenti
		                int[] n = neighbors(input, next);		       
		                int total = sumIntArray(n);
		                
		                if (total == 0) {
		                    input[y][x] = 0;
		                    result += "0";
		                    done = true;
		                } else {
		                    int direction = 0;

		                    for (int i = 0; i < n.length; i++) {
		                        if (n[i] == 1) {
		                            direction = i + 1;
		                            break;
		                        }
		                    }

		                    result += "" + direction;

		                    input[y][x] = 0;
		                    next = decider(next, direction); //fungsi decider untuk menggerakkan
		                    x = next.getX();
		                    y = next.getY();
		                }
		            }
		        }

		        return result;
		    }

		    // finds the first foreground pixel that has only one neighbor
		    // if there is none, take the first foreground pixel you meet
		    private Point findFirstPixel(int[][] input) {
		        Point result = null;
		        boolean firstPixelFound = false;

		        for (int y = 1; y < input.length - 1; y++) {
		            for (int x = 1; x < input[y].length - 1; x++) {
		                if (input[y][x] == 1) {
		                	//System.out.println(input[y][x]);
		                    int[] n = neighbors(input, new Point(x, y));
		                    int total = sumIntArray(n);
		                    //System.out.println(total);

		                    if (total == 1) {
		                        result = new Point(x, y);
		                       // System.out.println(x+ " " +y);
		                        firstPixelFound = true;
		                     
		                        break;
		                    }
		                }
		            }
		            if (firstPixelFound) {
		                break;
		            }
		        }

		        if (!firstPixelFound) {
		            for (int y = 1; y < input.length - 1; y++) {
		                for (int x = 1; x < input[y].length - 1; x++) {
		                    if (input[y][x] == 1) {
		                        result = new Point(x, y);
		                        break;
		                    }
		                }
		                if (result != null) {
		                    break;
		                }
		            }
		        }

		        return result;
		    }

		    private int[] neighbors(int[][] input, Point p) { //mencari tetangga
		        try {
		            int x = p.getX();
		            int y = p.getY();
		            int[] result = new int[]{    //arah tetangga dijelaskan dicatatan aina
		                    input[y - 1][x - 1],
		                    input[y - 1][x],
		                    input[y - 1][x + 1],
		                    input[y][x + 1],
		                    input[y + 1][x + 1],
		                    input[y + 1][x],
		                    input[y + 1][x - 1],
		                    input[y][x - 1]
		            };

		            return result;
		        } catch (Exception ex){
		            return new int[]{};
		        }
		        
		    }

		    private int sumIntArray(int[] input) { //menjumlahkan tetangga
		        int result = 0;

		        for (int i : input) {
		            result += i;
		        }

		        return result;
		    }

		    private Point decider(Point p, int input) { //untuk menggerakkan
		        Point result;
		        int x = p.getX();
		        int y = p.getY();

		        switch (input) {
		            case 1:
		                x--;
		                y--;
		                break;
		            case 2:
		                y--;
		                break;
		            case 3:
		                x++;
		                y--;
		                break;
		            case 4:
		                x++;
		                break;
		            case 5:
		                x++;
		                y++;
		                break;
		            case 6:
		                y++;
		                break;
		            case 7:
		                x--;
		                y++;
		                break;
		            case 8:
		                x--;
		                break;
		        }

		        result = new Point(x, y);
		        return result;
		    }
		}
	   }


