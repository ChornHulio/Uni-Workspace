package de.tdreher.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import de.tdreher.algorithms.NearestNeighbor;
import de.tdreher.common.ArgInterpreter;

public class Main {
	
	private static Settings settings = null;
	private static Features features = new Features();
	private static Features codebooks = new Features();

	public static void main(String[] args) throws Exception {
		// init settings
		settings = ArgInterpreter.interpret(args);
		if(settings == null) {
			return;
		}
		
		// read input files (feature vectors)
		for(int i = 0; i < settings.getFeatureFiles().size(); i++) {
			try {
				readOneFile(settings.getFeatureFiles().get(i), true);
			} catch (Exception e) {
				System.err.println(e.getMessage());
				return;
			}			
		}
		
		// read input files (codebook vectors)
		for(int i = 0; i < settings.getCodebookFiles().size(); i++) {
			try {
				readOneFile(settings.getCodebookFiles().get(i), false);
			} catch (Exception e) {
				System.err.println(e.getMessage());
				return;
			}			
		}
		
		// check if input files wasn't empty
		if(features.isEmpty() || codebooks.isEmpty()) {
			System.err.println("Input files are empty");
			return;
		}
		
		// clear output file if it is necassary
		clearOutputFile();
		
		// get nearest neighbor between n feature vectors and the codebooks
		NearestNeighbor nn = new NearestNeighbor(codebooks);
		int[] labels = nn.calc(features, settings.getNumber());

		// write in output file
		writeOutputFile(labels);
	}
	
	private static void readOneFile(File file, boolean featureFile) throws Exception {
		FileReader fr = null;
		try {
			fr = new FileReader(file);
			BufferedReader in = new BufferedReader(fr);
			String line = null;
			// read vectors
			while ((line = in.readLine()) != null) {
				line = line.trim();
				String[] strings = line.split(" ");
				// number of coefficents
				int i = 0;
				if(featureFile || !strings[0].contains(":")) {
					settings.setCoefficents(strings.length-1);
					i = 1;
				} else {
					settings.setCoefficents(strings.length);
				}
				// read features of the current vector
				double[] vector = new double[settings.getCoefficents()];
				for(; i < strings.length; i++) {
					String[] feature = strings[i].split(":");
					vector[i-1] = Double.parseDouble(feature[1]);
				}
				if(featureFile) {
					features.add(vector);
				} else {
					codebooks.add(vector,Integer.parseInt(strings[0]));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void clearOutputFile() {
		Writer fw = null;
		try {
			fw = new FileWriter(settings.getOutputFile(), settings.getAppendFlag());
			fw.write("");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void writeOutputFile(int[] labels) {
		Writer fw = null;
		try {
			fw = new FileWriter(settings.getOutputFile(), true);
			String str = "";
			for (int i = 0; i < labels.length; i++) {
				str += labels[i] + System.getProperty("line.separator");
			}
			fw.write(str);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}		
	}
}