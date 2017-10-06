package HMMFInal;

import java.io.File;

public class Const {
        public static final Const instance = new Const();
//        public static final String APP_FOLDER = "/home/gazandic" + File.separator + "Aina-Tesis" + File.separator;
//        public static final String TRAIN_DATA = APP_FOLDER +"datasetHurufArab" + File.separator;
//        public static final String TRAIN2_DATA = APP_FOLDER +"datasetKalimatArab" + File.separator;
//        public static final String MODEL_DATA = APP_FOLDER +"modelHurufArab/";
//        public static final int NUM_SYMBOLS = 8; // 8 karakter chaincode untuk training
//        public static final String TEST_IMAGES = APP_FOLDER + "dataHuruf/";
//        public static final String RESULT_IMAGES = APP_FOLDER + "result/";
        
      public static final String APP_FOLDER = "D:" + File.separator + "tesis" + File.separator + "final";
      public static final String TRAIN_DATA = APP_FOLDER + File.separator + "datasetHurufArab" + File.separator;  
      public static final String MODEL_DATAMAX = APP_FOLDER +"modelMaxHurufArab/";
      public static final String MODEL_DATA = APP_FOLDER + File.separator + "modelHurufArab" + File.separator;
      public static final int NUM_SYMBOLS = 8; // 8 karakter chaincode untuk training
      public static final String TEST_IMAGES = APP_FOLDER + File.separator + "dataHuruf" + File.separator;
        
        public static final String lineName = "line_";
        public static final String charName = "char_";
        public static final String segmentName = "segment_";
        public static final String thinSegmentName = "thin_";
}