package de.tdreher.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import de.tdreher.algorithms.*;
import de.tdreher.common.ArgInterpreter;

public class Main {
	
	private static Settings settings = null;
	private static Features features = new Features();
		
	public static void main(String[] args) throws Exception {
		// init settings
		settings = ArgInterpreter.interpret(args);
		if(settings == null) {
			return;
		}
		
		// read input files
		for(int i = 0; i < settings.getInputFiles().size(); i++) {
			try {
				readOneFile(settings.getInputFiles().get(i));
			} catch (Exception e) {
				System.err.println(e.getMessage());
				return;
			}			
		}
		
		// check if features are empty
		if(features.isEmpty()) {
			System.err.println("Input files are empty");
			return;
		}
		
		// clear output file if it is necassary
		clearOutputFile();
		
		// create codebook for every label
		for(int i = 0; i < features.getCountOfLabels(); i++) {
			NeuralGas ng = new NeuralGas(settings.getSize(),settings.getIterations(),settings.getCoefficents());
			int label = features.getLabel(i);
			double[][] codebook = ng.calc(features.getData(label));
			
			// write in output file
			writeOutputFile(label,codebook);
		}
	}
	
	private static void readOneFile(File file) throws Exception {
		FileReader fr = null;
		try {
			fr = new FileReader(file);
			BufferedReader in = new BufferedReader(fr);
			String line = null;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				String[] strings = line.split(" ");
				settings.setCoefficents(strings.length-1);
				double[] vector = new double[settings.getCoefficents()];
				for(int i = 1; i < strings.length; i++) {
					String[] feature = strings[i].split(":");
					vector[i-1] = Double.parseDouble(feature[1]);
				}
				features.add(vector,Integer.parseInt(strings[0]));
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

	private static void writeOutputFile(int label, double[][] codebook) {
		Writer fw = null;
		try {
			fw = new FileWriter(settings.getOutputFile(), true);
			for (int i = 0; i < codebook.length; i++) {
				String str = "" + label + " ";
				for (int j = 0; j < codebook[i].length; j++) {
					str += (j + 1) + ":" + codebook[i][j] + " ";
				}
				str += System.getProperty("line.separator"); // e.g. "\n"
				fw.write(str);
			}
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
