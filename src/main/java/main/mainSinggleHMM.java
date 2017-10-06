package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import ArabicOCR.ArabicOCR;
import HMM2.HMM;
import HMMFInal.Const;
import image.BinaryImageShell;
import serialize.ReadObject;
import serialize.WriteObject;
import utils.ChainCode;
import utils.ImageSegmentation;
import utils.ImageTransformation;
import utils.STDChainCode;
import utils.Thinning;
import utils.segment.zidouri2.Segmentation;

public class mainSinggleHMM {
	  private static final String modelFolder = "D:\\Data\\Kerja\\Individual Project\\Aina\\Test Gambar\\model huruf arab\\";

	    // for testing purposes only
	    public static void segmentationTest() throws Exception {
//	        String localLoc = "D:\\Data\\Kerja\\Individual Project\\Aina\\Test Gambar\\arabic5.png";
//	        // load image file into BufferedImage
//	        BufferedImage mainImage = ImageIO.read(new File(localLoc));
//	        // transform it into black-white image
//	        ImageTransformation imageTrans = new ImageTransformation(mainImage);
//	        BufferedImage bwImage = imageTrans.doBinary();
//	        // do line segmentation based on horizontal projection
//	        List<BufferedImage> lines = ImageSegmentation.lineSegmentation(bwImage);
//	        // do character segmentation
//	        for (int i = 0; i < lines.size(); i++) {
//	            BufferedImage imageChar = Segmentation.characterSegmentation(lines.get(i));
//	            ImageIO.write(imageChar, "bmp", new File(localLoc + "main_res.bmp"));
//	            List<BufferedImage> charSegments = ImageSegmentation.subWordSegmentation(imageChar);
//	            for (int j = 0; j < charSegments.size(); j++) {
//	                ImageIO.write(charSegments.get(j), "bmp", new File(localLoc + "res" + i + j + ".bmp"));
//	            }
//	        }
	    }

	    // for testing purposes only
	    public static void testModel() throws Exception {
//	        // load image
//	        BufferedImage image = ImageIO.read(new File(mainLocation + imageFileName));
//	        // transformation image
//	        ImageTransformation imageTrans = new ImageTransformation(image);
//	        BufferedImage bwImage = imageTrans.doBinary();
//	        // thinning
//	        Thinning thinning = new Thinning(bwImage);
//	        BufferedImage thinImage = thinning.doZhangSuen();
//	        // chain code
//	        int[][] arrayImage = ChainCode.imageToArray(thinImage);
//	        List<String> chains = ChainCode.multipleChain(arrayImage);
//	        String allChain = "";
//	        for (String chain : chains) {
//	            allChain += chain;
//	        }
//	        // init HMM object
//	        HMM hmm = new HMM(28, 8);
//	        hmm.initHMM();
//	        //// model before train
//	        System.out.println("Before training");
//	        hmm.print();
//	        // train
//	        //// init train data
//	        int[] o = new int[allChain.length()];
//	        for (int i = 0; i < o.length; i++) {
//	            o[i] = Integer.parseInt(allChain.substring(i, i + 1)) - 1;
//	        }
//	        hmm.train(o, 100);
//	        System.out.println("");
//	        System.out.println("After training");
//	        hmm.print();
//	        // write object
//	        WriteObject.serializeModel(hmm, mainLocation + "model.ser");
//	        HMM hmm2 = ReadObject.deserializeObject(mainLocation + "model.ser");
//	        System.out.println("This is what hmm2 prints");
//	        hmm2.print();
	    }

