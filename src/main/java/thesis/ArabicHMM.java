package thesis;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import org.apache.commons.io.Charsets;
import org.apache.mahout.classifier.sequencelearning.hmm.BaumWelchTrainer;
import org.apache.mahout.classifier.sequencelearning.hmm.HmmEvaluator;
import org.apache.mahout.classifier.sequencelearning.hmm.HmmModel;
import org.apache.mahout.classifier.sequencelearning.hmm.HmmTrainer;
import org.apache.mahout.classifier.sequencelearning.hmm.HmmUtils;
import org.apache.mahout.math.Matrix;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.mahout.classifier.sequencelearning.hmm.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * This class implements a sample program that uses a pre-tagged training data
 * set to train an HMM model as a POS tagger. The training data is automatically
 * downloaded from the following URL:
 * http://www.jaist.ac.jp/~hieuxuan/flexcrfs/CoNLL2000-NP/train.txt It then
 * trains an HMM Model using supervised learning and tests the model on the
 * following test data set:
 * http://www.jaist.ac.jp/~hieuxuan/flexcrfs/CoNLL2000-NP/test.txt Further
 * details regarding the data files can be found at
 * http://flexcrfs.sourceforge.net/#Case_Study
 */
public final class ArabicHMM {

  private static final Logger log = LoggerFactory.getLogger(ArabicHMM.class);

  private static final Pattern SPACE = Pattern.compile(" ");
  private static final Pattern SPACES = Pattern.compile("[ ]+");

  static int correctCount;
  static int totalInputPrediksi;
  /**
   * No public constructors for utility classes.
   */
  private ArabicHMM() {
    // nothing to do here really.
  }

  /**
   * Model trained in the example.
   */
  private static HmmModel taggingModel;

  /**
   * Map for storing the IDs for the POS tags (hidden states)
   */
  private static Map<String, Integer> tagIDs;

  /**
   * Counter for the next assigned POS tag ID The value of 0 is reserved for
   * "unknown POS tag"
   */
  private static int nextTagId;

  /**
   * Map for storing the IDs for observed words (observed states)
   */
  private static Map<String, Integer> wordIDs;

  /**
   * Counter for the next assigned word ID The value of 0 is reserved for
   * "unknown word"
   */
  private static int nextWordId = 1; // 0 is reserved for "unknown word"

  /**
   * Used for storing a list of POS tags of read sentences.
   */
  private static List<int[]> hiddenSequences;

  /**
   * Used for storing a list of word tags of read sentences.
   */
  private static List<int[]> observedSequences;

  /**
   * number of read lines
   */
  private static int readLines;

  /**
   * Given an URL, this function fetches the data file, parses it, assigns POS
   * Tag/word IDs and fills the hiddenSequences/observedSequences lists with
   * data from those files. The data is expected to be in the following format
   * (one word per line): word pos-tag np-tag sentences are closed with the .
   * pos tag
   *
   * @param url       Where the data file is stored
   * @param assignIDs Should IDs for unknown words/tags be assigned? (Needed for
   *                  training data, not needed for test data)
   * @throws IOException in case data file cannot be read.
   */
  private static void readFromURL(String url, boolean assignIDs) throws IOException {
    // initialize the data structure
    hiddenSequences = new LinkedList<>();
    observedSequences = new LinkedList<>();
    readLines = 0;

    // now read line by line of the input file
    List<Integer> observedSequence = new LinkedList<>();
    List<Integer> hiddenSequence = new LinkedList<>();

    for (String line :Resources.readLines(new URL(url), Charsets.UTF_8)) {
      if (line.isEmpty()) {
        // new sentence starts
        int[] observedSequenceArray = new int[observedSequence.size()];
        int[] hiddenSequenceArray = new int[hiddenSequence.size()];
        for (int i = 0; i < observedSequence.size(); ++i) {
          observedSequenceArray[i] = observedSequence.get(i);
          hiddenSequenceArray[i] = hiddenSequence.get(i);
        }
        // now register those arrays
        hiddenSequences.add(hiddenSequenceArray);
        observedSequences.add(observedSequenceArray);
        // and reset the linked lists
        observedSequence.clear();
        hiddenSequence.clear();
        continue;
      }
      readLines++;
      // we expect the format [word] [POS tag] [NP tag]
      String[] tags = SPACE.split(line);
      // when analyzing the training set, assign IDs
      if (assignIDs) {
        if (!wordIDs.containsKey(tags[0])) {
          wordIDs.put(tags[0], nextWordId++);
        }
        if (!tagIDs.containsKey(tags[1])) {
          tagIDs.put(tags[1], nextTagId++);
        }
      }
      // determine the IDs
      Integer wordID = wordIDs.get(tags[0]);
      Integer tagID = tagIDs.get(tags[1]);
      // now construct the current sequence
      if (wordID == null) {
        observedSequence.add(0);
      } else {
        observedSequence.add(wordID);
      }

      if (tagID == null) {
        hiddenSequence.add(0);
      } else {
        hiddenSequence.add(tagID);
      }
    }

    // if there is still something in the pipe, register it
    if (!observedSequence.isEmpty()) {
      int[] observedSequenceArray = new int[observedSequence.size()];
      int[] hiddenSequenceArray = new int[hiddenSequence.size()];
      for (int i = 0; i < observedSequence.size(); ++i) {
        observedSequenceArray[i] = observedSequence.get(i);
        hiddenSequenceArray[i] = hiddenSequence.get(i);
      }
      // now register those arrays
      hiddenSequences.add(hiddenSequenceArray);
      observedSequences.add(observedSequenceArray);
    }
  }

  private static void trainModel(String trainingURL) throws IOException {
    tagIDs = new HashMap<>(44); // we expect 44 distinct tags
    wordIDs = new HashMap<>(19122); // we expect 19122
    // distinct words
    log.info("Reading and parsing training data file from URL: {}", trainingURL);
    long start = System.currentTimeMillis();
    readFromURL(trainingURL, true);
    long end = System.currentTimeMillis();
    double duration = (end - start) / 1000.0;
    log.info("Parsing done in {} seconds!", duration);
    log.info("Read {} lines containing {} sentences with a total of {} distinct words and {} distinct POS tags.",
             readLines, hiddenSequences.size(), nextWordId - 1, nextTagId - 1);
    start = System.currentTimeMillis();
    log.info("HmmTrainer.trainSupervisedSequence(nextTagId={}, nextWordId={}, ...",
    		nextTagId, nextWordId);
    log.info("hiddenSequences.size={} observedSequences.size={}", 
    		hiddenSequences.size(), observedSequences.size());
    log.info("hidden[0] = {}", hiddenSequences.get(0));
    log.info("observed[0] = {}", observedSequences.get(0));
    

    
//    log.info("hidden[8] = {}", hiddenSequences.get(8));
//    log.info("observe[8] = {}", observedSequences.get(8));
    //training
    taggingModel = HmmTrainer.trainSupervisedSequence(
    		// jumlah tag / jumlah hidden state
    		nextTagId,
    		// jumlah output/observed state yang mungkin
    		nextWordId,
    		// List 2 dimensi yang berisi sequence klasifikasi untuk masing-masing sample
	        hiddenSequences,
	        
	        // List 2 dimensi yang berisi sequence observasi untuk masing-masing sample
	        observedSequences,
	        // pseudo-count
	        0.05);
    
//    System.out.println("hidden adalah " +hiddenSequences);
//   //  for Arabic --> no adjustment of emissions
//    // we have to adjust the model a bit,
//    // since we assume a higher probability that a given unknown word is NNP
//    // than anything else
//    Matrix emissions = taggingModel.getEmissionMatrix();
//    for (int i = 0; i < taggingModel.getNrOfHiddenStates(); ++i) {
//      emissions.setQuick(i, 0, 0.1 / taggingModel.getNrOfHiddenStates());
//    }
//    int nnptag = tagIDs.get("NNP");
//    emissions.setQuick(nnptag, 0, 1 / (double) taggingModel.getNrOfHiddenStates());

    
    // re-normalize the emission probabilities
    HmmUtils.normalizeModel(taggingModel);
    // now register the names
    taggingModel.registerHiddenStateNames(tagIDs);
    taggingModel.registerOutputStateNames(wordIDs);
    end = System.currentTimeMillis();
    duration = (end - start) / 1000.0;
    log.info("Trained HMM models in {} seconds!", duration);
  }
  
  

