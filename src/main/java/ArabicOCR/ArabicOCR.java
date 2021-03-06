/*
 * 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ArabicOCR;

import HMMFInal.Const;
import HMMFInal.*;
import image.BinaryImageShell;
import image.MainbodySOSet;
import image.classifier.FeatureExtraction;
import image.segmentator.Hilditch;
import image.segmentator.SegmentatorChar;
import image.segmentator.SegmentatorSubword;
import java.awt.Color;
import java.awt.image.RenderedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.ChainCode;
import utils.STDChainCode;

/**
 *
 * 
 */
public class ArabicOCR {
	private static final Logger LOG = LoggerFactory.getLogger(ArabicOCR.class);

	public static void getBoundaryFt(BinaryImageShell main, double[] distribution) {
		/* Boundary Ft. */
		ArrayList<Integer> boundaryChainCode = FeatureExtraction.chainCodeBoundary(main);
		double perimeter = FeatureExtraction.perimeterLength(boundaryChainCode);
		double perimeterToDiag = FeatureExtraction.perimeterToDiagonalRatio(perimeter, main.getWidth(),
				main.getHeight());
		double compactness = FeatureExtraction.compactnessRatio(perimeter,
				(int) distribution[FeatureExtraction.ID_ACRE]);
		double bendingEnergy = FeatureExtraction.bendingEnergy(perimeter, boundaryChainCode);
	}

	public static void getAnother(BinaryImageShell main) {
		/* Distribution */
		BinaryImageShell envelopedImageOri = main.clone();
		envelopedImageOri.cropEnvelope();
		envelopedImageOri.updateImage();
		double[] distribution = FeatureExtraction.distribution(envelopedImageOri);
		/* Aspect Ratio */
		double aspectRatio = FeatureExtraction.aspectRatio(envelopedImageOri);
		getBoundaryFt(main, distribution);
		/* Loop */
		int loop = FeatureExtraction.countLoop_byCC(main);
	}

	/**
	 * Memproses karakter.
	 * @param main
	 * @param mainWithSec
	 * @param mainThin
	 * @param segCharacter
	 * @param baseLine
	 * @param j
	 * @return Array berisi features untuk HMM. Format: 6 karakter pertama adalah jumlah loop. (misal: 000000 atau 111111)
	 * 		diikuti dengan chaincode tapi start-from-0.
	 */
	public static int[] process3Image(BinaryImageShell main, BinaryImageShell mainWithSec, BinaryImageShell mainThin,
			SegmentatorChar segCharacter, int baseLine, int j) {
		/* Interest Point */
		int[][] interestPoint = FeatureExtraction.interestPoint(mainThin);
		/* Secondary Object */
		int[][] secCount = FeatureExtraction.defineSecObject(segCharacter.getSecondaries(j), baseLine);
		// chain code
		int loop = FeatureExtraction.countLoop_byCC(main);
		int[][] arrayImage = ChainCode.imageToArray(mainThin.getBufferedImage());
		List<String> chains = ChainCode.multipleChain(arrayImage);
		String allChain = "";
		for (String chain : chains) {
			allChain += chain;
		}
		List<String> lst = new ArrayList<>();
		lst.add(allChain);

		STDChainCode s = new STDChainCode(30, '8');
		chains = s.standarizeModel(lst);
		allChain = "";
		for (String chain : chains) {
			allChain += chain;
		}
		// preparing chain
		System.out.println("    Process char " + j + " " + main.getName() + ":" + allChain);
		int[] o = new int[allChain.length()];
		for (int iterate = 0; iterate < o.length; iterate++) {
			o[iterate] = Integer.parseInt(allChain.substring(iterate, iterate + 1)) - 1;
		}
		int[][] finalchain = { o };
		DataTrain d = new DataTrain(interestPoint, secCount, finalchain, loop);
		int[] train = d.getDataTrain(1);
		return train;
	}

	public static int[] processCharacter(SegmentatorChar segCharacter, int j, int baseLine) {
		BinaryImageShell main = segCharacter.getChar_plain(j);
		BinaryImageShell mainWithSec = segCharacter.getChar_withSecondary(j);
		BinaryImageShell mainThin = segCharacter.getChar_thin(j);
		return process3Image(main, mainWithSec, mainThin, segCharacter, baseLine, j);
	}

