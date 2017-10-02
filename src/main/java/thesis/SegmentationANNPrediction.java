package thesis;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ArabicOCR.ArabicOCR;
import image.BinaryImageShell;

public class SegmentationANNPrediction {

	public static void main(String[] args) throws IOException {
		File fileLocation = new File("D:\\filetestingsegmentation\\");
		File input = new File(fileLocation, "test7.png");
		BinaryImageShell image = new BinaryImageShell(input);
		List<File> segmentFiles = ArabicOCR.segmentize(image, fileLocation);
		System.out.println(segmentFiles);
		
		Thinning1 thinning1 = new Thinning1();
		List<File> thinnedFiles = thinning1.thinning(fileLocation, segmentFiles);
		
		FeatureExtraction featureextraction = new FeatureExtraction();
		featureextraction.extractfeatures(new File(fileLocation, "zhangsuen"), thinnedFiles);
		
//		String expected_result = "sad";
//		System.out.println(expected_result);
//		float correctness = ArabicOCR.test(result, expected_result);
//		System.out.println(correctness);
	}
}
