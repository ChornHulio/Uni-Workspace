package de.tdreher.core;

public class Main {

	// settings
	static int sizeOfPop = 12;

	// settings for mutation
	static int mutationMin = 1;
	static int mutationMax = 100;
	static double learnOverall = 0.5;
	static double learnProp = 0.5;

	static Population pop = null;

	public static void main(String[] args) {
		
		String foldername = "ea";
		if(args.length == 1) {
			foldername = args[0];
		}
		
		Logger.clearLogFile();
		pop = new Population(sizeOfPop, foldername);
		int generation = 0;
		while(true) {
			generation++;
			System.out.println("Generation: " + generation);
			pop.adaptMutationRate(mutationMin, mutationMax, learnOverall, learnProp);
			pop.mutation();
			pop.evaluation(); // inclusive processing of speaker recognition
			pop.selection();
			Logger.log(generation, pop);
		}
	}
}
