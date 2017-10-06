package thesis;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class SingleANNPrediction2 {

	public static void main (String[]args) throws IOException{
		File folder = new File ("D:\\filetesting\\");
		File folder1 = new File(folder, "zhangsuen");
		File featuresFile = new File(folder1, "zhangsuen.Features.json");
		String[] expectedResult ={ "ain", "alif", "ba", "dal", "dhad", "dzal", "dzo", "fa",
				"ghoin", "hamzah", "ha", "habesar", "jim", "kaf", "kha", "lam", "mim", "nun", "qaf", "ra", "sad", "sheen",
				"sin", "tamarbuto", "ta", "tho", "tsa", "waw", "ya", "za"};
		Thinning1 thinning1 = new Thinning1();
		thinning1.thinningZhangSuen(folder);
		
		FeatureExtraction featureextraction = new FeatureExtraction();
		featureextraction.extractFeatures(folder1);
		
		ArabicPrediction2 arabicprediction = new ArabicPrediction2();
		arabicprediction.arabicPrediction(featuresFile, true);
		
//		String[] prediction = arabicprediction
//				.arabicPrediction(new File(folder, "zhangsuen\\zhangsuen.Features.json"), false);
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
