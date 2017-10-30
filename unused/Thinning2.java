import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import image.BinaryImageShell;
import image.segmentator.Hilditch;
import model.Zhangsuen2;

public class Thinning2 {

	public static void main(String[] args) throws IOException {
		String fileLocation = "D:\\Test2\\";
		String fileName = fileLocation + "timesnewroman_sheen_terpisahtr.png";
		File input = new File(fileName);
		// BufferedImage image = ImageIO.read(input);

		// load the model image
		BinaryImageShell image1 = new BinaryImageShell(fileName);
		Thinning_Zhang_Suen thinning = new Thinning_Zhang_Suen(image1);
		// Zhangsuen2 thinning= new Zhangsuen2(image);
		BinaryImageShell thinImage = thinning.getSkeleton();

		ImageIO.write(thinImage.getBufferedImage(), "png", new File(fileLocation + "timesnewroman_sheen_terpisahtr2.png"));

		System.out.println("X");
	}

}
