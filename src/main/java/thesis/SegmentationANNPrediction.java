package thesis;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import ArabicOCR.ArabicOCR;
import image.BinaryImageShell;

public class SegmentationANNPrediction {

	public static void main(String[] args) throws IOException {
		File fileLocation = new File("D:\\filetestingsegmentation\\");
		File input = new File(fileLocation, "test.png");
		
//		String[] expectedResult = { "alif"};
		//test7
//		String[] expectedResult = { "alif", "lam", "kha" , "ba", "za", "tho", 
//									"ain", "mim", "habesar", "alif", "lam",
//									"ha", "lam", "waw"};
		//test
		String[] expectedResult = { "alif", "lam", "ain" , "alif", "tho", "fa", "tamarbuto", 
									"fa", "ya", "alif", "lam", "ha",
									"ya", "alif", "tamarbuto"};
		//test2
//		String[] expectedResult = { "ain", "mim", "lam" , "waw", "alif", "lam", "dal", "ya", 
//									"fa", "ya", "alif", "lam", "mim",
//									"kaf" , "ta", "ba", "unknown"};
		//test4
//		String[] expectedResult = { "alif", "lam", "ha" , "fa", "alif", "dzo", "ain", "lam", 
//									"ya", "sad", "unknown", "ha", "ta",
//									"kaf" , "unknown", "ha", "ya" , "alif", "tamarbuto", "tho", 
//									"ya", "ba", "tamarbuto"};
		BinaryImageShell image = new BinaryImageShell(input);
		List<File> segmentFiles = ArabicOCR.segmentize(image, fileLocation);
		System.out.println(segmentFiles);

		Thinning1 thinning1 = new Thinning1();
		List<File> thinnedFiles = thinning1.thinning(fileLocation, segmentFiles);

		FeatureExtraction featureextraction = new FeatureExtraction();
		featureextraction.extractfeatures(new File(fileLocation, "zhangsuen"), thinnedFiles);

		ArabicPrediction arabicprediction = new ArabicPrediction();
		String[] prediction = arabicprediction
				.arabicPrediction(new File(fileLocation, "zhangsuen\\zhangsuen.Features.json"), false);
		System.out.println("Expected result: " + Arrays.asList(expectedResult));
		System.out.println("Prediction: " + Arrays.asList(prediction));
		System.out.println("Matches: " + expectedResult.equals(prediction));
		
		int correctCount = 0;
		for (int i = 0; i < expectedResult.length; i++) {
			String expected = expectedResult[i];
			String predicted = i < prediction.length ? prediction[i] : "";
			if (expected.equals(predicted)) {
				System.out.println(i + " - " + expected + " -> " + predicted + " (OK)");
				correctCount++;
			} else {
				System.out.println(i + " - " + expected + " -> " + predicted + " (WRONG)");
			}
		}
		double accuracy = correctCount * 1.0 / expectedResult.length;
		System.out.println("Accuracy: " + accuracy);

	}
}
