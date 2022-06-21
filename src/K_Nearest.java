/*Exemplo plugin para k-nearest
   Prof. Joaquim Felipe */

import java.io.*;
import ij.*;
import ij.io.*;
import ij.process.*;
import ij.gui.*;
import ij.plugin.filter.*;

public class K_Nearest implements PlugInFilter {
    ImagePlus reference;        // Reference image
    int k;                      // Number of nearest neighbors
    int level;                  // Wavelet decoposition level

    public int setup(String arg, ImagePlus imp) {
        reference = imp;
        ImageConverter ic = new ImageConverter(imp);
        ic.convertToGray8();
        return DOES_ALL;
    }

    public void run(ImageProcessor img) {

        GenericDialog gd = new GenericDialog("k-nearest neighbor search", IJ.getInstance());
        gd.addNumericField("Number of nearest neighbors (K):", 1, 0);
        gd.addNumericField("Wavelet decomposition level:", 1, 0);
        gd.showDialog();
        if (gd.wasCanceled())
            return;
        k = (int) gd.getNextNumber();
        level = (int) gd.getNextNumber();

        SaveDialog sd = new SaveDialog("Open search folder...", "any file (required)", "");
        if (sd.getFileName()==null) return;
        String dir = sd.getDirectory();
        search(dir);
    }

    public static ImagePlus rbgToGrayScale(ImagePlus img){
        ImageConverter ic = new ImageConverter(img);
        ic.convertToGray8();
        img.updateAndDraw();

        return img;
    }

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
        
        for(int i = 0; i < 256; i++){
            histString += Integer.toString(hist[i]) + ", ";
        }

        histString += "]";

        write(histString);

        return hist;
    } 

    public void search(String dir) {
        IJ.log("");
        IJ.log("Searching images");
        if (!dir.endsWith(File.separator))
            dir += File.separator;
        String[] list = new File(dir).list();  /* lista de arquivos */
        if (list==null) return;
        for (int i=0; i<list.length; i++) {
            IJ.showStatus(i+"/"+list.length+": "+list[i]);   /* mostra na interface */
            IJ.showProgress((double)i / list.length);  /* barra de progresso */
            File f = new File(dir+list[i]);
            if (!f.isDirectory()) {
                ImagePlus image = new Opener().openImage(dir, list[i]); /* abre imagem image */
                if (image != null) {
                    image.show();
                    
                    image = rbgToGrayScale(image);
                    MetricHist mh = new MetricHist(getHistogram(image));
                    mh.saveMetricHist();

                }
            }
        }

        
        IJ.showProgress(1.0);
        IJ.showStatus("");      
     }      
}