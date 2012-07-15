package de.tdreher.common;

import java.io.File;

import de.tdreher.algorithms.windows.HammingWindow;
import de.tdreher.algorithms.windows.HannWindow;
import de.tdreher.core.Settings;

public class ArgInterpreter {
	
	private static Settings settings = new Settings();
	private static boolean inputFileSet = false;
	private static boolean outputFileSet = false;
	private static boolean labelSet = false;
	private static boolean mfcc = true;

	public static Settings interpret(String[] args) {
				
		try { 
			// go through given arguments
			for(int i = 0; i < args.length; i++) {
				if(args[i].startsWith("-")) {
					if(args[i].equalsIgnoreCase("--help") || args[i].equalsIgnoreCase("-h")) {
						return printHelp();
					} else if((args[i].equalsIgnoreCase("--inputFile") || args[i].equalsIgnoreCase("-i")) && args.length > (i+1)) {						
						addInputFile(args[++i]);						
					} else if((args[i].equalsIgnoreCase("--outputFile") || args[i].equalsIgnoreCase("-o")) && args.length > (i+1)) {						
						setOutputFile(args[++i]);						
					} else if((args[i].equalsIgnoreCase("--label") || args[i].equalsIgnoreCase("-l")) && args.length > (i+1)) {						
						setLabel(args[++i]);						
					} else if((args[i].equalsIgnoreCase("--sampleWidth") || args[i].equalsIgnoreCase("-sw")) && args.length > (i+1)) {						
						setSampleWidth(args[++i]);						
					} else if((args[i].equalsIgnoreCase("--slidingRate") || args[i].equalsIgnoreCase("-sr")) && args.length > (i+1)) {						
						setSlidingRate(args[++i]);						
					} else if((args[i].equalsIgnoreCase("--coefficents") || args[i].equalsIgnoreCase("-c")) && args.length > (i+1)) {						
						setCoefficents(args[++i]);						
					} else if((args[i].equalsIgnoreCase("--energyLevel") || args[i].equalsIgnoreCase("-e")) && args.length > (i+1)) {						
						setEnergyLevel(args[++i]);						
					} else if((args[i].equalsIgnoreCase("--window") || args[i].equalsIgnoreCase("-w")) && args.length > (i+1)) {						
						setWindow(args[++i]);						
					} else if(args[i].equalsIgnoreCase("--mfcc")) {						
						mfcc = true;						
					} else if(args[i].equalsIgnoreCase("--lpc")) {						
						mfcc = false;						
					} else if((args[i].equalsIgnoreCase("--append") || args[i].equalsIgnoreCase("-a"))) {						
						settings.setAppendFlag();						
					} else {
						throw new Exception("Wrong argument: " + args[i]);
					}
				}
			}
			
			// check if all mandatory arguements are set
			if(!inputFileSet || !outputFileSet || !labelSet)
				throw new Exception("At least one mandatory argument misses");
			
			// set feature extraction method at last
			settings.setFeatureExtraction(mfcc);
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return printHelp();
		}
		
		return settings;
	}

