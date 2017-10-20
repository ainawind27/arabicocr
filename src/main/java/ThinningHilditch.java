import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.Zhangsuen2;
import image.BinaryImageShell;
import image.segmentator.*;
import java.util.ArrayList;
import java.util.List;

public class ThinningHilditch {

	public static void main(String[] args) throws IOException {
		String fileLocation = "D:\\Thin\\";
		List<File> queuedFiles = new ArrayList<File>();

		File folder = new File(fileLocation);
		// deklarasi variable listOfFiles bertipe array of File, diisi dengan pemanggilan fungsi listFiles pada objek folder
		File[] listOfFiles = folder.listFiles();
		
		for (int i = 0; i < listOfFiles.length; i++) {
			File file = listOfFiles[i];
			if (file.isFile() && file.getName().toLowerCase().endsWith(".png")) {
				queuedFiles.add(file);
			}
		}

		File targetFolder = new File(folder, "hilditch");
		targetFolder.mkdirs();
		for (int i = 0; i < queuedFiles.size(); i++) {
			// deklarasi variable image bertipe BufferedImage dengan cara memanggil fungsi read pada tipe ImageIO
			// dengan parameter berupa objek bertipe File dengan parameter queuedFiles.get(i).getPath()
//			BufferedImage image = 
//					ImageIO.read(
//							// ambil File dengan indeks i dari queuedFiles
//							// lalu ubah menjadi String dengan cara memanggil method .getPath()
//							new File(queuedFiles.get(i).getPath())
//					);
			BinaryImageShell image = new BinaryImageShell(queuedFiles.get(i) );
			Hilditch thinning = new Hilditch(image);
			BinaryImageShell thinImage = thinning.getSkeleton();
//			
//			A b =
//					C.d(
//							e0
//						);
			

			// load the model image
//			Zhangsuen2 thinning = 
//					new Zhangsuen2(
//							image, 
//							queuedFiles.get(i).getName()
//					);
//			A b =
//					C.d(
//							e0,
//							e1
//						);

			
//			BufferedImage thinImage = thinning.doZhangSuen();

			File thinnedFile = new File(targetFolder, queuedFiles.get(i).getName().replace(".", "_hilditch."));
			ImageIO.write(thinImage.getBufferedImage(), "png", thinnedFile);

			System.out.println("Written " + thinnedFile.getPath());
		}
	}
}
