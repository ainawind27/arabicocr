import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;



public class image_to_matrix {

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
		            	   gambar[j][i] = 1; //1 adalah nilai hitam
		               } else 
		               {
		            	   gambar [j][i] = 0; //0 adalah nilai putih
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
		   image_to_matrix.binerisasi("D:\\lam.PNG",127);
		   //int [][]chaincode =  T_Binerisasi_dan_chaincode_1.binerisasi ("D:\\lam.PNG", 127);
	   }
}