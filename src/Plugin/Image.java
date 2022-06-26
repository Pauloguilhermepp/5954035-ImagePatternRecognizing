import java.util.*;
import java.io.*;

public class Image implements Comparable<Image>{
    private double dist;
    private MetricHist mh;
    private String imageName, mhStr;

    // Get distance to the reference image
    public double getDist(){
        return dist;
    }
    
    // Get metric histogram
    public MetricHist getMh(){
        return mh;
    }

    // Get image name
    public String getImageName(){
        return imageName;
    }

    // Get metric histogram in string format
    public String getMhStr(){
        return mhStr;
    }
    
    // Set distance to the reference image
    public void setDist(double Dist){
        dist = Dist;
    }
    
    // Set metric histogram
    public void setMh(MetricHist Mh){
        mh = Mh;
    }

    // Set image name
    public void setImageName(String ImageName){
        imageName = ImageName;
    }

    // Set metric histogram in string format
    public void setMhStr(String MhStr){
        mhStr = MhStr;
    }

    // Define comparation between two image objects 
    public int compareTo(Image img) {
        return Double.compare((getDist()), (img.getDist()));
    }

    // Pass metric histogram from string to vector of doubles
    public void strToVector(){
		double [] doubleArray;
		String [] tempArrayStr, arrayStr = mhStr.split(";");
		Vector<double []> data = new Vector<double []>();

        tempArrayStr = arrayStr[0].split(",");
        tempArrayStr[0] = tempArrayStr[0].substring(2, tempArrayStr[0].length());
        tempArrayStr[2] = tempArrayStr[2].substring(0, tempArrayStr[2].length() - 1);
        double [] tempDouble1 = {Double.parseDouble(tempArrayStr[0]),
                                 Double.parseDouble(tempArrayStr[1]),
                                 Double.parseDouble(tempArrayStr[2])};
        data.add(tempDouble1);

		for(int i = 1; i < arrayStr.length - 1; i++){

			tempArrayStr = arrayStr[i].split(",");
			tempArrayStr[0] = tempArrayStr[0].substring(1, tempArrayStr[0].length());
			tempArrayStr[2] = tempArrayStr[2].substring(0, tempArrayStr[2].length() - 1);
			double [] tempDouble2 = {Double.parseDouble(tempArrayStr[0]),
                                     Double.parseDouble(tempArrayStr[1]),
                                     Double.parseDouble(tempArrayStr[2])};
			data.add(tempDouble2);

		}

        tempArrayStr = arrayStr[arrayStr.length - 1].split(",");
        tempArrayStr[0] = tempArrayStr[0].substring(1, tempArrayStr[0].length());
        tempArrayStr[2] = tempArrayStr[2].substring(0, tempArrayStr[2].length() - 2);
        double [] tempDouble3 = {Double.parseDouble(tempArrayStr[0]),
                                 Double.parseDouble(tempArrayStr[1]),
                                 Double.parseDouble(tempArrayStr[2])};
        data.add(tempDouble3);

		setMh(new MetricHist(data));
	}
}