import java.util.*;
import java.io.*;

public class Image implements Comparable<Image>{
    private double dist;
    private MetricHist mh;
    private String imageName, mhStr;

    public void setDist(double Dist){
        dist = Dist;
    }
    
    public void setMh(MetricHist Mh){
        mh = Mh;
    }

    public void setImageName(String ImageName){
        imageName = ImageName;
    }

    public void setMhStr(String MhStr){
        mhStr = MhStr;
    }

    public double getDist(){
        return dist;
    }
    
    public MetricHist getMh(){
        return mh;
    }

    public String getImageName(){
        return imageName;
    }

    public String getMhStr(){
        return mhStr;
    }

    public int compareTo(Image img) {
        return Double.compare((getDist()), (img.getDist()));
    }

    public void strToVector(){
		double [] doubleArray;
		String [] tempArrayStr, arrayStr = mhStr.split(";");
		Vector<double []> data = new Vector<double []>();
		
		for(int i = 0; i < arrayStr.length; i++){
			if(i == 0){
				tempArrayStr = arrayStr[i].split(",");
				tempArrayStr[0] = tempArrayStr[0].substring(2, tempArrayStr[0].length());
				tempArrayStr[2] = tempArrayStr[2].substring(0, tempArrayStr[2].length() - 1);
				double [] tempDouble = {Double.parseDouble(tempArrayStr[0]), Double.parseDouble(tempArrayStr[1]), Double.parseDouble(tempArrayStr[2])};
				data.add(tempDouble);
				continue;
			}else if(i == arrayStr.length - 1){
				tempArrayStr = arrayStr[i].split(",");
				tempArrayStr[0] = tempArrayStr[0].substring(1, tempArrayStr[0].length());
				tempArrayStr[2] = tempArrayStr[2].substring(0, tempArrayStr[2].length() - 2);
				double [] tempDouble = {Double.parseDouble(tempArrayStr[0]), Double.parseDouble(tempArrayStr[1]), Double.parseDouble(tempArrayStr[2])};
				data.add(tempDouble);
				continue;
			}

			tempArrayStr = arrayStr[i].split(",");
			tempArrayStr[0] = tempArrayStr[0].substring(1, tempArrayStr[0].length());
			tempArrayStr[2] = tempArrayStr[2].substring(0, tempArrayStr[2].length() - 1);
			double [] tempDouble = {Double.parseDouble(tempArrayStr[0]), Double.parseDouble(tempArrayStr[1]), Double.parseDouble(tempArrayStr[2])};
			data.add(tempDouble);
		}

		setMh(new MetricHist(data));
	}
}