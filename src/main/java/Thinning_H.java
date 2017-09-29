import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Thinning_H {

	public static void main(String[]args) throws IOException {
		String fileLocation = "D:\\";
		String fileName = fileLocation + "ainnn.PNG";
		File input = new File(fileName);
        BufferedImage image = ImageIO.read(input);
		
        // load the model image
		ZhangSuen thinning = new ZhangSuen();
        BufferedImage thinImage = thinning.doZhangSuen();
        
        ImageIO.write(thinImage, "png", new File(fileLocation + "ainnn.PNG"));
        
        System.out.println("X");
	}

}

public class Thinning_H {

	public Thinning_H() {
		// TODO Auto-generated constructor stub
	}

}
