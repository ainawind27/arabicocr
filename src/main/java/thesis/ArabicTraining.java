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
public class ArabicTraining {

	static final List<String> LABELS = ImmutableList.of(
			"ain", "alif", "ba", "dal", "dhad", 
			"dzal", "dzo", "fa", "ghoin", "hamzah",
			"ha", "habesar", "jim", "kaf", "kha",
			"lam", "mim", "nun", "qaf", "ra", "sad",
			"sheen", "sin", "tamarbuto", "ta", "tho",
			"tsa", "waw", "ya", "za","unknown"
			);
	
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
		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public int getLabelId() {
			return labelId;
		}

		public void setLabelId(int labelId) {
			this.labelId = labelId;
		}
	}

    static class ChainInfo {
        String chain;
        // 0 = di atas
        // 1 = di tengah
        // 2 = di bawah
        int yPos;
        
        public ChainInfo() {
			super();
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
        ObjectMapper mapper = new ObjectMapper();
        File featuresFile = new File("D:\\filetraining\\zhangsuen\\zhangsuen.Features.json");
        Features features = mapper.readValue(featuresFile, Features.class);
        
    	int trainingSampleCount = features.segments.size();
    	
        // list off input values, 4 training samples with data for 2
        // input-neurons each
        INDArray input = Nd4j.zeros(trainingSampleCount, 12);//input untuk setiap sampel

        // correspondending list with expected output values, 4 training samples
        // with data for 2 output-neurons each
        INDArray labels = Nd4j.zeros(trainingSampleCount, 31);//output untuk setiap sampel

        for (int i = 0; i<features.segments.size();i++){
        	Segment segment = features.segments.get(i); 
            
            // create first dataset
            // input ain
            //0 -> 2*(0/3) -1 = -1
            //1 -> 2*(1/3) -1 = -0.333333333333333333333333333
            //2 -> 2*(2/3) -1 = 0.3
            //3 -> 2*(3/3) -1 = +1

            input.putScalar(new int[]{i, 0}, 2.0*(segment.dotCount/3.0)-1.0);
            input.putScalar(new int[]{i, 1}, 2.0*(segment.dotPos/2.0)-1.0);
            input.putScalar(new int[]{i, 2}, 2.0*((segment.normalizedBodyChain[0]-1.0)/7.0)-1.0);
            input.putScalar(new int[]{i, 3}, 2.0*((segment.normalizedBodyChain[1]-1.0)/7.0)-1.0);
            input.putScalar(new int[]{i, 4}, 2.0*((segment.normalizedBodyChain[2]-1.0)/7.0)-1.0);
            input.putScalar(new int[]{i, 5}, 2.0*((segment.normalizedBodyChain[3]-1.0)/7.0)-1.0);
            input.putScalar(new int[]{i, 6}, 2.0*((segment.normalizedBodyChain[4]-1.0)/7.0)-1.0);
            input.putScalar(new int[]{i, 7}, 2.0*((segment.normalizedBodyChain[5]-1.0)/7.0)-1.0);
            input.putScalar(new int[]{i, 8}, 2.0*((segment.normalizedBodyChain[6]-1.0)/7.0)-1.0);
            input.putScalar(new int[]{i, 9}, 2.0*((segment.normalizedBodyChain[7]-1.0)/7.0)-1.0);
            input.putScalar(new int[]{i, 10}, 2.0*((segment.normalizedBodyChain[8]-1.0)/7.0)-1.0);
            input.putScalar(new int[]{i, 11}, 2.0*((segment.normalizedBodyChain[9]-1.0)/7.0)-1.0);
            // then the first output fires for false, and the second is 0 (see class
            // comment)
            
            labels.putScalar(new int[]{i, segment.labelId}, 1);
        }
        
        
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
        outputLayerBuilder.nOut(31); //jumlah output
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
        
        File modelFile = new File(featuresFile.getParentFile(), "nnmodel.zip");
        System.out.println("Writing model to " + modelFile);
        ModelSerializer.writeModel(net, modelFile, true);
    }
}
