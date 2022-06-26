import java.io.*;
import ij.*;
import ij.io.*;
import ij.process.*;
import ij.gui.*;
import ij.plugin.filter.*;

public class MetricHistGenerator implements PlugInFilter {
    private static ImagePlus reference;        // Image reference
    private static int k;                      // Number of nearest neighbors

    public int setup(String arg, ImagePlus imp) {
        reference = imp;
        ImageConverter ic = new ImageConverter(imp);
        ic.convertToGray8();
        return DOES_ALL;
    }

    public void run(ImageProcessor img) {

        SaveDialog sd = new SaveDialog("Open the folder to be evaluated", "Any file (required)", "");
        if (sd.getFileName() == null) return;

        String dir = sd.getDirectory();
        search(dir);
    }

    private static ImagePlus rbgToGrayScale(ImagePlus img){
        ImageConverter ic = new ImageConverter(img);
        ic.convertToGray8();
        img.updateAndDraw();

        return img;
    } 

    private void search(String dir) {
        IJ.log("Evaluating images");

        if (!dir.endsWith(File.separator))
            dir += File.separator;
        
        String[] list = new File(dir).list();  /* Files list */
        
        if (list==null) return;
        
        for (int i = 0; i < list.length; i++) {

            IJ.showStatus(i + "/" + list.length + ": " + list[i]);
            IJ.showProgress((double)i / list.length);
            
            File f = new File(dir + list[i]);
            
            if (!f.isDirectory()) {
                
                ImagePlus image = new Opener().openImage(dir, list[i]); /* Open a image */
                
                if (image != null) {
                    
                    image = rbgToGrayScale(image); // Convert image to grayscale
                    MetricHist mh = new MetricHist(Utils.getHistogram(image)); // Calculate the image metric histogram
                    mh.saveMetricHist(dir + list[i], i);// Save image metric histogram

                }

            }

        }
        
        IJ.showProgress(1.0);
        IJ.showStatus("");      
     }      
}