  private static void testModel(String testingURL) throws IOException {
    log.info("Reading and parsing test data file from URL: {}", testingURL);
    long start = System.currentTimeMillis();
    readFromURL(testingURL, false);
    long end = System.currentTimeMillis();
    double duration = (end - start) / 1000.0;
    log.info("Parsing done in {} seconds!", duration);
    log.info("Read {} lines containing {} sentences.", readLines, hiddenSequences.size());

    start = System.currentTimeMillis();
    int errorCount = 0;
    int totalCount = 0;
    for (int i = 0; i < observedSequences.size(); ++i) {
      // fetch the viterbi path as the POS tag for this observed sequence
    	//prediksi
      int[] posEstimate = HmmEvaluator.decode(
    		  // model HMM hasil training
    		  taggingModel,
    		  // sequence kalimat berisi ID-ID word yang akan di-predict
    		  // misal: [McDonalds, is, a, huge, ...]
    		  observedSequences.get(i),
    		  // scaled: Use log-scaled computations, this requires higher computational effort but is numerically more stable for large observation sequences
    		  false);
      // compare with the expected
      int[] posExpected = hiddenSequences.get(i);
      for (int j = 0; j < posExpected.length; ++j) {
        totalCount++;
        if (posEstimate[j] != posExpected[j]) {
          errorCount++;
        }
      }
    }
    end = System.currentTimeMillis();
    duration = (end - start) / 1000.0;
    log.info("POS tagged test file in {} seconds!", duration);
    double errorRate = (double) errorCount / totalCount;
    log.info("Tagged the test file with an error rate of: {}", errorRate);
  }

  private static List<String> tagSentence(String sentence) {
    // first, we need to isolate all punctuation characters, so that they
    // can be recognized
    sentence = sentence.replaceAll("[,.!?:;\"]", " $0 ");
    sentence = sentence.replaceAll("''", " '' ");
    // now we tokenize the sentence
    String[] tokens = SPACES.split(sentence);
    // now generate the observed sequence
    int[] observedSequence = HmmUtils.encodeStateSequence(taggingModel, Arrays.asList(tokens), true, 0);
    // POS tag this observedSequence
    int[] hiddenSequence = HmmEvaluator.decode(taggingModel, observedSequence, false);
    // and now decode the tag names
    return HmmUtils.decodeStateSequence(taggingModel, hiddenSequence, false, null);
  }

  static final List<String> LABELS = ImmutableList.of(
			"ain", "alif", "ba", "dal", "dhad", 
			"dzal", "dzo", "fa", "ghoin", "hamzah",
			"ha", "habesar", "jim", "kaf", "kha",
			"lam", "mim", "nun", "qaf", "ra", "sad",
			"sheen", "sin", "tamarbuto", "ta", "tho",
			"tsa", "waw", "ya", "za","lamalif"
			);
  
  //segment.setLabel(LABELS.get(labelId));
  public static boolean inputPrediksi (String test, String namahuruf) {
	  
	  totalInputPrediksi++;
	  
	  String [] testWords;
	  List<String>posTags;
	//  String test = "McDonalds is a huge company with many employees .";
	    testWords = SPACE.split(test);
	    posTags = tagSentence(test);
	    log.info("Testing {} ...{}", test , namahuruf);
	    
	    String firstPosTagStr = posTags.get(0);
	    int firstposTagint = Integer.parseInt(firstPosTagStr);
	    String firstnameOfCharacter = LABELS.get(firstposTagint);
	    
	    boolean cekHasilPrediksi = namahuruf.equals(firstnameOfCharacter);
	    
	    if (cekHasilPrediksi == false) {
	    	System.out.println("------------------------------------------------------------------------------>WRONG");
	    } else {
	    	System.out.println("------------------------------------------------------------------------------>OK");
	    	correctCount++;
	    }
	    
	    for (int i = 0; i < posTags.size(); ++i) {
	    	 String numberposTag = posTags.get(i);
	    	 int numberposTagInt = Integer.parseInt(numberposTag);
	    	 String nameOfCharacter = LABELS.get(numberposTagInt);
	      log.info("{} -> {} -> {}", testWords[i], numberposTag , nameOfCharacter);
	    }
	    
	    return cekHasilPrediksi;
  }
  
