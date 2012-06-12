package de.tdreher.algorithm.classification.svm;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class SVMWrapper {

	ArrayList<String> output = new ArrayList<String>();

	public void addSVMData(ArrayList<double[]> data, int label) {
		for (int i = 0; i < data.size(); i++) {
			String str = "" + label + " ";
			for (int j = 0; j < data.get(i).length; j++) {
				str += (j + 1) + ":" + data.get(i)[j] + " ";
			}
			str += System.getProperty("line.separator"); // e.g. "\n"
			output.add(str);
		}
	}

	public void saveSVMData(String filename) {
		Writer fw = null;
		try {
			fw = new FileWriter(filename);
			for(int i = 0; i < output.size(); i++) {
				fw.write(output.get(i));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null)
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

}
