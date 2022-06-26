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
                img.setImageName(array[0]);
                img.setMhStr(array[1]);

                // Saving metric histogram:
                img.strToVector();
                img.setDist(MetricHist.distance(img.getMh(), mh1));
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
			ImagePlus img = new Opener().openImage(imagesDataBase.get(i).getImageName());
			img.show();
		}

	}
}