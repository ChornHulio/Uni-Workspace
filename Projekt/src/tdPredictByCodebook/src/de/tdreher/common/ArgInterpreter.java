package de.tdreher.common;

import java.io.File;

import de.tdreher.core.Settings;

public class ArgInterpreter {
	
	private static Settings settings = new Settings();
	private static boolean featureFileSet = false;
	private static boolean codebookFileSet = false;
	private static boolean outputFileSet = false;

	public static Settings interpret(String[] args) {
				
		try { 
			// try to interpret arguments
			for(int i = 0; i < args.length; i++) {
				if(args[i].startsWith("-")) {
					if(args[i].equalsIgnoreCase("--help") || args[i].equalsIgnoreCase("-h")) {
						return printHelp();
					} else if((args[i].equalsIgnoreCase("--featureFile") || args[i].equalsIgnoreCase("-ff")) && args.length > (i+1)) {						
						addFeatureFile(args[++i]);						
					} else if((args[i].equalsIgnoreCase("--codebookFile") || args[i].equalsIgnoreCase("-cf")) && args.length > (i+1)) {						
						addCodebookFile(args[++i]);						
					} else if((args[i].equalsIgnoreCase("--outputFile") || args[i].equalsIgnoreCase("-o")) && args.length > (i+1)) {						
						setOutputFile(args[++i]);						
					} else if((args[i].equalsIgnoreCase("--number") || args[i].equalsIgnoreCase("-n")) && args.length > (i+1)) {						
						setNumber(args[++i]);						
					} else if((args[i].equalsIgnoreCase("--append") || args[i].equalsIgnoreCase("-a"))) {						
						settings.setAppendFlag();						
					} else {
						throw new Exception("Wrong argument: " + args[i]);
					}
				}
			}
			
			// check if all mandatory arguements are set
			if(!featureFileSet || !codebookFileSet || !outputFileSet)
				throw new Exception("At least one mandatory argument misses");
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return printHelp();
		}
		
		return settings;
	}

	private static Settings printHelp() {
		System.out.println();
		System.out.println("PredictionByCodebook - predict which label is meant for the given feature vectors");
		System.out.println("Author: Tobias Dreher");
		System.out.println("Date: July 2012");
		System.out.println();
		System.out.println("USAGE:");
		System.out.println("\tjava -jar PredictionByCodebook [options]");
		System.out.println("\tThe options -ff, -cf and -o are mandatory!");
		System.out.println();
		System.out.println("OPTIONS:");
		System.out.println("\t-ff\t--featurefile\tfile with feature vectors");
		System.out.println("\t\t\t\t(if you use more than one input file, you have to use this option again)");
		System.out.println("\t-cf\t--codebookfile\tfile with codebook");
		System.out.println("\t\t\t\t(if you use more than one input file, you have to use this option again)");
		System.out.println("\t-o\t--outputfile\toutput file");
		System.out.println("\t-a\t--append\tappend the output at the end of the output file");
		System.out.println("\t-n\t--number\tnumber of feature vectors for one prediction, default: 1, range: 1..n");
		System.out.println();
		System.out.println("EXAMPLES:");
		System.out.println("\t1.) standard prediction (n=1) from one input file each");
		System.out.println("\t\tjava -jar PredictionByCodebook -if features.txt -ic codebook.txt -o result.txt");
		System.out.println("\t2.) predictions over 10 feature vectors with two input files each and \n\t\t" +
				"one output file (if it exists it would be append)");
		System.out.println("\t\tjava -jar PredictionByCodebook -if features_01.txt -if features_02.txt -ic codebook_01.txt \n\t\t" +
				"-i codebook_02.txt -o result.txt -a");
		return null;
	}
	
	private static void addFeatureFile(String str) throws Exception {
		File f = new File(str);
		if(!f.exists())
			throw new Exception("File "+str+" not found");
		settings.addFeatureFile(f);
		featureFileSet = true;
	}
	
	private static void addCodebookFile(String str) throws Exception {
		File f = new File(str);
		if(!f.exists())
			throw new Exception("File "+str+" not found");
		settings.addCodebookFile(f);
		codebookFileSet = true;
	}

	private static void setOutputFile(String str) throws Exception {
		settings.setOutputFile(new File(str));
		outputFileSet = true;
	}

	private static void setNumber(String str) throws Exception {
		int s;
		try {
			s = Integer.parseInt(str);
		} catch (Exception e) {
			throw new Exception("The given number is not a number");
		}
		if(s < 1)
			throw new Exception("The given number must be greater than 0");
		settings.setNumber(s);
	}
}
