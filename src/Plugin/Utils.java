import java.util.*;
import java.io.*;
import ij.*;
import ij.io.*;
import ij.process.*;
import ij.gui.*;
import ij.plugin.filter.*;

public class Utils{
    public static void write(String content){
        try {
            FileWriter myWriter = new FileWriter("Debugger.txt", true);
            myWriter.write(content + "\n");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] getHistogram(ImagePlus img){
        String histString = "[";
        int [] hist = new int[256];
        int nx = img.getWidth(); 
		int ny = img.getHeight();

        for(int i = 0; i < nx; i++){
            for(int j = 0; j < ny; j++){
                hist[img.getPixel(i, j)[0]]++;
            }
        }

        return hist;
    } 
}