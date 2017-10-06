/*
 * Copyright 2017 ainawind27.
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
package utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ainawind27
 */
public class STDChainCode {
    
    private int chainLongStd;
    private char additionChar;
    
    public STDChainCode(int chainLongStd, char additionChar){
        this.chainLongStd = chainLongStd + 1;
        this.additionChar = additionChar;
    }
    
//    public List<String> standarizeModel2(List<String> chains) {
//        List<String> resultingChain = new ArrayList<>();
//        List<List<Integer>> lipair = new ArrayList<>();
//        String current = "";
//        int sum_partial = 0;
//        for (int i = 0; i < chains.size(); i++) {
//            String now = chains.get(i);
//            sum_partial++;
//            if (current.equals("") || !current.equals(now)) {
//                current = now;
//                if ((float) sum_partial / chains.size() * 10 > 1.0f) {
//                    List<Integer> pair = new ArrayList<>();
//                    pair.add(Integer.parseInt(current));
//                    pair.add(sum_partial);
//                    lipair.add(pair);
//                }
//                sum_partial = 0;
//            }
//        }
//        for (int i=0; i < lipair.size(); i++) {
//            List<Integer> each = lipair.get(i);
//            int partial = each.get(1);
//            Math.round(lipair)
//        }
//        return resultingChain;
//    }
    
    public List<String> standarizeModel(List<String> chains) {
        List<String> resultingChain = new ArrayList<>();
        for (int i = 0; i < chains.size(); i++){
            String chainString = chains.get(i);
            int chainCount = chainString.length();
            if (chainCount < chainLongStd){
                chainString += '8'; 
                while (chainString.length() < chainLongStd) {
                    for (int j = 0; j < chainString.length() - 1; j++){
                        char examined1 = chainString.charAt(j);
                        char examined2 = chainString.charAt(j+1);
                        if (examined1 != examined2){
                            chainString = chainString.substring(0, j) + examined1 + chainString.substring(j, chainString.length());
                            j++;
                        }
                        if (chainString.length() >= chainLongStd) break;
                    }
                }
            } else {
                int before = 0;
                int totalSum = 1;
                while (chainString.length() >= chainLongStd && totalSum > 0) {
                    totalSum = 0;
                    for (int j = 0; j < chainString.length() - 1; j++) {
                        char examined1 = chainString.charAt(j);
                        char examined2 = chainString.charAt(j+1);
                        int sameChar = 0;
                        while (examined1 == examined2){
                            if (sameChar == 0) {
                                before = j;
                            }
                            j++;
                            sameChar++;
                            if (j+1>=chainLongStd) break;
                            examined1 = chainString.charAt(j);
                            examined2 = chainString.charAt(j+1);
                        }
                        if (sameChar > 0){
                            totalSum += sameChar;
                            chainString = replaceCharAt(chainString,j,"");
                            j--;
                        }
                        if (chainString.length() <= chainLongStd) break;
                    }
                }
//                System.out.println(chainString);
                totalSum = 1;
                while (chainString.length() >= chainLongStd && totalSum > 0) {
                    totalSum = 0;
                    for (int j = 0; j < chainString.length() - 3; j+=2) {
                        try {
                            char examined1 = chainString.charAt(j);
                            char examined2 = chainString.charAt(j+1);
                            char examined3 = chainString.charAt(j+2);
                            char examined4 = chainString.charAt(j+3);
                            int sameChar = 0;
                            while (examined1 == examined3 && examined2 == examined4){
                                if (sameChar == 0) {
                                    before = j;
                                }
                                j+=2;
                                sameChar++;
                                if (j+1>=chainLongStd) break;
                                try {
                                    examined1 = chainString.charAt(j);
                                    examined2 = chainString.charAt(j+1);
                                    examined3 = chainString.charAt(j+2);
                                    examined4 = chainString.charAt(j+3);
                                } catch (Exception e) {
                                    break;
                                }                             
                            }
                            if (sameChar > 0){
                                totalSum += sameChar;
                                chainString = replaceCharAt(chainString,j,"");
                                chainString = replaceCharAt(chainString,j,"");
                                j-=2;
                            }
                            if (chainString.length() <= chainLongStd) break;
                        } catch (Exception e) {
                            break;
                        }
                    }
                }
            }
            resultingChain.add(chainString);
        }
        return resultingChain;
    }
    
    private static String replaceCharAt(String s, int pos, String c){
        return s.substring(0,pos)+c+s.substring(pos+1);
    }
}
