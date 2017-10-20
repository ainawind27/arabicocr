package thesis;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.Zhangsuen2;
import java.util.ArrayList;
import java.util.List;

public class Thinning1 {
	public List<File> listFiles(File folder){
		List<File> queuedFiles = new ArrayList<File>();

		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			File file = listOfFiles[i];
			if (file.isFile() && file.getName().toLowerCase().endsWith(".png")) {
				queuedFiles.add(file);
			}
		}
		return queuedFiles;
	}
	
	public List<File> thinning(File folder, List<File> queuedFiles) throws IOException {
		final List<File> thinnedFiles = new ArrayList<File>();
		File targetFolder = new File(folder, "zhangsuen");
		targetFolder.mkdirs();
		for (int i = 0; i < queuedFiles.size(); i++) {
			BufferedImage image = ImageIO.read(
					queuedFiles.get(i)
				);

			// load the model image
			Zhangsuen2 thinning = new Zhangsuen2(image, queuedFiles.get(i).getName());
			BufferedImage thinImage = thinning.doZhangSuen();

			File thinnedFile = new File(targetFolder, queuedFiles.get(i).getName().replace(".", "_steintiford."));
			ImageIO.write(thinImage, "png", thinnedFile);
			thinnedFiles.add(thinnedFile);

			System.out.println("Written " + thinnedFile.getPath());
		}
		return thinnedFiles;
	}
	
	public void thinningZhangSuen(File folder) throws IOException {
		List<File> queuedFiles = listFiles(folder);
		thinning(folder, queuedFiles);
	}

	public static void main(String[] args) throws IOException {
		String fileLocation = "D:\\filetraining\\";
//		String fileLocation = "D:\\test\\";
//		String fileLocation = "D:\\filetestingsegmentation\\hasilthinning\\a";
		File folder = new File(fileLocation);
		Thinning1 thinning = new Thinning1();
		thinning.thinningZhangSuen(folder);
	}

}
