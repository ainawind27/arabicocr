package thesis;

import java.io.File;
import java.io.IOException;

public class SingleANNPrediction {

	public static void main (String[]args) throws IOException{
		File folder = new File ("D:\\filetesting\\");
		File folder1 = new File(folder, "zhangsuen");
		File featuresFile = new File(folder1, "zhangsuen.Features.json");
		
		Thinning1 thinning1 = new Thinning1();
		thinning1.thinningZhangSuen(folder);
		
		FeatureExtraction featureextraction = new FeatureExtraction();
		featureextraction.extractFeatures(folder1);
		
		ArabicPrediction arabicprediction = new ArabicPrediction();
		arabicprediction.arabicPrediction(featuresFile);
	}
}
