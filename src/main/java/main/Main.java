package main;

import ArabicOCR.ArabicOCR;
import HMM2.HMM;
import HMMFInal.Const;
import image.BinaryImageShell;
import image.MainbodySOSet;
import image.classifier.FeatureExtraction;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import org.junit.internal.runners.model.EachTestNotifier;

import serialize.ReadObject;
import serialize.WriteObject;
import utils.ChainCode;
import utils.ImageTransformation;
import utils.STDChainCode;
import utils.Thinning;

public class Main {

	public static BufferedImage whiteBorder(BufferedImage input) {
		int height = input.getHeight();
		int width = input.getWidth();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (x == 0 || y == 0 || y == height - 1 || x == width - 1) {
					input.setRGB(x, y, 0xffffffff);
				}
			}
		}

		return input;
	}

	public static float HMMtest() {
		File dataset = new File(HMMFInal.Const.TEST_IMAGES + "100%satu");
		System.out.println("Testing folder " + dataset);
		int total = 0;
		float correctness = 0.0f;
		for (File file : dataset.listFiles()) {
			System.out.println(" Testing " + file.getName() + " ...");
			String expected_result = file.getName().replace(".png", "");
			BinaryImageShell image = new BinaryImageShell(file.getAbsolutePath());
			String result = ArabicOCR.process(image);
			int sum = expected_result.split("-").length;
			total += sum;
			correctness += (ArabicOCR.test(result, expected_result) * sum);
			float eachIteration = (float) correctness / total;
			System.out.println(" " + file.getName() + ": Expected=" + expected_result + " -> Actual=" + result +
					" (running correctness: " + eachIteration + ")");
		}
		// System.out.println(total);
		return (float) correctness / total;
	}

	private static void copyFolderLocalMax() {
		File dataset = new File(Const.MODEL_DATA);
		InputStream inStream = null;
		OutputStream outStream = null;
		for (File dir : dataset.listFiles()) {
			if (dir.isDirectory()) {
				String name = dir.getName();
				File model = new File(Const.MODEL_DATAMAX + name);
				if (!model.exists()) {
					model.mkdir();
				}
				for (File file : dir.listFiles()) {
					String name2 = file.getName();
					File lastfinal2 = new File(model.getAbsolutePath() + "/" + name2);
					if (!lastfinal2.exists()) {
						try {
							lastfinal2.createNewFile();
						} catch (IOException ex) {
							Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
						}
					}
					try {
						inStream = new FileInputStream(file);
						outStream = new FileOutputStream(lastfinal2);

						byte[] buffer = new byte[1024];

						int length;
						// copy the file content in bytes
						while ((length = inStream.read(buffer)) > 0) {
							outStream.write(buffer, 0, length);
						}

						inStream.close();
						outStream.close();
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			}
		}
	}

	private static void copyFolder() {
		File dataset = new File(Const.MODEL_DATA);
		InputStream inStream = null;
		OutputStream outStream = null;
		for (File dir : dataset.listFiles()) {
			if (dir.isDirectory()) {
				String name = dir.getName();
				File model = new File(Const.MODEL_DATAMAX + name);
				if (!model.exists()) {
					model.mkdir();
				}
				for (File file : dir.listFiles()) {
					String name2 = file.getName();
					File lastfinal2 = new File(model.getAbsolutePath() + "/" + name2);
					if (!lastfinal2.exists()) {
						try {
							lastfinal2.createNewFile();
						} catch (IOException ex) {
							Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
						}
					}
					try {
						inStream = new FileInputStream(file);
						outStream = new FileOutputStream(lastfinal2);

						byte[] buffer = new byte[1024];

						int length;
						// copy the file content in bytes
						while ((length = inStream.read(buffer)) > 0) {
							outStream.write(buffer, 0, length);
						}

						inStream.close();
						outStream.close();
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		// testNewChainCode();
		// testAndHMMTrain();
		System.out.println(HMMtest());
		// HMM3.Process.printHMM();
	}

	private static void testAndHMMTrain() throws Exception {
		float correctness = 0.0f;
		int iterasi = 1;
		float max = 0.0f;
		int maxIteration = 0;
		while ((float) correctness < 0.7f && iterasi <= 10) {
			correctness = 0.0f;
			HMMFInal.Process.train();
			// HMMFInal.Process.train2();
			correctness = HMMtest();
			if (correctness > max) {
				max = (float) correctness;
				maxIteration = iterasi;
				if (correctness > 0.6744186)
					copyFolder();
				else
					copyFolderLocalMax();
			}
			iterasi++;
			
		}
		System.out.println("maxIteration: " + maxIteration);
		System.out.println("maxIteration: " + max);

	}

}
