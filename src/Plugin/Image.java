import java.util.*;
import java.io.*;

public class Image implements Comparable<Image>{
    public double dist;
    public MetricHist mh;
    public String imageName, mhStr;

    public static void main(String []args){
        System.out.println("oi");
    }

    public double getDist(){
        return dist;
    }

    public int compareTo(Image img) {
        return Integer.compare((int)(getDist()), (int)(img.getDist()));
    }

}