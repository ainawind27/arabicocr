package thesis;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import ArabicOCR.ArabicOCR;
import image.BinaryImageShell;

public class SegmentationANNPrediction2 {

	public static void main(String[] args) throws IOException {
		File fileLocation = new File("D:\\filetestingsegmentation\\");
		File input = new File(fileLocation, "timesnewroman_lamalif_terpisah.png");
	
		//test4, 68%
//		String[] expectedResult = { "alif", "lam", "ha" , "fa", "alif", "dzo", "ainlam", "ya", 
//									 "sad", "unknown", "ha", "ta",
//									"kaf" , "unknown", "ha", "ya" , "alif", "tamarbuto", "tho", 
//									"ya", "ba", "tamarbuto"};
		//test5, 74%
//		String[] expectedResult = { "alif", "lam", "ha", "fa", "alif", "dzo", "ain", "lam", 
//									"ya", "sad", "unknown", "ha", "ta",
//									"kaf" , "unknown", "ha", "ya", "alif", 
//									"tamarbuto", "tho", "ya", "ba", "tamarbuto"};

		//test8, 76%
		String[] expectedResult = { "fa", "alif", "kaf" , "habesar", "tamarbuto", "dal", "waw", "ra", 
									"ya", "alif", "nun", "alif", "lamalif",
									"dzal" , "waw", "alif", "qaf" , "jim", "ya", "dal", 
									"tamarbuto"};
//		//test9, 
//		String[] expectedResult = { "ya", "za", "dal" , "habesarra", "alif", 
//									"lamalif", "ra", 
//									"za", "fa", "ya", "ha", "qaf", "waw", "lam",
//									"alif", "lamalif", "ra", "za"};

//		//test11, 71%
//		String[] expectedResult = { "alif", "lam", "mim" , "ain", "lam", 
//									"mim", "ya", 
//									"nun", "ya", "ain", "lam", "mim", "waw",
//									"nun"};
//		//test12, 82%
//		String[] expectedResult = { "alif", "lam", "qaf" , "mim", "ra", 
//									"ta", "ba", 
//									"dal", "waw", "jim", "mim", "ya", "lam",
//									"tamarbuto", "jim", "dal", "alif"};
		
//		//test13, hasil 71%
//		String[] expectedResult = { "jim", "dal", "tamarbuto" , "alif", "lamalif", 
//									"ra", "za", 
//									"alif", "lam", "mim", "tho", "ba", "waw",
//									"jim"};
		//test0, hasil 79%
//		String[] expectedResult = { "alif", "lamalif", "ha" , "ta", "ra", 
//									"alif", "mim", 
//									"alif", "lam", "mim", "ta", "ba", "alif",
//									"dal", "lam", "ba", "ya", "nun", "alif",
//									"lamalif", "dal", "ya", "alif", "nun"};
		//test16, hasil 76%
//		String[] expectedResult = { "ta", "ta", "kha" , "lam", "ya", 
//									"alif", "ba", 
//									"dal", "alif", "fa", "ya", "alif", "lam",
//									"ha", "ya", "alif", "tamarbuto"};
//		
//		//test17, hasil 72%
//		String[] expectedResult = { "ya", "ta", "kaf" , "lam", "mim", 
//									"ba", "ba", 
//									"tho", "hamzah", "mim", "fa", "habesar", "waw",
//									"mim", "tamarbuto", "ha", "ta", "ya"};
		//test18, hasil 76%
//		String[] expectedResult = { "alif", "nun", "ta" , "dzo", "ra", 
//									"alif", "ya", 
//									"mim", "ha", "alif", "kaf", "mim", "tamarbuto",
//									};
		BinaryImageShell image = new BinaryImageShell(input);
		List<File> segmentFiles = ArabicOCR.segmentize(image, fileLocation);
		System.out.println(segmentFiles);

//		Thinning1 thinning1 = new Thinning1();
//		List<File> thinnedFiles = thinning1.thinning(fileLocation, segmentFiles);
//
//		FeatureExtraction featureextraction = new FeatureExtraction();
//		featureextraction.extractfeatures(new File(fileLocation, "zhangsuen"), thinnedFiles);

//		ArabicPrediction arabicprediction = new ArabicPrediction();
//		String[] prediction = arabicprediction
//				.arabicPrediction(new File(fileLocation, "zhangsuen\\zhangsuen.Features.json"), false);
//		System.out.println("Expected result: " + Arrays.asList(expectedResult));
//		System.out.println("Prediction: " + Arrays.asList(prediction));
//		System.out.println("Matches: " + expectedResult.equals(prediction));
//		
//		int correctCount = 0;
//		for (int i = 0; i < expectedResult.length; i++) {
//			String expected = expectedResult[i];
//			String predicted = i < prediction.length ? prediction[i] : "";
//			if (expected.equals(predicted)) {
//				System.out.println(i + " - " + expected + " -> " + predicted + " (OK)");
//				correctCount++;
//			} else {
//				System.out.println(i + " - " + expected + " -> " + predicted + " (WRONG)");
//			}
//		}
//		double accuracy = correctCount * 1.0 / expectedResult.length;
//		System.out.println("Accuracy: " + accuracy);

	}
}
