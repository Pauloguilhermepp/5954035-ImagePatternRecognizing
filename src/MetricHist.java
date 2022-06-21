import java.util.*;
import java.io.*;
public class MetricHist{

    private double maxError = 2000;
    private Vector<double []> data = new Vector<double []>();

    public MetricHist(int[] hist){
        loadMetricHist(hist);
    }

    
    public MetricHist(Vector<double []> dt){
        data = dt;
    }

    private static double[] getCoef(double x1, double y1, double x2, double y2){
        double [] coef = new double[2];
        coef[0] = (y1 - y2) / (x1 - x2);
        coef[1] = (x1 * y2 - x2 * y1) / (x1 - x2);

        return coef;
    }

    public static int [] loadHist(){
        int [] hist = {194, 391, 370, 379, 352, 216, 353, 362, 244, 287, 268, 402, 290, 305, 279, 478, 274, 300, 323, 311, 302, 330, 340, 353, 164, 330, 358, 203, 338, 346, 353, 166, 415, 214, 396, 231, 201, 413, 222, 238, 475, 224, 252, 237, 216, 463, 228, 253, 268, 259, 285, 196, 290, 285, 267, 314, 286, 304, 322, 282, 286, 314, 319, 304, 0, 321, 308, 306, 294, 341, 0, 331, 333, 388, 0, 360, 329, 0, 371, 409, 0, 397, 377, 0, 420, 405, 0, 387, 426, 0, 421, 0, 436, 0, 457, 440, 0, 428, 0, 434, 0, 483, 0, 524, 0, 516, 0, 474, 0, 557, 0, 517, 0, 459, 0, 524, 0, 538, 0, 576, 0, 549, 0, 0, 584, 0, 547, 0, 576, 0, 582, 0, 0, 579, 0, 0, 591, 0, 552, 0, 0, 623, 0, 601, 0, 632, 0, 0, 614, 0, 628, 0, 0, 661, 0, 0, 687, 0, 645, 0, 0, 684, 0, 0, 632, 0, 0, 750, 0, 677, 0, 0, 687, 0, 0, 712, 0, 0, 653, 0, 0, 713, 0, 0, 714, 0, 0, 615, 0, 0, 786, 0, 0, 698, 0, 0, 642, 0, 0, 718, 0, 0, 733, 0, 0, 672, 0, 0, 668, 0, 0, 628, 0, 0, 732, 0, 0, 676, 0, 0, 620, 0, 586, 0, 0, 593, 0, 565, 0, 0, 594, 0, 509, 0, 0, 547, 0, 486, 0, 478, 0, 445, 0, 395, 0, 435, 367, 0, 324, 298, 263, 230, 217, 182, 335, 119};
        return hist;
    }

    
    public void loadMetricHist(int[] hist){
        int start = 0, end = 0;
        double aBin = 0, aSum = 0;
        boolean startNewBin = false;
        
        for(int i = 0;  i < 255; i++){
            if(startNewBin){
                start = i - 1;
                aSum = 0;
                startNewBin = false;
            }
            
            end = i;

            aBin = (hist[start] + hist[end + 1]) * ((end + 1) - start) / 2.0;

            aSum += (hist[end] + hist[end + 1]) / 2.0;

            if(Math.abs(aBin - aSum) > maxError){
                double [] coefs = getCoef(start, hist[start], end, hist[end]);
                double [] array = {coefs[0], coefs[1], end};
                this.data.add(array);
                startNewBin = true;
            }

        }

        if(this.data.get(this.data.size() - 1)[2] != 255){
            double [] coefs = getCoef(start, hist[start], 255, hist[255]);
            double [] array = {coefs[0], coefs[1], 255};
            this.data.add(array);
        }
        
    }

    public String toString(){
        String metricHistStr = "[";

        for(int i = 0; i < data.size(); i++){
            metricHistStr += Arrays.toString(data.get(i)) + ";";
        }
        
        metricHistStr = metricHistStr.substring(0, metricHistStr.length() - 1);
        metricHistStr += "]";

        return metricHistStr;
    }

    
    public void saveMetricHist(String text){
        text += ":" + toString() + "\n";
        try {
            FileWriter myWriter = new FileWriter("MetricHistograms.txt", true);
            myWriter.write(text);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static boolean inters(double x1, double x2, double a1, double b1, double a2, double b2){
        if(a1 == a2){
            return false;
        }
        double x = (b2 - b1) / (a1 - a2);

        if(x1 < x && x < x2){
            return true;
        }

        return false;

    }
    private static double trapDif(double b1, double b2, double h){
        return Math.abs(b1 - b2) * h / 2;
    }

    public static double distance(MetricHist mh1, MetricHist mh2){
        int p1 = 0, p2 = 0;
        double distance = 0, b1, b2, x;

        for(int i = 0; i < 255; i++){

            if(mh1.data.get(p1)[2] == i){
                p1++;
            }

            if(mh2.data.get(p2)[2] == i){
                p2++;
            }

            if(inters(i, i+1, mh1.data.get(p1)[0], mh1.data.get(p1)[1], mh2.data.get(p2)[0], mh2.data.get(p2)[1])){
                
                x = (mh2.data.get(p2)[1] - mh1.data.get(p1)[1]) / (mh1.data.get(p1)[0] - mh2.data.get(p2)[0]);

                b1 = (mh1.data.get(p1)[0] * i + mh1.data.get(p1)[1]) + (mh1.data.get(p1)[0] * x + mh1.data.get(p1)[1]);
                b2 = (mh2.data.get(p2)[0] * i + mh2.data.get(p2)[1]) + (mh2.data.get(p2)[0] * x + mh2.data.get(p2)[1]);
                
                distance += trapDif(b1, b2, x - i);
                
                b1 = (mh1.data.get(p1)[0] * x + mh1.data.get(p1)[1]) + (mh1.data.get(p1)[0] * (i+1) + mh1.data.get(p1)[1]);
                b2 = (mh2.data.get(p2)[0] * x + mh2.data.get(p2)[1]) + (mh2.data.get(p2)[0] * (i+1) + mh2.data.get(p2)[1]);

                distance += trapDif(b1, b2, (i+1) - x);

            }else{
                b1 = (mh1.data.get(p1)[0] * i + mh1.data.get(p1)[1]) + (mh1.data.get(p1)[0] * (i+1) + mh1.data.get(p1)[1]);
                b2 = (mh2.data.get(p2)[0] * i + mh2.data.get(p2)[1]) + (mh2.data.get(p2)[0] * (i+1) + mh2.data.get(p2)[1]);
                distance += trapDif(b1, b2, 1);

            }

        }

        return distance; 
    }
    public static void main(String[] args){
        // Loading histogram:
        int [] hist = loadHist();
        int [] hist1 = loadHist();
        hist1[0] = 195;

        // Calculating the Metric Histogram:
        MetricHist mh = new MetricHist(hist);
        MetricHist mh1 = new MetricHist(hist1);
        System.out.println(distance(mh, mh1));
        //mh1.saveMetricHist();

    }
}