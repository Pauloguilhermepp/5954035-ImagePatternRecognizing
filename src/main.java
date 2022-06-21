
import java.io.*;
import java.util.*;

import ij.*;
import ij.io.*;
import ij.process.*;
import ij.gui.*;
import ij.plugin.filter.*;

// Main class
public class main {

	static Vector<double []> strToVectorDoubles(String mhStr){
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

	// main driver method
	public static void main(String[] args) throws Exception
	{
		
		Image img = new Image();
		String [] array = new String[2];
		File file = new File(
			"/home/nfs/12542755/MetricHistograms.txt");

		BufferedReader br
			= new BufferedReader(new FileReader(file));

		// Lendo imagem de comparação:
		ImagePlus image = new Opener().openImage("/home/nfs/12542755/Documentos/ImageJ/img", "BARK1.bmp");
		image.show();
		
		/*
		String st;
		while ((st = br.readLine()) != null){
			// Lendo a linha:
			array = st.split(":");
			img.imageName = array[0];
			img.mhStr = array[1];

			// Salvando histograma métrico:
			MetricHist mh1 = new MetricHist(strToVectorDoubles(img.mhStr));
			
		}*/
	}
}