  public static void main(String[] args) throws IOException {
    // generate the model from URL
//    trainModel("http://www.jaist.ac.jp/~hieuxuan/flexcrfs/CoNLL2000-NP/train.txt");
//    testModel("http://www.jaist.ac.jp/~hieuxuan/flexcrfs/CoNLL2000-NP/test.txt");
	  
	  
//    trainModel("file:///D:/hmm baru aina/arabictrain2.txt");
//    testModel("file:///D:/hmm baru aina/arabictest2.txt");
    
//	  
    trainModel("file:///D:/hmm baru aina/trainarabicaina.txt");
    testModel("file:///D:/hmm baru aina/testarabicaina.txt");
//	  
//    trainModel("file:///D:/hmm baru aina/trainarial.txt");
//    testModel("file:///D:/hmm baru aina/testarial.txt");
//    

//   trainModel("file:///D:/hmm baru aina/traintnr.txt");
//    testModel("file:///D:/hmm baru aina/testtnr.txt");
////	
//	  trainModel("file:///D:/hmm baru aina/traintahoma.txt");
//	    testModel("file:///D:/hmm baru aina/testtahoma.txt");
    // tag an exemplary sentence
    String test;
    String[] testWords;
    List<String> posTags;

   
    
    //batas arial tunggal
//    inputPrediksi("0 0 1 8 6 5 4 8 6 6 4 4","ain");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("1 2 6 6 6 5 4 4 4 4 3 2","ba");
//    inputPrediksi("0 0 5 5 5 6 6 7 8 8 8 8","dal");
//    inputPrediksi("1 0 6 3 6 8 4 2 7 6 8 2","dhad");
//    inputPrediksi("1 0 5 5 5 6 6 7 8 8 8 8","dzal");
//    inputPrediksi("1 0 6 6 3 5 8 8 6 3 8 7","dzo");
//    inputPrediksi("1 0 6 5 4 8 2 5 6 1 6 4","fa");
//    inputPrediksi("1 0 1 8 6 5 4 8 6 6 4 4","ghoin");
//    inputPrediksi("0 0 8 8 7 6 5 4 4 8 8 8","hamzah");
//    inputPrediksi("0 0 4 4 7 7 6 6 5 4 4 4","ha");
//    inputPrediksi("0 0 5 6 8 2 3 7 6 4 2 1","habesar");
//    inputPrediksi("1 2 4 4 7 7 6 6 5 4 4 4","jim");
//    inputPrediksi("1 0 6 6 6 6 6 7 8 8 1 2","kaf");
//    inputPrediksi("1 0 4 4 7 7 6 6 5 4 4 4","kha");
//    inputPrediksi("0 0 6 6 6 6 6 6 8 8 2 2","lam");
//    inputPrediksi("0 0 2 2 2 4 6 8 3 2 7 6","mim");
//    inputPrediksi("1 0 6 6 6 5 4 4 3 2 2 2","nun");
//    inputPrediksi("2 0 6 5 4 2 2 5 6 1 6 4","qaf");
//    inputPrediksi("0 0 6 6 6 6 6 6 7 8 8 8","ra");
//    inputPrediksi("0 0 6 3 6 8 4 2 7 6 8 2","sad");
//    inputPrediksi("3 0 6 8 2 7 2 6 6 8 1 2","sheen");
//    inputPrediksi("0 0 6 8 2 7 2 6 6 8 1 2","sin");
//    inputPrediksi("2 0 5 6 8 1 2 6 5 4 2 1","tamarbuto");
//    inputPrediksi("2 0 6 6 6 5 4 4 4 4 3 2","ta");
//    inputPrediksi("0 0 6 6 3 5 8 8 4 4 8 8","tho");
//    inputPrediksi("3 0 6 6 6 5 4 4 4 4 3 2","tsa");
//    inputPrediksi("0 0 4 4 2 1 7 4 8 2 5 6","waw");
//    inputPrediksi("2 2 1 8 7 5 5 7 8 8 1 2","ya");
//    inputPrediksi("1 0 6 6 6 6 6 6 7 8 8 8","za");
//    inputPrediksi("0 0 6 6 6 6 6 8 8 5 8 8","lamalif");
    

 //--------------------------------------batas huruf tunggal timesnewroman-----------------------------------
    
//    inputPrediksi("0 0 8 5 3 6 4 4 8 1 3 8","ain");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("1 2 6 6 5 4 4 4 4 4 2 2", "ba");
//    inputPrediksi("0 0 5 5 6 6 8 8 8 8 1 2", "dal");
//    inputPrediksi("1 0 6 4 6 8 4 2 7 7 1 2", "dhad");
//    inputPrediksi("1 0 5 5 6 6 8 8 8 8 1 2", "dzal");
//    inputPrediksi("1 0 6 6 4 8 8 8 7 4 2 8", "dzo");
//    inputPrediksi("1 0 6 5 4 4 4 1 4 2 7 5", "fa");
//    inputPrediksi("1 0 8 5 3 6 4 4 8 1 3 8", "ghoin");
//    inputPrediksi("0 0 8 8 7 5 4 4 6 2 8 8", "hamzah");
//    inputPrediksi("0 0 8 8 7 6 4 8 8 2 3 4", "ha");
//    inputPrediksi("0 0 6 5 7 8 2 7 5 4 2 8", "habesar");
//    inputPrediksi("1 1 8 8 7 6 4 8 8 2 3 4", "jim");
//    inputPrediksi("1 0 6 6 6 6 8 8 8 8 2 2", "kaf");
//    inputPrediksi("1 0 8 8 7 6 4 8 8 2 3 4", "kha");
//    inputPrediksi("0 0 6 6 6 6 6 8 8 1 2 2", "lam");
//    inputPrediksi("0 0 4 5 6 8 8 7 6 6 6 6", "mim");
//    inputPrediksi("1 0 6 6 6 7 8 8 8 1 2 2", "nun");
//    inputPrediksi("2 0 6 6 4 4 2 2 8 8 4 6", "qaf");
//    inputPrediksi("0 0 6 6 6 6 7 7 7 8 8 8", "ra");
//    inputPrediksi("0 0 6 4 6 8 4 2 7 7 1 2", "sad");
//    inputPrediksi("3 0 6 5 2 2 6 6 6 8 2 2", "sheen");
//    inputPrediksi("0 0 6 5 2 2 6 6 6 8 2 2", "sin");
//    inputPrediksi("2 0 6 5 6 8 8 6 4 3 2 8", "tamarbuto");
//    inputPrediksi("2 0 6 6 5 4 4 4 4 4 2 2", "ta");
//    inputPrediksi("0 0 6 6 4 8 8 8 7 4 2 8", "tho");
//    inputPrediksi("3 0 6 6 5 4 4 4 4 4 2 2", "tsa");
//    inputPrediksi("0 0 4 4 3 2 1 6 4 1 4 6", "waw");
//    inputPrediksi("2 2 1 7 6 4 7 8 8 1 2 2", "ya");
//    inputPrediksi("1 0 6 6 6 6 7 7 7 8 8 8", "za");
//    inputPrediksi("0 0 6 6 7 1 6 2 7 2 5 5", "lamalif");
    
  //--------------------------------------batas huruf tunggal tahoma-----------------------------------
//   
//    inputPrediksi("0 0 8 7 6 4 8 7 6 4 4 2", "ain");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6", "alif");
//    inputPrediksi("1 2 6 6 7 8 8 8 8 1 2 2", "ba");
//    inputPrediksi("0 0 5 5 5 5 6 6 8 8 8 8", "dal");
//    inputPrediksi("1 0 6 5 2 4 3 8 8 4 8 6", "dhad");
//    inputPrediksi("1 0 5 5 5 5 6 6 8 8 8 8", "dzal");
//    inputPrediksi("1 0 6 6 3 6 8 8 6 4 1 8", "dzo");
//    inputPrediksi("1 0 6 5 4 4 1 6 4 8 4 6", "fa");
//    inputPrediksi("1 0 8 7 6 4 8 7 6 4 4 2", "ghoin");
//    inputPrediksi("0 0 8 8 7 6 5 4 4 8 8 8", "hamzah");
//    inputPrediksi("0 0 4 4 8 6 4 4 8 1 3 8", "ha");
//    inputPrediksi("0 0 4 5 6 1 2 7 6 4 2 1", "habesar");
//    inputPrediksi("1 1 4 4 8 6 4 4 8 1 3 8", "jim");
//    inputPrediksi("1 1 6 6 6 6 6 8 8 8 8 2", "kaf");
//    inputPrediksi("1 0 4 4 8 6 4 4 8 1 3 8", "kha");
//    inputPrediksi("0 0 6 6 6 6 6 6 8 8 2 2", "lam");
//    inputPrediksi("0 0 4 3 3 5 8 1 4 2 7 6", "mim");
//    inputPrediksi("1 0 6 6 6 5 4 4 4 2 2 2", "nun");
//    inputPrediksi("2 0 6 4 4 2 2 6 4 1 4 6", "qaf");
//    inputPrediksi("0 0 5 6 6 6 6 6 7 8 8 8", "ra");
//    inputPrediksi("0 0 6 5 2 4 3 8 8 4 8 6", "sad");
//    inputPrediksi("3 0 6 7 2 6 8 2 6 8 2 2", "sheen");
//    inputPrediksi("0 0 6 7 2 6 8 2 6 8 2 2", "sin");
//    inputPrediksi("2 0 5 6 8 1 3 7 6 4 2 1", "tamarbuto");
//    inputPrediksi("2 0 6 6 7 8 8 8 8 1 2 2", "ta");
//    inputPrediksi("0 0 6 6 3 6 8 8 6 4 1 8", "tho");
//    inputPrediksi("3 0 6 6 7 8 8 8 8 1 2 2", "tsa");
//    inputPrediksi("0 0 4 3 2 8 6 4 8 2 4 6", "waw");
//    inputPrediksi("2 2 8 7 5 6 8 8 8 8 2 2", "ya");
//    inputPrediksi("1 0 5 6 6 6 6 6 7 8 8 8 ", "za");
//    inputPrediksi("0 0 6 6 6 6 4 1 2 2 6 8", "lamalif");
//    
    
    
    
//--------------------------------------batas kalimat times new roman-----------------------------------    
    // batas test kalimat 2
    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6", "alif");
    inputPrediksi("0 0 6 6 6 6 6 6 6 6 8 8", "lam");
    inputPrediksi("2 0 4 4 2 1 7 4 8 3 5 6", "qaf");
    inputPrediksi("0 0 4 3 3 6 8 1 4 2 7 6", "mim");
    inputPrediksi("0 0 6 5 6 6 6 7 7 8 8 8", "ra");
    inputPrediksi("2 0 6 6 6 6 6 8 8 8 8 8", "ta");
    inputPrediksi("1 2 6 6 6 6 6 6 8 8 8 8", "ba");
    inputPrediksi("0 0 5 5 6 6 6 8 8 8 8 8 1", "dal");
    inputPrediksi("0 0 6 6 7 8 1 5 4 3 2 1", "waw");
    inputPrediksi("1 2 4 4 5 4 8 8 8 8 8 8", "jim");
    inputPrediksi("0 0 4 3 3 5 4 8 8 2 7 6", "mim");
    inputPrediksi("2 2 6 6 6 6 6 8 8 8 8 8", "ya");
    inputPrediksi("0 0 6 6 6 6 6 6 6 6 8 8", "lam");
    inputPrediksi("2 0 6 6 6 4 8 3 4 4 2 2", "tamarbuto");
    inputPrediksi("1 2 4 4 5 4 8 8 8 8 8 8", "jim");
    inputPrediksi("0 0 5 5 6 6 6 8 8 8 8 1", "dal");
    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6", "alif");

    
    
    
    //kalimat 1

//   inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//   inputPrediksi("0 0 6 5 2 2 6 3 4 2 6 6","lamalif");
//   inputPrediksi("0 0 4 4 5 4 8 8 8 8 8 8","ha");
//   inputPrediksi("2 0 6 6 6 6 6 4 8 8 8 8","ta");
//   inputPrediksi("0 0 6 6 6 6 7 7 7 8 8 8","ra"); 
//   inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif"); 
//   inputPrediksi("0 0 6 6 8 6 6 2 2 4 2 8","mim");
//   inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//   inputPrediksi("0 0 6 6 6 6 6 6 6 6 8 8","lam");
//   inputPrediksi("0 0 4 3 3 6 8 1 4 2 7 6","mim");
//   inputPrediksi("2 0 6 6 6 6 6 8 8 8 8 8","ta");
//   inputPrediksi("1 2 6 6 6 6 6 6 8 8 8 8","ba");
//   inputPrediksi("0 0 6 6 6 6 6 6 6 6 4 4","alif");
//   inputPrediksi("0 0 5 5 6 6 8 8 8 8 1 2","dal");
//   inputPrediksi("0 0 6 6 6 6 6 8 8 1 2 2","lam");
//   inputPrediksi("1 2 6 6 6 6 6 6 8 8 8 8","ba");
//   inputPrediksi("2 2 6 6 6 6 4 8 8 8 1 2","ya");
//   inputPrediksi("1 0 6 6 6 7 8 8 1 2 2 2","nun");
//   inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//   inputPrediksi("0 0 6 6 1 1 6 3 4 2 5 5","lamalif");
//   inputPrediksi("0 0 5 5 6 6 8 8 8 8 1 2","dal");
//   inputPrediksi("2 2 6 6 6 6 6 6 8 8 8 8","ya");
//   inputPrediksi("0 0 6 6 6 6 6 6 6 6 5 4","alif");
//   inputPrediksi("0 0 6 6 6 7 8 8 8 1 2 2", "nun");

 
 //batas kalimat ke 3
    
//    inputPrediksi("1 0 4 4 2 2 8 5 8 1 5 6", "fa");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 4", "alif");
//    inputPrediksi("0 0 8 7 6 5 5 6 8 8 8 8", "kaf");
//    inputPrediksi("0 0 4 3 4 6 2 6 2 8 2 7", "habesar");
//    inputPrediksi("2 0 6 6 6 8 2 7 6 4 1 2", "tamarbuto");
//    inputPrediksi("0 0 5 5 6 6 8 8 8 8 1 2", "dal");
//    inputPrediksi("0 0 6 8 6 7 8 4 3 2 4 1", "waw");
//    inputPrediksi("0 0 6 5 6 6 7 7 7 8 8 8", "ra");
//    inputPrediksi("2 2 6 6 6 6 6 6 8 8 8 8", "ya");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 5 4", "alif");
//    inputPrediksi("1 0 6 6 6 7 8 8 8 1 2 2", "nun");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6", "alif");
//    inputPrediksi("0 0 6 6 1 1 6 3 4 2 5 5", "lamalif");
//    inputPrediksi("1 0 5 5 6 6 6 8 8 8 8 1", "dzal");
//    inputPrediksi("0 0 6 6 7 8 1 5 4 3 2 1", "waw");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6", "alif");
//    inputPrediksi("2 0 6 6 4 4 2 1 5 8 3 6", "Qaf");
//    inputPrediksi("1 2 4 4 5 4 8 8 8 8 8 8", "jim");
//    inputPrediksi("2 2 6 6 6 6 6 8 8 8 8 8", "ya");
//    inputPrediksi("0 0 5 5 6 6 8 8 8 8 1 2", "dal");
//    inputPrediksi("2 0 6 5 6 8 8 6 4 3 2 8", "tamarbuto");
    
 //batas kalimat ke 4
    
//    inputPrediksi("2 0 6 6 6 6 6 6 8 8 8 8", "ta");
//    inputPrediksi("2 0 6 6 6 6 6 6 8 8 8 8", "ta");
//    inputPrediksi("1 0 4 4 5 6 4 8 8 8 8 8", "kha");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 4 8 8", "lam");
//    inputPrediksi("2 2 6 6 4 4 4 3 8 2 4 4", "ya");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6", "alif");
//    inputPrediksi("1 2 6 5 6 6 6 6 6 8 8 8", "ba");
//    inputPrediksi("0 0 5 5 6 6 6 8 8 8 8 2", "dal");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6", "alif");
//    inputPrediksi("1 0 4 4 2 2 1 6 4 1 3 6", "fa");
//    inputPrediksi("2 2 6 6 4 4 4 3 8 2 4 4", "ya");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6", "alif");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 8 8", "lam");
//    inputPrediksi("0 0 4 4 5 6 4 8 8 8 8 8", "ha");
//    inputPrediksi("2 2 6 5 6 6 6 4 8 8 8 8", "ya");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 5 4", "alif");
//    inputPrediksi("2 0 6 5 7 8 2 6 4 4 2 1", "tamarbuto");
    
 //batas kalimat ke 5
    
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6", "alif");
//    inputPrediksi("1 0 6 6 6 6 6 6 6 8 8 8", "nun");
//    inputPrediksi("2 0 6 6 6 6 6 8 8 8 8 8", "ta");
//    inputPrediksi("1 0 6 6 4 6 8 3 4 4 1 8", "dzo");
//    inputPrediksi("0 0 6 5 6 6 7 7 7 8 8 8", "ra");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6", "alif");
//    inputPrediksi("2 2 6 1 7 4 5 7 8 8 2 2", "ya");
//    inputPrediksi("0 0 6 8 2 3 8 4 7 6 4 2", "mim");
//    inputPrediksi("0 0 4 4 5 4 8 8 8 8 8 8", "ha");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 4 4", "alif");
//    inputPrediksi("0 0 7 7 6 5 5 5 8 8 8 8", "kaf");
//    inputPrediksi("0 0 6 8 2 3 8 4 7 6 4 2", "mim");
//    inputPrediksi("2 0 6 6 6 4 8 3 4 8 2 1", "tamarbuto");
    
 //batas kalimat ke 6
    
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6", "alif");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 8 8", "lam");
//    inputPrediksi("0 0 4 4 5 4 8 8 8 8 8 8", "ha");
//    inputPrediksi("1 0 4 4 2 2 7 5 1 4 6 6", "fa");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 5", "alif");
//    inputPrediksi("1 0 6 6 4 8 8 8 7 4 2 8", "dzo");
//    inputPrediksi("0 0 8 8 6 5 4 4 4 8 8 8", "ain");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 8 8", "lam");
//    inputPrediksi("2 2 8 8 4 5 8 8 8 1 2 2", "ya");
//    inputPrediksi("0 0 4 4 6 8 8 4 4 2 8 7", "sad");
//    inputPrediksi("0 0 6 6 6 5 4 8 8 1 8 8", "unknown");
//    inputPrediksi("0 0 4 4 5 4 8 8 8 8 8 8", "ha");
//    inputPrediksi("2 0 6 6 6 6 6 8 8 8 8 8", "ta");
//    inputPrediksi("2 1 6 6 6 6 6 6 8 8 8 8", "kaf");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 5 5 5", "unknown");
//    inputPrediksi("0 0 4 4 5 4 8 8 8 8 8 8", "ha");
//    inputPrediksi("2 2 6 6 6 6 6 6 8 8 8 8","ya");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 5 4","alif");
//    inputPrediksi("2 0 5 6 7 8 2 6 4 3 2 1","tamarbuto");
//    inputPrediksi("0 0 6 6 4 6 8 3 4 4 1 8","tho");
//    inputPrediksi("2 2 6 6 6 6 6 6 8 8 8 8","ya");
//    inputPrediksi("1 2 6 6 6 6 6 6 8 8 8 8","ba");
//    inputPrediksi("2 0 6 5 6 4 8 7 4 8 2 1","tamarbuto");
    