	    // generate models for every arabic characters
	    public static void createModel() throws Exception {
	        HMM hmm;
	        String datasetFolder = "D:\\Data\\Kerja\\Individual Project\\Aina\\Test Gambar\\dataset huruf arab\\";
	        // create model directory for each data
	        File dataset = new File(datasetFolder);
	        for (File file : dataset.listFiles()) {
	            if (file.isDirectory()) {
	                String name = file.getName();
	                File model = new File(modelFolder + name);
	                if (!model.exists()) {
	                    model.mkdir();
	                }
	            }
	        }
	        // write model to each destined folder
	        for (File file : dataset.listFiles()) {
	            if (file.isDirectory()) {
	                String name = file.getName();
	                for (File fImage : file.listFiles()) {
	                    if (!fImage.isDirectory()) {
	                        // get image's name for naming model
	                        String iName = fImage.getName();
	                        System.out.println(iName);
	                        // load the model image
	                        BufferedImage tempImage = ImageIO.read(fImage);
	                        // convert the image into black and white
	                        ImageTransformation imageTrans = new ImageTransformation(tempImage);
	                        BufferedImage bwImage = imageTrans.doBinary();
	                        // thinning
	                        Thinning thinning = new Thinning(bwImage);
	                        BufferedImage thinImage = thinning.doZhangSuen();
	                        // chain code
	                        int[][] arrayImage = ChainCode.imageToArray(thinImage);
	                        List<String> chains = ChainCode.multipleChain(arrayImage);
	                        String allChain = "";
	                        for (String chain : chains) {
	                            allChain += chain;
	                        }
	                        // init HMM object
	                        hmm = new HMM(28, 8);
	                        // train
	                        int[] o = new int[allChain.length()];
	                        for (int i = 0; i < o.length; i++) {
	                            o[i] = Integer.parseInt(allChain.substring(i, i + 1)) - 1;
	                        }
	                        hmm.train(o, 10);
	                        // write object
	                        WriteObject.serializeModel(hmm, modelFolder + name + "\\" + iName + ".ser");
	                    }
	                }
	            }
	        }
	    }

	    // end-to-end testing
	    public static void testing() throws Exception {
	        String imageLocation = "D:\\Data\\Kerja\\Individual Project\\Aina\\Test Gambar\\";
	        String imageName = "arabic1.png";
	        String clearName = imageName.split("\\.")[0];
	        String lineName = "line_";
	        String charName = "char_";
	        String segmentName = "segment_";
	        String thinSegmentName = "thin_";
	        // load the model image
	        BufferedImage tempImage = ImageIO.read(new File(imageLocation + imageName));
	        // convert the image into black and white
	        ImageTransformation imageTrans = new ImageTransformation(tempImage);
	        BufferedImage bwImage = imageTrans.doBinary();
	        // do line segmentation based on horizontal projection
	        List<BufferedImage> lines = ImageSegmentation.lineSegmentation(bwImage);
	        // do character segmentation
	        for (int i = 0; i < lines.size(); i++) {
	            ImageIO.write(lines.get(i), "bmp", new File(imageLocation + lineName + clearName + i + ".bmp"));
	            BufferedImage imageChar = Segmentation.characterSegmentation(lines.get(i));
	            ImageIO.write(imageChar, "bmp", new File(imageLocation + charName + i + ".bmp"));
	            List<BufferedImage> charSegments = ImageSegmentation.subWordSegmentation(imageChar);
	            // iterate through every image on the list
	            System.out.println("Gambar ke-" + i);
	            for (int j = 0; j < charSegments.size(); j++) {
	                ImageIO.write(charSegments.get(j), "bmp", new File(imageLocation + segmentName + j + ".bmp"));
	                // create white border around image
	                BufferedImage segment = charSegments.get(j);
	                segment = whiteBorder(segment);
	                // thinning
	                Thinning thinning = new Thinning(segment);
	                BufferedImage thinImage = thinning.doZhangSuen();
	                ImageIO.write(thinImage, "bmp", new File(imageLocation + thinSegmentName + j + ".bmp"));
	                // chain code
	                int[][] arrayImage = ChainCode.imageToArray(thinImage);
	                List<String> chains = ChainCode.multipleChain(arrayImage);
	                String allChain = "";
	                for (String chain : chains) {
	                    allChain += chain;
	                }
	                // preparing chain
	                int[] o = new int[allChain.length()];
	                for (int iterate = 0; iterate < o.length; iterate++) {
	                    o[iterate] = Integer.parseInt(allChain.substring(iterate, iterate + 1)) - 1;
	                }
	                // init similarity
	                double biggestSimilarity = -100.0;
	                String currentSim = "";
	                // load every model that has been created earlier
	                File models = new File(modelFolder);
	                for (File model : models.listFiles()) {
	                    if (model.isDirectory()) {
	                        // take the name of the folder
	                        String name = model.getName();
	                        // load every file inside that folder
	                        for (File modelFile : model.listFiles()) {
	                            // compute similarity
	                            if (!modelFile.isDirectory()) {
	                                String fullPath = modelFile.getAbsolutePath();
	                                HMM currentModel = ReadObject.deserializeObject(fullPath);
	                                double similarity = HMM.similarity(currentModel.pi, currentModel.a, currentModel.b, o);
	                                if (similarity > biggestSimilarity) {
	                                    biggestSimilarity = similarity;
	                                    currentSim = name;
	                                }
	                            }
	                        }
	                    }
	                }
	                // prints the answer
	                System.out.println(currentSim);
	            }
	        }
	    }

