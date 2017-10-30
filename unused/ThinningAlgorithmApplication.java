import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Profile;

import java.io.File;

@SpringBootApplication
@Profile("thinningalgorithmapp")
public class ThinningAlgorithmApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(ThinningAlgorithmApplication.class);
    static {
        log.info("java.library.path = {}", System.getProperty("java.library.path"));
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        SpringApplicationBuilder fs = new SpringApplicationBuilder(ThinningAlgorithmApplication.class).profiles("thinningalgorithmapp");
        fs.run();
    }

    public void run(String... args){
        Histogram hist = new Histogram();
        LookupTable lookupTable = new LookupTable();
        BinnaryTreshold bn = new BinnaryTreshold();

        final File imageFile= new File("D:/MAGISTER TMDG 09 FILE/PengenalanPola/BinerPic/platnew.jpg");
        log.info("Processing image file '{}' ...", imageFile);
        Mat imgMat = Highgui.imread(imageFile.getPath());
        Mat imgGry = Highgui.imread(imageFile.getPath(), Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        Mat imgGryCpy =imgGry.clone();

        log.info("Image mat: rows={} cols={}", imgMat.rows(), imgMat.cols());

        hist.setHistogram(imgMat);
        System.out.println("ada " + hist.uniqueColor + " colors");

        //--------------------------------
        lookupTable.setSinglelookup();
        //bn.setTreshold(70);
        bn.setTreshold(125);

        lookupTable = bn.createBinaryLookup();
        imgGryCpy = bn.getBinaryImage(imgGry, lookupTable);
        bn.cek(imgGryCpy);
        System.out.println("BATAS1");
        imgGryCpy = bn.getInvers(imgGryCpy);
        bn.cek(imgGryCpy);
        System.out.println("BATASNYA");
        ZhangSuen zs = new ZhangSuen();
        imgGryCpy = zs.process(imgGryCpy);
        bn.cek(imgGryCpy);
    }
}