    //batas kalimat ke 7
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 8 8","lam");
//    inputPrediksi("0 0 4 4 2 5 6 8 4 2 7 6","mim");
//    inputPrediksi("0 0 4 4 6 5 8 8 4 1 2 8","ain");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 8 8","lam");
//    inputPrediksi("0 0 6 8 8 3 8 4 7 4 4 2","mim");
//    inputPrediksi("2 2 6 6 6 6 6 8 8 8 1 2","ya");
//    inputPrediksi("1 0 6 6 6 7 8 8 1 2 2 2","nun");
//    inputPrediksi("2 2 6 6 6 6 6 8 8 8 8 8","ya");
//    inputPrediksi("0 0 8 8 4 6 5 4 8 1 7 8","ain");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 8 8","lam");
//    inputPrediksi("0 0 4 3 3 6 8 1 4 2 7 6","mim");
//    inputPrediksi("0 0 4 4 3 2 1 6 8 2 5 6","waw");
//    inputPrediksi("1 0 6 6 6 7 8 8 8 1 2 2","nun");
    
    //batas kalimat 8 timesnewroman
//    inputPrediksi("1 2 4 4 5 4 8 8 8 8 8 8","jim"); 
//    inputPrediksi("0 0 5 5 6 6 8 8 8 8 1 2","dal");
//    inputPrediksi("2 0 6 5 6 8 8 6 4 3 2 8","tamarbuto");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("0 0 6 6 1 1 5 6 8 3 4 1","lamalif");
//    inputPrediksi("0 0 6 6 6 6 6 7 7 8 8 8","ra");
//    inputPrediksi("1 0 6 5 6 6 6 7 7 8 8 8","za");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 8 8","lam");
//    inputPrediksi("0 0 4 3 3 6 8 1 4 2 7 6","mim");
//    inputPrediksi("0 0 4 6 4 8 8 4 4 8 2 8","tho");
//    inputPrediksi("1 2 6 6 6 6 6 6 8 8 8 8","ba");
//    inputPrediksi("0 0 2 7 4 7 8 4 3 8 3 6","waw");
//    inputPrediksi("1 0 8 8 7 6 4 8 8 2 3 4","kha");
    
