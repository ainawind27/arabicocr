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
		File input = new File(fileLocation, "kalimat1.png");
//		File input = new File(fileLocation, "tah_kalimat10.png");
//		File input = new File(fileLocation, "ar_kalimat10.png");	
		
		//kalimat1, hasil 79%
		String[] expectedResult = { "alif", "lamalif", "ha" , "ta", "ra", 
									"alif", "mim", 
									"alif", "lam", "mim", "ta", "ba", "alif",
									"dal", "lam", "ba", "ya", "nun", "alif",
									"lamalif", "dal", "ya", "alif", "nun"};
		//ar_kalimat1
//		String[] expectedResult = { "alif", "lamalif", "ha" , "ra", 
//									"alif", "mim", 
//									"alif", "lam", "mim", "ta", "ba", "alif",
//									"dal", "lam", "ba", "ya", "alif",
//									"lamalif", "dal", "ya", "alif", "nun"};
//		//tah_kalimat1 
//		String[] expectedResult = { "alif", "lamalif", "ha" , "ta", "ra", 
//									"alif", "mim", 
//									"alif", "lam", "mim", "ta", "ba", "alif",
//									"dal", "lam", "unknown","ba", "ya", "nun", "alif",
//									"lamalif", "dal", "ya", "alif", "nun", "nun"};
//		//kalimat2, 82%
//		String[] expectedResult = { "alif", "lam", "qaf" , "mim", "ra", 
//									"ta", "ba", 
//									"dal", "waw", "jim", "mim", "ya", "lam",
//									"tamarbuto", "jim", "dal", "alif"};
		
		//kalimat3, 76%
//		String[] expectedResult = { "fa", "alif", "kaf" , "habesar", "tamarbuto", "dal", "waw", "ra", 
//									"ya", "alif", "nun", "alif", "lamalif",
//									"dzal" , "waw", "alif", "qaf" , "jim", "ya", "dal", 
//									"tamarbuto"};
		// tah_kalimat3
//		String[] expectedResult = { "fa", "alif", "kaf" , "habesar", "tamarbuto", "dal", "waw", "ra", 
//									"ya", "alif", "nun", "nun" ,"alif", "lamalif",
//									"dzal" , "waw", "alif", "qaf" , "jim", "ya", "dal", 
//									"tamarbuto"};
		//ar_kalimat3
//		String[] expectedResult = { "fa", "alif", "ha" , "tamarbuto", "dal",
//									"waw", "ra", 
//									"ya", "alif", "nun","alif", "lamalif",
//									"dzal" , "waw", "alif", "qaf" , "jim", "ya", "dal", 
//									"tamarbuto"};
		
		//kalimat4, hasil 76%
//		String[] expectedResult = { "ta", "ta", "kha" , "lam", "ya", 
//									"alif", "ba", 
//									"dal", "alif", "fa", "ya", "alif", "lam",
//									"ha", "ya", "alif", "tamarbuto"};
		
		//ar_kalimat4
//		String[] expectedResult = { "ta", "ta", "kha" , "ya", 
//									"alif", "ba", 
//									"dal", "alif", "fa", "ya", "alif", "lam",
//									"ha", "ya", "alif", "tamarbuto"};
		//kalimat5, hasil 76%
//		String[] expectedResult = { "alif", "nun", "ta" , "dzo", "ra", 
//									"alif", "ya", 
//									"mim", "ha", "alif", "kaf", "mim", "tamarbuto",
//									};
//		//tah_kalimat5
//		String[] expectedResult = { "alif", "nun", "ta" , "dzo", "ra", 
//									"alif", "ya", "ya",
//									"mim", "ha", "alif", "kaf", "tamarbuto",
//								  };
		//kalimat6, 74%
//		String[] expectedResult = { "alif", "lam", "ha", "fa", "alif", "dzo", "ain", "lam", 
//									"ya", "sad", "unknown", "ha", "ta",
//									"kaf" , "unknown", "ha", "ya", "alif", 
//									"tamarbuto", "tho", "ya", "ba", "tamarbuto"};
	//ar_kalimat6
//		String[] expectedResult = { "alif", "lam", "ha", "fa", "alif", 
//									"dzo", "ain", "ya", 
//									 "sad", "unknown", "ha", "ta",
//									"kaf" , "unknown", "ha", "ya", "alif", 
//									"tamarbuto", "tho", "ya", "ba", "tamarbuto"};
		//tah_kalimat6
//		String[] expectedResult = { "alif", "lam", "ha", "fa", "alif", "dzo", "ain", "lam", 
//									"ya", "sad", "ha", "ta",
//									"kaf" , "unknown", "ha", "ya", "alif", 
//									"tamarbuto", "tho", "ya", "ba", "tamarbuto"};

		//kalimat7, 71%
//		String[] expectedResult = { "alif", "lam", "mim" , "ain", "lam", 
//									"mim", "ya", 
//									"nun", "ya", "ain", "lam", "mim", "waw",
//									"nun", "nun"};
		//ar_kalimat7
//		String[] expectedResult = { "alif", "lam", "mim" , "ain", "lam", 
//									 "ya", 
//									 "ya", "ain", "lam", "mim", "waw",
//									"nun"};
//		//kalimat8, hasil 71%
//		String[] expectedResult = { "jim", "dal", "tamarbuto" , "alif", "lamalif", 
//									"ra", "za", 
//									"alif", "lam", "mim", "tho", "ba", "waw",
//									"jim"};
//		//kalimat9, hasil 72%
//		String[] expectedResult = { "ya", "ta", "kaf" , "lam", "mim", 
//									"ba", "ba", 
//									"tho", "hamzah", "mim", "fa", "habesar", "waw",
//									"mim", "tamarbuto", "ha", "ta", "ya"};
		//ar_kalimat9
//		String[] expectedResult = { "ya", "ta", "kaf" , "lam",  
//									"ba", "ba", 
//									"tho", "hamzah", "mim", "fa", "habesar", "waw",
//									"mim", "tamarbuto", "ha", "ya"};
		
		//kalimat10, 68%
//		String[] expectedResult = { "alif", "lam", "ha" , "fa", "alif", "dzo", "ainlam", "ya", 
//									 "sad", "unknown", "ha", "ta",
//									"kaf" , "unknown", "ha", "ya" , "alif", "tamarbuto", "tho", 
//									"ya", "ba", "tamarbuto"};
		//tah_kalimat10
//		String[] expectedResult = { "alif", "lam", "ha" , "fa", "alif", "dzo", "ain",
//									"lam", "ya", 
//									"sad", "ha", "ta",
//									"kaf" , "unknown", "ha", "ya" , 
//									"alif", "tamarbuto", "tho", 
//									"ya", "ba", "tamarbuto"};
		
		//ar_kalimat10
//		String[] expectedResult = { "alif", "lam", "ha" , "fa", "alif", "dzo", "ain",
//									"ya",  
//									"sad", "unknown", "ha", "ta",
//									"kaf" , "unknown", "ha", "ya" , 
//									"alif", "tamarbuto", "tho", 
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
