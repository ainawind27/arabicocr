package thesis;

import com.google.common.collect.ImmutableList;
import org.apache.mahout.classifier.sequencelearning.hmm.HmmEvaluator;
import org.apache.mahout.classifier.sequencelearning.hmm.HmmModel;
import org.apache.mahout.classifier.sequencelearning.hmm.HmmTrainer;
import org.apache.mahout.classifier.sequencelearning.hmm.HmmUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Example: 
 * https://github.com/apache/mahout/blob/08e02602e947ff945b9bd73ab5f0b45863df3e53/examples/src/main/java/org/apache/mahout/classifier/sequencelearning/hmm/PosTagger.java
 * Created by ceefour on 14/01/2017.
 */
public class SentenceTagger {

    private static Logger log = LoggerFactory.getLogger(SentenceTagger.class);

    public enum Tag {
        _,
        DOT,
        COMMA,
        THX,
        GREET,
        I
    }

    static class Sample {
        Tag hidden;
        String text;

        public Sample(Tag hidden, String text) {
            this.hidden = hidden;
            this.text = text;
        }

        int[] toHiddenSeq() {
            return fill(text.length(), hidden.ordinal());
        }

        int[] toTextSeq() {
            return textToSeq(text);
        }

    }

    private List<Sample> trainSamples = new ArrayList<>();

    public static int[] textToSeq(String text) {
        final String lower = text.toLowerCase();
        int[] result = new int[lower.length()];
        for (int i = 0; i < lower.length(); i++) {
            result[i] = lower.codePointAt(i);
        }
        return result;
    }

    public static int[] fill(int count, int body) {
        int[] result = new int[count];
        Arrays.fill(result, body);
        return result;
    }

    public void addTrainSample(Tag hidden, String text) {
        trainSamples.add(new Sample(hidden, text));
    }

    public void run() {
        // hidden states:
        // 0: UNKNOWN, 1: THANK_YOU, 2: GREETING
        // observed states
        // lowercased. 7-bit only. total 128 states

        // TRAIN
        addTrainSample(Tag._, " ");
        addTrainSample(Tag.DOT, ".");
        addTrainSample(Tag.COMMA, ",");
        addTrainSample(Tag.I, "aku");
//        addTrainSample(Tag.I, "I");
        addTrainSample(Tag.THX, "thx");
//        addTrainSample(Tag.THX, "thank you");
//        addTrainSample(Tag.THX, "terima kasih");
//        addTrainSample(Tag.THX, "makasih");
//        addTrainSample(Tag.THX, "thanks");
//        addTrainSample(Tag.THX, "thanx");
        addTrainSample(Tag.GREET, "pagi");
//        addTrainSample(Tag.GREET, "met pagi");
//        addTrainSample(Tag.GREET, "met siang");
//        addTrainSample(Tag.GREET, "selamat pagi");
//        addTrainSample(Tag.GREET, "selamat siang");
//        addTrainSample(Tag.GREET, "good morning");
//        addTrainSample(Tag.GREET, "good day");
//        addTrainSample(Tag.GREET, "good afternoon");

        final int hiddenStateCount = Tag.values().length;

        final HmmModel hmm = HmmTrainer.trainSupervisedSequence(
        		// jumlah class atau tag atau hidden state, untuk program ini jumlah class-nya ada 6:
        		// _, DOT, COMMA, THX, GREET, I
        		hiddenStateCount, 
        		// jumlah output state
        		128,
                // List 2 dimensi yang berisi hidden untuk masing-masing sample
                // Mungkin ini adalah chaincode (ternormalisasi atau nggak?) plus kondisi titik ? 
        		// [i] => [6, 5, 7, 4, 4, 4, 7, ...]
                trainSamples.stream().map(Sample::toHiddenSeq).collect(Collectors.toList()),
                // List 2 dimensi yang berisi klasifikasi untuk masing-masing sample
                // Misalnya huruf ain = 0, maka
                // [i] => [0, 0, 0, 0, 0, ...] (sampai sejumlah input)
                // kalau huruf alif = 1, maka
                // [i] => [1, 1, 1, 1, 1, ...] (sampai sejumlah input)
                trainSamples.stream().map(Sample::toTextSeq).collect(Collectors.toList()),
                // pseudoCount: Value that is assigned to non-occurring transitions to avoid zero probabilities.
                0.05);
        hmm.registerHiddenStateNames(ImmutableList.copyOf(Tag.values()).stream().map(Tag::name).toArray(String[]::new));
        String[] outputStateNames = new String[128];
        for (int i = 0; i < 128; i++) {
            outputStateNames[i] = new String(new int[] { i }, 0, 1);
        }
        hmm.registerOutputStateNames(outputStateNames);

        // TEST
        final String testText = "aku thxxxx pagi thx p4gi";
        log.info("Test text: {}", testText);
        final int[] decoded = HmmEvaluator.decode(hmm, textToSeq(testText), false);
        final List<String> decodedNames = HmmUtils.decodeStateSequence(hmm, decoded, false, "");
        log.info("Decoded: {}", (Object) decoded);
        log.info("Decoded: {}", decodedNames);
    }

    public static void main(String... args) {
        new SentenceTagger().run();
    }
}