    //batas kalimat 9
//    inputPrediksi("2 2 6 6 6 6 6 8 8 8 8 8","ya");
//    inputPrediksi("2 0 6 6 6 6 6 6 8 8 8 8","ta");
//    inputPrediksi("0 0 8 7 6 5 5 5 8 8 8 8","kaf");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 8 8","lam");
//    inputPrediksi("0 0 4 5 6 8 8 8 7 6 6 6","mim");
//    inputPrediksi("1 2 6 6 6 6 6 6 6 8 8 8","ba");
//    inputPrediksi("1 2 6 6 6 6 6 4 8 8 8 8","ba");
//    inputPrediksi("0 0 6 6 4 4 8 8 7 4 1 8","tho");
//    inputPrediksi("0 0 6 8 6 5 4 4 6 2 8 8","hamzah");
//    inputPrediksi("0 0 6 8 8 3 8 4 7 4 4 2","mim");
//    inputPrediksi("1 0 4 4 2 2 8 5 8 2 5 6","fa");
//    inputPrediksi("0 0 4 4 4 6 2 6 2 8 2 7","habesar");
//    inputPrediksi("0 0 4 4 3 2 1 6 4 1 4 6","waw");
//    inputPrediksi("0 0 4 3 2 5 6 1 4 2 7 6","mim");
//    inputPrediksi("2 0 6 6 5 8 2 7 6 4 1 2","tamarbuto");
//    inputPrediksi("0 0 4 4 5 4 8 8 8 8 8 8","ha");
//    inputPrediksi("2 0 6 6 6 6 6 8 8 8 8 8","ta");
//    inputPrediksi("2 2 6 5 5 4 4 4 8 2 4 4","ya");
    
    
  //  kalimat 10
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 8 8","lam");
//    inputPrediksi("0 0 4 4 5 5 8 8 8 8 8 8","ha");
//    inputPrediksi("1 0 4 4 2 8 6 4 1 4 6 4","fa");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 5 4","alif");
//    inputPrediksi("1 0 6 6 4 8 8 8 4 4 2 8","dzo");
//    inputPrediksi("0 0 6 6 6 2 4 4 8 8 8 8","ain");
//    inputPrediksi("2 2 6 6 5 4 4 4 1 8 4 4","ya");
//    inputPrediksi("0 0 4 3 6 8 8 4 4 4 8 7","sad");
//    inputPrediksi("0 0 6 6 6 6 4 8 7 8 8 8","unknown");
//    inputPrediksi("0 0 4 4 5 4 8 8 8 8 8 8","ha");
//    inputPrediksi("2 0 6 6 6 6 6 8 8 8 8 8","ta");
//    inputPrediksi("1 0 6 6 6 6 6 6 8 8 8 8","kaf");
//    inputPrediksi("1 1 6 6 6 6 6 6 5 5 5 5","unknown");
//    inputPrediksi("0 0 4 4 5 4 8 8 8 8 8 8","ha");
//    inputPrediksi("2 2 6 6 6 6 6 8 8 8 8 8","ya");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 5 4","alif");
//    inputPrediksi("2 0 5 6 7 8 2 6 4 3 2 1","tamarbuto");
//    inputPrediksi("0 0 6 6 4 6 8 3 4 4 1 8","tho");
//    inputPrediksi("2 2 6 6 6 6 6 6 8 8 8 8","ya");
//    inputPrediksi("1 2 6 6 6 6 6 6 8 8 8 8","ba");
//    inputPrediksi("2 0 6 5 6 4 8 7 4 8 2 1","tamarbuto");
    
//-----------------------------------------------batas kalimat arial---------------------------------------------------
    
    //kalimat 1
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("0 0 6 6 6 6 6 6 1 5 8 8","lamalif");
//    inputPrediksi("0 0 4 4 4 4 5 6 8 8 8 8","ha");
//    inputPrediksi("2 0 6 6 1 8 2 8 8 4 2 6","ta");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("0 0 6 6 8 7 6 2 3 5 2 1","mim");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 7 8","lam");
//    inputPrediksi("0 0 4 3 6 5 8 4 8 2 8 6","mim");
//    inputPrediksi("2 0 6 6 6 6 6 6 4 8 8 8","ta");
//    inputPrediksi("1 2 6 6 6 6 6 4 8 1 8 8","ba");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 5","alif");
//    inputPrediksi("0 0 4 5 5 6 6 6 7 8 8 8","dal");
//    inputPrediksi("0 0 6 6 6 6 6 6 8 8 1 2","lam");
//    inputPrediksi("1 2 6 6 6 6 6 6 7 8 8 8","ba");
//    inputPrediksi("2 3 6 6 8 2 6 8 8 1 2 2","ya");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("0 0 6 6 6 6 6 6 1 5 8 8","lamalif");
//    inputPrediksi("0 0 5 5 5 6 6 6 6 8 8 8","dal");
//    inputPrediksi("1 2 6 6 6 6 6 6 7 7 8 8","ba");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 4","alif");
//    inputPrediksi("1 0 6 6 6 7 8 8 1 2 2 2","nun");
    
    //kalimat 2
    
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif"); 
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 7 8","lam"); 
//    inputPrediksi("2 0 4 2 4 6 4 4 8 2 7 6","tamarbuto"); 
//    inputPrediksi("0 0 4 3 6 5 8 4 8 2 8 6","mim"); 
//    inputPrediksi("0 0 6 6 7 8 8 8 4 4 2 8","ra"); 
//    inputPrediksi("1 0 6 6 6 6 6 6 7 7 8 8","nun");
//    inputPrediksi("1 2 6 6 6 6 5 4 8 1 8 8","ba");
//    inputPrediksi("0 0 4 5 6 6 6 6 8 8 8 8","dal");
//    inputPrediksi("0 0 6 6 1 6 8 4 2 5 2 1","waw");
//    inputPrediksi("1 2 4 4 4 4 5 6 8 8 8 8","jim");
//    inputPrediksi("0 0 4 3 6 5 8 4 3 2 8 6","mim");
//    inputPrediksi("2 2 6 6 6 6 5 4 8 1 8 8","ya");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 8 8","lam");
//    inputPrediksi("2 0 6 6 6 8 3 8 5 4 2 2","tamarbuto");
//    inputPrediksi("1 2 5 4 4 4 5 6 8 8 8 8","jim");
//    inputPrediksi("0 0 5 6 6 6 5 8 1 8 8 8","dal");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
    
    
    //kalimat 3
    
//    inputPrediksi("1 0 4 4 2 1 5 4 8 2 6 6","fa"); 
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 5 4","alif"); 
//    inputPrediksi("0 0 6 5 8 2 4 8 6 2 2 6","ha");  
//    inputPrediksi("2 0 6 6 5 8 2 7 5 8 2 2","tamarbuto");
//    inputPrediksi("0 0 4 5 5 6 6 6 7 8 8 8","dal"); 
//    inputPrediksi("0 0 6 6 1 6 8 4 3 6 2 1","waw");  
//    inputPrediksi("0 0 6 6 6 6 6 8 8 8 8 1","ra");
//    inputPrediksi("1 2 6 6 6 6 6 6 7 7 8 8","ba");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 4","alif"); 
//    inputPrediksi("1 0 6 6 6 5 4 4 4 2 2 2","nun");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("0 0 6 6 6 6 6 6 1 5 8 8","lamalif");
//    inputPrediksi("1 0 5 5 5 6 6 6 6 8 8 8","dzal");
//    inputPrediksi("0 0 6 6 1 6 8 4 3 6 2 1","waw");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("2 0 6 5 4 2 1 3 6 2 6 4","qaf");
//    inputPrediksi("1 2 6 4 5 8 8 4 4 1 8 8","jim");
//    inputPrediksi("2 2 6 6 6 6 5 4 8 1 8 8","ya");
//    inputPrediksi("0 0 4 5 6 6 6 6 8 8 8 8","dal");
//    inputPrediksi("0 2 5 6 8 1 3 7 5 4 2 1","tamarbuto");
    
    
 //kalimat 4
    
//    inputPrediksi("1 0 6 6 6 6 6 6 7 7 8 8","nun"); 
//    inputPrediksi("2 0 6 6 6 6 5 4 8 1 8 8","ta"); 
//    inputPrediksi("1 0 4 4 5 6 7 4 1 8 8 8","kha");
//    inputPrediksi("2 2 6 6 6 6 8 8 8 8 8 2","ya"); 
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("1 2 6 6 6 6 6 6 7 8 8 8","ba");
//    inputPrediksi("0 0 6 6 6 6 5 1 1 8 8 8","dal");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("1 0 4 3 8 2 4 6 2 7 5 4","fa");
//    inputPrediksi("2 2 6 6 4 4 4 4 4 2 4 4","ya");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 7 8","lam");
//    inputPrediksi("0 0 4 4 4 6 5 4 8 1 8 8","ha");
//    inputPrediksi("2 2 6 6 6 6 6 6 4 8 8 8","ya");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 5","alif");
//    inputPrediksi("2 0 5 6 8 1 3 7 5 4 2 1","tamarbuto");
    
