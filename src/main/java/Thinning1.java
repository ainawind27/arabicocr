import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.Zhangsuen2;
import java.util.ArrayList;
import java.util.List;

public class Thinning1 {

	public static void main(String[] args) throws IOException {
		String fileLocation = "D:\\filetesting\\";
		List<File> queuedFiles = new ArrayList<File>();

		File folder = new File(fileLocation);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			File file = listOfFiles[i];
			if (file.isFile() && file.getName().toLowerCase().endsWith(".png")) {
				queuedFiles.add(file);
			}
		}

		File targetFolder = new File(folder, "zhangsuen");
		targetFolder.mkdirs();
		for (int i = 0; i < queuedFiles.size(); i++) {
			BufferedImage image = ImageIO.read(
					queuedFiles.get(i)
				);

			// load the model image
			Zhangsuen2 thinning = new Zhangsuen2(image, queuedFiles.get(i).getName());
			BufferedImage thinImage = thinning.doZhangSuen();

			File thinnedFile = new File(targetFolder, queuedFiles.get(i).getName().replace(".", "_zhangsuen."));
			ImageIO.write(thinImage, "png", thinnedFile);

			System.out.println("Written " + thinnedFile.getPath());
		}
	}

}
