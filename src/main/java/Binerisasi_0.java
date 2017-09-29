import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Binerisasi_0 {

   BufferedImage  image;
   int width;
   int height;
   int threshold=127;
   public Binerisasi_0() {
   
      try {
         File input = new File("D:\\digital_image_processing.jpg");
         image = ImageIO.read(input);
         width = image.getWidth();
         height = image.getHeight();
         
         for(int i=0; i<height; i++){
         
            for(int j=0; j<width; j++){
            
               Color c = new Color(image.getRGB(j, i));
               int grayscale = (int) (c.getRed() * 0.299) + (int) (c.getBlue()*0.114) +(int) (c.getGreen()*0.587);
               if (grayscale < threshold)
               {
            	   image.setRGB(j, i, Color.BLACK.getRGB());
               } else
               {
            	   image.setRGB(j, i, Color.WHITE.getRGB());
               }
            }
         }
         
         File ouptut = new File("D:\\grayscale.jpg");
         ImageIO.write(image, "jpg", ouptut);
         
      } catch (Exception e) {}
   }
   
   static public void main(String args[]) throws Exception 
   {
      Binerisasi_0 obj = new Binerisasi_0();
   }
}