	private static Settings printHelp() {
		System.out.println();
		System.out.println("tdFeatureExtraction - extract feature vectors from one or more given .wav-files");
		System.out.println("Author: Tobias Dreher");
		System.out.println("Date: July 2012");
		System.out.println();
		System.out.println("USAGE:");
		System.out.println("\tjava -jar tdFeatureExtraction [options]");
		System.out.println("\tThe options -i, -o and -l are mandatory!");
		System.out.println();
		System.out.println("OPTIONS:");
		System.out.println("\t-i\t--inputfile\t.wav-file for feature extraction");
		System.out.println("\t\t\t\t(if you use more than one input file, you have to use this option again)");
		System.out.println("\t-l\t--label\t\tlabel for the given data (1..n)");
		System.out.println("\t-o\t--outputfile\toutput file");
		System.out.println("\t-a\t--append\tappend the output at the end of the output file");
		System.out.println("\t--mfcc\t\t\tuse the MFCC method for feature extraction (default)");
		System.out.println("\t--lpc\t\t\tuse the LPC method for feature extraction");
		System.out.println("\t-sw\t--samplewidth\tsample width (or number of sampling points) for one feature vector, default: 512");
		System.out.println("\t\t\t\t(for MFCC this number must be 2^n and at least 32)");
		System.out.println("\t-sr\t--slidingrate\toverlap of the windows is set with samplewidth/slidingrate, default: 2 (-> 50%)");
		System.out.println("\t\t\t\t(this option is noneffective if you use MFCC)");
		System.out.println("\t-c\t--coefficents\tsize of feature vector, default: 20 ");
		System.out.println("\t-w\t--window\twindow function, default: 'hamming', alternative: 'hann' ");
		System.out.println("\t-e\t--energylevel\tenergy level (in percentage) to use a sample, default: 90 ");
		System.out.println();
		System.out.println("EXAMPLES:");
		System.out.println("\t1.) extract input.wav with label '1' and the standard values (MFCC, etc.)");
		System.out.println("\t\tjava -jar tdFeatureExtraction -i input.wav -l 1 -o features.txt");
		System.out.println("\t2.) extract two input files with label '5' and the standard values (MFCC, etc.)");
		System.out.println("\t\tjava -jar tdFeatureExtraction -i input_01.wav -i input_02.wav -l 5 -o features.txt");
		System.out.println("\t3.) extract input.wav with label '42', the LPC method and a vector size of 12 and \n" +
				"\t\tappend the output to the given file (if it exists or not)");
		System.out.println("\t\tjava -jar tdFeatureExtraction -i input.wav -l 42 -o features.txt --lpc -c 12 -a");
		return null;
	}
	
	private static void addInputFile(String str) throws Exception {
		File f = new File(str);
		if(!f.exists())
			throw new Exception("File "+str+" not found");
		settings.addInputFile(f);
		inputFileSet = true;
	}

	private static void setOutputFile(String str) throws Exception {
		settings.setOutputFile(new File(str));
		outputFileSet = true;
	}

	private static void setLabel(String str) throws Exception {
		int l;
		try {
			l = Integer.parseInt(str);
		} catch (Exception e) {
			throw new Exception("The given label is not a number");
		}
		if(l < 1)
			throw new Exception("The given label must be greater than 0");
		settings.setLabel(l);
		labelSet = true;
	}

	private static void setSampleWidth(String str) throws Exception {
		int sw;
		try {
			sw = Integer.parseInt(str);
		} catch (Exception e) {
			throw new Exception("The given sampleWidth is not a number");
		}
		if(sw < 32)
			throw new Exception("The given sampleWidth must be greater or equal than 32");
		settings.setSampleWidth(sw);
	}
	
	private static void setSlidingRate(String str) throws Exception {
		int sr;
		try {
			sr = Integer.parseInt(str);
		} catch (Exception e) {
			throw new Exception("The given slidingRate is not a number");
		}
		if(sr < 1)
			throw new Exception("The given slidingRate must be greater than 0");
		settings.setSlidingRate(sr);
	}

	private static void setCoefficents(String str) throws Exception {
		int c;
		try {
			c = Integer.parseInt(str);
		} catch (Exception e) {
			throw new Exception("The given number of coefficents is not a number");
		}
		if(c < 1)
			throw new Exception("The given number of coefficents must be greater than 0");
		settings.setCoefficents(c);
	}

	private static void setEnergyLevel(String str) throws Exception {
		int el;
		try {
			el = Integer.parseInt(str);
		} catch (Exception e) {
			throw new Exception("The given energyLevel is not a number");
		}
		if(el < 0)
			throw new Exception("The given energyLevel must be greater or equal than 0");
		if(el > 99)
			throw new Exception("The given energyLevel must be less than 99");
		settings.setEnergyLevel(el);
	}

	private static void setWindow(String str) throws Exception {
		if(str.equalsIgnoreCase("hamming")) {
			settings.setWindow(new HammingWindow());
		} else if(str.equalsIgnoreCase("hann")) {
			settings.setWindow(new HannWindow());
		} else {
			throw new Exception("The given window function is not correct");
		}
		
	}
}
