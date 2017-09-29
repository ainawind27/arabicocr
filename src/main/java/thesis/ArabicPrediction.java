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
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

        // list off input values, 4 training samples with data for 2
        // input-neurons each
        INDArray input = Nd4j.zeros(30, 12);//input untuk setiap sampel

        // correspondending list with expected output values, 4 training samples
        // with data for 2 output-neurons each
        INDArray labels = Nd4j.zeros(30, 30);//output untuk setiap sampel

        ObjectMapper mapper = new ObjectMapper();
        Features features = mapper.readValue(new File("D:\\Thin-Latif\\thin\\thin.Features.json"), 
        		Features.class);
        Segment segment = features.segments.get(0); 
        
        // create first dataset
        // input ain
        //0 -> 2*(0/3) -1 = -1
        //1 -> 2*(1/3) -1 = -0.333333333333333333333333333
        //2 -> 2*(2/3) -1 = 0.3
        //3 -> 2*(3/3) -1 = +1

        input.putScalar(new int[]{0, 0}, 2.0*(segment.dotCount/3.0)-1.0);
        input.putScalar(new int[]{0, 1}, 2.0*(segment.dotPos/2.0)-1.0);
        input.putScalar(new int[]{0, 2}, 2.0*((segment.normalizedBodyChain[0]-1.0)/7.0)-1.0);
        input.putScalar(new int[]{0, 3}, 2.0*((segment.normalizedBodyChain[1]-1.0)/7.0)-1.0);
        input.putScalar(new int[]{0, 4}, 2.0*((segment.normalizedBodyChain[2]-1.0)/7.0)-1.0);
        input.putScalar(new int[]{0, 5}, 2.0*((segment.normalizedBodyChain[3]-1.0)/7.0)-1.0);
        input.putScalar(new int[]{0, 6}, 2.0*((segment.normalizedBodyChain[4]-1.0)/7.0)-1.0);
        input.putScalar(new int[]{0, 7}, 2.0*((segment.normalizedBodyChain[5]-1.0)/7.0)-1.0);
        input.putScalar(new int[]{0, 8}, 2.0*((segment.normalizedBodyChain[6]-1.0)/7.0)-1.0);
        input.putScalar(new int[]{0, 9}, 2.0*((segment.normalizedBodyChain[7]-1.0)/7.0)-1.0);
        input.putScalar(new int[]{0, 10}, 2.0*((segment.normalizedBodyChain[8]-1.0)/7.0)-1.0);
        input.putScalar(new int[]{0, 11}, 2.0*((segment.normalizedBodyChain[9]-1.0)/7.0)-1.0);
        // then the first output fires for false, and the second is 0 (see class
        // comment)
        // (0) = alif
        // (1) = ain
        // (2) = ba
        // (3) = dal
        // (4) = dhad
        // (5) = dzal
        // (6) = dzo
        // (7) = fa
        // (8) = ghoin
        // (9) = ha besar
        // (10) = ha
        // (11) = jim
        // (12) = kaf
        // (13) = kha
        // (14) = lam
        // (15) = mim
        // (16) = nun
        // (17) = qaf
        // (18) = ra
        // (19) = sad
        // (20) = sin
        // (21) = sheen
        // (22) = ta marbuto
        // (23) = ta
        // (24) = tho
        // (25) = tsa
        // (26) = wau
        // (27) = ya
        // (28) = zay
        // (29) = alif_lam
        labels.putScalar(new int[]{0, 0}, 1);
        labels.putScalar(new int[]{0, 1}, 0);
        labels.putScalar(new int[]{0, 2}, 0);
        labels.putScalar(new int[]{0, 3}, 0);
        labels.putScalar(new int[]{0, 4}, 0);
        labels.putScalar(new int[]{0, 5}, 0);
        labels.putScalar(new int[]{0, 6}, 0);
        labels.putScalar(new int[]{0, 7}, 0);
        labels.putScalar(new int[]{0, 8}, 0);
        labels.putScalar(new int[]{0, 9}, 0);
        labels.putScalar(new int[]{0, 10}, 0);
        labels.putScalar(new int[]{0, 11}, 0);
        labels.putScalar(new int[]{0, 12}, 0);
        labels.putScalar(new int[]{0, 13}, 0);
        labels.putScalar(new int[]{0, 14}, 0);
        labels.putScalar(new int[]{0, 15}, 0);
        labels.putScalar(new int[]{0, 16}, 0);
        labels.putScalar(new int[]{0, 17}, 0);
        labels.putScalar(new int[]{0, 18}, 0);
        labels.putScalar(new int[]{0, 19}, 0);
        labels.putScalar(new int[]{0, 20}, 0);
        labels.putScalar(new int[]{0, 21}, 0);
        labels.putScalar(new int[]{0, 22}, 0);
        labels.putScalar(new int[]{0, 23}, 0);
        labels.putScalar(new int[]{0, 24}, 0);
        labels.putScalar(new int[]{0, 25}, 0);
        labels.putScalar(new int[]{0, 26}, 0);
        labels.putScalar(new int[]{0, 27}, 0);
        labels.putScalar(new int[]{0, 28}, 0);
        labels.putScalar(new int[]{0, 29}, 0);


        // input 1 alif
        input.putScalar(new int[]{1, 0}, 0.0/3.0);
        input.putScalar(new int[]{1, 1}, 0.0/2.0);
        input.putScalar(new int[]{1, 2}, 5.0/7.0);
        input.putScalar(new int[]{1, 3}, 5.0/7.0);
        input.putScalar(new int[]{1, 4}, 5.0/7.0);
        input.putScalar(new int[]{1, 5}, 5.0/7.0);
        input.putScalar(new int[]{1, 6}, 5.0/7.0);
        input.putScalar(new int[]{1, 7}, 5.0/7.0);
        input.putScalar(new int[]{1, 8}, 5.0/7.0);
        input.putScalar(new int[]{1, 9}, 5.0/7.0);
        input.putScalar(new int[]{1, 10}, 5.0/7.0);
        input.putScalar(new int[]{1, 11}, 5.0/7.0);


        labels.putScalar(new int[]{1, 1}, 1);


        // input 2 ba
        input.putScalar(new int[]{2, 0}, 1.0/3.0);
        input.putScalar(new int[]{2, 1}, 2.0/2.0);
        input.putScalar(new int[]{2, 2}, 5.0/7.0);
        input.putScalar(new int[]{2, 3}, 5.0/7.0);
        input.putScalar(new int[]{2, 4}, 4.0/7.0);
        input.putScalar(new int[]{2, 5}, 3.0/7.0);
        input.putScalar(new int[]{2, 6}, 3.0/7.0);
        input.putScalar(new int[]{2, 7}, 3.0/7.0);
        input.putScalar(new int[]{2, 8}, 3.0/7.0);
        input.putScalar(new int[]{2, 9}, 3.0/7.0);
        input.putScalar(new int[]{2, 10}, 1.0/7.0);
        input.putScalar(new int[]{2, 11}, 1.0/7.0);


        labels.putScalar(new int[]{2, 2}, 1);


        // input 3 dal
        input.putScalar(new int[]{3, 0}, 0.0/3.0);
        input.putScalar(new int[]{3, 1}, 0.0/2.0);
        input.putScalar(new int[]{3, 2}, 4.0/7.0);
        input.putScalar(new int[]{3, 3}, 4.0/7.0);
        input.putScalar(new int[]{3, 4}, 5.0/7.0);
        input.putScalar(new int[]{3, 5}, 5.0/7.0);
        input.putScalar(new int[]{3, 6}, 5.0/7.0);
        input.putScalar(new int[]{3, 7}, 7.0/7.0);
        input.putScalar(new int[]{3, 8}, 7.0/7.0);
        input.putScalar(new int[]{3, 9}, 7.0/7.0);
        input.putScalar(new int[]{3, 10}, 7.0/7.0);
        input.putScalar(new int[]{3, 11}, 0.0/7.0);

        labels.putScalar(new int[]{3, 3}, 1);

        // input 4 dhad
        input.putScalar(new int[]{4, 0}, 1.0/3.0);
        input.putScalar(new int[]{4, 1}, 0.0/2.0);
        input.putScalar(new int[]{4, 2}, 5.0/7.0);
        input.putScalar(new int[]{4, 3}, 3.0/7.0);
        input.putScalar(new int[]{4, 4}, 5.0/7.0);
        input.putScalar(new int[]{4, 5}, 7.0/7.0);
        input.putScalar(new int[]{4, 6}, 3.0/7.0);
        input.putScalar(new int[]{4, 7}, 1.0/7.0);
        input.putScalar(new int[]{4, 8}, 6.0/7.0);
        input.putScalar(new int[]{4, 9}, 6.0/7.0);
        input.putScalar(new int[]{4, 10}, 0.0/7.0);
        input.putScalar(new int[]{4, 11}, 1.0/7.0);

        labels.putScalar(new int[]{4, 4}, 1);

        // input 5 dzal
        input.putScalar(new int[]{5, 0}, 1.0/3.0);
        input.putScalar(new int[]{5, 1}, 0.0/2.0);
        input.putScalar(new int[]{5, 2}, 4.0/7.0);
        input.putScalar(new int[]{5, 3}, 4.0/7.0);
        input.putScalar(new int[]{5, 4}, 5.0/7.0);
        input.putScalar(new int[]{5, 5}, 5.0/7.0);
        input.putScalar(new int[]{5, 6}, 5.0/7.0);
        input.putScalar(new int[]{5, 7}, 7.0/7.0);
        input.putScalar(new int[]{5, 8}, 7.0/7.0);
        input.putScalar(new int[]{5, 9}, 7.0/7.0);
        input.putScalar(new int[]{5, 10}, 7.0/7.0);
        input.putScalar(new int[]{5, 11}, 0.0/7.0);

        labels.putScalar(new int[]{5, 5}, 1);

        // input 6 dzo
        input.putScalar(new int[]{6, 0}, 1.0/3.0);
        input.putScalar(new int[]{6, 1}, 0.0/2.0);
        input.putScalar(new int[]{6, 2}, 5.0/7.0);
        input.putScalar(new int[]{6, 3}, 5.0/7.0);
        input.putScalar(new int[]{6, 4}, 3.0/7.0);
        input.putScalar(new int[]{6, 5}, 7.0/7.0);
        input.putScalar(new int[]{6, 6}, 7.0/7.0);
        input.putScalar(new int[]{6, 7}, 7.0/7.0);
        input.putScalar(new int[]{6, 8}, 6.0/7.0);
        input.putScalar(new int[]{6, 9}, 3.0/7.0);
        input.putScalar(new int[]{6, 10},1.0/7.0);
        input.putScalar(new int[]{6, 11}, 7.0/7.0);

        labels.putScalar(new int[]{6, 6}, 1);

        // input 7 fa
        input.putScalar(new int[]{7, 0}, 1.0/3.0);
        input.putScalar(new int[]{7, 1}, 0.0/2.0);
        input.putScalar(new int[]{7, 2}, 5.0/7.0);
        input.putScalar(new int[]{7, 3}, 4.0/7.0);
        input.putScalar(new int[]{7, 4}, 3.0/7.0);
        input.putScalar(new int[]{7, 5}, 3.0/7.0);
        input.putScalar(new int[]{7, 6}, 3.0/7.0);
        input.putScalar(new int[]{7, 7}, 0.0/7.0);
        input.putScalar(new int[]{7, 8}, 4.0/7.0);
        input.putScalar(new int[]{7, 9}, 1.0/7.0);
        input.putScalar(new int[]{7, 10},7.0/7.0);
        input.putScalar(new int[]{7, 11}, 4.0/7.0);

        labels.putScalar(new int[]{7, 7}, 1);

        /// input 8 ghoin
        input.putScalar(new int[]{8, 0}, 1.0/3.0);
        input.putScalar(new int[]{8, 1}, 0.0/2.0);
        input.putScalar(new int[]{8, 2}, 7.0/7.0);
        input.putScalar(new int[]{8, 3}, 4.0/7.0);
        input.putScalar(new int[]{8, 4}, 3.0/7.0);
        input.putScalar(new int[]{8, 5}, 5.0/7.0);
        input.putScalar(new int[]{8, 6}, 3.0/7.0);
        input.putScalar(new int[]{8, 7}, 3.0/7.0);
        input.putScalar(new int[]{8, 8}, 7.0/7.0);
        input.putScalar(new int[]{8, 9}, 0.0/7.0);
        input.putScalar(new int[]{8, 10}, 2.0/7.0);
        input.putScalar(new int[]{8, 11}, 7.0/7.0);

        labels.putScalar(new int[]{8, 8}, 1);

        // input 9 hamzah
        input.putScalar(new int[]{9, 0}, 0.0/3.0);
        input.putScalar(new int[]{9, 1}, 0.0/2.0);
        input.putScalar(new int[]{9, 2}, 7.0/7.0);
        input.putScalar(new int[]{9, 3}, 6.0/7.0);
        input.putScalar(new int[]{9, 4}, 5.0/7.0);
        input.putScalar(new int[]{9, 5}, 5.0/7.0);
        input.putScalar(new int[]{9, 6}, 3.0/7.0);
        input.putScalar(new int[]{9, 7}, 3.0/7.0);
        input.putScalar(new int[]{9, 8}, 5.0/7.0);
        input.putScalar(new int[]{9, 9}, 1.0/7.0);
        input.putScalar(new int[]{9, 10}, 7.0/7.0);
        input.putScalar(new int[]{9, 11}, 7.0/7.0);

        labels.putScalar(new int[]{9, 9}, 1);

        // input 10 ha
        input.putScalar(new int[]{10, 0}, 0.0/3.0);
        input.putScalar(new int[]{10, 1}, 0.0/2.0);
        input.putScalar(new int[]{10, 2}, 7.0/7.0);
        input.putScalar(new int[]{10, 3}, 7.0/7.0);
        input.putScalar(new int[]{10, 4}, 6.0/7.0);
        input.putScalar(new int[]{10, 5}, 5.0/7.0);
        input.putScalar(new int[]{10, 6}, 3.0/7.0);
        input.putScalar(new int[]{10, 7}, 7.0/7.0);
        input.putScalar(new int[]{10, 8}, 7.0/7.0);
        input.putScalar(new int[]{10, 9}, 1.0/7.0);
        input.putScalar(new int[]{10, 10}, 2.0/7.0);
        input.putScalar(new int[]{10, 11}, 3.0/7.0);

        labels.putScalar(new int[]{10, 10}, 1);

        // input 11 ha besar
        input.putScalar(new int[]{11, 0}, 0.0/3.0);
        input.putScalar(new int[]{11, 1}, 0.0/2.0);
        input.putScalar(new int[]{11, 2}, 5.0/7.0);
        input.putScalar(new int[]{11, 3}, 4.0/7.0);
        input.putScalar(new int[]{11, 4}, 5.0/7.0);
        input.putScalar(new int[]{11, 5}, 7.0/7.0);
        input.putScalar(new int[]{11, 6}, 1.0/7.0);
        input.putScalar(new int[]{11, 7}, 6.0/7.0);
        input.putScalar(new int[]{11, 8}, 4.0/7.0);
        input.putScalar(new int[]{11, 9}, 2.0/7.0);
        input.putScalar(new int[]{11, 10}, 0.0/7.0);
        input.putScalar(new int[]{11, 11}, 7.0/7.0);

        labels.putScalar(new int[]{11, 10}, 1);

        // input 12 jim
        input.putScalar(new int[]{12, 0}, 1.0/3.0);
        input.putScalar(new int[]{12, 1}, 1.0/2.0);
        input.putScalar(new int[]{12, 2}, 7.0/7.0);
        input.putScalar(new int[]{12, 3}, 7.0/7.0);
        input.putScalar(new int[]{12, 4}, 6.0/7.0);
        input.putScalar(new int[]{12, 5}, 5.0/7.0);
        input.putScalar(new int[]{12, 6}, 3.0/7.0);
        input.putScalar(new int[]{12, 7}, 7.0/7.0);
        input.putScalar(new int[]{12, 8}, 7.0/7.0);
        input.putScalar(new int[]{12, 9}, 1.0/7.0);
        input.putScalar(new int[]{12, 10}, 2.0/7.0);
        input.putScalar(new int[]{12, 11}, 3.0/7.0);

        labels.putScalar(new int[]{12, 12}, 1);

        // input 13 kaf
        input.putScalar(new int[]{13, 0}, 1.0/3.0);
        input.putScalar(new int[]{13, 1}, 0.0/2.0);
        input.putScalar(new int[]{13, 2}, 5.0/7.0);
        input.putScalar(new int[]{13, 3}, 4.0/7.0);
        input.putScalar(new int[]{13, 4}, 3.0/7.0);
        input.putScalar(new int[]{13, 5}, 5.0/7.0);
        input.putScalar(new int[]{13, 6}, 5.0/7.0);
        input.putScalar(new int[]{13, 7}, 6.0/7.0);
        input.putScalar(new int[]{13, 8}, 7.0/7.0);
        input.putScalar(new int[]{13, 9}, 7.0/7.0);
        input.putScalar(new int[]{13, 10}, 7.0/7.0);
        input.putScalar(new int[]{13, 11}, 7.0/7.0);

        labels.putScalar(new int[]{13, 13}, 1);

        // input 14 kha
        input.putScalar(new int[]{14, 0}, 1.0/3.0);
        input.putScalar(new int[]{14, 1}, 0.0/2.0);
        input.putScalar(new int[]{14, 2}, 7.0/7.0);
        input.putScalar(new int[]{14, 3}, 7.0/7.0);
        input.putScalar(new int[]{14, 4}, 6.0/7.0);
        input.putScalar(new int[]{14, 5}, 5.0/7.0);
        input.putScalar(new int[]{14, 6}, 3.0/7.0);
        input.putScalar(new int[]{14, 7}, 7.0/7.0);
        input.putScalar(new int[]{14, 8}, 7.0/7.0);
        input.putScalar(new int[]{14, 9}, 1.0/7.0);
        input.putScalar(new int[]{14, 10}, 2.0/7.0);
        input.putScalar(new int[]{14, 11}, 3.0/7.0);

        labels.putScalar(new int[]{14, 14}, 1);

        // input 15 lam
        input.putScalar(new int[]{15, 0}, 0.0/3.0);
        input.putScalar(new int[]{15, 1}, 0.0/2.0);
        input.putScalar(new int[]{15, 2}, 5.0/7.0);
        input.putScalar(new int[]{15, 3}, 5.0/7.0);
        input.putScalar(new int[]{15, 4}, 5.0/7.0);
        input.putScalar(new int[]{15, 5}, 5.0/7.0);
        input.putScalar(new int[]{15, 6}, 5.0/7.0);
        input.putScalar(new int[]{15, 7}, 6.0/7.0);
        input.putScalar(new int[]{15, 8}, 7.0/7.0);
        input.putScalar(new int[]{15, 9}, 7.0/7.0);
        input.putScalar(new int[]{15, 10}, 1.0/7.0);
        input.putScalar(new int[]{15, 11}, 1.0/7.0);

        labels.putScalar(new int[]{15, 15}, 1);

        // input 16 mim
        input.putScalar(new int[]{16, 0}, 0.0/3.0);
        input.putScalar(new int[]{16, 1}, 0.0/2.0);
        input.putScalar(new int[]{16, 2}, 3.0/7.0);
        input.putScalar(new int[]{16, 3}, 3.0/7.0);
        input.putScalar(new int[]{16, 4}, 4.0/7.0);
        input.putScalar(new int[]{16, 5}, 5.0/7.0);
        input.putScalar(new int[]{16, 6}, 7.0/7.0);
        input.putScalar(new int[]{16, 7}, 7.0/7.0);
        input.putScalar(new int[]{16, 8}, 6.0/7.0);
        input.putScalar(new int[]{16, 9}, 5.0/7.0);
        input.putScalar(new int[]{16, 10}, 5.0/7.0);
        input.putScalar(new int[]{16, 11}, 5.0/7.0);

        labels.putScalar(new int[]{16, 16}, 1);

        // input 17 nun
        input.putScalar(new int[]{17, 0}, 0.0/3.0);
        input.putScalar(new int[]{17, 1}, 0.0/2.0);
        input.putScalar(new int[]{17, 2}, 5.0/7.0);
        input.putScalar(new int[]{17, 3}, 5.0/7.0);
        input.putScalar(new int[]{17, 4}, 5.0/7.0);
        input.putScalar(new int[]{17, 5}, 6.0/7.0);
        input.putScalar(new int[]{17, 6}, 7.0/7.0);
        input.putScalar(new int[]{17, 7}, 7.0/7.0);
        input.putScalar(new int[]{17, 8}, 0.0/7.0);
        input.putScalar(new int[]{17, 9}, 1.0/7.0);
        input.putScalar(new int[]{17, 10}, 1.0/7.0);
        input.putScalar(new int[]{17, 11}, 1.0/7.0);

        labels.putScalar(new int[]{17, 17}, 1);

        // input 18 qaf
        input.putScalar(new int[]{18, 0}, 2.0/3.0);
        input.putScalar(new int[]{18, 1}, 0.0/2.0);
        input.putScalar(new int[]{18, 2}, 5.0/7.0);
        input.putScalar(new int[]{18, 3}, 5.0/7.0);
        input.putScalar(new int[]{18, 4}, 3.0/7.0);
        input.putScalar(new int[]{18, 5}, 3.0/7.0);
        input.putScalar(new int[]{18, 6}, 1.0/7.0);
        input.putScalar(new int[]{18, 7}, 1.0/7.0);
        input.putScalar(new int[]{18, 8}, 7.0/7.0);
        input.putScalar(new int[]{18, 9}, 7.0/7.0);
        input.putScalar(new int[]{18, 10}, 3.0/7.0);
        input.putScalar(new int[]{18, 11}, 5.0/7.0);

        labels.putScalar(new int[]{18, 18}, 1);

        // input 19 ra
        input.putScalar(new int[]{19, 0}, 0.0/3.0);
        input.putScalar(new int[]{19, 1}, 0.0/2.0);
        input.putScalar(new int[]{19, 2}, 5.0/7.0);
        input.putScalar(new int[]{19, 3}, 5.0/7.0);
        input.putScalar(new int[]{19, 4}, 5.0/7.0);
        input.putScalar(new int[]{19, 5}, 5.0/7.0);
        input.putScalar(new int[]{19, 6}, 6.0/7.0);
        input.putScalar(new int[]{19, 7}, 6.0/7.0);
        input.putScalar(new int[]{19, 8}, 6.0/7.0);
        input.putScalar(new int[]{19, 9}, 7.0/7.0);
        input.putScalar(new int[]{19, 10}, 7.0/7.0);
        input.putScalar(new int[]{19, 11}, 7.0/7.0);

        labels.putScalar(new int[]{19, 19}, 1);

        // input 20 sad
        input.putScalar(new int[]{20, 0}, 0.0/3.0);
        input.putScalar(new int[]{20, 1}, 0.0/2.0);
        input.putScalar(new int[]{20, 2}, 5.0/7.0);
        input.putScalar(new int[]{20, 3}, 3.0/7.0);
        input.putScalar(new int[]{20, 4}, 5.0/7.0);
        input.putScalar(new int[]{20, 5}, 7.0/7.0);
        input.putScalar(new int[]{20, 6}, 3.0/7.0);
        input.putScalar(new int[]{20, 7}, 1.0/7.0);
        input.putScalar(new int[]{20, 8}, 6.0/7.0);
        input.putScalar(new int[]{20, 9}, 6.0/7.0);
        input.putScalar(new int[]{20, 10}, 0.0/7.0);
        input.putScalar(new int[]{20, 11}, 1.0/7.0);

        labels.putScalar(new int[]{20, 20}, 1);

        // input 21 sheen
        input.putScalar(new int[]{21, 0}, 3.0/3.0);
        input.putScalar(new int[]{21, 1}, 0.0/2.0);
        input.putScalar(new int[]{21, 2}, 5.0/7.0);
        input.putScalar(new int[]{21, 3}, 3.0/7.0);
        input.putScalar(new int[]{21, 4}, 1.0/7.0);
        input.putScalar(new int[]{21, 5}, 1.0/7.0);
        input.putScalar(new int[]{21, 6}, 5.0/7.0);
        input.putScalar(new int[]{21, 7}, 5.0/7.0);
        input.putScalar(new int[]{21, 8}, 5.0/7.0);
        input.putScalar(new int[]{21, 9}, 7.0/7.0);
        input.putScalar(new int[]{21, 10}, 1.0/7.0);
        input.putScalar(new int[]{21, 11}, 1.0/7.0);

        labels.putScalar(new int[]{21, 21}, 1);

        // input 22 sin
        input.putScalar(new int[]{22, 0}, 0.0/3.0);
        input.putScalar(new int[]{22, 1}, 0.0/2.0);
        input.putScalar(new int[]{22, 2}, 5.0/7.0);
        input.putScalar(new int[]{22, 3}, 3.0/7.0);
        input.putScalar(new int[]{22, 4}, 1.0/7.0);
        input.putScalar(new int[]{22, 5}, 1.0/7.0);
        input.putScalar(new int[]{22, 6}, 5.0/7.0);
        input.putScalar(new int[]{22, 7}, 5.0/7.0);
        input.putScalar(new int[]{22, 8}, 5.0/7.0);
        input.putScalar(new int[]{22, 9}, 7.0/7.0);
        input.putScalar(new int[]{22, 10}, 1.0/7.0);
        input.putScalar(new int[]{22, 11}, 1.0/7.0);

        labels.putScalar(new int[]{22, 22}, 1);

        // input 23 ta marbuto
        input.putScalar(new int[]{23, 0}, 2.0/3.0);
        input.putScalar(new int[]{23, 1}, 0.0/2.0);
        input.putScalar(new int[]{23, 2}, 5.0/7.0);
        input.putScalar(new int[]{23, 3}, 4.0/7.0);
        input.putScalar(new int[]{23, 4}, 6.0/7.0);
        input.putScalar(new int[]{23, 5}, 7.0/7.0);
        input.putScalar(new int[]{23, 6}, 1.0/7.0);
        input.putScalar(new int[]{23, 7}, 5.0/7.0);
        input.putScalar(new int[]{23, 8}, 3.0/7.0);
        input.putScalar(new int[]{23, 9}, 2.0/7.0);
        input.putScalar(new int[]{23, 10}, 1.0/7.0);
        input.putScalar(new int[]{23, 11}, 7.0/7.0);

        labels.putScalar(new int[]{23, 23}, 1);

        // input 24 ta
        input.putScalar(new int[]{24, 0}, 2.0/3.0);
        input.putScalar(new int[]{24, 1}, 0.0/2.0);
        input.putScalar(new int[]{24, 2}, 5.0/7.0);
        input.putScalar(new int[]{24, 3}, 5.0/7.0);
        input.putScalar(new int[]{24, 4}, 4.0/7.0);
        input.putScalar(new int[]{24, 5}, 3.0/7.0);
        input.putScalar(new int[]{24, 6}, 3.0/7.0);
        input.putScalar(new int[]{24, 7}, 3.0/7.0);
        input.putScalar(new int[]{24, 8}, 3.0/7.0);
        input.putScalar(new int[]{24, 9}, 3.0/7.0);
        input.putScalar(new int[]{24, 10}, 1.0/7.0);
        input.putScalar(new int[]{24, 11}, 1.0/7.0);

        labels.putScalar(new int[]{24, 24}, 1);

        // input 25 tho
        input.putScalar(new int[]{25, 0}, 0.0/3.0);
        input.putScalar(new int[]{25, 1}, 0.0/2.0);
        input.putScalar(new int[]{25, 2}, 5.0/7.0);
        input.putScalar(new int[]{25, 3}, 5.0/7.0);
        input.putScalar(new int[]{25, 4}, 3.0/7.0);
        input.putScalar(new int[]{25, 5}, 7.0/7.0);
        input.putScalar(new int[]{25, 6}, 7.0/7.0);
        input.putScalar(new int[]{25, 7}, 7.0/7.0);
        input.putScalar(new int[]{25, 8}, 6.0/7.0);
        input.putScalar(new int[]{25, 9}, 3.0/7.0);
        input.putScalar(new int[]{25, 10}, 1.0/7.0);
        input.putScalar(new int[]{25, 11}, 7.0/7.0);

        labels.putScalar(new int[]{25, 25}, 1);

        // input 26 tsa
        input.putScalar(new int[]{26, 0}, 3.0/3.0);
        input.putScalar(new int[]{26, 1}, 0.0/2.0);
        input.putScalar(new int[]{26, 2}, 5.0/7.0);
        input.putScalar(new int[]{26, 3}, 5.0/7.0);
        input.putScalar(new int[]{26, 4}, 4.0/7.0);
        input.putScalar(new int[]{26, 5}, 3.0/7.0);
        input.putScalar(new int[]{26, 6}, 3.0/7.0);
        input.putScalar(new int[]{26, 7}, 3.0/7.0);
        input.putScalar(new int[]{26, 8}, 3.0/7.0);
        input.putScalar(new int[]{26, 9}, 3.0/7.0);
        input.putScalar(new int[]{26, 10}, 1.0/7.0);
        input.putScalar(new int[]{26, 11}, 1.0/7.0);

        labels.putScalar(new int[]{26, 26}, 1);

        // input 27 wau
        input.putScalar(new int[]{27, 0}, 0.0/3.0);
        input.putScalar(new int[]{27, 1}, 0.0/2.0);
        input.putScalar(new int[]{27, 2}, 3.0/7.0);
        input.putScalar(new int[]{27, 3}, 3.0/7.0);
        input.putScalar(new int[]{27, 4}, 2.0/7.0);
        input.putScalar(new int[]{27, 5}, 1.0/7.0);
        input.putScalar(new int[]{27, 6}, 0.0/7.0);
        input.putScalar(new int[]{27, 7}, 5.0/7.0);
        input.putScalar(new int[]{27, 8}, 3.0/7.0);
        input.putScalar(new int[]{27, 9}, 0.0/7.0);
        input.putScalar(new int[]{27, 10}, 3.0/7.0);
        input.putScalar(new int[]{27, 11}, 5.0/7.0);

        labels.putScalar(new int[]{27, 27}, 1);

        // input 28 ya
        input.putScalar(new int[]{28, 0}, 2.0/3.0);
        input.putScalar(new int[]{28, 1}, 2.0/2.0);
        input.putScalar(new int[]{28, 2}, 2.0/7.0);
        input.putScalar(new int[]{28, 3}, 7.0/7.0);
        input.putScalar(new int[]{28, 4}, 5.0/7.0);
        input.putScalar(new int[]{28, 5}, 3.0/7.0);
        input.putScalar(new int[]{28, 6}, 6.0/7.0);
        input.putScalar(new int[]{28, 7}, 7.0/7.0);
        input.putScalar(new int[]{28, 8}, 7.0/7.0);
        input.putScalar(new int[]{28, 9}, 0.0/7.0);
        input.putScalar(new int[]{28, 10},1.0/7.0);
        input.putScalar(new int[]{28, 11},1.0/7.0);

        labels.putScalar(new int[]{28, 28}, 1);

        // input 29
        input.putScalar(new int[]{29, 0}, 1.0/3.0);
        input.putScalar(new int[]{29, 1}, 0.0/2.0);
        input.putScalar(new int[]{29, 2}, 5.0/7.0);
        input.putScalar(new int[]{29, 3}, 5.0/7.0);
        input.putScalar(new int[]{29, 4}, 5.0/7.0);
        input.putScalar(new int[]{29, 5}, 5.0/7.0);
        input.putScalar(new int[]{29, 6}, 6.0/7.0);
        input.putScalar(new int[]{29, 7}, 6.0/7.0);
        input.putScalar(new int[]{29, 8}, 6.0/7.0);
        input.putScalar(new int[]{29, 9}, 7.0/7.0);
        input.putScalar(new int[]{29, 10},7.0/7.0);
        input.putScalar(new int[]{29, 11},7.0/7.0);

        labels.putScalar(new int[]{29, 29}, 1);



        System.out.println("Training inputs: " + input);
        System.out.println("Training labels: " + labels);

        // create dataset object
        DataSet ds = new DataSet(input, labels);

        // Set up network configuration
        NeuralNetConfiguration.Builder builder = new NeuralNetConfiguration.Builder();
        // how often should the training set be run, we need something above
        // 1000, or a higher learning-rate - found this values just by trial and
        // error
        builder.iterations(10000);
        // learning rate
        builder.learningRate(0.1);
        // fixed seed for the random generator, so any run of this program
        // brings the same results - may not work if you do something like
        // ds.shuffle()
        builder.seed(123);
        // not applicable, this network is to small - but for bigger networks it
        // can help that the network will not only recite the training data
        builder.useDropConnect(false);
        // a standard algorithm for moving on the error-plane, this one works
        // best for me, LINE_GRADIENT_DESCENT or CONJUGATE_GRADIENT can do the
        // job, too - it's an empirical value which one matches best to
        // your problem
        builder.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT);
        // init the bias with 0 - empirical value, too
        builder.biasInit(0);
        // from "http://deeplearning4j.org/architecture": The networks can
        // process the input more quickly and more accurately by ingesting
        // minibatches 5-10 elements at a time in parallel.
        // this example runs better without, because the dataset is smaller than
        // the mini batch size
        builder.miniBatch(false);

        // create a multilayer network with 2 layers (including the output
        // layer, excluding the input payer)
        ListBuilder listBuilder = builder.list();

        DenseLayer.Builder hiddenLayerBuilder = new DenseLayer.Builder();
        // two input connections - simultaneously defines the number of input
        // neurons, because it's the first non-input-layer
        hiddenLayerBuilder.nIn(12);//jumlah input
        // number of outgooing connections, nOut simultaneously defines the
        // number of neurons in this layer
        hiddenLayerBuilder.nOut(12);//jumlah hidden neurons 1
        // put the output through the sigmoid function, to cap the output
        // valuebetween 0 and 1
        hiddenLayerBuilder.activation(Activation.SIGMOID);
        // random initialize weights with values between 0 and 1
        hiddenLayerBuilder.weightInit(WeightInit.DISTRIBUTION);
        hiddenLayerBuilder.dist(new UniformDistribution(0, 1));

        // build and set as layer 0
        listBuilder.layer(0, hiddenLayerBuilder.build());

        // MCXENT or NEGATIVELOGLIKELIHOOD (both are mathematically equivalent) work ok for this example - this
        // function calculates the error-value (aka 'cost' or 'loss function value'), and quantifies the goodness
        // or badness of a prediction, in a differentiable way
        // For classification (with mutually exclusive classes, like here), use multiclass cross entropy, in conjunction
        // with softmax activation function
        Builder outputLayerBuilder = new Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD);
        // must be the same amout as neurons in the layer before
        outputLayerBuilder.nIn(12);//jumlah hidden neuron1
        // two neurons in this layer
        outputLayerBuilder.nOut(30); //jumlah output
        outputLayerBuilder.activation(Activation.SOFTMAX);
        outputLayerBuilder.weightInit(WeightInit.DISTRIBUTION);
        outputLayerBuilder.dist(new UniformDistribution(0, 1));
        listBuilder.layer(1, outputLayerBuilder.build());

        // no pretrain phase for this network
        listBuilder.pretrain(false);

        // seems to be mandatory
        // according to agibsonccc: You typically only use that with
        // pretrain(true) when you want to do pretrain/finetune without changing
        // the previous layers finetuned weights that's for autoencoders and
        // rbms
        listBuilder.backprop(true);

        // build and init the network, will check if everything is configured
        // correct
        MultiLayerConfiguration conf = listBuilder.build();
        MultiLayerNetwork net = new MultiLayerNetwork(conf);
        net.init();

        // add an listener which outputs the error every 100 parameter updates
        net.setListeners(new ScoreIterationListener(100));

        // C&P from GravesLSTMCharModellingExample
        // Print the number of parameters in the network (and for each layer)
        Layer[] layers = net.getLayers();
        int totalNumParams = 0;
        for (int i = 0; i < layers.length; i++) {
            int nParams = layers[i].numParams();
            System.out.println("Number of parameters in layer " + i + ": " + nParams);
            totalNumParams += nParams;
        }
        System.out.println("Total number of network parameters: " + totalNumParams);

        // here the actual learning takes place
        net.fit(ds);//batas training

        // PREDIKSI -----------------------

        INDArray predictionInput = Nd4j.zeros(30, 12);//input untuk setiap sampel
        // Analogi untuk Allah:
        // segment ke-0: ain
        predictionInput.putScalar(new int[]{0, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{0, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{0, 2}, 7.0/7.0);
        predictionInput.putScalar(new int[]{0, 3}, 6.0/7.0);
        predictionInput.putScalar(new int[]{0, 4}, 4.0/7.0);
        predictionInput.putScalar(new int[]{0, 5}, 2.0/7.0);
        predictionInput.putScalar(new int[]{0, 6}, 7.0/7.0);
        predictionInput.putScalar(new int[]{0, 7}, 5.0/7.0);
        predictionInput.putScalar(new int[]{0, 8}, 4.0/7.0);
        predictionInput.putScalar(new int[]{0, 9}, 3.0/7.0);
        predictionInput.putScalar(new int[]{0, 10},3.0/7.0);
        predictionInput.putScalar(new int[]{0, 11},3.0/7.0);
//        // segment ke-1: alif
        predictionInput.putScalar(new int[]{1, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{1, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{1, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{1, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{1, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{1, 5}, 5.0/7.0);
        predictionInput.putScalar(new int[]{1, 6}, 5.0/7.0);
        predictionInput.putScalar(new int[]{1, 7}, 5.0/7.0);
        predictionInput.putScalar(new int[]{1, 8}, 5.0/7.0);
        predictionInput.putScalar(new int[]{1, 9}, 5.0/7.0);
        predictionInput.putScalar(new int[]{1, 10}, 5.0/7.0);
        predictionInput.putScalar(new int[]{1, 11}, 5.0/7.0);

        // segment ke-2: ba
        predictionInput.putScalar(new int[]{2, 0}, 1.0/3.0);
        predictionInput.putScalar(new int[]{2, 1}, 2.0/2.0);
        predictionInput.putScalar(new int[]{2, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{2, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{2, 4}, 6.0/7.0);
        predictionInput.putScalar(new int[]{2, 5}, 7.0/7.0);
        predictionInput.putScalar(new int[]{2, 6}, 7.0/7.0);
        predictionInput.putScalar(new int[]{2, 7}, 7.0/7.0);
        predictionInput.putScalar(new int[]{2, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{2, 9}, 0.0/7.0);
        predictionInput.putScalar(new int[]{2, 10}, 1.0/7.0);
        predictionInput.putScalar(new int[]{2, 11}, 1.0/7.0);
        // segment ke-3: dal
        predictionInput.putScalar(new int[]{3, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{3, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{3, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{3, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{3, 4}, 4.0/7.0);
        predictionInput.putScalar(new int[]{3, 5}, 4.0/7.0);
        predictionInput.putScalar(new int[]{3, 6}, 5.0/7.0);
        predictionInput.putScalar(new int[]{3, 7}, 5.0/7.0);
        predictionInput.putScalar(new int[]{3, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{3, 9}, 7.0/7.0);
        predictionInput.putScalar(new int[]{3, 10}, 7.0/7.0);
        predictionInput.putScalar(new int[]{3, 11}, 7.0/7.0);

        // segment ke-4: dhad
        predictionInput.putScalar(new int[]{4, 0}, 1.0/3.0);
        predictionInput.putScalar(new int[]{4, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{4, 2}, 4.0/7.0);
        predictionInput.putScalar(new int[]{4, 3}, 2.0/7.0);
        predictionInput.putScalar(new int[]{4, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{4, 5}, 7.0/7.0);
        predictionInput.putScalar(new int[]{4, 6}, 3.0/7.0);
        predictionInput.putScalar(new int[]{4, 7}, 7.0/7.0);
        predictionInput.putScalar(new int[]{4, 8}, 5.0/7.0);
        predictionInput.putScalar(new int[]{4, 9}, 6.0/7.0);
        predictionInput.putScalar(new int[]{4, 10}, 0.0/7.0);
        predictionInput.putScalar(new int[]{4, 11}, 1.0/7.0);

        // segment ke-5: dzal
        predictionInput.putScalar(new int[]{5, 0}, 1.0/3.0);
        predictionInput.putScalar(new int[]{5, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{5, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{5, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{5, 4}, 4.0/7.0);
        predictionInput.putScalar(new int[]{5, 5}, 4.0/7.0);
        predictionInput.putScalar(new int[]{5, 6}, 5.0/7.0);
        predictionInput.putScalar(new int[]{5, 7}, 5.0/7.0);
        predictionInput.putScalar(new int[]{5, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{5, 9}, 7.0/7.0);
        predictionInput.putScalar(new int[]{5, 10}, 7.0/7.0);
        predictionInput.putScalar(new int[]{5, 11}, 7.0/7.0);

        // segment ke-6: dzo
        predictionInput.putScalar(new int[]{6, 0}, 1.0/3.0);
        predictionInput.putScalar(new int[]{6, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{6, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{6, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{6, 4}, 3.0/7.0);
        predictionInput.putScalar(new int[]{6, 5}, 4.0/7.0);
        predictionInput.putScalar(new int[]{6, 6}, 7.0/7.0);
        predictionInput.putScalar(new int[]{6, 7}, 7.0/7.0);
        predictionInput.putScalar(new int[]{6, 8}, 3.0/7.0);
        predictionInput.putScalar(new int[]{6, 9}, 3.0/7.0);
        predictionInput.putScalar(new int[]{6, 10}, 0.0/7.0);
        predictionInput.putScalar(new int[]{6, 11}, 7.0/7.0);

        // segment ke-7: fa
        predictionInput.putScalar(new int[]{7, 0}, 1.0/3.0);
        predictionInput.putScalar(new int[]{7, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{7, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{7, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{7, 4}, 3.0/7.0);
        predictionInput.putScalar(new int[]{7, 5}, 4.0/7.0);
        predictionInput.putScalar(new int[]{7, 6}, 7.0/7.0);
        predictionInput.putScalar(new int[]{7, 7}, 7.0/7.0);
        predictionInput.putScalar(new int[]{7, 8}, 3.0/7.0);
        predictionInput.putScalar(new int[]{7, 9}, 3.0/7.0);
        predictionInput.putScalar(new int[]{7, 10}, 0.0/7.0);
        predictionInput.putScalar(new int[]{7, 11}, 7.0/7.0);

        // segment ke-8: ghoin
        predictionInput.putScalar(new int[]{8, 0}, 1.0/3.0);
        predictionInput.putScalar(new int[]{8, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{8, 2}, 7.0/7.0);
        predictionInput.putScalar(new int[]{8, 3}, 6.0/7.0);
        predictionInput.putScalar(new int[]{8, 4}, 4.0/7.0);
        predictionInput.putScalar(new int[]{8, 5}, 2.0/7.0);
        predictionInput.putScalar(new int[]{8, 6}, 7.0/7.0);
        predictionInput.putScalar(new int[]{8, 7}, 5.0/7.0);
        predictionInput.putScalar(new int[]{8, 8}, 4.0/7.0);
        predictionInput.putScalar(new int[]{8, 9}, 3.0/7.0);
        predictionInput.putScalar(new int[]{8, 10}, 3.0/7.0);
        predictionInput.putScalar(new int[]{8, 11}, 3.0/7.0);

        // segment ke-9: hamzah
        predictionInput.putScalar(new int[]{9, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{9, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{9, 2}, 7.0/7.0);
        predictionInput.putScalar(new int[]{9, 3}, 7.0/7.0);
        predictionInput.putScalar(new int[]{9, 4}, 6.0/7.0);
        predictionInput.putScalar(new int[]{9, 5}, 4.0/7.0);
        predictionInput.putScalar(new int[]{9, 6}, 4.0/7.0);
        predictionInput.putScalar(new int[]{9, 7}, 3.0/7.0);
        predictionInput.putScalar(new int[]{9, 8}, 3.0/7.0);
        predictionInput.putScalar(new int[]{9, 9}, 7.0/7.0);
        predictionInput.putScalar(new int[]{9, 10},7.0/7.0);
        predictionInput.putScalar(new int[]{9, 11}, 6.0/7.0);

        // segment ke-10: ha
        predictionInput.putScalar(new int[]{10, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{10, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{10, 2}, 7.0/7.0);
        predictionInput.putScalar(new int[]{10, 3}, 7.0/7.0);
        predictionInput.putScalar(new int[]{10, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{10, 5}, 4.0/7.0);
        predictionInput.putScalar(new int[]{10, 6}, 3.0/7.0);
        predictionInput.putScalar(new int[]{10, 7}, 7.0/7.0);
        predictionInput.putScalar(new int[]{10, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{10, 9}, 1.0/7.0);
        predictionInput.putScalar(new int[]{10, 10}, 2.0/7.0);
        predictionInput.putScalar(new int[]{10, 11}, 3.0/7.0);

        // segment ke-11: ha besar
        predictionInput.putScalar(new int[]{11, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{11, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{11, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{11, 3}, 4.0/7.0);
        predictionInput.putScalar(new int[]{11, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{11, 5}, 7.0/7.0);
        predictionInput.putScalar(new int[]{11, 6}, 1.0/7.0);
        predictionInput.putScalar(new int[]{11, 7}, 5.0/7.0);
        predictionInput.putScalar(new int[]{11, 8}, 4.0/7.0);
        predictionInput.putScalar(new int[]{11, 9}, 2.0/7.0);
        predictionInput.putScalar(new int[]{11, 10}, 1.0/7.0);
        predictionInput.putScalar(new int[]{11, 11}, 0.0/7.0);

        // segment ke-12: jim
        predictionInput.putScalar(new int[]{12, 0}, 1.0/3.0);
        predictionInput.putScalar(new int[]{12, 1}, 1.0/2.0);
        predictionInput.putScalar(new int[]{12, 2}, 7.0/7.0);
        predictionInput.putScalar(new int[]{12, 3}, 7.0/7.0);
        predictionInput.putScalar(new int[]{12, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{12, 5}, 4.0/7.0);
        predictionInput.putScalar(new int[]{12, 6}, 3.0/7.0);
        predictionInput.putScalar(new int[]{12, 7}, 7.0/7.0);
        predictionInput.putScalar(new int[]{12, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{12, 9}, 1.0/7.0);
        predictionInput.putScalar(new int[]{12, 10}, 2.0/7.0);
        predictionInput.putScalar(new int[]{12, 11}, 3.0/7.0);

        // segment ke-13: kaf
        predictionInput.putScalar(new int[]{13, 0}, 1.0/3.0);
        predictionInput.putScalar(new int[]{13, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{13, 2}, 6.0/7.0);
        predictionInput.putScalar(new int[]{13, 3}, 6.0/7.0);
        predictionInput.putScalar(new int[]{13, 4}, 4.0/7.0);
        predictionInput.putScalar(new int[]{13, 5}, 4.0/7.0);
        predictionInput.putScalar(new int[]{13, 6}, 6.0/7.0);
        predictionInput.putScalar(new int[]{13, 7}, 7.0/7.0);
        predictionInput.putScalar(new int[]{13, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{13, 9}, 7.0/7.0);
        predictionInput.putScalar(new int[]{13, 10}, 7.0/7.0);
        predictionInput.putScalar(new int[]{13, 11}, 7.0/7.0);

        // segment ke-14: kha
        predictionInput.putScalar(new int[]{14, 0}, 1.0/3.0);
        predictionInput.putScalar(new int[]{14, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{14, 2}, 7.0/7.0);
        predictionInput.putScalar(new int[]{14, 3}, 7.0/7.0);
        predictionInput.putScalar(new int[]{14, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{14, 5}, 4.0/7.0);
        predictionInput.putScalar(new int[]{14, 6}, 3.0/7.0);
        predictionInput.putScalar(new int[]{14, 7}, 7.0/7.0);
        predictionInput.putScalar(new int[]{14, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{14, 9}, 1.0/7.0);
        predictionInput.putScalar(new int[]{14, 10}, 2.0/7.0);
        predictionInput.putScalar(new int[]{14, 11}, 3.0/7.0);

        // segment ke-15: lam
        predictionInput.putScalar(new int[]{15, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{15, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{15, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{15, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{15, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{15, 5}, 5.0/7.0);
        predictionInput.putScalar(new int[]{15, 6}, 5.0/7.0);
        predictionInput.putScalar(new int[]{15, 7}, 5.0/7.0);
        predictionInput.putScalar(new int[]{15, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{15, 9}, 7.0/7.0);
        predictionInput.putScalar(new int[]{15, 10}, 1.0/7.0);
        predictionInput.putScalar(new int[]{15, 11}, 1.0/7.0);

        // segment ke-16: mim
        predictionInput.putScalar(new int[]{16, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{16, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{16, 2}, 3.0/7.0);
        predictionInput.putScalar(new int[]{16, 3}, 3.0/7.0);
        predictionInput.putScalar(new int[]{16, 4}, 7.0/7.0);
        predictionInput.putScalar(new int[]{16, 5}, 7.0/7.0);
        predictionInput.putScalar(new int[]{16, 6}, 5.0/7.0);
        predictionInput.putScalar(new int[]{16, 7}, 5.0/7.0);
        predictionInput.putScalar(new int[]{16, 8}, 5.0/7.0);
        predictionInput.putScalar(new int[]{16, 9}, 5.0/7.0);
        predictionInput.putScalar(new int[]{16, 10}, 5.0/7.0);
        predictionInput.putScalar(new int[]{16, 11}, 5.0/7.0);

        // segment ke-17: nun
        predictionInput.putScalar(new int[]{17, 0}, 1.0/3.0);
        predictionInput.putScalar(new int[]{17, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{17, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{17, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{17, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{17, 5}, 6.0/7.0);
        predictionInput.putScalar(new int[]{17, 6}, 7.0/7.0);
        predictionInput.putScalar(new int[]{17, 7}, 7.0/7.0);
        predictionInput.putScalar(new int[]{17, 8}, 0.0/7.0);
        predictionInput.putScalar(new int[]{17, 9}, 1.0/7.0);
        predictionInput.putScalar(new int[]{17, 10},1.0/7.0);
        predictionInput.putScalar(new int[]{17, 11}, 1.0/7.0);

        // segment ke-18: qaf
        predictionInput.putScalar(new int[]{18, 0}, 2.0/3.0);
        predictionInput.putScalar(new int[]{18, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{18, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{18, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{18, 4}, 3.0/7.0);
        predictionInput.putScalar(new int[]{18, 5}, 3.0/7.0);
        predictionInput.putScalar(new int[]{18, 6}, 3.0/7.0);
        predictionInput.putScalar(new int[]{18, 7}, 1.0/7.0);
        predictionInput.putScalar(new int[]{18, 8}, 1.0/7.0);
        predictionInput.putScalar(new int[]{18, 9}, 3.0/7.0);
        predictionInput.putScalar(new int[]{18, 10}, 1.0/7.0);
        predictionInput.putScalar(new int[]{18, 11}, 5.0/7.0);

        // segment ke-19: ra
        predictionInput.putScalar(new int[]{19, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{19, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{19, 2}, 4.0/7.0);
        predictionInput.putScalar(new int[]{19, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{19, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{19, 5}, 5.0/7.0);
        predictionInput.putScalar(new int[]{19, 6}, 6.0/7.0);
        predictionInput.putScalar(new int[]{19, 7}, 6.0/7.0);
        predictionInput.putScalar(new int[]{19, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{19, 9}, 7.0/7.0);
        predictionInput.putScalar(new int[]{19, 10}, 7.0/7.0);
        predictionInput.putScalar(new int[]{19, 11}, 7.0/7.0);

        // segment ke-20: sad
        predictionInput.putScalar(new int[]{20, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{20, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{20, 2}, 4.0/7.0);
        predictionInput.putScalar(new int[]{20, 3}, 2.0/7.0);
        predictionInput.putScalar(new int[]{20, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{20, 5}, 7.0/7.0);
        predictionInput.putScalar(new int[]{20, 6}, 3.0/7.0);
        predictionInput.putScalar(new int[]{20, 7}, 7.0/7.0);
        predictionInput.putScalar(new int[]{20, 8}, 5.0/7.0);
        predictionInput.putScalar(new int[]{20, 9}, 6.0/7.0);
        predictionInput.putScalar(new int[]{20, 10}, 0.0/7.0);
        predictionInput.putScalar(new int[]{20, 11}, 1.0/7.0);

        // segment ke-21: sheen
        predictionInput.putScalar(new int[]{21, 0}, 3.0/3.0);
        predictionInput.putScalar(new int[]{21, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{21, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{21, 3}, 7.0/7.0);
        predictionInput.putScalar(new int[]{21, 4}, 1.0/7.0);
        predictionInput.putScalar(new int[]{21, 5}, 6.0/7.0);
        predictionInput.putScalar(new int[]{21, 6}, 7.0/7.0);
        predictionInput.putScalar(new int[]{21, 7}, 6.0/7.0);
        predictionInput.putScalar(new int[]{21, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{21, 9}, 7.0/7.0);
        predictionInput.putScalar(new int[]{21, 10}, 1.0/7.0);
        predictionInput.putScalar(new int[]{21, 11}, 1.0/7.0);

        // segment ke-22: sin
        predictionInput.putScalar(new int[]{22, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{22, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{22, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{22, 3}, 7.0/7.0);
        predictionInput.putScalar(new int[]{22, 4}, 1.0/7.0);
        predictionInput.putScalar(new int[]{22, 5}, 6.0/7.0);
        predictionInput.putScalar(new int[]{22, 6}, 7.0/7.0);
        predictionInput.putScalar(new int[]{22, 7}, 6.0/7.0);
        predictionInput.putScalar(new int[]{22, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{22, 9}, 7.0/7.0);
        predictionInput.putScalar(new int[]{22, 10}, 1.0/7.0);
        predictionInput.putScalar(new int[]{22, 11}, 1.0/7.0);

        // segment ke-23: ta marbuto
        predictionInput.putScalar(new int[]{23, 0}, 2.0/3.0);
        predictionInput.putScalar(new int[]{23, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{23, 2}, 4.0/7.0);
        predictionInput.putScalar(new int[]{23, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{23, 4}, 7.0/7.0);
        predictionInput.putScalar(new int[]{23, 5}, 1.0/7.0);
        predictionInput.putScalar(new int[]{23, 6}, 1.0/7.0);
        predictionInput.putScalar(new int[]{23, 7}, 5.0/7.0);
        predictionInput.putScalar(new int[]{23, 8}, 5.0/7.0);
        predictionInput.putScalar(new int[]{23, 9}, 3.0/7.0);
        predictionInput.putScalar(new int[]{23, 10}, 1.0/7.0);
        predictionInput.putScalar(new int[]{23, 11}, 0.0/7.0);

        // segment ke-24: ta
        predictionInput.putScalar(new int[]{24, 0}, 2.0/3.0);
        predictionInput.putScalar(new int[]{24, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{24, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{24, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{24, 4}, 6.0/7.0);
        predictionInput.putScalar(new int[]{24, 5}, 7.0/7.0);
        predictionInput.putScalar(new int[]{24, 6}, 7.0/7.0);
        predictionInput.putScalar(new int[]{24, 7}, 7.0/7.0);
        predictionInput.putScalar(new int[]{24, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{24, 9}, 0.0/7.0);
        predictionInput.putScalar(new int[]{24, 10}, 1.0/7.0);
        predictionInput.putScalar(new int[]{24, 11}, 1.0/7.0);

        // segment ke-25: tho
        predictionInput.putScalar(new int[]{25, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{25, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{25, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{25, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{25, 4}, 3.0/7.0);
        predictionInput.putScalar(new int[]{25, 5}, 4.0/7.0);
        predictionInput.putScalar(new int[]{25, 6}, 7.0/7.0);
        predictionInput.putScalar(new int[]{25, 7}, 7.0/7.0);
        predictionInput.putScalar(new int[]{25, 8}, 3.0/7.0);
        predictionInput.putScalar(new int[]{25, 9}, 3.0/7.0);
        predictionInput.putScalar(new int[]{25, 10}, 0.0/7.0);
        predictionInput.putScalar(new int[]{25, 11}, 7.0/7.0);

        // segment ke-26: tsa
        predictionInput.putScalar(new int[]{26, 0}, 3.0/3.0);
        predictionInput.putScalar(new int[]{26, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{26, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{26, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{26, 4}, 6.0/7.0);
        predictionInput.putScalar(new int[]{26, 5}, 7.0/7.0);
        predictionInput.putScalar(new int[]{26, 6}, 7.0/7.0);
        predictionInput.putScalar(new int[]{26, 7}, 7.0/7.0);
        predictionInput.putScalar(new int[]{26, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{26, 9}, 0.0/7.0);
        predictionInput.putScalar(new int[]{26, 10}, 1.0/7.0);
        predictionInput.putScalar(new int[]{26, 11}, 1.0/7.0);

        // segment ke-27: wau
        predictionInput.putScalar(new int[]{27, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{27, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{27, 2}, 4.0/7.0);
        predictionInput.putScalar(new int[]{27, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{27, 4}, 7.0/7.0);
        predictionInput.putScalar(new int[]{27, 5}, 7.0/7.0);
        predictionInput.putScalar(new int[]{27, 6}, 7.0/7.0);
        predictionInput.putScalar(new int[]{27, 7}, 3.0/7.0);
        predictionInput.putScalar(new int[]{27, 8}, 3.0/7.0);
        predictionInput.putScalar(new int[]{27, 9}, 3.0/7.0);
        predictionInput.putScalar(new int[]{27, 10}, 1.0/7.0);
        predictionInput.putScalar(new int[]{27, 11}, 0.0/7.0);

        // segment ke-28: ya
        predictionInput.putScalar(new int[]{28, 0}, 2.0/3.0);
        predictionInput.putScalar(new int[]{28, 1}, 2.0/2.0);
        predictionInput.putScalar(new int[]{28, 2}, 7.0/7.0);
        predictionInput.putScalar(new int[]{28, 3}, 6.0/7.0);
        predictionInput.putScalar(new int[]{28, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{28, 5}, 3.0/7.0);
        predictionInput.putScalar(new int[]{28, 6}, 6.0/7.0);
        predictionInput.putScalar(new int[]{28, 7}, 7.0/7.0);
        predictionInput.putScalar(new int[]{28, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{28, 9}, 0.0/7.0);
        predictionInput.putScalar(new int[]{28, 10}, 1.0/7.0);
        predictionInput.putScalar(new int[]{28, 11}, 1.0/7.0);


        // segment ke-29: za
        predictionInput.putScalar(new int[]{29, 0}, 1.0/3.0);
        predictionInput.putScalar(new int[]{29, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{29, 2}, 4.0/7.0);
        predictionInput.putScalar(new int[]{29, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{29, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{29, 5}, 5.0/7.0);
        predictionInput.putScalar(new int[]{29, 6}, 6.0/7.0);
        predictionInput.putScalar(new int[]{29, 7}, 6.0/7.0);
        predictionInput.putScalar(new int[]{29, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{29, 9}, 7.0/7.0);
        predictionInput.putScalar(new int[]{29, 10}, 7.0/7.0);
        predictionInput.putScalar(new int[]{29, 11}, 7.0/7.0);

        // create output for every training sample
        INDArray predictionOutput = net.output(predictionInput); //prediksi
//        System.out.println("test:"+predictionOutput.amax(0));
          System.out.println("Prediction output: " + predictionOutput);

        System.out.println();
        System.out.println();
        INDArray maxIdxPerRow = predictionOutput.argMax(0);// ambil indeks maksimum ditiap baris
        for (int i =0; i<maxIdxPerRow.length(); i++) {
            float curIdx = (int) maxIdxPerRow.getInt(i); // ambil nilai integer di nilai indeks ke i

            if (curIdx==0){
                System.out.println("ain");
            }else if (curIdx==1){
                System.out.println("alif");
            } else if (curIdx==2){
                System.out.println("ba");
            } else if (curIdx==3){
                System.out.println("dal");
            } else if (curIdx==4){
                System.out.println("dhad");
            }else if (curIdx==5){
                System.out.println("dzal");
            }else if (curIdx==6){
                System.out.println("dzo");
            }else if (curIdx==7){
                System.out.println("fa");
            }else if (curIdx==8){
                System.out.println("ghoin");
            }else if (curIdx==9){
                System.out.println("hamzah");
            }else if (curIdx==10){
                System.out.println("ha");
            }else if (curIdx==11){
                System.out.println("ha besar");
            }else if(curIdx==12){
                System.out.println("jim");
            }else if (curIdx==13){
                System.out.println("kaf");
            }else if (curIdx==14){
                System.out.println("kha");
            }else if (curIdx==15){
                System.out.println("lam");
            }else if(curIdx==16){
                System.out.println("mim");
            }else if (curIdx==17){
                System.out.println("nun");
            }else if (curIdx==18){
                System.out.println("qaf");
            }else if (curIdx==19){
                System.out.println("ra");
            }else if (curIdx==20){
                System.out.println("sad");
            }else if (curIdx==21){
                System.out.println("sheen");
            }else if (curIdx==22){
                System.out.println("sin ");
            }else if (curIdx==23){
                System.out.println("ta marbuto");
            }else if (curIdx==24){
                System.out.println("ta");
            }else if (curIdx==25){
                System.out.println("tho");
            }else if (curIdx==26){
                System.out.println("tsa");
            }else if (curIdx==27){
                System.out.println("wau");
            }else if (curIdx==28){
                System.out.println("ya");
            }else if (curIdx==29){
                System.out.println("za");
            }

          //  System.out.println(curIdx);
        }


        // let Evaluation prints stats how often the right output had the
        // highest value
        Evaluation eval = new Evaluation(30);
        eval.eval(ds.getLabels(), predictionOutput);
        System.out.println(eval.stats());

    }
}