	/**
	 * segmentasi huruf untuk satu kesambungan
	 * 
	 * @param meso
	 * @param segSubword
	 * @param i
	 * @param fileLocation
	 * @return
	 * @throws Exception
	 */
	public static List<File> segmentizeSubword(MainbodySOSet meso, SegmentatorSubword segSubword, int i,
			File fileLocation) throws Exception {
		final List<File> segmentFiles = new ArrayList<>();
		SegmentatorChar segCharacter = new SegmentatorChar(meso);
		segCharacter.zidouri();
		int size = segCharacter.getSegmentSize();
		for (int j = 0; j < size; j++) {
			try {
				BinaryImageShell charWithSec = segCharacter.getChar_withSecondary(j);
				String fname = String.format("%02d-%02d.png", i, j);

				File segmentFile = new File(fileLocation, "segments\\" + fname);
				segmentFile.getParentFile().mkdirs();
				ImageIO.write(charWithSec.getBufferedImage(), "png", segmentFile);
				segmentFiles.add(segmentFile);
			} catch (Exception e) {
				LOG.error("Cannot segmentize {}", meso, e);
			}
		}
		return segmentFiles;
	}

	/**
	 * 
	 * @param meso
	 * @param segSubword
	 * @return Subword prediction, eg. "alif+lam"
	 * @throws Exception
	 */
	public static String processSubword(MainbodySOSet meso, SegmentatorSubword segSubword) throws Exception {
		System.out.println("   Predicting subword " + meso + " ...");
		SegmentatorChar segCharacter = new SegmentatorChar(meso);
		segCharacter.zidouri();
		int size = segCharacter.getSegmentSize();
		ArrayList<String> results = new ArrayList<>();
		for (int j = 0; j < size; j++) {
			String currentSim = "";
			double highest = Double.NEGATIVE_INFINITY;
			int[] data = processCharacter(segCharacter, j, segSubword.getBaseline());
			
			File models = new File(Const.MODEL_DATA);
			for (File model : models.listFiles()) {
				if (model.isDirectory()) {
					// take the name of the folder
					String name = model.getName();
					// load every file inside that folder
					for (File modelFile : model.listFiles()) {
						// compute similarity
						if (!modelFile.isDirectory()) {
							String fullPath = modelFile.getAbsolutePath();
							HiddenMarkov currentModel = Serialize.deserializeObject(fullPath);
							// int[] o = data[data.length-1];
							double viterbi = currentModel.viterbi(data);
							if (viterbi > highest) {
								highest = viterbi;
								currentSim = name;
							}
						}
					}
				}
			}

			System.out.print("    Char " + meso + " " + j + ": ");
			for (int k = 0; k < data.length; k++) {
				System.out.print(data[k]);
			}
			System.out.println(" -> " + currentSim);
			results.add(currentSim);
		}
		
		final String currentSubword = results.stream().collect(Collectors.joining("-"));
		System.out.println(String.format("   Segmented %s -> %s",
				meso, currentSubword));
		
		return currentSubword;
	}

	public static int[][] train(BinaryImageShell image) {
		SegmentatorSubword segSubword = new SegmentatorSubword(image);
		segSubword.blockSegment();
		segSubword.groupBlocks();
		MainbodySOSet[] subwordBlocks = segSubword.getAllSegments();
		MainbodySOSet mso = segSubword.getSegment(0);
		SegmentatorChar segCharacter = new SegmentatorChar(mso);
		BinaryImageShell main = segCharacter.getInputImage_plain();
		BinaryImageShell mainWithSec = segCharacter.getInputImage_withSecondary();
		BinaryImageShell mainThin = segCharacter.getInputImage_thin();
		segCharacter.zidouri();
		// int[][] data = new int[segCharacter.getSegmentSize()][];
		// for (int j=0;j<segCharacter.getSegmentSize();j++) {
		int[] odata = processCharacter(segCharacter, 0, segSubword.getBaseline());
		// data[j] = odata;
		int[][] data = { odata };
		// }
		// int[] o = process3Image(main, mainWithSec, mainThin, segCharacter,
		// segSubword.getBaseline(), 0);
		// data[0] = o;
		// int[] data = processCharacter(segCharacter,0,);
		return data;
	}

