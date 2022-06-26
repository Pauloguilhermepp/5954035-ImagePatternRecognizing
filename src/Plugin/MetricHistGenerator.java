import java.io.*;
import ij.*;
import ij.io.*;
import ij.process.*;
import ij.gui.*;
import ij.plugin.filter.*;

public class MetricHistGenerator implements PlugInFilter {
    private static ImagePlus reference;        // Image reference
    private static int k;                      // Number of nearest neighbors
    
    // Convert image to grayscale:
    public int setup(String arg, ImagePlus imp) {
        reference = imp;
        ImageConverter ic = new ImageConverter(imp);
        ic.convertToGray8();
        return DOES_ALL;
    }

    // Open dialog box and call evaluate function
    public void run(ImageProcessor img) {

        SaveDialog sd = new SaveDialog("Open the folder to be evaluated", "Any file (required)", "");
        if (sd.getFileName() == null) return;

        String dir = sd.getDirectory();
        evaluate(dir);
    }

    // Create the metric histogram of each image
    private void evaluate(String dir) {
        IJ.log("Evaluating images");

        if (!dir.endsWith(File.separator))
            dir += File.separator;
        
        String[] list = new File(dir).list();  /* Files list */
        
        if (list == null) return;
        
        for (int i = 0; i < list.length; i++) {

            IJ.showStatus(i + "/" + list.length + ": " + list[i]);
            IJ.showProgress((double)i / list.length);
            
            File f = new File(dir + list[i]);
            
            if (!f.isDirectory()) {
                
                ImagePlus image = new Opener().openImage(dir, list[i]); /* Open a image */
                MetricHist.evaluateImage(image, dir + list[i], i);

            }

        }
        
        IJ.showProgress(1.0);
        IJ.showStatus("");      
     }      
}