	    public static BufferedImage whiteBorder(BufferedImage input) {
	        int height = input.getHeight();
	        int width = input.getWidth();

	        for (int y = 0; y < height; y++) {
	            for (int x = 0; x < width; x++) {
	                if (x == 0 || y == 0 || y == height - 1 || x == width - 1) {
	                    input.setRGB(x, y, 0xffffffff);
	                }
	            }
	        }

	        return input;
	    }

	    public static void createModelNumber() throws Exception {
	        HMM hmm;
	        String datasetFolder = "D:\\Data\\Kerja\\Individual Project\\Aina\\dataset angka\\";
	        String modelLocal = "D:\\Data\\Kerja\\Individual Project\\Aina\\model angka\\";
	        // get all dataset
	        File data = new File(datasetFolder);
	        for (File d : data.listFiles()) {
	            String name = d.getName();
	            if (!d.isDirectory()) {
	                BufferedImage tempImage = ImageIO.read(new File(d.getAbsolutePath()));
	                // convert the image into black and white
	                ImageTransformation imageTrans = new ImageTransformation(tempImage);
	                BufferedImage bwImage = imageTrans.doBinary();
	                // thinning
	                Thinning thinning = new Thinning(bwImage);
	                BufferedImage thinImage = thinning.doZhangSuen();
	                // resize image
	                BufferedImage resizedImage = ImageTransformation.resizeImage(thinImage, 100, 100);
	                // chain code
	                int[][] arrayImage = ChainCode.imageToArray(resizedImage);
	                List<String> chains = ChainCode.multipleChain(arrayImage);
	                String allChain = "";
	                for (String chain : chains) {
	                    allChain += chain;
	                }
	                // init HMM object
	                hmm = new HMM(10, 8);
	                // train
	                int[] o = new int[allChain.length()];
	                for (int i = 0; i < o.length; i++) {
	                    o[i] = Integer.parseInt(allChain.substring(i, i + 1)) - 1;
	                }
	                hmm.train(o, 10);
	                // write object
	                WriteObject.serializeModel(hmm, modelLocal + name + ".ser");
	            }
	        }
	    }

	    public static void testModelNumber() throws Exception {
	        HMM hmm;
	        String datasetFolder = "D:\\Data\\Kerja\\Individual Project\\Aina\\dataset angka\\";
	        String modelLocal = "D:\\Data\\Kerja\\Individual Project\\Aina\\model angka\\";
	        String nameData = "9.JPG";
	        // load image
	        BufferedImage testImage = ImageIO.read(new File(datasetFolder + nameData));
	        // convert the image into black and white
	        ImageTransformation imageTrans = new ImageTransformation(testImage);
	        BufferedImage bwImage = imageTrans.doBinary();
	        // thinning
	        Thinning thinning = new Thinning(bwImage);
	        BufferedImage thinImage = thinning.doZhangSuen();
	        // resize image
	        BufferedImage resizedImage = ImageTransformation.resizeImage(thinImage, 100, 100);
	        // chain code
	        int[][] arrayImage = ChainCode.imageToArray(resizedImage);
	        List<String> chains = ChainCode.multipleChain(arrayImage);
	        String allChain = "";
	        for (String chain : chains) {
	            allChain += chain;
	        }
	        // HMM prep
	        int[] o = new int[allChain.length()];
	        for (int i = 0; i < o.length; i++) {
	            o[i] = Integer.parseInt(allChain.substring(i, i + 1)) - 1;
	        }
	        // init similarity
	        double biggestSimilarity = -100.0;
	        String currentSim = "";
	        // load every model that has been created earlier
	        File models = new File(modelLocal);
	        for (File model : models.listFiles()) {
	            if (!model.isDirectory()) {
	                HMM currentModel = ReadObject.deserializeObject(model.getAbsolutePath());
	                double similarity = HMM.similarity(currentModel.pi, currentModel.a, currentModel.b, o);
	                if (similarity > biggestSimilarity) {
	                    biggestSimilarity = similarity;
	                    currentSim = model.getName();
	                }
	            }
	        }
	        System.out.println(currentSim);
	    }

