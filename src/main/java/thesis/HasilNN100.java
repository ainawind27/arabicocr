package thesis;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.Layer;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration.ListBuilder;
import org.deeplearning4j.nn.conf.distribution.UniformDistribution;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer.Builder;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;

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
public class HasilNN100 {
    public static void main(String[] args) {

        // list off input values, 4 training samples with data for 2
        // input-neurons each
        INDArray input = Nd4j.zeros(30, 12);//input untuk setiap sampel

        // correspondending list with expected output values, 4 training samples
        // with data for 2 output-neurons each
        INDArray labels = Nd4j.zeros(30, 30);//output untuk setiap sampel

        // create first dataset
        // input alif
        input.putScalar(new int[]{0, 0}, 0.0/3.0);
        input.putScalar(new int[]{0, 1}, 0.0/2.0);
        input.putScalar(new int[]{0, 2}, 5.0/7.0);
        input.putScalar(new int[]{0, 3}, 5.0/7.0);
        input.putScalar(new int[]{0, 4}, 5.0/7.0);
        input.putScalar(new int[]{0, 5}, 5.0/7.0);
        input.putScalar(new int[]{0, 6}, 5.0/7.0);
        input.putScalar(new int[]{0, 7}, 5.0/7.0);
        input.putScalar(new int[]{0, 8}, 5.0/7.0);
        input.putScalar(new int[]{0, 9}, 5.0/7.0);
        input.putScalar(new int[]{0, 10}, 5.0/7.0);
        input.putScalar(new int[]{0, 11}, 5.0/7.0);
        // then the first output fires for false, and the second is 0 (see class
        // comment)
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


        // input 1
        input.putScalar(new int[]{1, 0}, 0.0/3.0);
        input.putScalar(new int[]{1, 1}, 0.0/2.0);
        input.putScalar(new int[]{1, 2}, 7.0/7.0);
        input.putScalar(new int[]{1, 3}, 7.0/7.0);
        input.putScalar(new int[]{1, 4}, 6.0/7.0);
        input.putScalar(new int[]{1, 5}, 4.0/7.0);
        input.putScalar(new int[]{1, 6}, 4.0/7.0);
        input.putScalar(new int[]{1, 7}, 4.0/7.0);
        input.putScalar(new int[]{1, 8}, 3.0/7.0);
        input.putScalar(new int[]{1, 9}, 3.0/7.0);
        input.putScalar(new int[]{1, 10}, 2.0/7.0);
        input.putScalar(new int[]{1, 11}, 2.0/7.0);


        labels.putScalar(new int[]{1, 1}, 1);


        // input 2
        input.putScalar(new int[]{2, 0}, 1.0/3.0);
        input.putScalar(new int[]{2, 1}, 2.0/2.0);
        input.putScalar(new int[]{2, 2}, 5.0/7.0);
        input.putScalar(new int[]{2, 3}, 5.0/7.0);
        input.putScalar(new int[]{2, 4}, 3.0/7.0);
        input.putScalar(new int[]{2, 5}, 3.0/7.0);
        input.putScalar(new int[]{2, 6}, 3.0/7.0);
        input.putScalar(new int[]{2, 7}, 3.0/7.0);
        input.putScalar(new int[]{2, 8}, 3.0/7.0);
        input.putScalar(new int[]{2, 9}, 3.0/7.0);
        input.putScalar(new int[]{2, 10}, 1.0/7.0);
        input.putScalar(new int[]{2, 11}, 1.0/7.0);


        labels.putScalar(new int[]{2, 2}, 1);


        // input 3
        input.putScalar(new int[]{3, 0}, 0.0/3.0);
        input.putScalar(new int[]{3, 1}, 0.0/2.0);
        input.putScalar(new int[]{3, 2}, 4.0/7.0);
        input.putScalar(new int[]{3, 3}, 4.0/7.0);
        input.putScalar(new int[]{3, 4}, 5.0/7.0);
        input.putScalar(new int[]{3, 5}, 5.0/7.0);
        input.putScalar(new int[]{3, 6}, 5.0/7.0);
        input.putScalar(new int[]{3, 7}, 6.0/7.0);
        input.putScalar(new int[]{3, 8}, 7.0/7.0);
        input.putScalar(new int[]{3, 9}, 7.0/7.0);
        input.putScalar(new int[]{3, 10}, 7.0/7.0);
        input.putScalar(new int[]{3, 11}, 7.0/7.0);

        labels.putScalar(new int[]{3, 3}, 1);

        // input 4
        input.putScalar(new int[]{4, 0}, 1.0/3.0);
        input.putScalar(new int[]{4, 1}, 0.0/2.0);
        input.putScalar(new int[]{4, 2}, 5.0/7.0);
        input.putScalar(new int[]{4, 3}, 5.0/7.0);
        input.putScalar(new int[]{4, 4}, 3.0/7.0);
        input.putScalar(new int[]{4, 5}, 2.0/7.0);
        input.putScalar(new int[]{4, 6}, 3.0/7.0);
        input.putScalar(new int[]{4, 7}, 5.0/7.0);
        input.putScalar(new int[]{4, 8}, 7.0/7.0);
        input.putScalar(new int[]{4, 9}, 7.0/7.0);
        input.putScalar(new int[]{4, 10}, 7.0/7.0);
        input.putScalar(new int[]{4, 11}, 7.0/7.0);

        labels.putScalar(new int[]{4, 4}, 1);

        // input 5
        input.putScalar(new int[]{5, 0}, 1.0/3.0);
        input.putScalar(new int[]{5, 1}, 0.0/2.0);
        input.putScalar(new int[]{5, 2}, 4.0/7.0);
        input.putScalar(new int[]{5, 3}, 4.0/7.0);
        input.putScalar(new int[]{5, 4}, 5.0/7.0);
        input.putScalar(new int[]{5, 5}, 5.0/7.0);
        input.putScalar(new int[]{5, 6}, 5.0/7.0);
        input.putScalar(new int[]{5, 7}, 6.0/7.0);
        input.putScalar(new int[]{5, 8}, 7.0/7.0);
        input.putScalar(new int[]{5, 9}, 7.0/7.0);
        input.putScalar(new int[]{5, 10}, 7.0/7.0);
        input.putScalar(new int[]{5, 11}, 7.0/7.0);

        labels.putScalar(new int[]{5, 5}, 1);

        // input 6
        input.putScalar(new int[]{6, 0}, 1.0/3.0);
        input.putScalar(new int[]{6, 1}, 0.0/2.0);
        input.putScalar(new int[]{6, 2}, 5.0/7.0);
        input.putScalar(new int[]{6, 3}, 5.0/7.0);
        input.putScalar(new int[]{6, 4}, 5.0/7.0);
        input.putScalar(new int[]{6, 5}, 3.0/7.0);
        input.putScalar(new int[]{6, 6}, 3.0/7.0);
        input.putScalar(new int[]{6, 7}, 5.0/7.0);
        input.putScalar(new int[]{6, 8}, 7.0/7.0);
        input.putScalar(new int[]{6, 9}, 7.0/7.0);
        input.putScalar(new int[]{6, 10}, 7.0/7.0);
        input.putScalar(new int[]{6, 11}, 2.0/7.0);

        labels.putScalar(new int[]{6, 6}, 1);

        // input 7
        input.putScalar(new int[]{7, 0}, 1.0/3.0);
        input.putScalar(new int[]{7, 1}, 0.0/2.0);
        input.putScalar(new int[]{7, 2}, 5.0/7.0);
        input.putScalar(new int[]{7, 3}, 4.0/7.0);
        input.putScalar(new int[]{7, 4}, 3.0/7.0);
        input.putScalar(new int[]{7, 5}, 1.0/7.0);
        input.putScalar(new int[]{7, 6}, 0.0/7.0);
        input.putScalar(new int[]{7, 7}, 7.0/7.0);
        input.putScalar(new int[]{7, 8}, 6.0/7.0);
        input.putScalar(new int[]{7, 9}, 5.0/7.0);
        input.putScalar(new int[]{7, 10}, 4.0/7.0);
        input.putScalar(new int[]{7, 11}, 3.0/7.0);

        labels.putScalar(new int[]{7, 7}, 1);

        /// input 8
        input.putScalar(new int[]{8, 0}, 1.0/3.0);
        input.putScalar(new int[]{8, 1}, 0.0/2.0);
        input.putScalar(new int[]{8, 2}, 7.0/7.0);
        input.putScalar(new int[]{8, 3}, 7.0/7.0);
        input.putScalar(new int[]{8, 4}, 7.0/7.0);
        input.putScalar(new int[]{8, 5}, 6.0/7.0);
        input.putScalar(new int[]{8, 6}, 5.0/7.0);
        input.putScalar(new int[]{8, 7}, 4.0/7.0);
        input.putScalar(new int[]{8, 8}, 4.0/7.0);
        input.putScalar(new int[]{8, 9}, 4.0/7.0);
        input.putScalar(new int[]{8, 10}, 3.0/7.0);
        input.putScalar(new int[]{8, 11}, 2.0/7.0);

        labels.putScalar(new int[]{8, 8}, 1);

        // input 9
        input.putScalar(new int[]{9, 0}, 0.0/3.0);
        input.putScalar(new int[]{9, 1}, 0.0/2.0);
        input.putScalar(new int[]{9, 2}, 4.0/7.0);
        input.putScalar(new int[]{9, 3}, 4.0/7.0);
        input.putScalar(new int[]{9, 4}, 5.0/7.0);
        input.putScalar(new int[]{9, 5}, 5.0/7.0);
        input.putScalar(new int[]{9, 6}, 6.0/7.0);
        input.putScalar(new int[]{9, 7}, 7.0/7.0);
        input.putScalar(new int[]{9, 8}, 7.0/7.0);
        input.putScalar(new int[]{9, 9}, 1.0/7.0);
        input.putScalar(new int[]{9, 10}, 1.0/7.0);
        input.putScalar(new int[]{9, 11}, 2.0/7.0);

        labels.putScalar(new int[]{9, 9}, 1);

        // input 10
        input.putScalar(new int[]{10, 0}, 0.0/3.0);
        input.putScalar(new int[]{10, 1}, 0.0/2.0);
        input.putScalar(new int[]{10, 2}, 5.0/7.0);
        input.putScalar(new int[]{10, 3}, 5.0/7.0);
        input.putScalar(new int[]{10, 4}, 2.0/7.0);
        input.putScalar(new int[]{10, 5}, 2.0/7.0);
        input.putScalar(new int[]{10, 6}, 2.0/7.0);
        input.putScalar(new int[]{10, 7}, 3.0/7.0);
        input.putScalar(new int[]{10, 8}, 4.0/7.0);
        input.putScalar(new int[]{10, 9}, 5.0/7.0);
        input.putScalar(new int[]{10, 10}, 5.0/7.0);
        input.putScalar(new int[]{10, 11}, 5.0/7.0);

        labels.putScalar(new int[]{10, 10}, 1);

        // input 11
        input.putScalar(new int[]{11, 0}, 1.0/3.0);
        input.putScalar(new int[]{11, 1}, 1.0/2.0);
        input.putScalar(new int[]{11, 2}, 5.0/7.0);
        input.putScalar(new int[]{11, 3}, 5.0/7.0);
        input.putScalar(new int[]{11, 4}, 2.0/7.0);
        input.putScalar(new int[]{11, 5}, 2.0/7.0);
        input.putScalar(new int[]{11, 6}, 2.0/7.0);
        input.putScalar(new int[]{11, 7}, 3.0/7.0);
        input.putScalar(new int[]{11, 8}, 4.0/7.0);
        input.putScalar(new int[]{11, 9}, 5.0/7.0);
        input.putScalar(new int[]{11, 10}, 5.0/7.0);
        input.putScalar(new int[]{11, 11}, 5.0/7.0);

        labels.putScalar(new int[]{11, 10}, 1);

        // input 12
        input.putScalar(new int[]{12, 0}, 0.0/3.0);
        input.putScalar(new int[]{12, 1}, 0.0/2.0);
        input.putScalar(new int[]{12, 2}, 3.0/7.0);
        input.putScalar(new int[]{12, 3}, 5.0/7.0);
        input.putScalar(new int[]{12, 4}, 5.0/7.0);
        input.putScalar(new int[]{12, 5}, 5.0/7.0);
        input.putScalar(new int[]{12, 6}, 5.0/7.0);
        input.putScalar(new int[]{12, 7}, 7.0/7.0);
        input.putScalar(new int[]{12, 8}, 7.0/7.0);
        input.putScalar(new int[]{12, 9}, 7.0/7.0);
        input.putScalar(new int[]{12, 10}, 7.0/7.0);
        input.putScalar(new int[]{12, 11}, 7.0/7.0);

        labels.putScalar(new int[]{12, 12}, 1);

        // input 13
        input.putScalar(new int[]{13, 0}, 1.0/3.0);
        input.putScalar(new int[]{13, 1}, 0.0/2.0);
        input.putScalar(new int[]{13, 2}, 5.0/7.0);
        input.putScalar(new int[]{13, 3}, 5.0/7.0);
        input.putScalar(new int[]{13, 4}, 2.0/7.0);
        input.putScalar(new int[]{13, 5}, 2.0/7.0);
        input.putScalar(new int[]{13, 6}, 2.0/7.0);
        input.putScalar(new int[]{13, 7}, 3.0/7.0);
        input.putScalar(new int[]{13, 8}, 4.0/7.0);
        input.putScalar(new int[]{13, 9}, 5.0/7.0);
        input.putScalar(new int[]{13, 10}, 5.0/7.0);
        input.putScalar(new int[]{13, 11}, 5.0/7.0);

        labels.putScalar(new int[]{13, 13}, 1);

        // input 14
        input.putScalar(new int[]{14, 0}, 0.0/3.0);
        input.putScalar(new int[]{14, 1}, 0.0/2.0);
        input.putScalar(new int[]{14, 2}, 5.0/7.0);
        input.putScalar(new int[]{14, 3}, 5.0/7.0);
        input.putScalar(new int[]{14, 4}, 5.0/7.0);
        input.putScalar(new int[]{14, 5}, 5.0/7.0);
        input.putScalar(new int[]{14, 6}, 5.0/7.0);
        input.putScalar(new int[]{14, 7}, 6.0/7.0);
        input.putScalar(new int[]{14, 8}, 7.0/7.0);
        input.putScalar(new int[]{14, 9}, 7.0/7.0);
        input.putScalar(new int[]{14, 10}, 1.0/7.0);
        input.putScalar(new int[]{14, 11}, 1.0/7.0);

        labels.putScalar(new int[]{14, 14}, 1);

        // input 15
        input.putScalar(new int[]{15, 0}, 0.0/3.0);
        input.putScalar(new int[]{15, 1}, 0.0/2.0);
        input.putScalar(new int[]{15, 2}, 3.0/7.0);
        input.putScalar(new int[]{15, 3}, 4.0/7.0);
        input.putScalar(new int[]{15, 4}, 5.0/7.0);
        input.putScalar(new int[]{15, 5}, 7.0/7.0);
        input.putScalar(new int[]{15, 6}, 7.0/7.0);
        input.putScalar(new int[]{15, 7}, 7.0/7.0);
        input.putScalar(new int[]{15, 8}, 5.0/7.0);
        input.putScalar(new int[]{15, 9}, 5.0/7.0);
        input.putScalar(new int[]{15, 10}, 5.0/7.0);
        input.putScalar(new int[]{15, 11}, 5.0/7.0);

        labels.putScalar(new int[]{15, 15}, 1);

        // input 16
        input.putScalar(new int[]{16, 0}, 1.0/3.0);
        input.putScalar(new int[]{16, 1}, 0.0/2.0);
        input.putScalar(new int[]{16, 2}, 5.0/7.0);
        input.putScalar(new int[]{16, 3}, 5.0/7.0);
        input.putScalar(new int[]{16, 4}, 5.0/7.0);
        input.putScalar(new int[]{16, 5}, 6.0/7.0);
        input.putScalar(new int[]{16, 6}, 7.0/7.0);
        input.putScalar(new int[]{16, 7}, 7.0/7.0);
        input.putScalar(new int[]{16, 8}, 7.0/7.0);
        input.putScalar(new int[]{16, 9}, 1.0/7.0);
        input.putScalar(new int[]{16, 10}, 1.0/7.0);
        input.putScalar(new int[]{16, 11}, 1.0/7.0);

        labels.putScalar(new int[]{16, 16}, 1);

        // input 17
        input.putScalar(new int[]{17, 0}, 2.0/3.0);
        input.putScalar(new int[]{17, 1}, 0.0/2.0);
        input.putScalar(new int[]{17, 2}, 5.0/7.0);
        input.putScalar(new int[]{17, 3}, 5.0/7.0);
        input.putScalar(new int[]{17, 4}, 3.0/7.0);
        input.putScalar(new int[]{17, 5}, 3.0/7.0);
        input.putScalar(new int[]{17, 6}, 3.0/7.0);
        input.putScalar(new int[]{17, 7}, 1.0/7.0);
        input.putScalar(new int[]{17, 8}, 1.0/7.0);
        input.putScalar(new int[]{17, 9}, 7.0/7.0);
        input.putScalar(new int[]{17, 10}, 5.0/7.0);
        input.putScalar(new int[]{17, 11}, 3.0/7.0);

        labels.putScalar(new int[]{17, 17}, 1);

        // input 18
        input.putScalar(new int[]{18, 0}, 0.0/3.0);
        input.putScalar(new int[]{18, 1}, 0.0/2.0);
        input.putScalar(new int[]{18, 2}, 5.0/7.0);
        input.putScalar(new int[]{18, 3}, 5.0/7.0);
        input.putScalar(new int[]{18, 4}, 5.0/7.0);
        input.putScalar(new int[]{18, 5}, 5.0/7.0);
        input.putScalar(new int[]{18, 6}, 6.0/7.0);
        input.putScalar(new int[]{18, 7}, 6.0/7.0);
        input.putScalar(new int[]{18, 8}, 7.0/7.0);
        input.putScalar(new int[]{18, 9}, 7.0/7.0);
        input.putScalar(new int[]{18, 10}, 7.0/7.0);
        input.putScalar(new int[]{18, 11}, 7.0/7.0);

        labels.putScalar(new int[]{18, 18}, 1);

        // input 19
        input.putScalar(new int[]{19, 0}, 0.0/3.0);
        input.putScalar(new int[]{19, 1}, 0.0/2.0);
        input.putScalar(new int[]{19, 2}, 5.0/7.0);
        input.putScalar(new int[]{19, 3}, 5.0/7.0);
        input.putScalar(new int[]{19, 4}, 3.0/7.0);
        input.putScalar(new int[]{19, 5}, 1.0/7.0);
        input.putScalar(new int[]{19, 6}, 3.0/7.0);
        input.putScalar(new int[]{19, 7}, 5.0/7.0);
        input.putScalar(new int[]{19, 8}, 7.0/7.0);
        input.putScalar(new int[]{19, 9}, 7.0/7.0);
        input.putScalar(new int[]{19, 10}, 7.0/7.0);
        input.putScalar(new int[]{19, 11}, 7.0/7.0);

        labels.putScalar(new int[]{19, 19}, 1);

        // input 20
        input.putScalar(new int[]{20, 0}, 0.0/3.0);
        input.putScalar(new int[]{20, 1}, 0.0/2.0);
        input.putScalar(new int[]{20, 2}, 5.0/7.0);
        input.putScalar(new int[]{20, 3}, 5.0/7.0);
        input.putScalar(new int[]{20, 4}, 5.0/7.0);
        input.putScalar(new int[]{20, 5}, 5.0/7.0);
        input.putScalar(new int[]{20, 6}, 6.0/7.0);
        input.putScalar(new int[]{20, 7}, 7.0/7.0);
        input.putScalar(new int[]{20, 8}, 0.0/7.0);
        input.putScalar(new int[]{20, 9}, 1.0/7.0);
        input.putScalar(new int[]{20, 10}, 1.0/7.0);
        input.putScalar(new int[]{20, 11}, 1.0/7.0);

        labels.putScalar(new int[]{20, 20}, 1);

        // input 21
        input.putScalar(new int[]{21, 0}, 3.0/3.0);
        input.putScalar(new int[]{21, 1}, 0.0/2.0);
        input.putScalar(new int[]{21, 2}, 5.0/7.0);
        input.putScalar(new int[]{21, 3}, 5.0/7.0);
        input.putScalar(new int[]{21, 4}, 5.0/7.0);
        input.putScalar(new int[]{21, 5}, 5.0/7.0);
        input.putScalar(new int[]{21, 6}, 4.0/7.0);
        input.putScalar(new int[]{21, 7}, 3.0/7.0);
        input.putScalar(new int[]{21, 8}, 3.0/7.0);
        input.putScalar(new int[]{21, 9}, 3.0/7.0);
        input.putScalar(new int[]{21, 10}, 1.0/7.0);
        input.putScalar(new int[]{21, 11}, 1.0/7.0);

        labels.putScalar(new int[]{21, 21}, 1);

        // input 22
        input.putScalar(new int[]{22, 0}, 2.0/3.0);
        input.putScalar(new int[]{22, 1}, 0.0/2.0);
        input.putScalar(new int[]{22, 2}, 4.0/7.0);
        input.putScalar(new int[]{22, 3}, 4.0/7.0);
        input.putScalar(new int[]{22, 4}, 5.0/7.0);
        input.putScalar(new int[]{22, 5}, 5.0/7.0);
        input.putScalar(new int[]{22, 6}, 6.0/7.0);
        input.putScalar(new int[]{22, 7}, 7.0/7.0);
        input.putScalar(new int[]{22, 8}, 7.0/7.0);
        input.putScalar(new int[]{22, 9}, 1.0/7.0);
        input.putScalar(new int[]{22, 10}, 1.0/7.0);
        input.putScalar(new int[]{22, 11}, 1.0/7.0);

        labels.putScalar(new int[]{22, 22}, 1);

        // input 23
        input.putScalar(new int[]{23, 0}, 2.0/3.0);
        input.putScalar(new int[]{23, 1}, 0.0/2.0);
        input.putScalar(new int[]{23, 2}, 5.0/7.0);
        input.putScalar(new int[]{23, 3}, 5.0/7.0);
        input.putScalar(new int[]{23, 4}, 3.0/7.0);
        input.putScalar(new int[]{23, 5}, 3.0/7.0);
        input.putScalar(new int[]{23, 6}, 3.0/7.0);
        input.putScalar(new int[]{23, 7}, 3.0/7.0);
        input.putScalar(new int[]{23, 8}, 3.0/7.0);
        input.putScalar(new int[]{23, 9}, 3.0/7.0);
        input.putScalar(new int[]{23, 10}, 1.0/7.0);
        input.putScalar(new int[]{23, 11}, 1.0/7.0);

        labels.putScalar(new int[]{23, 23}, 1);

        // input 24
        input.putScalar(new int[]{24, 0}, 0.0/3.0);
        input.putScalar(new int[]{24, 1}, 0.0/2.0);
        input.putScalar(new int[]{24, 2}, 5.0/7.0);
        input.putScalar(new int[]{24, 3}, 5.0/7.0);
        input.putScalar(new int[]{24, 4}, 5.0/7.0);
        input.putScalar(new int[]{24, 5}, 3.0/7.0);
        input.putScalar(new int[]{24, 6}, 3.0/7.0);
        input.putScalar(new int[]{24, 7}, 5.0/7.0);
        input.putScalar(new int[]{24, 8}, 7.0/7.0);
        input.putScalar(new int[]{24, 9}, 7.0/7.0);
        input.putScalar(new int[]{24, 10}, 7.0/7.0);
        input.putScalar(new int[]{24, 11}, 2.0/7.0);

        labels.putScalar(new int[]{24, 24}, 1);

        // input 25
        input.putScalar(new int[]{25, 0}, 3.0/3.0);
        input.putScalar(new int[]{25, 1}, 0.0/2.0);
        input.putScalar(new int[]{25, 2}, 5.0/7.0);
        input.putScalar(new int[]{25, 3}, 5.0/7.0);
        input.putScalar(new int[]{25, 4}, 3.0/7.0);
        input.putScalar(new int[]{25, 5}, 3.0/7.0);
        input.putScalar(new int[]{25, 6}, 3.0/7.0);
        input.putScalar(new int[]{25, 7}, 3.0/7.0);
        input.putScalar(new int[]{25, 8}, 3.0/7.0);
        input.putScalar(new int[]{25, 9}, 3.0/7.0);
        input.putScalar(new int[]{25, 10}, 1.0/7.0);
        input.putScalar(new int[]{25, 11}, 1.0/7.0);

        labels.putScalar(new int[]{25, 25}, 1);

        // input 26
        input.putScalar(new int[]{26, 0}, 0.0/3.0);
        input.putScalar(new int[]{26, 1}, 0.0/2.0);
        input.putScalar(new int[]{26, 2}, 3.0/7.0);
        input.putScalar(new int[]{26, 3}, 3.0/7.0);
        input.putScalar(new int[]{26, 4}, 3.0/7.0);
        input.putScalar(new int[]{26, 5}, 2.0/7.0);
        input.putScalar(new int[]{26, 6}, 2.0/7.0);
        input.putScalar(new int[]{26, 7}, 2.0/7.0);
        input.putScalar(new int[]{26, 8}, 1.0/7.0);
        input.putScalar(new int[]{26, 9}, 6.0/7.0);
        input.putScalar(new int[]{26, 10}, 4.0/7.0);
        input.putScalar(new int[]{26, 11}, 3.0/7.0);

        labels.putScalar(new int[]{26, 26}, 1);

        // input 27
        input.putScalar(new int[]{27, 0}, 2.0/3.0);
        input.putScalar(new int[]{27, 1}, 2.0/2.0);
        input.putScalar(new int[]{27, 2}, 0.0/7.0);
        input.putScalar(new int[]{27, 3}, 6.0/7.0);
        input.putScalar(new int[]{27, 4}, 5.0/7.0);
        input.putScalar(new int[]{27, 5}, 3.0/7.0);
        input.putScalar(new int[]{27, 6}, 5.0/7.0);
        input.putScalar(new int[]{27, 7}, 7.0/7.0);
        input.putScalar(new int[]{27, 8}, 7.0/7.0);
        input.putScalar(new int[]{27, 9}, 7.0/7.0);
        input.putScalar(new int[]{27, 10}, 1.0/7.0);
        input.putScalar(new int[]{27, 11}, 1.0/7.0);

        labels.putScalar(new int[]{27, 27}, 1);

        // input 28
        input.putScalar(new int[]{28, 0}, 1.0/3.0);
        input.putScalar(new int[]{28, 1}, 0.0/2.0);
        input.putScalar(new int[]{28, 2}, 5.0/7.0);
        input.putScalar(new int[]{28, 3}, 5.0/7.0);
        input.putScalar(new int[]{28, 4}, 5.0/7.0);
        input.putScalar(new int[]{28, 5}, 5.0/7.0);
        input.putScalar(new int[]{28, 6}, 6.0/7.0);
        input.putScalar(new int[]{28, 7}, 6.0/7.0);
        input.putScalar(new int[]{28, 8}, 7.0/7.0);
        input.putScalar(new int[]{28, 9}, 7.0/7.0);
        input.putScalar(new int[]{28, 10}, 7.0/7.0);
        input.putScalar(new int[]{28, 11}, 7.0/7.0);

        labels.putScalar(new int[]{28, 28}, 1);

        // input 29
        input.putScalar(new int[]{29, 0}, 0.0/3.0);
        input.putScalar(new int[]{29, 1}, 0.0/2.0);
        input.putScalar(new int[]{29, 2}, 4.0/7.0);
        input.putScalar(new int[]{29, 3}, 4.0/7.0);
        input.putScalar(new int[]{29, 4}, 4.0/7.0);
        input.putScalar(new int[]{29, 5}, 4.0/7.0);
        input.putScalar(new int[]{29, 6}, 4.0/7.0);
        input.putScalar(new int[]{29, 7}, 1.0/7.0);
        input.putScalar(new int[]{29, 8}, 1.0/7.0);
        input.putScalar(new int[]{29, 9}, 1.0/7.0);
        input.putScalar(new int[]{29, 10}, 1.0/7.0);
        input.putScalar(new int[]{29, 11}, 1.0/7.0);

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
        builder.iterations(500);
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
        Builder outputLayerBuilder = new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD);
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
        // segment ke-0: alif
        predictionInput.putScalar(new int[]{0, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{0, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{0, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{0, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{0, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{0, 5}, 5.0/7.0);
        predictionInput.putScalar(new int[]{0, 6}, 5.0/7.0);
        predictionInput.putScalar(new int[]{0, 7}, 5.0/7.0);
        predictionInput.putScalar(new int[]{0, 8}, 5.0/7.0);
        predictionInput.putScalar(new int[]{0, 9}, 5.0/7.0);
        predictionInput.putScalar(new int[]{0, 10}, 5.0/7.0);
        predictionInput.putScalar(new int[]{0, 11}, 5.0/7.0);
//        // segment ke-1: lam
        predictionInput.putScalar(new int[]{1, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{1, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{1, 2}, 7.0/7.0);
        predictionInput.putScalar(new int[]{1, 3}, 7.0/7.0);
        predictionInput.putScalar(new int[]{1, 4}, 6.0/7.0);
        predictionInput.putScalar(new int[]{1, 5}, 4.0/7.0);
        predictionInput.putScalar(new int[]{1, 6}, 4.0/7.0);
        predictionInput.putScalar(new int[]{1, 7}, 4.0/7.0);
        predictionInput.putScalar(new int[]{1, 8}, 3.0/7.0);
        predictionInput.putScalar(new int[]{1, 9}, 3.0/7.0);
        predictionInput.putScalar(new int[]{1, 10}, 2.0/7.0);
        predictionInput.putScalar(new int[]{1, 11}, 2.0/7.0);

        // segment ke-2: lam
        predictionInput.putScalar(new int[]{2, 0}, 1.0/3.0);
        predictionInput.putScalar(new int[]{2, 1}, 2.0/2.0);
        predictionInput.putScalar(new int[]{2, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{2, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{2, 4}, 3.0/7.0);
        predictionInput.putScalar(new int[]{2, 5}, 3.0/7.0);
        predictionInput.putScalar(new int[]{2, 6}, 3.0/7.0);
        predictionInput.putScalar(new int[]{2, 7}, 3.0/7.0);
        predictionInput.putScalar(new int[]{2, 8}, 3.0/7.0);
        predictionInput.putScalar(new int[]{2, 9}, 3.0/7.0);
        predictionInput.putScalar(new int[]{2, 10}, 1.0/7.0);
        predictionInput.putScalar(new int[]{2, 11}, 1.0/7.0);
        // segment ke-3: ha
        predictionInput.putScalar(new int[]{3, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{3, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{3, 2}, 4.0/7.0);
        predictionInput.putScalar(new int[]{3, 3}, 4.0/7.0);
        predictionInput.putScalar(new int[]{3, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{3, 5}, 5.0/7.0);
        predictionInput.putScalar(new int[]{3, 6}, 5.0/7.0);
        predictionInput.putScalar(new int[]{3, 7}, 6.0/7.0);
        predictionInput.putScalar(new int[]{3, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{3, 9}, 7.0/7.0);
        predictionInput.putScalar(new int[]{3, 10}, 7.0/7.0);
        predictionInput.putScalar(new int[]{3, 11}, 7.0/7.0);

        // segment ke-4: ha
        predictionInput.putScalar(new int[]{4, 0}, 1.0/3.0);
        predictionInput.putScalar(new int[]{4, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{4, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{4, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{4, 4}, 3.0/7.0);
        predictionInput.putScalar(new int[]{4, 5}, 2.0/7.0);
        predictionInput.putScalar(new int[]{4, 6}, 3.0/7.0);
        predictionInput.putScalar(new int[]{4, 7}, 5.0/7.0);
        predictionInput.putScalar(new int[]{4, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{4, 9}, 7.0/7.0);
        predictionInput.putScalar(new int[]{4, 10}, 7.0/7.0);
        predictionInput.putScalar(new int[]{4, 11}, 7.0/7.0);

        // segment ke-5: ha
        predictionInput.putScalar(new int[]{5, 0}, 1.0/3.0);
        predictionInput.putScalar(new int[]{5, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{5, 2}, 4.0/7.0);
        predictionInput.putScalar(new int[]{5, 3}, 4.0/7.0);
        predictionInput.putScalar(new int[]{5, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{5, 5}, 5.0/7.0);
        predictionInput.putScalar(new int[]{5, 6}, 5.0/7.0);
        predictionInput.putScalar(new int[]{5, 7}, 6.0/7.0);
        predictionInput.putScalar(new int[]{5, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{5, 9}, 7.0/7.0);
        predictionInput.putScalar(new int[]{5, 10}, 7.0/7.0);
        predictionInput.putScalar(new int[]{5, 11}, 7.0/7.0);

        // segment ke-6: ha
        predictionInput.putScalar(new int[]{6, 0}, 1.0/3.0);
        predictionInput.putScalar(new int[]{6, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{6, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{6, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{6, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{6, 5}, 3.0/7.0);
        predictionInput.putScalar(new int[]{6, 6}, 3.0/7.0);
        predictionInput.putScalar(new int[]{6, 7}, 5.0/7.0);
        predictionInput.putScalar(new int[]{6, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{6, 9}, 7.0/7.0);
        predictionInput.putScalar(new int[]{6, 10}, 7.0/7.0);
        predictionInput.putScalar(new int[]{6, 11}, 2.0/7.0);

        // segment ke-7: ha
        predictionInput.putScalar(new int[]{7, 0}, 1.0/3.0);
        predictionInput.putScalar(new int[]{7, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{7, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{7, 3}, 4.0/7.0);
        predictionInput.putScalar(new int[]{7, 4}, 3.0/7.0);
        predictionInput.putScalar(new int[]{7, 5}, 1.0/7.0);
        predictionInput.putScalar(new int[]{7, 6}, 0.0/7.0);
        predictionInput.putScalar(new int[]{7, 7}, 7.0/7.0);
        predictionInput.putScalar(new int[]{7, 8}, 6.0/7.0);
        predictionInput.putScalar(new int[]{7, 9}, 5.0/7.0);
        predictionInput.putScalar(new int[]{7, 10}, 4.0/7.0);
        predictionInput.putScalar(new int[]{7, 11}, 3.0/7.0);

        // segment ke-8: ha
        predictionInput.putScalar(new int[]{8, 0}, 1.0/3.0);
        predictionInput.putScalar(new int[]{8, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{8, 2}, 7.0/7.0);
        predictionInput.putScalar(new int[]{8, 3}, 7.0/7.0);
        predictionInput.putScalar(new int[]{8, 4}, 7.0/7.0);
        predictionInput.putScalar(new int[]{8, 5}, 6.0/7.0);
        predictionInput.putScalar(new int[]{8, 6}, 5.0/7.0);
        predictionInput.putScalar(new int[]{8, 7}, 4.0/7.0);
        predictionInput.putScalar(new int[]{8, 8}, 4.0/7.0);
        predictionInput.putScalar(new int[]{8, 9}, 4.0/7.0);
        predictionInput.putScalar(new int[]{8, 10}, 3.0/7.0);
        predictionInput.putScalar(new int[]{8, 11}, 2.0/7.0);

        // segment ke-9: ha
        predictionInput.putScalar(new int[]{9, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{9, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{9, 2}, 4.0/7.0);
        predictionInput.putScalar(new int[]{9, 3}, 4.0/7.0);
        predictionInput.putScalar(new int[]{9, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{9, 5}, 5.0/7.0);
        predictionInput.putScalar(new int[]{9, 6}, 6.0/7.0);
        predictionInput.putScalar(new int[]{9, 7}, 7.0/7.0);
        predictionInput.putScalar(new int[]{9, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{9, 9}, 1.0/7.0);
        predictionInput.putScalar(new int[]{9, 10}, 1.0/7.0);
        predictionInput.putScalar(new int[]{9, 11}, 2.0/7.0);

        // segment ke-10: ha
        predictionInput.putScalar(new int[]{10, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{10, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{10, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{10, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{10, 4}, 2.0/7.0);
        predictionInput.putScalar(new int[]{10, 5}, 2.0/7.0);
        predictionInput.putScalar(new int[]{10, 6}, 2.0/7.0);
        predictionInput.putScalar(new int[]{10, 7}, 3.0/7.0);
        predictionInput.putScalar(new int[]{10, 8}, 4.0/7.0);
        predictionInput.putScalar(new int[]{10, 9}, 5.0/7.0);
        predictionInput.putScalar(new int[]{10, 10}, 5.0/7.0);
        predictionInput.putScalar(new int[]{10, 11}, 5.0/7.0);

        // segment ke-11: ha
        predictionInput.putScalar(new int[]{11, 0}, 1.0/3.0);
        predictionInput.putScalar(new int[]{11, 1}, 1.0/2.0);
        predictionInput.putScalar(new int[]{11, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{11, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{11, 4}, 2.0/7.0);
        predictionInput.putScalar(new int[]{11, 5}, 2.0/7.0);
        predictionInput.putScalar(new int[]{11, 6}, 2.0/7.0);
        predictionInput.putScalar(new int[]{11, 7}, 3.0/7.0);
        predictionInput.putScalar(new int[]{11, 8}, 4.0/7.0);
        predictionInput.putScalar(new int[]{11, 9}, 5.0/7.0);
        predictionInput.putScalar(new int[]{11, 10}, 5.0/7.0);
        predictionInput.putScalar(new int[]{11, 11}, 5.0/7.0);

        // segment ke-12: ha
        predictionInput.putScalar(new int[]{12, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{12, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{12, 2}, 3.0/7.0);
        predictionInput.putScalar(new int[]{12, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{12, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{12, 5}, 5.0/7.0);
        predictionInput.putScalar(new int[]{12, 6}, 5.0/7.0);
        predictionInput.putScalar(new int[]{12, 7}, 7.0/7.0);
        predictionInput.putScalar(new int[]{12, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{12, 9}, 7.0/7.0);
        predictionInput.putScalar(new int[]{12, 10}, 7.0/7.0);
        predictionInput.putScalar(new int[]{12, 11}, 7.0/7.0);

        // segment ke-13: ha
        predictionInput.putScalar(new int[]{13, 0}, 1.0/3.0);
        predictionInput.putScalar(new int[]{13, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{13, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{13, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{13, 4}, 2.0/7.0);
        predictionInput.putScalar(new int[]{13, 5}, 2.0/7.0);
        predictionInput.putScalar(new int[]{13, 6}, 2.0/7.0);
        predictionInput.putScalar(new int[]{13, 7}, 3.0/7.0);
        predictionInput.putScalar(new int[]{13, 8}, 4.0/7.0);
        predictionInput.putScalar(new int[]{13, 9}, 5.0/7.0);
        predictionInput.putScalar(new int[]{13, 10}, 5.0/7.0);
        predictionInput.putScalar(new int[]{13, 11}, 5.0/7.0);

        // segment ke-14: ha
        predictionInput.putScalar(new int[]{14, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{14, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{14, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{14, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{14, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{14, 5}, 5.0/7.0);
        predictionInput.putScalar(new int[]{14, 6}, 5.0/7.0);
        predictionInput.putScalar(new int[]{14, 7}, 6.0/7.0);
        predictionInput.putScalar(new int[]{14, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{14, 9}, 7.0/7.0);
        predictionInput.putScalar(new int[]{14, 10}, 1.0/7.0);
        predictionInput.putScalar(new int[]{14, 11}, 1.0/7.0);

        // segment ke-15: ha
        predictionInput.putScalar(new int[]{15, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{15, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{15, 2}, 3.0/7.0);
        predictionInput.putScalar(new int[]{15, 3}, 4.0/7.0);
        predictionInput.putScalar(new int[]{15, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{15, 5}, 7.0/7.0);
        predictionInput.putScalar(new int[]{15, 6}, 7.0/7.0);
        predictionInput.putScalar(new int[]{15, 7}, 7.0/7.0);
        predictionInput.putScalar(new int[]{15, 8}, 5.0/7.0);
        predictionInput.putScalar(new int[]{15, 9}, 5.0/7.0);
        predictionInput.putScalar(new int[]{15, 10}, 5.0/7.0);
        predictionInput.putScalar(new int[]{15, 11}, 5.0/7.0);

        // segment ke-16: ha
        predictionInput.putScalar(new int[]{16, 0}, 1.0/3.0);
        predictionInput.putScalar(new int[]{16, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{16, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{16, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{16, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{16, 5}, 6.0/7.0);
        predictionInput.putScalar(new int[]{16, 6}, 7.0/7.0);
        predictionInput.putScalar(new int[]{16, 7}, 7.0/7.0);
        predictionInput.putScalar(new int[]{16, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{16, 9}, 1.0/7.0);
        predictionInput.putScalar(new int[]{16, 10}, 1.0/7.0);
        predictionInput.putScalar(new int[]{16, 11}, 1.0/7.0);

        // segment ke-17: ha
        predictionInput.putScalar(new int[]{17, 0}, 2.0/3.0);
        predictionInput.putScalar(new int[]{17, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{17, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{17, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{17, 4}, 3.0/7.0);
        predictionInput.putScalar(new int[]{17, 5}, 3.0/7.0);
        predictionInput.putScalar(new int[]{17, 6}, 3.0/7.0);
        predictionInput.putScalar(new int[]{17, 7}, 1.0/7.0);
        predictionInput.putScalar(new int[]{17, 8}, 1.0/7.0);
        predictionInput.putScalar(new int[]{17, 9}, 7.0/7.0);
        predictionInput.putScalar(new int[]{17, 10}, 5.0/7.0);
        predictionInput.putScalar(new int[]{17, 11}, 3.0/7.0);

        // segment ke-18: ha
        predictionInput.putScalar(new int[]{18, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{18, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{18, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{18, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{18, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{18, 5}, 5.0/7.0);
        predictionInput.putScalar(new int[]{18, 6}, 6.0/7.0);
        predictionInput.putScalar(new int[]{18, 7}, 6.0/7.0);
        predictionInput.putScalar(new int[]{18, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{18, 9}, 7.0/7.0);
        predictionInput.putScalar(new int[]{18, 10}, 7.0/7.0);
        predictionInput.putScalar(new int[]{18, 11}, 7.0/7.0);

        // segment ke-19: ha
        predictionInput.putScalar(new int[]{19, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{19, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{19, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{19, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{19, 4}, 3.0/7.0);
        predictionInput.putScalar(new int[]{19, 5}, 1.0/7.0);
        predictionInput.putScalar(new int[]{19, 6}, 3.0/7.0);
        predictionInput.putScalar(new int[]{19, 7}, 5.0/7.0);
        predictionInput.putScalar(new int[]{19, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{19, 9}, 7.0/7.0);
        predictionInput.putScalar(new int[]{19, 10}, 7.0/7.0);
        predictionInput.putScalar(new int[]{19, 11}, 7.0/7.0);

        // segment ke-20: ha
        predictionInput.putScalar(new int[]{20, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{20, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{20, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{20, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{20, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{20, 5}, 5.0/7.0);
        predictionInput.putScalar(new int[]{20, 6}, 6.0/7.0);
        predictionInput.putScalar(new int[]{20, 7}, 7.0/7.0);
        predictionInput.putScalar(new int[]{20, 8}, 0.0/7.0);
        predictionInput.putScalar(new int[]{20, 9}, 1.0/7.0);
        predictionInput.putScalar(new int[]{20, 10}, 1.0/7.0);
        predictionInput.putScalar(new int[]{20, 11}, 1.0/7.0);

        // segment ke-21: ha
        predictionInput.putScalar(new int[]{21, 0}, 3.0/3.0);
        predictionInput.putScalar(new int[]{21, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{21, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{21, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{21, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{21, 5}, 5.0/7.0);
        predictionInput.putScalar(new int[]{21, 6}, 4.0/7.0);
        predictionInput.putScalar(new int[]{21, 7}, 3.0/7.0);
        predictionInput.putScalar(new int[]{21, 8}, 3.0/7.0);
        predictionInput.putScalar(new int[]{21, 9}, 3.0/7.0);
        predictionInput.putScalar(new int[]{21, 10}, 1.0/7.0);
        predictionInput.putScalar(new int[]{21, 11}, 1.0/7.0);

        // segment ke-22: ha
        predictionInput.putScalar(new int[]{22, 0}, 2.0/3.0);
        predictionInput.putScalar(new int[]{22, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{22, 2}, 4.0/7.0);
        predictionInput.putScalar(new int[]{22, 3}, 4.0/7.0);
        predictionInput.putScalar(new int[]{22, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{22, 5}, 5.0/7.0);
        predictionInput.putScalar(new int[]{22, 6}, 6.0/7.0);
        predictionInput.putScalar(new int[]{22, 7}, 7.0/7.0);
        predictionInput.putScalar(new int[]{22, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{22, 9}, 1.0/7.0);
        predictionInput.putScalar(new int[]{22, 10}, 1.0/7.0);
        predictionInput.putScalar(new int[]{22, 11}, 1.0/7.0);

        // segment ke-23: ha
        predictionInput.putScalar(new int[]{23, 0}, 2.0/3.0);
        predictionInput.putScalar(new int[]{23, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{23, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{23, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{23, 4}, 3.0/7.0);
        predictionInput.putScalar(new int[]{23, 5}, 3.0/7.0);
        predictionInput.putScalar(new int[]{23, 6}, 3.0/7.0);
        predictionInput.putScalar(new int[]{23, 7}, 3.0/7.0);
        predictionInput.putScalar(new int[]{23, 8}, 3.0/7.0);
        predictionInput.putScalar(new int[]{23, 9}, 3.0/7.0);
        predictionInput.putScalar(new int[]{23, 10}, 1.0/7.0);
        predictionInput.putScalar(new int[]{23, 11}, 1.0/7.0);

        // segment ke-24: ha
        predictionInput.putScalar(new int[]{24, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{24, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{24, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{24, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{24, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{24, 5}, 3.0/7.0);
        predictionInput.putScalar(new int[]{24, 6}, 3.0/7.0);
        predictionInput.putScalar(new int[]{24, 7}, 5.0/7.0);
        predictionInput.putScalar(new int[]{24, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{24, 9}, 7.0/7.0);
        predictionInput.putScalar(new int[]{24, 10}, 7.0/7.0);
        predictionInput.putScalar(new int[]{24, 11}, 2.0/7.0);

        // segment ke-25: ha
        predictionInput.putScalar(new int[]{25, 0}, 3.0/3.0);
        predictionInput.putScalar(new int[]{25, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{25, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{25, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{25, 4}, 3.0/7.0);
        predictionInput.putScalar(new int[]{25, 5}, 3.0/7.0);
        predictionInput.putScalar(new int[]{25, 6}, 3.0/7.0);
        predictionInput.putScalar(new int[]{25, 7}, 3.0/7.0);
        predictionInput.putScalar(new int[]{25, 8}, 3.0/7.0);
        predictionInput.putScalar(new int[]{25, 9}, 3.0/7.0);
        predictionInput.putScalar(new int[]{25, 10}, 1.0/7.0);
        predictionInput.putScalar(new int[]{25, 11}, 1.0/7.0);

        // segment ke-26: ha
        predictionInput.putScalar(new int[]{26, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{26, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{26, 2}, 3.0/7.0);
        predictionInput.putScalar(new int[]{26, 3}, 3.0/7.0);
        predictionInput.putScalar(new int[]{26, 4}, 3.0/7.0);
        predictionInput.putScalar(new int[]{26, 5}, 2.0/7.0);
        predictionInput.putScalar(new int[]{26, 6}, 2.0/7.0);
        predictionInput.putScalar(new int[]{26, 7}, 2.0/7.0);
        predictionInput.putScalar(new int[]{26, 8}, 1.0/7.0);
        predictionInput.putScalar(new int[]{26, 9}, 6.0/7.0);
        predictionInput.putScalar(new int[]{26, 10}, 4.0/7.0);
        predictionInput.putScalar(new int[]{26, 11}, 3.0/7.0);

        // segment ke-27: ha
        predictionInput.putScalar(new int[]{27, 0}, 2.0/3.0);
        predictionInput.putScalar(new int[]{27, 1}, 2.0/2.0);
        predictionInput.putScalar(new int[]{27, 2}, 0.0/7.0);
        predictionInput.putScalar(new int[]{27, 3}, 6.0/7.0);
        predictionInput.putScalar(new int[]{27, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{27, 5}, 3.0/7.0);
        predictionInput.putScalar(new int[]{27, 6}, 5.0/7.0);
        predictionInput.putScalar(new int[]{27, 7}, 7.0/7.0);
        predictionInput.putScalar(new int[]{27, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{27, 9}, 7.0/7.0);
        predictionInput.putScalar(new int[]{27, 10}, 1.0/7.0);
        predictionInput.putScalar(new int[]{27, 11}, 1.0/7.0);

        // segment ke-28: ha
        predictionInput.putScalar(new int[]{28, 0}, 1.0/3.0);
        predictionInput.putScalar(new int[]{28, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{28, 2}, 5.0/7.0);
        predictionInput.putScalar(new int[]{28, 3}, 5.0/7.0);
        predictionInput.putScalar(new int[]{28, 4}, 5.0/7.0);
        predictionInput.putScalar(new int[]{28, 5}, 5.0/7.0);
        predictionInput.putScalar(new int[]{28, 6}, 6.0/7.0);
        predictionInput.putScalar(new int[]{28, 7}, 6.0/7.0);
        predictionInput.putScalar(new int[]{28, 8}, 7.0/7.0);
        predictionInput.putScalar(new int[]{28, 9}, 7.0/7.0);
        predictionInput.putScalar(new int[]{28, 10}, 7.0/7.0);
        predictionInput.putScalar(new int[]{28, 11}, 7.0/7.0);


        // segment ke-29: ha
        predictionInput.putScalar(new int[]{29, 0}, 0.0/3.0);
        predictionInput.putScalar(new int[]{29, 1}, 0.0/2.0);
        predictionInput.putScalar(new int[]{29, 2}, 4.0/7.0);
        predictionInput.putScalar(new int[]{29, 3}, 4.0/7.0);
        predictionInput.putScalar(new int[]{29, 4}, 4.0/7.0);
        predictionInput.putScalar(new int[]{29, 5}, 4.0/7.0);
        predictionInput.putScalar(new int[]{29, 6}, 4.0/7.0);
        predictionInput.putScalar(new int[]{29, 7}, 1.0/7.0);
        predictionInput.putScalar(new int[]{29, 8}, 1.0/7.0);
        predictionInput.putScalar(new int[]{29, 9}, 1.0/7.0);
        predictionInput.putScalar(new int[]{29, 10}, 1.0/7.0);
        predictionInput.putScalar(new int[]{29, 11}, 1.0/7.0);

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
                System.out.println("alif");
            }else if (curIdx==1){
                System.out.println("ain");
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
                System.out.println("ha besar");
            }else if (curIdx==10){
                System.out.println("ha");
            }else if (curIdx==11){
                System.out.println("jim");
            }else if(curIdx==12){
                System.out.println("kaf");
            }else if (curIdx==13){
                System.out.println("kha");
            }else if (curIdx==14){
                System.out.println("lam");
            }else if (curIdx==15){
                System.out.println("mim");
            }else if(curIdx==16){
                System.out.println("nun");
            }else if (curIdx==17){
                System.out.println("qaf");
            }else if (curIdx==18){
                System.out.println("ra");
            }else if (curIdx==19){
                System.out.println("sad");
            }else if (curIdx==20){
                System.out.println("sin");
            }else if (curIdx==21){
                System.out.println("sheen");
            }else if (curIdx==22){
                System.out.println("ta marbuto");
            }else if (curIdx==23){
                System.out.println("ta");
            }else if (curIdx==24){
                System.out.println("tho");
            }else if (curIdx==25){
                System.out.println("tsa");
            }else if (curIdx==26){
                System.out.println("wau");
            }else if (curIdx==27){
                System.out.println("ya");
            }else if (curIdx==28){
                System.out.println("zay");
            }else if (curIdx==29){
                System.out.println("alif_lam");
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
