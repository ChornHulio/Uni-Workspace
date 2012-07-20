package de.tdreher.core;

public class Main {

	// settings
	static int sizeOfPop = 10;
	static int generations = 100;

	// settings for mutation
	static int mutationMin = 1;
	static int mutationMax = 100;
	static double learnOverall = 0.5;
	static double learnProp = 0.5;

	static Population pop = null;

	public static void main(String[] args) {
		Logger.clearLogFile();
		pop = new Population(sizeOfPop);
		for(int i = 0; i < generations; i++) {
			System.out.println("Generation: " + i);
			pop.adaptMutationRate(mutationMin, mutationMax, learnOverall, learnProp);
			pop.mutation();
			pop.evaluation(); // inclusive processing of speaker recognition
			pop.selection();
			Logger.log(i, pop);
		}
	}
}
