package thesis;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class SingleANNPrediction {

	public static void main (String[]args) throws IOException{
//		File folder = new File ("D:\\filetesting\\");	
		File folder = new File ("D:\\font tesis aina\\font times new roman");
		File folder1 = new File(folder, "zhangsuen");
		File featuresFile = new File(folder1, "zhangsuen.Features.json");
		
		Thinning1 thinning1 = new Thinning1();
		thinning1.thinningZhangSuen(folder);
		
		FeatureExtraction featureextraction = new FeatureExtraction();
		featureextraction.extractFeatures(folder1);
		
		ArabicPrediction arabicprediction = new ArabicPrediction();
		arabicprediction.arabicPrediction(featuresFile, true);
		
		String[] expectedResult = {"ain", "alif", "ba", "dal", "dhad",
									"dzal", "dzo", "fa", "ghoin", "habesar",
									"hamzah", "ha", "jim", "kaf", "kha",
									"lamalif", "lam", "mim", "nun", "qaf",
									"ra", "sad", "sheen", "sin", "tamarbuto",
									"ta", "tho", "tsa", "waw", "ya", "za"};
		
		ArabicPrediction arabicprediction1 = new ArabicPrediction();
		String[] prediction = arabicprediction1
				.arabicPrediction(new File(folder, "zhangsuen\\zhangsuen.Features.json"), false);
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
