import java.util.*;
import java.io.*;
import ij.*;
import ij.io.*;
import ij.process.*;
import ij.gui.*;
import ij.plugin.filter.*;

public class K_Nearest implements PlugInFilter {
    private static ImagePlus reference;        // Image reference
    private static int k;                      // Number of nearest neighbors
    private static Vector<Image> imagesDataBase = new Vector <Image>();

    public int setup(String arg, ImagePlus imp) {
        reference = imp;
        return DOES_ALL;
    }

    public boolean dialog(){
        GenericDialog gd = new GenericDialog("k-nearest neighbor search", IJ.getInstance());
        gd.addNumericField("Number of nearest neighbors (K):", 5, 0);
        gd.showDialog();
        
        if (gd.wasCanceled())
            return false;
        
        k = (int) gd.getNextNumber();

        return true;
    }

    public void run(ImageProcessor img) {

        if(!dialog()) return;

        showKNN();

    }

	private static Vector<double []> strToVector(String mhStr){
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

		return data;
	}

	private static void loadImagesDataBase(MetricHist mh1){
		String [] array = new String[2];

        
        File file = new File("MetricHistograms.txt");
        
        try {
            BufferedReader br
                = new BufferedReader(new FileReader(file));

            String st;
            while ((st = br.readLine()) != null){
                // Reading line:
                Image img = new Image();
                array = st.split(":");
                img.imageName = array[0];
                img.mhStr = array[1];

                // Saving metric histogram:
                img.mh = new MetricHist(strToVector(img.mhStr));
                img.dist = MetricHist.distance(img.mh, mh1);
                imagesDataBase.add(img);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

	}
	
	private static void showKNN(){
		// Loading comparative image
        ImagePlus image = new Opener().openImage(reference.getOriginalFileInfo().directory, 
                                                 reference.getOriginalFileInfo().fileName);

        int [] hist = Utils.getHistogram(image);
		MetricHist mh1 = new MetricHist(hist);

		// Reading text file
		loadImagesDataBase(mh1);

		// Sorting images
		Collections.sort(imagesDataBase);

		// Showing K nearest neighbors
		for(int i = 0;  i < k; i++){
			ImagePlus img = new Opener().openImage(imagesDataBase.get(i).imageName);
			img.show();
		}

	}
}