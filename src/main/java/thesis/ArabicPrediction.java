package thesis;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.Layer;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration.ListBuilder;
import org.deeplearning4j.nn.conf.distribution.UniformDistribution;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer.Builder;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;

import thesis.ArabicTraining.Features;
import thesis.ArabicTraining.Segment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This basic example shows how to manually create a DataSet and train it to an
 * basic Network.
 * <p>
 * The network consists in 2 input-neurons, 1 hidden-layer with 4
 * hidden-neurons, and 2 output-neurons.
 * <p>
 * I choose 2 output neurons, (the first fires for false, the second fires for
 * true) because the Evaluation class needs one neuron per classification.
 *
 * @author Peter Großmann
 */
public class ArabicPrediction {
	static final List<String> LABELS = ImmutableList.of(
			"ain", "alif", "ba", "dal", "dhad", 
			"dzal", "dzo", "fa", "ghoin", "hamzah",
			"ha", "habesar", "jim", "kaf", "kha",
			"lam", "mim", "nun", "qaf", "ra", "sad",
			"sheen", "sin", "tamarbuto", "ta", "tho",
			"tsa", "wau", "ya", "za"
			);
	public void arabicPrediction (File featuresFile) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		
      
        Features features = mapper.readValue(featuresFile, Features.class);
        
    	int testingSampleCount = features.segments.size();

    	  INDArray predictionInput = Nd4j.zeros(testingSampleCount, 12);
    	  INDArray labels = Nd4j.zeros(testingSampleCount, 30);
    	  for (int i = 0; i<features.segments.size();i++){
          	Segment segment = features.segments.get(i); 
          	
          	predictionInput.putScalar(new int[]{i, 0}, 2.0*(segment.dotCount/3.0)-1.0);
            predictionInput.putScalar(new int[]{i, 1}, 2.0*(segment.dotPos/2.0)-1.0);
            predictionInput.putScalar(new int[]{i, 2}, 2.0*((segment.normalizedBodyChain[0]-1.0)/7.0)-1.0);
            predictionInput.putScalar(new int[]{i, 3}, 2.0*((segment.normalizedBodyChain[1]-1.0)/7.0)-1.0);
            predictionInput.putScalar(new int[]{i, 4}, 2.0*((segment.normalizedBodyChain[2]-1.0)/7.0)-1.0);
            predictionInput.putScalar(new int[]{i, 5}, 2.0*((segment.normalizedBodyChain[3]-1.0)/7.0)-1.0);
            predictionInput.putScalar(new int[]{i, 6}, 2.0*((segment.normalizedBodyChain[4]-1.0)/7.0)-1.0);
            predictionInput.putScalar(new int[]{i, 7}, 2.0*((segment.normalizedBodyChain[5]-1.0)/7.0)-1.0);
            predictionInput.putScalar(new int[]{i, 8}, 2.0*((segment.normalizedBodyChain[6]-1.0)/7.0)-1.0);
            predictionInput.putScalar(new int[]{i, 9}, 2.0*((segment.normalizedBodyChain[7]-1.0)/7.0)-1.0);
            predictionInput.putScalar(new int[]{i, 10}, 2.0*((segment.normalizedBodyChain[8]-1.0)/7.0)-1.0);
            predictionInput.putScalar(new int[]{i, 11}, 2.0*((segment.normalizedBodyChain[9]-1.0)/7.0)-1.0);
            // then the first output fires for false, and the second is 0 (see class
            // comment)
            
            labels.putScalar(new int[]{i, segment.labelId}, 1);
        }
        
          	
        DataSet ds = new DataSet(predictionInput, labels);
        
        MultiLayerNetwork net = ModelSerializer.restoreMultiLayerNetwork(new File("D:\\filetraining\\zhangsuen\\nnmodel.zip"));
       
        // PREDIKSI -----------------------

        // create output for every training sample
        INDArray predictionOutput = net.output(predictionInput); //prediksi
//        System.out.println("test:"+predictionOutput.amax(0));
          System.out.println("Prediction output: " + predictionOutput);

        System.out.println();
        System.out.println();
        INDArray maxIdxPerRow = predictionOutput.argMax(1);// ambil indeks maksimum ditiap baris
        for (int i =0; i<maxIdxPerRow.length(); i++) {
            int curIdx = (int) maxIdxPerRow.getInt(i); // ambil nilai integer di nilai indeks ke i

             System.out.println(i + " - " + features.segments.get(i).name + " -> " + LABELS.get(curIdx));

          //  System.out.println(curIdx);
        }


        // let Evaluation prints stats how often the right output had the
        // highest value
        System.out.println("Testing samples: " + testingSampleCount );
        Evaluation eval = new Evaluation(30);
        eval.eval(ds.getLabels(), predictionOutput);
        System.out.println(eval.stats());
	}
    static class Features {
        List<Segment> segments = new ArrayList<>();

        public List<Segment> getSegments() {
            return segments;
        }
    }

    static class Segment {
        String name;
        List<ChainInfo> chains = new ArrayList<>();
        String bodyChain;
        int[] normalizedBodyChain;
        int dotPos;
        int dotCount;
        String label;
		int labelId;
		
		public Segment() {
		}
		
		public String getLabel(){
			return label;
		}
		public void setLabel(String label){
			this.label = label;
		}
		public int getlabelId(){
			return labelId;
		}
		public void setlabelId(int labelId){
			this.labelId = labelId;
		}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ChainInfo> getChains() {
            return chains;
        }

        public String getBodyChain() {
            return bodyChain;
        }

        public void setBodyChain(String bodyChain) {
            this.bodyChain = bodyChain;
        }

        public int[] getNormalizedBodyChain() {
            return normalizedBodyChain;
        }

        public void setNormalizedBodyChain(int[] normalizedBodyChain) {
            this.normalizedBodyChain = normalizedBodyChain;
        }

        public int getDotPos() {
            return dotPos;
        }

        public void setDotPos(int dotPos) {
            this.dotPos = dotPos;
        }

        public int getDotCount() {
            return dotCount;
        }

        public void setDotCount(int dotCount) {
            this.dotCount = dotCount;
        }
    }

    static class ChainInfo {
        String chain;
        // 0 = di atas
        // 1 = di tengah
        // 2 = di bawah
        int yPos;
        
        public ChainInfo(){
        	
        }
        public ChainInfo(String chain, int yPos) {
            super();
            this.chain = chain;
            this.yPos = yPos;
        }

        public String getChain() {
            return chain;
        }

        public void setChain(String chain) {
            this.chain = chain;
        }

        public int getyPos() {
            return yPos;
        }

        public void setyPos(int yPos) {
            this.yPos = yPos;
        }

        @Override
        public String toString() {
            return "ChainInfo [chain=" + chain + ", yPos=" + yPos + "]";
        }
    }

    public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
    	  File featuresFile = new File("D:\\filetesting\\zhangsuen\\zhangsuen.Features.json");
    	  ArabicPrediction arabicprediction = new ArabicPrediction();
    	  arabicprediction.arabicPrediction(featuresFile);
    }
}
