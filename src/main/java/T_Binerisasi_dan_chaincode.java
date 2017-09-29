import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import model.Point;
import javax.imageio.ImageIO;

public class T_Binerisasi_dan_chaincode {

	 static int[][] binerisasi(String fileInput, int threshold) {
	      try {
	    	  File input = new File(fileInput);
	          BufferedImage image = ImageIO.read(input);
	          int width = image.getWidth();
	          int height = image.getHeight();
	          int[][] gambar = new int [width][height];
	          
	         for(int i=0; i<height; i++){
	         
	            for(int j=0; j<width; j++){
	            
	               Color c = new Color(image.getRGB(j, i));
	               int grayscale = (int) (c.getRed() * 0.299) + (int) (c.getBlue()*0.114) +(int) (c.getGreen()*0.587);
	               if (grayscale < threshold)
	               {
	            	   gambar[j][i] = 1;
	               } else 
	               {
	            	   gambar [j][i] = 0;
	               }
	               System.out.print(gambar[j][i]);
	            }
	            System.out.println();
	         }
	         System.out.println();
	         
	         return gambar;
	         
	      } catch (Exception e) {
	    	  return null;
	      }
	   }
	   
	   static public void main(String args[]) throws Exception 
	   {
		   //T_Binerisasi_dan_chaincode.binerisasi("D:\\lam.PNG", 127);
		   int [][]chaincode =  T_Binerisasi_dan_chaincode.binerisasi ("D:\\Thin\\a_terpisah2.bmp", 127);
		   ChainCode c = new ChainCode ();
		   String chain_1 = c.chain(chaincode);
		   System.out.println(chain_1);
	   }
	   
 static class ChainCode {

		     String chain(int[][] input) {
 		        String result = "";
		        boolean done = false; //selagi masih ada yg ditelusuri dia lanjut terus
		        Point p = findFirstPixel(input); // mencari titik hitam pertama
		        
		        if (p != null) {
		            Point next = p; //bikin titik namanya next isinya sama dengan p
		            int x = p.getX(), y = p.getY();//ambil titik koordinat p

		            while (!done) { // ketika done true
//		            	System.out.println(next.getX() + " " + next.getY());
		                int[] n = neighbors(input, next);
//		                for(int i = 0; i < n.length; i++) {
//		                	System.out.print(n[i]);
//		                }
//		                System.out.println();
		                
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
		                    next = decider(next, direction);
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
		                    int[] n = neighbors(input, new Point(x, y));
		                    int total = sumIntArray(n);

		                    if (total == 1) {
		                        result = new Point(x, y);
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

		    private int[] neighbors(int[][] input, Point p) {
		        try {
		            int x = p.getX();
		            int y = p.getY();
		            int[] result = new int[]{
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

		    private int sumIntArray(int[] input) {
		        int result = 0;

		        for (int i : input) {
		            result += i;
		        }

		        return result;
		    }

		    private Point decider(Point p, int input) {
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
