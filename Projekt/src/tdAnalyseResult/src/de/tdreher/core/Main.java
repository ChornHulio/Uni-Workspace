package de.tdreher.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import de.tdreher.algorithms.Analysis;
import de.tdreher.common.ArgInterpreter;

public class Main {

	private static Settings settings = null;
	private static ArrayList<Integer> originLabels = new ArrayList<Integer>();
	private static ArrayList<Integer> predictedLabels = new ArrayList<Integer>();
	
	public static void main(String[] args) {
		// init settings
		settings = ArgInterpreter.interpret(args);
		if(settings == null) {
			return;
		}
		
		// read input files (feature vectors)
		for(int i = 0; i < settings.getOriginFiles().size(); i++) {
			try {
				readOneFile(settings.getOriginFiles().get(i), true);
			} catch (Exception e) {
				System.err.println(e.getMessage());
				return;
			}			
		}
		
		// read input files (codebook vectors)
		for(int i = 0; i < settings.getPredictionFiles().size(); i++) {
			try {
				readOneFile(settings.getPredictionFiles().get(i), false);
			} catch (Exception e) {
				System.err.println(e.getMessage());
				return;
			}			
		}
		
		// check if input files are correct
		if(originLabels.isEmpty() || predictedLabels.isEmpty()) {
			System.err.println("Input files are empty");
			return;
		}
		if(originLabels.size() != predictedLabels.size()) {
			System.out.println(originLabels.size() + " | " + predictedLabels.size());
			System.err.println("The original files contains not equal labels than the prediction files");
			return;
		}
		
		// clear output file if it is necassary
		clearOutputFile();
		
		// analyse it
		Analysis analysis = new Analysis();
		String res = analysis.process(originLabels, predictedLabels);

		// write in output file
		writeOutputFile(res);
	}
	
	private static void readOneFile(File file, boolean originFile) throws Exception {
		FileReader fr = null;
		try {
			fr = new FileReader(file);
			BufferedReader in = new BufferedReader(fr);
			String line = null;
			// read vectors
			while ((line = in.readLine()) != null) {
				line = line.trim();
				String[] strings = line.split(" ");
				if(originFile) {
					originLabels.add(Integer.parseInt(strings[0]));
				} else {
					predictedLabels.add(Integer.parseInt(strings[0]));
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

	private static void writeOutputFile(String str) {
		Writer fw = null;
		try {
			fw = new FileWriter(settings.getOutputFile(), true);
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