	    public static void printProbs() throws Exception {
	        String datasetFolder = "D:\\Data\\Kerja\\Individual Project\\Aina\\dataset huruf arab\\ain\\";
	        File datasetDir = new File(datasetFolder);
	        for (File file : datasetDir.listFiles()) {
	            if (!file.isDirectory()) {
	                String name = file.getName().trim().split("\\.")[0];
	                BufferedImage image = ImageIO.read(new File(file.getAbsolutePath()));
	                ImageTransformation transform = new ImageTransformation(image);
	                BufferedImage bwImage = transform.doBinary();
	                bwImage = ImageTransformation.cropObject(bwImage);
	                bwImage = ImageTransformation.resizeImage(bwImage, 50, 50);
	                bwImage = whiteBorder(bwImage);
	                Thinning thinning = new Thinning(bwImage);
	                BufferedImage thinImage = thinning.doZhangSuen();
	                ImageIO.write(thinImage, "bmp", new File(datasetFolder + name + ".bmp"));
	            }
	        }
	    }
	    
	    public static void changeDatasetExtension() throws Exception {
	        String datasetFolder = "D:\\Data\\Kerja\\Individual Project\\Aina\\dataset huruf arab\\";
	        File datasetDir = new File(datasetFolder);
	        for (File file : datasetDir.listFiles()) {
	            if (file.isDirectory()) {
	                for (File file2 : file.listFiles()) {
	                    String name = file2.getName().trim().split("\\.")[0];
	                    File fileReplacement = new File(file.getAbsolutePath() + "\\" + name + ".png");
	                    BufferedImage inImage = ImageIO.read(file2);
	                    ImageIO.write(inImage, "png", fileReplacement);
	                    file2.delete();
	                }
	            }
	        }
	    }
	    
	    public static float HMMtest() {
	        File dataset = new File(HMMFInal.Const.TEST_IMAGES + "100%/");
	        int total =0;
	        float correctness = 0.0f;
	        for (File file : dataset.listFiles()) {
	            String expected_result = file.getName().replace(".png", "");
	            BinaryImageShell image = new BinaryImageShell(file.getAbsolutePath());
	            String result = ArabicOCR.process(image);
	            int sum = expected_result.split("-").length;
	            total+=sum;
	            correctness += (ArabicOCR.test(result, expected_result) * sum);
	            System.out.println(result);
	            System.out.println(expected_result);
	            float eachIteration = (float) correctness/total;
	            System.out.println(eachIteration);
	        }
//	        System.out.println(total);
	        return (float) correctness/total ;
	    } 
	    
	    
	    private static void copyFolderLocalMax() {
	        File dataset = new File(Const.MODEL_DATA);
	        InputStream inStream = null;
		OutputStream outStream = null;
	        for (File dir : dataset.listFiles()) {
	            if (dir.isDirectory()) {
	               String name = dir.getName();
	               File model = new File(Const.MODEL_DATAMAX + name);
	               if (!model.exists()) {
	                   model.mkdir();
	               }
	               for (File file : dir.listFiles()) {
	                    String name2 = file.getName();
	                    File lastfinal2 = new File(model.getAbsolutePath() +"/"+ name2);
	                    if (!lastfinal2.exists()) {
	                        try {
	                            lastfinal2.createNewFile();
	                        } catch (IOException ex) {
	                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
	                        }
	                    }
	                    try {
	                        inStream = new FileInputStream(file);
	                        outStream = new FileOutputStream(lastfinal2);

	                        byte[] buffer = new byte[1024];

	                        int length;
	                        //copy the file content in bytes
	                        while ((length = inStream.read(buffer)) > 0){
	                            outStream.write(buffer, 0, length);
	                        }

	                        inStream.close();
	                        outStream.close();
	                    } catch (Exception e) {
	                        System.out.println(e);
	                    }
	               }
	            }
	        }
	    }
	    