 //kalimat 5
    
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("1 0 6 6 6 6 6 6 6 8 8 8","nun");
//    inputPrediksi("2 0 6 6 6 6 6 6 4 8 8 8","ta");
//    inputPrediksi("1 0 6 6 3 5 8 2 4 4 8 8","dzo");
//    inputPrediksi("0 0 6 6 6 7 8 8 4 4 2 8","ra");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("2 2 6 8 6 5 6 8 8 8 1 2","ya");
//    inputPrediksi("0 0 4 2 4 6 8 4 2 1 7 6","mim");
//    inputPrediksi("0 0 4 4 5 6 7 4 1 8 8 8","ha");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 5 4","alif");
//    inputPrediksi("0 0 6 7 7 5 5 5 6 6 8 8","kaf");
//    inputPrediksi("0 0 4 3 6 5 8 4 3 2 8 6","mim");
//    inputPrediksi("2 0 6 6 5 8 3 8 5 4 2 2","tamarbuto");
    
 // kalimat 6
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 7 8","lam");
//    inputPrediksi("0 0 4 4 4 6 5 4 8 1 8 8","ha");
//    inputPrediksi("1 0 4 2 4 6 4 4 3 1 7 6","fa");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 5 4","alif"); 
//    inputPrediksi("1 0 6 6 3 5 8 8 6 4 8 8","dzo"); 
//    inputPrediksi("0 0 1 8 7 6 5 4 4 8 8 8","ain"); 
//    inputPrediksi("0 0 6 6 6 6 8 8 7 8 8 2","ya"); 
//    inputPrediksi("0 0 4 6 8 8 3 7 4 4 2 8","sad"); 
//    inputPrediksi("0 0 6 6 6 6 6 5 1 1 8 8","unknown");
//    inputPrediksi("0 0 4 4 4 6 5 4 8 1 8 8","ha");
//    inputPrediksi("2 0 6 6 6 6 5 4 8 1 8 8","ta");
//    inputPrediksi("1 0 6 6 6 6 6 6 6 2 8 8","kaf");
//    inputPrediksi("0 0 6 6 6 6 6 5 5 5 4 4","unknown");
//    inputPrediksi("0 0 4 4 4 5 8 8 8 4 4 4","ha");
//    inputPrediksi("2 2 6 6 6 6 6 4 4 8 8 8","ya");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 4","alif");
//    inputPrediksi("2 0 5 6 8 2 3 7 6 4 2 1","tamarbuto");
//    inputPrediksi("0 0 6 6 3 5 8 8 4 4 1 7","tho");
//    inputPrediksi("2 2 6 6 6 6 5 4 8 1 8 8","ya");
//    inputPrediksi("1 2 6 6 6 6 6 6 4 8 8 8","ba");
//    inputPrediksi("2 0 6 6 6 8 3 8 5 4 2 2","tamarbuto");

    //kalimat 7
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 7 8","lam");
//    inputPrediksi("0 0 4 3 6 5 8 4 8 2 8 6","mim");
//    inputPrediksi("0 0 4 3 4 6 5 8 1 2 8 5","ain");
//    inputPrediksi("0 0 6 6 6 8 1 7 4 8 3 6","mim"); 
//    inputPrediksi("3 2 6 6 8 2 6 8 8 1 2 2","ya"); 
//    inputPrediksi("1 2 6 6 6 6 6 7 7 8 8 8","ba"); 
//    inputPrediksi("0 0 4 1 4 6 4 4 1 8 7 5","ain");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 4 1 8","lam");
//    inputPrediksi("0 0 8 2 7 4 4 4 8 2 5 6","mim");
//    inputPrediksi("0 0 8 1 5 6 8 4 2 1 4 6","waw");
//    inputPrediksi("1 0 6 6 6 7 8 8 1 2 2 2","nun");
    
    //kalimat 8
//    inputPrediksi("1 2 6 4 5 8 8 4 4 1 8 8","jim");
//    inputPrediksi("0 0 5 6 6 6 5 8 1 8 8 8","dal");
//    inputPrediksi("2 0 5 6 8 1 3 7 5 4 2 1","tamarbuto");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("0 0 6 6 6 6 6 6 1 5 8 8","lamalif");
//    inputPrediksi("0 0 6 6 6 6 6 7 8 8 8 8","ra"); 
//    inputPrediksi("1 0 6 6 6 6 6 7 8 8 8 8","za"); 
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 7 8","lam");
//    inputPrediksi("0 0 8 2 7 4 4 4 8 2 5 6","mim"); 
//    inputPrediksi("0 0 6 6 3 5 8 2 4 4 8 8","tho");
//    inputPrediksi("1 2 6 6 6 6 6 4 8 8 8 8","ba");
//    inputPrediksi("0 0 4 4 2 1 3 6 2 8 5 4","waw");
//    inputPrediksi("1 0 4 4 5 8 7 6 5 4 4 4","kha");
    
    //kalimat 9
//    inputPrediksi("1 2 6 6 6 6 6 6 7 8 8 8","ba");
//    inputPrediksi("2 0 6 6 6 6 6 6 4 8 8 8","ta");
//    inputPrediksi("0 0 6 8 5 5 5 6 8 4 2 1","kaf");
//    inputPrediksi("0 0 6 6 6 8 8 4 6 3 2 6","lam");
//    inputPrediksi("1 2 6 6 6 6 6 6 7 7 8 8","ba");
//    inputPrediksi("1 2 6 6 6 6 5 4 8 1 8 8","ba");
//    inputPrediksi("0 0 6 6 3 5 8 8 4 4 8 8","tho");
//    inputPrediksi("0 0 6 5 8 7 5 4 4 8 8 8","ain"); 
//    inputPrediksi("0 0 4 2 4 6 8 4 2 1 7 6","mim"); 
//    inputPrediksi("1 0 4 2 4 6 4 4 8 2 8 6","fa");
//    inputPrediksi("0 0 4 6 4 5 1 5 2 4 2 6","habesar");
//    inputPrediksi("0 0 8 1 5 6 8 4 3 1 4 6","waw");
//    inputPrediksi("0 0 4 2 4 6 8 4 2 1 7 6","mim");
//    inputPrediksi("2 0 6 6 5 8 3 8 5 4 2 2","tamarbuto"); 
//    inputPrediksi("0 0 4 4 4 4 5 6 8 8 8 8","ha"); 
//    inputPrediksi("2 2 6 8 8 8 8 1 6 4 4 2","ya"); 
 
