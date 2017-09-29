

import org.opencv.core.Mat;

/**
 * Created by Yusfia Hafid A on 10/8/2015.
 */

public class ZhangSuen {
    private byte[] P = new byte[9];
    private byte foreground = 1;
    private byte background = 0;
    private byte imageout[][];
    private boolean mark[][];

    public Mat process(Mat image) {
        int row = image.rows();
        int col = image.cols();
        boolean finish = false;
        imageout = new byte[row][col];
        mark = new boolean[row][col];
        while (!finish) {
            int count=0;
            //STEP 1 - Mark semua FG yang memenuhi kondisi 1 sampai 4
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {

                    byte data[] = new byte[1];
                    image.get(i, j, data);
                    P[0] = data[0];

                    if (P[0] == foreground) {
                        if (i == 0 || i == row - 1 || j == 0 || j == col - 1) {
                        } else {
                            byte data1[] = new byte[1];
                            image.get(i - 1, j, data1);
                            P[1] = data[0];

                            byte data2[] = new byte[1];
                            image.get(i - 1, j + 1, data2);
                            P[2] = data2[0];

                            byte data3[] = new byte[1];
                            image.get(i, j + 1, data3);
                            P[3] = data3[0];

                            byte data4[] = new byte[1];
                            image.get(i + 1, j + 1, data4);
                            P[4] = data4[0];

                            byte data5[] = new byte[1];
                            image.get(i + 1, j, data5);
                            P[5] = data5[0];

                            byte data6[] = new byte[1];
                            image.get(i + 1, j - 1, data6);
                            P[6] = data6[0];

                            byte data7[] = new byte[1];
                            image.get(i, j - 1, data7);
                            P[7] = data7[0];

                            byte data8[] = new byte[1];
                            image.get(i - 1, j - 1, data8);
                            P[8] = data8[0];

                            if (firstCondition() && secondCondition() && thirdCondition() && fourthCondition()) {
                                mark[i][j] = true;
                            }
                        }
                    }
                }
            }

            // STEP 2 - Ubah semua pixel yang sudah ditandai menjadi background
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if (mark[i][j]) {
                        byte data[] = new byte[1];
                        data[0] = background;
                        image.put(i, j, data[0]);
                    }
                    mark[i][j] = false;
                }
            }

            // STEP 3 - Mark semua FG yang memenuhi kondisi 5 & 8
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    byte data[] = new byte[1];
                    image.get(i, j, data);
                    P[0] = data[0];

                    if (P[0] == foreground) {
                        if (i == 0 || i == row - 1 || j == 0 || j == col - 1) {
                        } else {
                            byte data1[] = new byte[1];
                            image.get(i - 1, j, data1);
                            P[1] = data[0];

                            byte data2[] = new byte[1];
                            image.get(i - 1, j + 1, data2);
                            P[2] = data2[0];

                            byte data3[] = new byte[1];
                            image.get(i, j + 1, data3);
                            P[3] = data3[0];

                            byte data4[] = new byte[1];
                            image.get(i + 1, j + 1, data4);
                            P[4] = data4[0];

                            byte data5[] = new byte[1];
                            image.get(i + 1, j, data5);
                            P[5] = data5[0];

                            byte data6[] = new byte[1];
                            image.get(i + 1, j - 1, data6);
                            P[6] = data6[0];

                            byte data7[] = new byte[1];
                            image.get(i, j - 1, data7);
                            P[7] = data7[0];

                            byte data8[] = new byte[1];
                            image.get(i - 1, j - 1, data8);
                            P[8] = data8[0];

                            if (fifthCondition() && sixthCondition() && seventhCondition() && eighthCondition()) {
                                mark[i][j] = true;
                            }
                        }
                    }
                }
            }

            // STEP 4 - Ubah pixel yang mempunyai mark menjadi background
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if (mark[i][j]) {
                        byte data[] = new byte[1];
                        data[0] = background;
                        image.put(i, j, data[0]);
                        count++;
                    }
                    mark[i][j] = false;
                }
            }
            if (count==0){
                System.out.print("COUNT = "+count);
                finish = true;
            }else{
                //System.out.print("COUNT = "+count);
            }
        }
        return image;
    }

    private int displacementBgToFg() {
        int count = 0;
        for (int i = 1; i < P.length; i++) {
            if (i == 8) {
                if (P[8] == background && P[1] == foreground) count++;
            } else {
                if (P[i] == background && P[i + 1] == foreground) count++;
            }
        }
        return count;
    }

    private int neighbouringForeground() {
        int neighbour = 0;
        for (int i = 1; i < P.length; i++) {
            if (P[i] == foreground) neighbour++;
        }
        return neighbour;
    }

    private boolean firstCondition() { // 2<=N(P1)<=6
        if (2 <= neighbouringForeground() && neighbouringForeground() <= 6)
            return true;
        else return false;
    }

    private boolean secondCondition() { // S(P1) = 1
        return displacementBgToFg() == 1;
    }

    private boolean thirdCondition() { // P2*P4*P6=0
        return P[1] * P[3] * P[5] == 0;
    }

    private boolean fourthCondition() { //P4*P6*P8=0
        return P[3] * P[5] * P[7] == 0;
    }

    private boolean fifthCondition() { // 2<=N(P1)<=6
        return firstCondition();
    }

    private boolean sixthCondition() { //S(P1)=1
        return secondCondition();
    }

    private boolean seventhCondition() { //P2*P4*P8=0
        return P[1] * P[3] * P[7] == 0;
    }

    private boolean eighthCondition() { //P2*P6*P8=0
        return P[1] * P[5] * P[7] == 0;
    }
}