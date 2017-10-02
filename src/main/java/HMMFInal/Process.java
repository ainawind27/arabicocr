package HMMFInal;

import ArabicOCR.ArabicOCR;
import static ArabicOCR.ArabicOCR.processCharacter;
import image.BinaryImageShell;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;
import utils.ChainCode;
import utils.ImageSegmentation;
import utils.ImageTransformation;
import utils.Thinning;
import utils.segment.zidouri2.Segmentation;

public class Process {
    
    public static void train2() throws Exception {
        File dataset = new File(Const.TRAIN_DATA);
        System.out.println(dataset.getAbsolutePath());
        System.out.println(dataset.listFiles());
        for (File dir : dataset.listFiles()) {
            if (dir.isDirectory()) {
                for (File minidir : dir.listFiles()) {
                    if (minidir.isDirectory()) {
                        for (File file : minidir.listFiles()) {
                            String name = file.getName().replace(".png","").replace(".jpg","");
                            String[] listHuruf = name.split("_");
                            BinaryImageShell tempImage = new BinaryImageShell(file);
                            tempImage.getRedXList();
                            for (int j=0; j<listHuruf.length;j++) {
                                File model = new File(Const.MODEL_DATA + listHuruf[j]);
                                if (model.exists()) {
                                    try  {
//                                        File existingmodel = new File( Const.MODEL_DATA + listHuruf[j] + File.separator + name + ".ser");
//                                        if (!existingmodel.exists()) {
                                            int [][] data = ArabicOCR.train2(tempImage, j, listHuruf[j]);
//                                            System.out.println(Const.MODEL_DATA + listHuruf[j] + File.separator + name + ".ser");
                                            
                                            if (data != null && data.length > 0) {
//                                                HiddenMarkov hmm = new HiddenMarkov(data[0].length, Const.NUM_SYMBOLS);
//                                                hmm.setTrainSeq(data);
//                                                hmm.train();
//                                                // write object
//                                                Serialize.serializeModel(hmm, Const.MODEL_DATA + listHuruf[j] + File.separator + name + ".ser");
                                            }
                                            System.out.println();
                                            
//                                        }
                                    } catch (Exception ex) {
                                        System.out.println("exception at:" + name+listHuruf[j]);
                                        
                                        System.out.println(ex);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public static void train() throws Exception {
        // create model directory for each data
        File dataset = new File(Const.TRAIN_DATA);
        System.out.println(dataset.getAbsolutePath());
        System.out.println(dataset.listFiles());
        for (File file : dataset.listFiles()) {
            if (file.isDirectory()) {
                String name = file.getName();
                File model = new File(Const.MODEL_DATA + name);
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
                        BinaryImageShell tempImage = new BinaryImageShell(fImage);
                        int [][] data = ArabicOCR.train(tempImage);
                        HiddenMarkov hmm = new HiddenMarkov(data[0].length, Const.NUM_SYMBOLS);
                        hmm.setTrainSeq(data);
                        hmm.train();
//                        // write object
                        Serialize.serializeModel(hmm, Const.MODEL_DATA + name + File.separator + iName + ".ser");
                    }
                }
            }
        }
    }
    
    public static void test(String dirImage) throws Exception {
        File imageFile = new File(dirImage);
        BinaryImageShell image = new BinaryImageShell(imageFile);
        ArabicOCR.process(image);
    }
    
    private static BufferedImage whiteBorder(BufferedImage input) {
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
    
    public static void printHMM() throws Exception {
        File modelFiles = new File(Const.MODEL_DATA);
        for (File modelFolder : modelFiles.listFiles()) {
            if (modelFolder.isDirectory()) {
                System.out.println(modelFolder.getName());
                for (File modelFile : modelFolder.listFiles()) {
                    System.out.println(modelFile.getName());
                    HiddenMarkov currentModel = Serialize.deserializeObject(modelFile.getAbsolutePath());
                    currentModel.print();
                }
            }
        }
    }
}
