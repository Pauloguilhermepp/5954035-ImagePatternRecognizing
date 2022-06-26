import java.util.*;
import java.io.*;
import ij.*;
import ij.io.*;
import ij.process.*;
import ij.gui.*;
import ij.plugin.filter.*;

public class MetricHist{

    private double maxError = 2000;
    private Vector<double []> data = new Vector<double []>();

    // Create the metric histogram using a histogram
    public MetricHist(int[] hist){
        loadMetricHist(hist);
    }
    
    // Create the metric histogram using a metric histogram vector
    public MetricHist(Vector<double []> Data){
        setData(Data);
    }

    // Get maxError
    public double getMaxError(){
        return maxError;
    }

    // Get metric histogram vector
    public Vector<double []> getData(){
        return data;
    }

    // Set maxError
    public void setMaxError(double MaxError){
        maxError = MaxError;
    }

    // Set metric histogram vector
    public void setData(Vector<double []> Data){
        data = Data;
    }

    // Get coeficientes of a first order polinomial
    private static double[] getCoef(double x1, double y1, double x2, double y2){
        double [] coef = new double[2];
        coef[0] = (y1 - y2) / (x1 - x2);
        coef[1] = (x1 * y2 - x2 * y1) / (x1 - x2);

        return coef;
    }

    // Find metric histogram by usual histogram
    private void loadMetricHist(int[] hist){
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

            if(Math.abs(aBin - aSum) > getMaxError()){
                double [] coefs = getCoef(start, hist[start], end, hist[end]);
                double [] array = {coefs[0], coefs[1], end};
                data.add(array);
                startNewBin = true;
            }

        }

        if(data.get(data.size() - 1)[2] != 255){
            double [] coefs = getCoef(start, hist[start], 255, hist[255]);
            double [] array = {coefs[0], coefs[1], 255};
            data.add(array);
        }
        
    }

    // Pass metric histogram vector to string
    public String toString(){
        String metricHistStr = "[";

        for(int i = 0; i < data.size(); i++){
            metricHistStr += Arrays.toString(data.get(i)) + ";";
        }
        
        metricHistStr = metricHistStr.substring(0, metricHistStr.length() - 1);
        metricHistStr += "]";

        return metricHistStr;
    }

    // Save metric histogram vector in a text file
    public void saveMetricHist(String text, String file, int append){
        boolean appendMode = (append != 0);
        text += ":" + toString() + "\n";
        
        try {
            FileWriter myWriter = new FileWriter(file, appendMode);
            myWriter.write(text);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Change image to grayscale
    private static ImagePlus rbgToGrayScale(ImagePlus img){
        ImageConverter ic = new ImageConverter(img);
        ic.convertToGray8();
        img.updateAndDraw();

        return img;
    } 

    // Calculate the metric histogram of an image
    public static void evaluateImage(ImagePlus image, String imageName, int append){
        if (image == null) return;

        image = rbgToGrayScale(image); // Convert image to grayscale
        MetricHist mh = new MetricHist(Utils.getHistogram(image)); // Calculate the image metric histogram
        String file = "MetricHistograms.txt";
        mh.saveMetricHist(imageName, file, append);// Save image metric histogram

    }
    
    // Detects the intersection in a range of two lines
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

    // Calculate the difference of two trapezoids of same base:
    private static double trapDif(double b1, double b2, double h){
        return Math.abs(b1 - b2) * h / 2;
    }

    // Calculate the distance between two metric histograms:
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

}