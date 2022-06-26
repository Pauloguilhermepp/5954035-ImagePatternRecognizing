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
    private static Vector<Image> imagesDataBase = new Vector <Image>(); // Data base images

    // Convert image to grayscale:
    public int setup(String arg, ImagePlus imp) {
        reference = imp;
        ImageConverter ic = new ImageConverter(imp);
        ic.convertToGray8();
        return DOES_ALL;
    }

    // Show dialog box to the user and save number of nearest neighbors
    public boolean dialog(){
        GenericDialog gd = new GenericDialog("k-nearest neighbor search", IJ.getInstance());
        gd.addNumericField("Number of nearest neighbors (K):", 5, 0);
        gd.showDialog();
        
        if (gd.wasCanceled())
            return false;
        
        k = (int) gd.getNextNumber();

        return true;
    }

    // Call the dialog and showKNN methods
    public void run(ImageProcessor img) {
        if(!dialog()) return;

        showKNN();

    }

    // Load file with images metric histograms
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

    // Check if number of images requested if coherent
    private static int checkNumberOfImgs(int num1 ,int num2){
        int numIt = Math.min(num1, num2);

        if(num1 < 0 || num1 > num2){
            Utils.write("Number of images out of range!\n");
        }

        return numIt;
    }

    // Show k nearest neighbors
	private static void showKNN(){
        String dataBaseImageName, imageDir, imageFileName;
		
        // Loading comparative image
        imageDir = reference.getOriginalFileInfo().directory;
        imageFileName = reference.getOriginalFileInfo().fileName;
        ImagePlus image = new Opener().openImage(imageDir, imageFileName);

        int [] hist = Utils.getHistogram(image);
		MetricHist mh1 = new MetricHist(hist);

		// Reading text file
		loadImagesDataBase(mh1);

		// Sorting images
		Collections.sort(imagesDataBase);

        String file = "Answer.txt";
        mh1.saveMetricHist(imageDir + imageFileName + ":" + mh1.toString(), file, 0);

		// Showing and saving K nearest neighbors
        int numIt = Math.min(k, imagesDataBase.size());
		for(int i = 0;  i < numIt; i++){
            dataBaseImageName = imagesDataBase.get(i).getImageName();
			ImagePlus img = new Opener().openImage(dataBaseImageName);
            imagesDataBase.get(i).getMh().saveMetricHist(dataBaseImageName + ":" + imagesDataBase.get(i).getMhStr(), file, 1);
            img.show();
		}

	}
}