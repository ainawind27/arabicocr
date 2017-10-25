import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Thinning4 {
	public static void main(String[]args) throws IOException {
		String fileLocation = "D:\\font microsoft sans serif - Copy\\";
		List<String> file_Name = new ArrayList<String>();
		
		File folder = new File(fileLocation);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
		  File file = listOfFiles[i];
		  if (file.isFile() && file.getName().endsWith(".PNG")) {
		   file_Name.add(file.toPath().toString());
		    
		  } 
		}
		
		for (int i =0;i<file_Name.size();i++){
		
        BufferedImage image = ImageIO.read(new File (file_Name.get(i)));
		
        // load the model image
		Thinning_Zhang_Suen thinning = new Thinning_Zhang_Suen(image);
        BufferedImage thinImage = thinning.doZhangSuen();
        
        ImageIO.write(thinImage, "png", new File(fileLocation + "ZZ_Hasil"+i+".PNG"));
        
        System.out.println("X");
		}
	}

}
