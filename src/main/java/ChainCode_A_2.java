import model.Point;

public class ChainCode_A_2 {

static int[][] matrix() {
    	
        int [][] gambar ={
        		{0,0,0,0,0,0,0,0}, 
                {0,1,0,0,0,0,0,0},    
                {0,0,1,1,1,1,0,0}, 
                {0,1,0,0,0,0,1,0}, 
                {0,0,1,1,1,1,0,0}, 
                {0,0,0,0,0,0,0,0}, 
                };
        	int i, j;   
        	for ( i=0;i<gambar.length; i++){        
        		for ( j=0;j<gambar[i].length; j++){    
        			 System.out.print(gambar[i][j]+" ");
            // System.out.print("isi" +i+ "" + j + " : " +gambar[j][i]+" ");
             
            // System.out.print(i + "" + j + " ");
            }
        System.out.println("");         
        }
       // System.out.println("");
        return gambar;
    }
    
    	static public void main(String args[]) throws Exception 
 	   {
 		   int [][]chaincode =  Chaincodebaru.matrix();
 		   ChainCode c = new ChainCode ();
 		   String chain_1 = c.chain(chaincode);
 		  System.out.println("Hasil Chain Code adalah :");
 		   System.out.println(chain_1);
 	   }
    static class ChainCode{
    	
    	String chain(int [][] input){
    		String result = "";
    		boolean done = false;
    		Point p = findFirstPixel(input);
    		
    		if (p!= null){
    			Point next = p;
    			int x = next.getX(), y = next.getY();
    			
    			while (!done){
    				
    				int [] n = neighbors (input, next);
    				int total = sumIntArray(n);
    				
    				if (total == 0){
    					input [y][x]=0;
    					result += "0";
    					done = true;
    				
    				}else {
    					int direction = 0;
    					
    					for (int i = 0; i<n.length;i++){
    						if (n[i]==1){
    							direction = i+1;
    							break;
    						}
    					}
    					result += "" + direction;
    					
    					input [y][x]=0;
    					next = decider(next, direction);
    					x = next.getX();
    					y = next.getY();
    				}
    			}
    			
    		}
			return result;
    		
    	}

		private Point findFirstPixel(int[][] input) {
			Point result = null;
			boolean firstPixelFound = false;
			
			for (int x = 1; x<input.length-1;x++){
				for (int y =1; y<input[x].length-1;y++){
					if (input [y][x]==1){
						int [] n = neighbors (input, new Point (x,y));
						int total = sumIntArray(n);
						result = new Point (x,y);
						firstPixelFound = true;
						break;
					}
				}
				if (firstPixelFound){
					break;
				}
			}
			if(!firstPixelFound){
				for (int x = 1; x<input.length-1;x++){
					for (int y =1; y<input[x].length-1;y++){
						if (input [y][x]==1){
							result = new Point (x,y);
							break;
						}
					}
					if (result !=null){
						break;
					}
					
				}
			}
			return result;
		}

		private int[] neighbors(int[][] input, Point p) {
			int x = p.getX();
			int y = p.getY();
			int [] result = new int [] {
										input [y - 1][x - 1],
										input [y - 1][x],
										input [y - 1][x + 1],
										input [y][x + 1],
										input [y + 1][x + 1],
										input [y + 1][x],
										input [y + 1][x - 1],
										input [y][x - 1]
										};
			
			return result;
		}

		private int sumIntArray(int[] input) {
			int result = 0;
			for (int i : input){
				result +=i;
			}
			return result;
		}

		private Point decider(Point p, int input) {
			Point result;
			int x = p.getX();
			int y = p.getY();
			
			switch (input){
					case 1 : y--;
							 x--;
							 break;
					case 2 : y--;
							 break;
					case 3 : y--;
							 x++;
							 break;
					case 4 : x++;
							 break;
					case 5 : y++;
							 x++;
							 break;
					case 6 : y++;
							 break;
					case 7 : y++;
							 x--;
							 break;
					case 8 : x--;
							 break;
			}
			result = new Point (x,y);
			
			return result;
		}
    }

}
