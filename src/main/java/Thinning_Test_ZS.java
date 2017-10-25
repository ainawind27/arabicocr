import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Thinning_Test_ZS {
	public static void main(String[]args) throws IOException {
		String fileLocation = "D:\\Thin\\";
		String fileName = fileLocation + "microsoftss_tsa_terpisah.PNG";
		File input = new File(fileName);
        BufferedImage image = ImageIO.read(input);
		
        // load the model image
		Thinning_Zhang_Suen thinning = new Thinning_Zhang_Suen(image);
        BufferedImage thinImage = thinning.doZhangSuen();
        
        ImageIO.write(thinImage, "png", new File(fileLocation + "tsa_terpisah23.png"));
        
        System.out.println("X");
	}
}
