package thesis;

import java.io.File;

import ArabicOCR.ArabicOCR;
import image.BinaryImageShell;

public class SegmentationANNPrediction {

	public static void main (String[]args){
		String fileLocation = "D:\\Test2\\";
		String fileName = fileLocation + "test.png";
		File input = new File(fileName);
		   String file = "ain/ain";
	        BinaryImageShell image = new BinaryImageShell(fileName);
	        String result = ArabicOCR.process(image);
	        System.out.println(result);
	        String expected_result = "sad";
	        System.out.println(expected_result);
	        float correctness = ArabicOCR.test(result,expected_result);
	        System.out.println(correctness);
	}
}