    //kalimat 10
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif"); 
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 7 8","lam");
//    inputPrediksi("0 0 4 4 4 6 5 4 1 8 8 8","ha"); 
//    inputPrediksi("1 0 4 2 4 6 4 4 8 2 8 5","fa");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 4","lam"); 
//    inputPrediksi("1 0 6 6 3 5 8 8 6 4 1 7","dzo");
//    inputPrediksi("0 0 1 8 7 5 4 4 8 8 7 8","ain");
//    inputPrediksi("2 2 6 6 6 6 8 8 7 8 8 2","ya"); 
//    inputPrediksi("0 0 4 6 8 8 3 7 4 4 2 8","sad"); 
//    inputPrediksi("0 0 6 6 6 6 5 5 1 1 8 8","unknown"); 
//    inputPrediksi("0 0 4 4 4 6 5 4 1 8 8 8","ha"); 
//    inputPrediksi("2 0 6 6 6 6 5 4 8 1 8 8","ta"); 
//    inputPrediksi("1 0 6 6 6 6 6 6 6 7 8 8","kaf");
//    inputPrediksi("0 0 6 6 6 6 6 6 5 5 5 5","unknown");
//    inputPrediksi("0 0 4 4 4 4 5 6 8 8 8 8","ha"); 
//    inputPrediksi("2 2 6 6 6 6 6 6 4 8 8 8","ya"); 
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 5","alif");
//    inputPrediksi("2 0 5 6 8 2 3 7 6 4 2 1","tamarbuto");
//    inputPrediksi("0 0 6 6 3 5 8 8 4 4 1 7","tho");
//    inputPrediksi("2 2 6 6 6 6 5 4 8 1 8 8","ya");
//    inputPrediksi("1 2 6 6 6 6 6 6 4 8 8 8","ba");
//    inputPrediksi("2 0 6 6 6 8 3 8 5 4 2 2","tamarbuto");
    
  //-----------------------------------------------batas kalimat tahoma---------------------------------------------------
    //kalimat 1
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("0 0 6 6 6 6 7 2 2 6 7 8","lamalif");
//    inputPrediksi("0 0 4 4 4 4 2 8 7 8 8 8","ha");
//    inputPrediksi("2 0 6 6 6 5 4 4 8 8 1 8","ta");
//    inputPrediksi("0 0 5 6 4 6 8 8 4 3 2 8","ra");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("0 0 6 6 8 2 7 3 6 4 2 8","mim"); 
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif"); 
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 7 8","lam");  
//    inputPrediksi("0 0 4 2 5 4 8 5 3 2 7 6","mim"); 
//    inputPrediksi("2 0 6 6 6 6 5 4 8 7 8 8","ta"); 
//    inputPrediksi("1 2 6 6 6 5 4 4 8 8 1 8","ba");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 5 4","alif");
//    inputPrediksi("0 0 6 5 5 5 6 6 8 8 8 8","dal");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 8 8","lam");
//    inputPrediksi("0 0 6 6 6 6 6 5 5 4 4 4","unknown"); 
//    inputPrediksi("1 2 6 6 6 6 6 6 7 7 8 8","ba");
//    inputPrediksi("2 2 6 6 6 5 4 8 8 1 8 8","ya");
//    inputPrediksi("1 0 6 6 5 4 4 2 2 4 8 6","nun");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("0 0 6 6 6 6 7 2 2 6 7 8","lamalif");
//    inputPrediksi("0 0 6 5 5 5 6 6 8 8 8 8","dal");
//    inputPrediksi("2 2 6 6 6 6 6 6 7 7 8 8","ya");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 5 4","alif");
//    inputPrediksi("1 0 6 6 6 6 6 6 7 7 8 8","nun");
//    inputPrediksi("1 0 6 6 6 6 6 5 5 5 4 4","nun");
   
    
    //kalimat 2
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 7 8","lam"); 
//    inputPrediksi("2 0 4 2 4 6 4 4 1 2 8 5","qaf"); 
//    inputPrediksi("0 0 4 2 5 5 8 5 2 1 8 6","mim");
//    inputPrediksi("0 0 6 6 6 7 8 8 4 3 2 8","ra");
//    inputPrediksi("2 0 6 6 6 6 6 6 6 8 8 8","ta");
//    inputPrediksi("1 2 6 6 6 6 4 4 8 1 8 8","ba"); 
//    inputPrediksi("0 0 6 6 6 4 8 8 4 4 8 1","dal"); 
//    inputPrediksi("0 0 6 6 7 8 2 6 4 4 2 1","waw"); 
//    inputPrediksi("1 2 4 4 4 4 2 8 7 8 8 8","jim");
//    inputPrediksi("0 0 4 2 5 5 8 5 3 2 7 6","mim");
//    inputPrediksi("2 2 6 6 6 5 4 8 1 1 8 8","ya");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 4 1 8","lam");
//    inputPrediksi("2 0 8 2 2 8 4 4 8 4 6 6","tamarbuto");
//    inputPrediksi("1 2 4 4 4 5 3 7 7 8 8 8","jim");
//    inputPrediksi("0 0 6 6 6 5 4 8 7 8 8 8","dal");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    
    //kalimat 3
//    inputPrediksi("1 0 4 4 2 7 5 4 8 2 5 6","fa");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 5 4","lam");
//    inputPrediksi("0 0 6 7 5 5 5 6 6 8 8 8","kaf"); 
//    inputPrediksi("0 0 4 2 5 8 3 8 6 4 8 6","habesar");
//    inputPrediksi("2 0 8 2 2 8 5 8 2 4 6 6","tamarbuto");
//    inputPrediksi("0 0 6 5 5 5 6 6 8 8 8 8","dal");
//    inputPrediksi("0 0 6 6 7 8 2 6 4 4 2 1","waw");
//    inputPrediksi("0 0 6 6 6 6 6 7 7 8 8 8","ra"); 
//    inputPrediksi("2 2 6 6 6 6 6 6 7 7 8 8","ya");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 5 4","alif");
//    inputPrediksi("1 0 6 6 6 6 6 6 7 7 8 8","nun");
//    inputPrediksi("1 0 6 6 6 6 6 5 5 5 4 4","nun");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("0 0 6 6 6 6 7 2 2 6 7 8","lamalif");
//    inputPrediksi("1 0 5 5 5 5 6 6 8 8 8 8","dzal");
//    inputPrediksi("0 0 6 6 7 8 2 6 4 4 2 1","waw");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("2 0 6 4 4 2 2 6 4 8 4 6","qaf"); 
//    inputPrediksi("1 2 4 4 4 5 2 7 7 8 8 8","jim"); 
//    inputPrediksi("2 2 6 6 6 5 4 4 8 1 7 8","ya"); 
//    inputPrediksi("0 0 6 6 6 4 8 8 4 4 8 1","dal");  
//    inputPrediksi("2 0 4 5 6 8 2 8 6 4 2 1","tamarbuto");
    
    //kalimat 4
//    inputPrediksi("2 0 6 6 6 6 6 6 6 8 8 8","ta");  
//    inputPrediksi("2 0 6 6 6 5 4 8 8 7 8 8","ta"); 
//    inputPrediksi("1 0 4 4 5 6 7 4 8 1 8 8","kha"); 
//    inputPrediksi("0 0 6 6 6 6 6 6 6 4 1 8","lam"); 
//    inputPrediksi("2 2 6 5 4 4 4 3 1 4 5 4","ya"); 
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("1 2 6 6 6 6 6 6 7 7 8 8","ba");
//    inputPrediksi("0 0 6 6 6 4 8 8 4 4 3 1","dal");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("1 0 4 3 2 1 6 4 8 2 5 6","fa");
//    inputPrediksi("2 2 6 5 4 4 4 2 1 4 5 4","ya");   
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");  
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 7 8","lam");
//    inputPrediksi("0 0 4 4 5 6 7 4 8 1 8 8","ha"); 
//    inputPrediksi("2 2 6 6 6 5 4 4 8 1 7 8","ya");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 5 4","alif");
//    inputPrediksi("2 0 4 5 6 8 2 8 6 4 2 1","tamarbuto");
    
