package de.tdreher.core;

public class Main {
	
	// settings
	static int sizeOfPop = 10;
	static int generations = 10;
	
	// settings for mutation
	static int mutationMin = 1;
	static int mutationMax = 100;
	static double learnOverall = 0.5;
	static double learnProp = 0.5;
	
	static Population pop = null;
	
	public static void main(String[] args) {
		Logger.clearLogFile();
		pop = new Population(sizeOfPop);
		while(generations-- > 0) {
			pop.adaptMutationRate(mutationMin, mutationMax, learnOverall, learnProp);
			pop.mutation();
			pop.evaluation(); // inclusive processing of speaker recognition
			pop.selection();
			Logger.log(generations, pop);
		}
	}

}