	public static int[][] train2(BinaryImageShell image, int j, String lol) {
		// int lol = image.getWidth() + j + image.getHeight() + image.getWxH();
		BinaryImageShell wordImage = image.getImage(j);
		if (wordImage.checkColored())
			return new int[0][];
		try {
			ImageIO.write(wordImage.getBufferedImage(), "bmp",
					new File(HMMFInal.Const.TEST_IMAGES + "/test2/" + lol + "hasil" + j + ".bmp"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		SegmentatorSubword segSubword2 = new SegmentatorSubword(wordImage);
		segSubword2.blockSegment();
		segSubword2.groupBlocks();
		MainbodySOSet[] subwordBlocks2 = segSubword2.getAllSegments();
		MainbodySOSet mso2 = segSubword2.getSegment(0);
		SegmentatorChar segCharacter = new SegmentatorChar(mso2);
		BinaryImageShell main = segCharacter.getInputImage_plain();
		BinaryImageShell mainWithSec = segCharacter.getInputImage_withSecondary();
		BinaryImageShell mainThin = segCharacter.getInputImage_thin();
		segCharacter.zidouri();
		// main.drawLine(mso, true, Color.LIGHT_GRAY);
		int[] odata = process3Image(main, mainWithSec, mainThin, segCharacter, segSubword2.getBaseline(), 0);
		int[][] data = { odata };
		try {
			ImageIO.write(segCharacter.getChar_plain(0).getBufferedImage(), "bmp",
					new File(HMMFInal.Const.TEST_IMAGES + "/test/" + lol + "hasil" + j + ".bmp"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// int[] o = process3Image(main, mainWithSec, mainThin, segCharacter,
		// segSubword.getBaseline(), 0);
		// data[0] = o;
		// int[] data = processCharacter(segCharacter,0,);
		return data;
	}

	// public static segmentate

	/**
	 * Menghitung proporsi prediksi yang benar.
	 * @param s
	 * @param expected
	 * @return
	 */
	public static float test(String s, String expected) {
		String[] lis = s.split("-");
		String[] lis2 = expected.split("-");
		int sumCorrect = 0;
		for (int i = 0; i < lis2.length; i++) {
			if (lis2[i].equals("all"))
				sumCorrect++;
			else if (lis[i].contains(lis2[i]))
				sumCorrect++;
		}
		return (float) sumCorrect / lis2.length;
	}

	/**
	 * 
	 * @param image
	 * @return Prediction result, e.g. "tho+dzo-fa+qaf+waw-tho+dzo"
	 */
	public static String process(BinaryImageShell image) {
		System.out.println("  Processing " + image.getName());
		SegmentatorSubword segSubword = new SegmentatorSubword(image);
		segSubword.blockSegment();
		segSubword.groupBlocks();
		MainbodySOSet[] subwordBlocks = segSubword.getAllSegments();
		MainbodySOSet mso = segSubword.getSegment(0);
		int i = 0;
		String result = "";
		ArrayList<String> results = new ArrayList<>();
		for (MainbodySOSet meso : subwordBlocks) {
			i++;
			try {
				String s = processSubword(meso, segSubword);
				results.add(s);
				System.out.println("   Subword " + i + " -> " + s);
			} catch (Exception ex) {
				LOG.error("Cannot process {}", meso, ex);
			}
		}
		for (int j = 0; j < results.size() - 1; j++) {
			result += results.get(j) + "-";
		}
		// "qafdzalnunnunqafalifalifnundzaldzalnunqafalifalifhadzalsheendzal"
		result += results.get(results.size() - 1);
		System.out.println("  Processed " + image.getName() + " -> " + result);
		return result;
	}

	/**
	 * melakukan segmentasi zidouri untuk satu kalimat
	 * 
	 * @param image
	 * @param fileLocation
	 * @return
	 * 
	 */
	public static List<File> segmentize(BinaryImageShell image, File fileLocation) {
		SegmentatorSubword segSubword = new SegmentatorSubword(image);
		segSubword.blockSegment();
		segSubword.groupBlocks();
		MainbodySOSet[] subwordBlocks = segSubword.getAllSegments();
		int i = 0;
		ArrayList<File> results = new ArrayList<>();
		for (MainbodySOSet meso : subwordBlocks) {
			i++;
			try {
				List<File> subSegments = segmentizeSubword(meso, segSubword, i, fileLocation);
				results.addAll(subSegments);
			} catch (Exception ex) {
				LOG.error("Cannot process {}", meso, ex);
			}
		}
		return results;
	}
}
