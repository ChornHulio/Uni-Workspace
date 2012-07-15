package de.tdreher.common;

import java.io.File;

import de.tdreher.core.Settings;

public class ArgInterpreter {
	
	private static Settings settings = new Settings();
	private static boolean inputFileSet = false;
	private static boolean outputFileSet = false;

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
					} else if((args[i].equalsIgnoreCase("--size") || args[i].equalsIgnoreCase("-s")) && args.length > (i+1)) {						
						setSize(args[++i]);						
					} else if((args[i].equalsIgnoreCase("--iterations") || args[i].equalsIgnoreCase("-it")) && args.length > (i+1)) {						
						setIterations(args[++i]);						
					} else if((args[i].equalsIgnoreCase("--append") || args[i].equalsIgnoreCase("-a"))) {						
						settings.setAppendFlag();						
					} else {
						throw new Exception("Wrong argument: " + args[i]);
					}
				}
			}
			
			// check if all mandatory arguements are set
			if(!inputFileSet || !outputFileSet)
				throw new Exception("At least one mandatory argument misses");
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return printHelp();
		}
		
		return settings;
	}

	private static Settings printHelp() {
		System.out.println();
		System.out.println("tdCreateCodebook - reduce feature vectors to a smaller codebook");
		System.out.println("Author: Tobias Dreher");
		System.out.println("Date: July 2012");
		System.out.println();
		System.out.println("USAGE:");
		System.out.println("\tjava -jar tdCreateCodebook [options]");
		System.out.println("\tThe options -i and -o are mandatory!");
		System.out.println();
		System.out.println("OPTIONS:");
		System.out.println("\t-i\t--inputfile\tfile with feature vectors");
		System.out.println("\t\t\t\t(if you use more than one input file, you have to use this option again)");
		System.out.println("\t-o\t--outputfile\toutput file");
		System.out.println("\t-a\t--append\tappend the output at the end of the output file");
		System.out.println("\t-s\t--size\t\tsize of codebook, default: 100, range: 1..n");
		System.out.println("\t-it\t--iterations\tnumber of algorithm iterations, default: 15, range: 1..n");
		System.out.println();
		System.out.println("EXAMPLES:");
		System.out.println("\t1.) create standard codebook (size=100, iterations=15) from one input file");
		System.out.println("\t\tjava -jar tdCreateCodebook -i features.txt -o codebook.txt");
		System.out.println("\t2.) create codebook with size=50 and iterations=30 with two input files and \n\t\t" +
				"one output file (if it exists it would be append)");
		System.out.println("\t\tjava -jar tdCreateCodebook -i features_01.txt -i features_02.txt -o codebook.txt -a -s 50 -i 30");
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

	private static void setSize(String str) throws Exception {
		int s;
		try {
			s = Integer.parseInt(str);
		} catch (Exception e) {
			throw new Exception("The given size is not a number");
		}
		if(s < 1)
			throw new Exception("The given size must be greater than 0");
		settings.setSize(s);
	}
	
	private static void setIterations(String str) throws Exception {
		int i;
		try {
			i = Integer.parseInt(str);
		} catch (Exception e) {
			throw new Exception("The given argument (iterations) is not a number");
		}
		if(i < 1)
			throw new Exception("The number of iterations must be greater than 0");
		settings.setIterations(i);
	}
}