  //kalimat 5
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif"); 
//    inputPrediksi("1 0 6 6 6 6 6 6 7 7 8 8","nun");   
//    inputPrediksi("2 0 6 6 6 5 4 4 8 1 8 8","ta");
//    inputPrediksi("1 0 6 6 4 5 8 2 6 4 1 8","dzo");
//    inputPrediksi("0 0 5 6 4 6 8 8 4 3 2 8","ra");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("1 2 5 8 7 7 5 6 7 8 8 8","ya");
//    inputPrediksi("1 2 6 6 6 6 6 5 5 4 4 4","ba");
//    inputPrediksi("0 0 4 2 4 6 8 5 3 2 7 6","mim");
//    inputPrediksi("0 0 4 4 5 8 5 4 8 1 8 8","ha");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 5 4","alif");
//    inputPrediksi("0 0 6 5 8 1 4 3 2 5 3 1","kaf");
//    inputPrediksi("2 0 8 2 8 7 5 8 2 4 6 6","tamarbuto");
    
    //kalimat 6
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 7 8","lam");
//    inputPrediksi("0 0 4 4 4 8 5 4 8 8 8 8","ha");
//    inputPrediksi("1 0 4 1 4 6 4 4 1 2 8 5","fa");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 5 4","alif");
//    inputPrediksi("1 0 6 6 4 6 8 8 6 4 1 8","dzo");
//    inputPrediksi("0 0 1 8 7 5 4 4 8 8 8 8","ain");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 4 1 8","lam");
//    inputPrediksi("2 2 6 5 4 4 4 3 1 4 5 4","ya");
//    inputPrediksi("0 0 6 3 5 8 8 4 4 8 7 8","sad");
//    inputPrediksi("0 0 4 4 5 8 5 4 8 1 8 8","ha");
//    inputPrediksi("2 1 6 6 6 5 4 8 8 1 8 8","ta");
//    inputPrediksi("1 0 6 6 6 6 6 5 8 7 8 8","kaf");
//    inputPrediksi("0 0 6 6 6 6 5 5 4 4 4 4","unknown");
//    inputPrediksi("0 0 4 4 4 5 2 7 7 8 8 8","ha");
//    inputPrediksi("2 2 6 6 6 5 4 4 8 1 7 8","ya");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 5 4","alif");
//    inputPrediksi("2 0 4 5 6 8 2 7 5 3 2 1","tamarbuto");
//    inputPrediksi("0 0 6 6 4 5 8 2 6 4 1 8","dzo");
//    inputPrediksi("2 2 6 6 6 5 4 4 8 1 7 8","ya");
//    inputPrediksi("1 2 6 6 6 5 4 8 8 1 8 8","ba");
//    inputPrediksi("2 0 8 2 8 7 5 8 2 4 6 6","tamarbuto"); 
    
    //kalimat 7
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");    
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 8","lam"); 
//    inputPrediksi("0 0 4 2 4 4 8 5 3 2 8 6","mim");
//    inputPrediksi("0 0 4 1 3 4 5 8 1 8 7 5","ain");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 4 8 8","lam");
//    inputPrediksi("0 0 4 2 5 5 8 5 3 2 8 6","mim");
//    inputPrediksi("2 2 6 6 6 6 5 4 8 7 8 8","ya");
//    inputPrediksi("1 1 6 4 8 7 7 8 8 1 2 2","nun");
//    inputPrediksi("2 2 6 6 6 6 6 6 7 7 8 8","ya");
//    inputPrediksi("0 0 4 1 3 5 5 8 1 1 7 5","ain");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 4 8 8","lam");
//    inputPrediksi("0 0 4 2 4 4 8 5 3 2 8 6","mim");
//    inputPrediksi("0 0 2 1 6 6 8 4 2 2 5 6","waw"); 
//    inputPrediksi("1 0 6 6 6 6 6 6 7 7 8 8","nun"); 
//    inputPrediksi("1 0 6 6 6 6 6 5 5 5 4 4","nun");
    
    //kalimat 8
//    inputPrediksi("1 2 4 4 4 4 2 8 7 8 8 8","jim");   
//    inputPrediksi("0 0 6 6 6 4 8 8 4 4 8 1","dal");
//    inputPrediksi("2 0 4 5 6 8 2 8 6 4 2 1","tamarbuto");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("0 0 6 6 6 6 7 2 2 6 7 8","lamalif");
//    inputPrediksi("0 0 6 6 6 6 6 7 7 8 8 8","ra");
//    inputPrediksi("1 0 6 6 6 6 6 7 7 8 8 8","za");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 8","lam");
//    inputPrediksi("0 0 4 2 4 5 8 5 2 2 8 6","mim"); 
//    inputPrediksi("0 0 6 6 3 6 8 2 6 4 1 8","tho"); 
//    inputPrediksi("1 2 6 6 6 5 4 4 8 1 8 8","ba"); 
//    inputPrediksi("0 0 8 2 7 5 8 4 8 2 5 6","waw"); 
//    inputPrediksi("1 0 4 5 8 6 4 3 8 2 3 8","kha"); 
    
    
    //kalimat 9
//    inputPrediksi("2 2 6 6 6 6 6 6 7 7 8 8","ya"); 
//    inputPrediksi("2 0 6 6 6 5 4 8 8 1 8 8","ta"); 
//    inputPrediksi("0 0 7 5 5 6 5 4 8 8 8 8","kaf");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 4 1 8","lam");
//    inputPrediksi("0 0 8 2 7 4 7 3 7 1 3 6","mim");
//    inputPrediksi("1 2 6 6 6 6 6 6 7 7 8 8","ba");
//    inputPrediksi("1 2 6 6 6 5 4 8 8 1 8 8","ba");
//    inputPrediksi("0 0 6 6 4 6 8 2 6 4 1 8","tho");
//    inputPrediksi("0 0 8 8 6 5 4 5 8 8 7 8","ain");
//    inputPrediksi("0 0 4 2 4 6 8 5 3 2 7 6","mim");
//    inputPrediksi("1 0 8 3 1 7 7 4 8 3 6 7","fa");
//    inputPrediksi("0 0 4 2 6 8 2 7 6 3 8 8","habesar");
//    inputPrediksi("0 0 8 2 7 4 8 4 2 2 5 6","waw");
//    inputPrediksi("0 0 4 2 4 6 8 5 4 2 7 6","mim");
//    inputPrediksi("2 0 8 2 2 8 4 8 8 4 6 6","tamarbuto");
//    inputPrediksi("0 0 4 4 4 4 4 8 7 8 8 8","ha");
//    inputPrediksi("2 0 6 6 6 5 4 8 8 1 8 8","ta");
//    inputPrediksi("2 2 6 5 4 4 4 2 1 4 5 4","ya");
    
    
    
    //kalimat 10
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 6 6","alif"); 
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 7 8","lam"); 
//    inputPrediksi("0 0 4 4 4 8 5 4 8 8 8 8","ha"); 
//    inputPrediksi("1 0 4 1 4 6 4 4 1 2 8 5","fa"); 
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 5 4","alif");
//    inputPrediksi("1 0 6 6 4 6 8 8 6 4 1 8","dzo");
//    inputPrediksi("0 0 1 8 7 5 4 4 8 8 8 8","ain");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 4 1 8","lam");
//    inputPrediksi("2 2 6 5 4 4 4 3 1 4 5 4","ya");
//    inputPrediksi("0 0 6 3 5 8 8 4 4 8 7 8","sad");
//    inputPrediksi("0 0 4 4 5 8 5 4 8 1 8 8","ha");
//    inputPrediksi("2 1 6 6 6 5 4 8 8 1 8 8","ta");
//    inputPrediksi("1 0 6 6 6 6 6 5 8 7 8 8","kaf");
//    inputPrediksi("0 0 6 6 6 6 5 5 4 4 4 4","unknown");
//    inputPrediksi("0 0 4 4 4 5 2 7 7 8 8 8","ha");
//    inputPrediksi("2 2 6 6 6 5 4 4 8 1 7 8","ya");
//    inputPrediksi("0 0 6 6 6 6 6 6 6 6 5 4","alif");
//    inputPrediksi("2 0 4 5 6 8 2 7 5 3 2 1","tamarbuto");
//    inputPrediksi("0 0 6 6 4 5 8 2 6 4 1 8","tho");
//    inputPrediksi("2 2 6 6 6 5 4 4 8 1 7 8","ya");
//    inputPrediksi("1 2 6 6 6 5 4 8 8 1 8 8","ba");
//    inputPrediksi("2 0 8 2 8 7 5 8 2 4 6 6","tamarbuto");
//    
    
    int accuracy;
    accuracy = 100*correctCount/totalInputPrediksi;
    log.info("Accuracy = {}%", accuracy);
  }
}