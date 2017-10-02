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
    
    public DataTrain(int[][] interestPoint, int[][]secCount, int[][] chain){
        this.interestPoint =interestPoint;
        this.secCount = secCount;
        this.chain = chain;
    }
    
    public int[][] getMergeData(){
        int totallength = interestPoint.length + secCount.length + chain.length;
        int aLen = interestPoint.length;
        int bLen = secCount.length;
        int cLen = chain.length;
        int[][] data = new int[totallength][];
        System.arraycopy(interestPoint, 0, data, 0, aLen);
        System.arraycopy(secCount, 0, data, aLen, bLen);
        System.arraycopy(chain, 0, data, bLen+aLen, cLen);
        return data;
    }
    
    public int[] getDataTrain(int mode){
        int totallength = (secCount.length * secCount[0].length) + chain[0].length;// +  (interestPoint.length * interestPoint[0].length) ;
        int[] data = new int[totallength];
        int current = 0;
//        for (int i=0;i<interestPoint.length;i++) {
//            int aLen = interestPoint[i].length;
//            System.arraycopy(interestPoint[i], 0, data, current, aLen);
//            current+=aLen;
//        }
        for (int i=0;i<secCount.length;i++) {
            int bLen = secCount[i].length;
            System.arraycopy(secCount[i], 0, data, current, bLen);
            current+=bLen;
        }
        int cLen = chain[0].length;
        System.arraycopy(chain[0], 0, data, current, cLen);
        current+=cLen;
        for (int i=0;i<data.length;i++) {
            System.out.print(data[i]);
        }
        System.out.println();
        return data;
    }
}