	    private static void copyFolder() {
	        File dataset = new File(Const.MODEL_DATA);
	        InputStream inStream = null;
		OutputStream outStream = null;
	        for (File dir : dataset.listFiles()) {
	            if (dir.isDirectory()) {
	               String name = dir.getName();
	               File model = new File(Const.MODEL_DATAMAX + name);
	               if (!model.exists()) {
	                   model.mkdir();
	               }
	               for (File file : dir.listFiles()) {
	                    String name2 = file.getName();
	                    File lastfinal2 = new File(model.getAbsolutePath() +"/"+ name2);
	                    if (!lastfinal2.exists()) {
	                        try {
	                            lastfinal2.createNewFile();
	                        } catch (IOException ex) {
	                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
	                        }
	                    }
	                    try {
	                        inStream = new FileInputStream(file);
	                        outStream = new FileOutputStream(lastfinal2);

	                        byte[] buffer = new byte[1024];

	                        int length;
	                        //copy the file content in bytes
	                        while ((length = inStream.read(buffer)) > 0){
	                            outStream.write(buffer, 0, length);
	                        }

	                        inStream.close();
	                        outStream.close();
	                    } catch (Exception e) {
	                        System.out.println(e);
	                    }
	               }
	            }
	        }
	    }
	    
	    public static void main(String[] args) throws Exception {
//	        testNewChainCode();
//	          testAndHMMTrain();
	          System.out.println(HMMtest());
//	        HMM3.Process.printHMM();
	    }
	    
	    private static void testAndHMMTrain() throws Exception {
	        float correctness = 0.0f;
	        int iterasi = 1;
	        float max = 0.0f;
	        int maxIteration = 0;
	        while ((float) correctness < 0.7f && iterasi <= 10) {
	                correctness = 0.0f;
	                HMMFInal.Process.train();
	                //        HMMFInal.Process.train2();
	                correctness = HMMtest();
	                if (correctness > max) {
	                    max = (float) correctness;
	                    maxIteration = iterasi;
	                    if (correctness > 0.6744186) copyFolder();
	                    else  copyFolderLocalMax();
	                }
	                iterasi++;
	        }
	        System.out.println("maxIteration: "+maxIteration);
	        System.out.println("maxIteration: "+max);
	        
	    }
	    
	    private static void testAndSVMTrain() throws Exception {
	        float correctness = 0.0f;
	        int iterasi = 1;
	        float max = 0.0f;
	        int maxIteration = 0;
	        while ((float) correctness < 0.7f && iterasi <= 10) {
	                correctness = 0.0f;
	                HMMFInal.Process.train();
	                //        HMMFInal.Process.train2();
	                correctness = HMMtest();
	                if (correctness > max) {
	                    max = (float) correctness;
	                    maxIteration = iterasi;
	                    if (correctness > 0.6744186) copyFolder();
	                    else  copyFolderLocalMax();
	                }
	                iterasi++;
	        }
	        System.out.println("maxIteration: "+maxIteration);
	        System.out.println("maxIteration: "+max);
	        
	    }
	    private static void testNewChainCode() {
	        String allChain = "55445544554455445454540101010185544554455445544545454010101018";
	        List<String> lst = new ArrayList<>();
	        lst.add(allChain);
	       
	        STDChainCode s = new STDChainCode(30, '8');
	        List<String> chains = s.standarizeModel(lst);
	        allChain = "";
	        for (String chain : chains) {
	            allChain += chain;
	        }
	        System.out.println(allChain);
	    }
}
