package de.tdreher.common;

import java.io.File;

import de.tdreher.core.Settings;

public class ArgInterpreter {
	
	private static Settings settings = new Settings();
	private static boolean originFileSet = false;
	private static boolean predictionFileSet = false;
	private static boolean outputFileSet = false;

	public static Settings interpret(String[] args) {
				
		try { 
			// try to interpret arguments
			for(int i = 0; i < args.length; i++) {
				if(args[i].startsWith("-")) {
					if(args[i].equalsIgnoreCase("--help") || args[i].equalsIgnoreCase("-h")) {
						return printHelp();
					} else if((args[i].equalsIgnoreCase("--originalFile") || args[i].equalsIgnoreCase("-i")) && args.length > (i+1)) {						
						addOriginFile(args[++i]);						
					} else if((args[i].equalsIgnoreCase("--predictionFile") || args[i].equalsIgnoreCase("-p")) && args.length > (i+1)) {						
						addPredictionFile(args[++i]);						
					} else if((args[i].equalsIgnoreCase("--outputFile") || args[i].equalsIgnoreCase("-o")) && args.length > (i+1)) {						
						setOutputFile(args[++i]);						
					} else if((args[i].equalsIgnoreCase("--accuracy") || args[i].equalsIgnoreCase("-a"))) {						
						setAccuracyFile(args[++i]);						
					} else {
						throw new Exception("Wrong argument: " + args[i]);
					}
				}
			}
			
			// check if all mandatory arguements are set
			if(!originFileSet || !predictionFileSet || !outputFileSet)
				throw new Exception("At least one mandatory argument misses");
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return printHelp();
		}
		
		return settings;
	}

	private static Settings printHelp() {
		System.out.println();
		System.out.println("Analyse - analyse the results of the prediction");
		System.out.println("Author: Tobias Dreher");
		System.out.println("Date: July 2012");
		System.out.println();
		System.out.println("USAGE:");
		System.out.println("\tjava -jar Analyse [options]");
		System.out.println("\tThe options -i, -p and -o are mandatory!");
		System.out.println();
		System.out.println("OPTIONS:");
		System.out.println("\t-i\t--originalFile\tfile with the correct labels");
		System.out.println("\t\t\t\t(if you use more than one input file, you have to use this option again)");
		System.out.println("\t-p\t--predictionFile\toutput file of the prediciton");
		System.out.println("\t\t\t\t(if you use more than one input file, you have to use this option again)");
		System.out.println("\t-o\t--outputfile\toutput file");
		System.out.println("\t-a\t--accuracy\twrite the accuracy in an extra output file");
		System.out.println();
		System.out.println("EXAMPLES:");
		System.out.println("\tstandard analysis from one input file each");
		System.out.println("\t\tjava -jar Analyse -i features.txt -p result.txt -o analysis.txt");
		return null;
	}
	
	private static void addOriginFile(String str) throws Exception {
		File f = new File(str);
		if(!f.exists())
			throw new Exception("File "+str+" not found");
		settings.addOriginFile(f);
		originFileSet = true;
	}
	
	private static void addPredictionFile(String str) throws Exception {
		File f = new File(str);
		if(!f.exists())
			throw new Exception("File "+str+" not found");
		settings.addPredictionFile(f);
		predictionFileSet = true;
	}

	private static void setOutputFile(String str) throws Exception {
		settings.setOutputFile(new File(str));
		outputFileSet = true;
	}
	
	private static void setAccuracyFile(String str) {
		settings.setAccuracyFile(new File(str));
	}
}
