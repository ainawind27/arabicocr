/*
 * Copyright 2017 gazandic.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ArabicOCR;

/**
 *
 * @author gazandic
 */
public class DataTrain {
    private int[][] interestPoint;
    private int[][] secCount;
    private int[][] chain;
    private int loop;
    public DataTrain(int[][] interestPoint, int[][]secCount, int[][] chain, int loop){
        this.interestPoint =interestPoint;
        this.secCount = secCount;
        this.chain = chain;
        this.loop = loop;
    }
    
    public static int[][] getMergeData(int[][] a, int[][] b){
        int totallength = a.length + b.length ;
        int aLen = a.length;
        int bLen = b.length;
        int[][] data = new int[totallength][];
        System.arraycopy(a, 0, data, 0, aLen);
        System.arraycopy(b, 0, data, aLen, bLen);
        return data;
    }
    
    public int[] getDataTrain(int mode){
//         +
        int loopCount = 6;
        int totallength =  chain[0].length + loopCount ; //(interestPoint.length * interestPoint[0].length * 2) +   (secCount.length * secCount[0].length * 6)
        int[] data = new int[totallength];
        int current = 0;
//        for (int k=0;k<2;k++) {
//            for (int i=0;i<interestPoint.length;i++) {
//                int aLen = interestPoint[i].length;
//                System.arraycopy(interestPoint[i], 0, data, current, aLen);
//                current+=aLen;
//            }
//        }
//        for (int k=0;k<6;k++) {
        int[] l = {loop};
        for (int i=0;i<loopCount;i++) {
            int bLen = 1;
            System.arraycopy(l, 0, data, current, bLen);
            current+=bLen;
        }
//        }
        int cLen = chain[0].length;
        System.arraycopy(chain[0], 0, data, current, cLen);
        current+=cLen;
        return data;
    }
}
