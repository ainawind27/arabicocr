import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Binerisasi_1 {
   public static int[][] binerisasi(String fileInput, int threshold) {
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
//               if (grayscale < threshold)
//               {
//            	   image.setRGB(j, i, Color.BLACK.getRGB());
//               } else
//               {
//            	   image.setRGB(j, i, Color.WHITE.getRGB());
//               }
               
               //batas belajar binerisasi
               
//               int red = (int)(c.getRed() * 0.299);
//               int green = (int)(c.getGreen() * 0.587);
//               int blue = (int)(c.getBlue() *0.114);
//               Color newColor = new Color(red+green+blue,red+green+blue,red+green+blue);
//               
//               image.setRGB(j,i,newColor.getRGB());
            }
            System.out.println();
         }
         
         
//         File ouptut = new File("D:\\grayscale.jpg");
//         ImageIO.write(image, "jpg", ouptut);
         
         
         return gambar;
         
      } catch (Exception e) {
    	  return null;
      }
   }
   
   static public void main(String args[]) throws Exception 
   {
      Binerisasi_1.binerisasi("D:\\digital_image_processing.jpg", 127);
   }
}