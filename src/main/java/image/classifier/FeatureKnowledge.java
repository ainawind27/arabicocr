/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package image.classifier;

import image.BinaryImageShell;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Albadr
 * @todo Integration with weka J48
 * @todo Code cleanup
 */
public class FeatureKnowledge {

    /*************************************************************************/
    /***********************   File for Database   ***************************/
    /*************************************************************************/
    public static String aspectRatio_PATH = "fx/aspectRatio.fsp";
    public static String densityRatio_PATH = "fx/densityRatio.fsp";
    public static String axisProjection_PATH = "fx/axisProjection.fsp";
    /*************************************************************************/
    /*************************   Feature Space   *****************************/
    /*************************************************************************/
    /**
     * String adalah kode huruf, 
     * ArrayList adalah daftar aspek ratio yang direkam dari contoh.
     */
    HashMap<String, ArrayList<Double>> aspectRatio_Space = new HashMap<String, ArrayList<Double>>();
    /**
     * String adalah kode huruf, 
     * ArrayList adalah daftar {topDownRatio, leftRightRatio} yang direkam.
     */
    HashMap<String, ArrayList<double[]>> densityRatio_Space = new HashMap<String, ArrayList<double[]>>();
    /**
     * String adalah kode huruf, 
     * ArrayList adalah daftar {horProj, verProj} yang direkam.
     * Masing masing horProj dan verProj adalah array akumulasi jumlah titik 
     * dihitung pada arah yang bersesuaian (horizontal dan vertikal).
     */
    HashMap<String, ArrayList<ArrayList<int[]>>> axisProjection_Space = new HashMap<String, ArrayList<ArrayList<int[]>>>();

    /**
     * Mengekstrak semua fitur yang mungkin dari gambar huruf yang diberikan.
     *
     * @param letterImage Representasi gambar asli, bukan skeleton yak!
     * @param letterRep Huruf yang ingin digunakan
     * @todo Tangani yang skeleton
     */
    public void letsLearn(BinaryImageShell letterImage, String letterRep) {
        //Aspect ratio
        double aspectRatio = FeatureExtraction.aspectRatio(letterImage);
        try {
            this.aspectRatio_Space.get(letterRep).add(aspectRatio);
        } catch (NullPointerException ex) {
            /*Assign new Feature Space (Array List) and add to HashMap*/
            ArrayList<Double> new_ARSpace = new ArrayList<Double>();
            new_ARSpace.add(aspectRatio);
            this.aspectRatio_Space.put(letterRep, new_ARSpace);
        }

        //Distribution
        double[] distrib = FeatureExtraction.distribution(letterImage);
        try {
            this.densityRatio_Space.get(letterRep).add(distrib);
        } catch (NullPointerException ex) {
            /*Assign new Feature Space (Array List) and add to HashMap*/
            ArrayList<double[]> new_DRSpace = new ArrayList<double[]>();
            new_DRSpace.add(distrib);
            this.densityRatio_Space.put(letterRep, new_DRSpace);
        }

        //Axis projection
        ArrayList<int[]> axProj = new ArrayList<int[]>();
        try {
            int[] horProj = FeatureExtraction.cumulativeHorizontalProjection(letterImage);
            int[] verProj = FeatureExtraction.cumulativeHorizontalProjection(letterImage);
            axProj.add(horProj);
            axProj.add(verProj);
            this.axisProjection_Space.get(letterRep).add(axProj);
        } catch (NullPointerException ex) {
            /*Assign new Feature Space (Array List) and add to HashMap*/
            ArrayList<ArrayList<int[]>> new_axProjSpace = new ArrayList<ArrayList<int[]>>();
            new_axProjSpace.add(axProj);
            this.axisProjection_Space.put(letterRep, new_axProjSpace);
        }


    }

    public void clearKnowledge() {
        this.aspectRatio_Space.clear();
        this.axisProjection_Space.clear();
        this.densityRatio_Space.clear();
    }

    public void saveKnowledge() {
        try {
            ObjectOutputStream save;
            /*SAVE ALL OF KNOWLEDGE FEATURE SPACE*/
            //Aspect Ratio
            save = new ObjectOutputStream(new FileOutputStream(FeatureKnowledge.aspectRatio_PATH));
            save.writeObject(aspectRatio_Space);
            save.close();
            //Density Ratio
            save = new ObjectOutputStream(new FileOutputStream(FeatureKnowledge.densityRatio_PATH));
            save.writeObject(densityRatio_Space);
            save.close();
            //Axis Projection
            save = new ObjectOutputStream(new FileOutputStream(FeatureKnowledge.axisProjection_PATH));
            save.writeObject(axisProjection_Space);
            save.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FeatureExtraction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FeatureExtraction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadKnowledge() {
        try {
            ObjectInputStream load;

            /*SAVE ALL OF KNOWLEDGE FEATURE SPACE*/
            //Aspect ratio
            load = new ObjectInputStream(new FileInputStream(FeatureKnowledge.aspectRatio_PATH));
            aspectRatio_Space = (HashMap<String, ArrayList<Double>>) load.readObject();
            load.close();


        } catch (FileNotFoundException ex) {
            Logger.getLogger(FeatureKnowledge.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FeatureKnowledge.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FeatureKnowledge.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public double distanceToAspectRatio(BinaryImageShell letter){
        double aspectRatio = FeatureExtraction.aspectRatio(letter);
        
        for (Entry<String, ArrayList<Double>> entry : aspectRatio_Space.entrySet()){
            String key = entry.getKey();
            ArrayList<Double> value = entry.getValue();
            double distance = 0;
            for (Double v : value){
                distance += v.doubleValue() - aspectRatio;
            }
            distance = distance / value.size();
        }
        
        
        
        return 2.0;
    }
    
    public void sout() {
        System.out.println(this.aspectRatio_Space);
        for (Entry<String, ArrayList<Double>> entry : aspectRatio_Space.entrySet()){
            String key = entry.getKey();
            ArrayList<Double> value = entry.getValue();
            System.out.println(key + "=" + value);
        }
    }
}
