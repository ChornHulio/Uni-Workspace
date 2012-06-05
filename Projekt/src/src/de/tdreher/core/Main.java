package de.tdreher.core;

import java.text.DecimalFormat;

import de.tdreher.algorithm.classification.*;

public class Main {
	
	private static boolean mfcc = true;

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		
		int startNoOfSpeakers = 1;
		int numberOfSpeakers = 10;
		Speaker speaker[] = new Speaker[numberOfSpeakers];
		
		// TRAINING
		for(int i = startNoOfSpeakers; i < startNoOfSpeakers+numberOfSpeakers; i++) {
			long time = System.currentTimeMillis();
			String str = "/media/Daten/Dokumente/Master/Projekt/Sprachdateien/";
			str = str.concat(Integer.toString(i));
			String str1 = str.concat("/1.wav");
			String str2 = str.concat("/2.wav");
			speaker[i-startNoOfSpeakers] = new Speaker(true, mfcc); // training
			speaker[i-startNoOfSpeakers].load(str1);
			speaker[i-startNoOfSpeakers].load(str2);			
			speaker[i-startNoOfSpeakers].createCodebook();
			long time1 = System.currentTimeMillis();
			System.out.println("speaker " + i + " trained in " + (time1-time)/1000 + " s");
		}
		
		// TESTING
		Speaker test[] = new Speaker[numberOfSpeakers];
		
		// read all training waves, create LPC values and create weights
		for(int i = startNoOfSpeakers; i < startNoOfSpeakers+numberOfSpeakers; i++) {
			long time = System.currentTimeMillis();
			String str = "/media/Daten/Dokumente/Master/Projekt/Sprachdateien/";
			str = str.concat(Integer.toString(i));
			String str1 = str.concat("/3.wav");
			test[i-startNoOfSpeakers] = new Speaker(false,mfcc); // testing
			test[i-startNoOfSpeakers].load(str1,2000);
			
			long time1 = System.currentTimeMillis();
			System.out.println("test speaker " + i + ": Features created in " + (time1-time) + " ms");
		}
		
		// CLASSIFICATION I
		int correctDetection = 0;
		IVoting vote = new Voting(speaker);
		for(int i = startNoOfSpeakers; i < startNoOfSpeakers+numberOfSpeakers; i++) {
			try {
				double[] voting = vote.process(test[i-startNoOfSpeakers]);
				if(voting == null) {
					continue;
				}
				
				// correctness
				boolean correct = true;
				for(int j = 0; j < numberOfSpeakers; j++) {
					if(i-startNoOfSpeakers != j && voting[i-startNoOfSpeakers] <= voting[j]) {
						correct = false;
					}
				}
				if(correct) {
					correctDetection++;
				}
				
				// sum for relative frequency
				int sum = 0;
				for(int j = 0; j < numberOfSpeakers; j++) {
					sum += (int)voting[j];
				}
				
				// output
				DecimalFormat douF = new DecimalFormat("#.##");
				for(int j = 0; j < voting.length; j++) {
					int votes = (int)voting[j];
					System.out.print(votes + " (" + douF.format((double) votes/sum) + ")\t");
				}
				System.out.println();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("correct detections: " + correctDetection + " of " + numberOfSpeakers);
		
		// CLASSIFICATION II
		correctDetection = 0;
		vote = new VotingOverK(speaker);
		for(int i = startNoOfSpeakers; i < startNoOfSpeakers+numberOfSpeakers; i++) {
			try {
				double[] voting = vote.process(test[i-startNoOfSpeakers]);
				if(voting == null) {
					continue;
				}
				
				// correctness
				boolean correct = true;
				for(int j = 0; j < numberOfSpeakers; j++) {
					if(i-startNoOfSpeakers != j && voting[i-startNoOfSpeakers] <= voting[j]) {
						correct = false;
					}
				}
				if(correct) {
					correctDetection++;
				}
				
				// sum for relative frequency
				int sum = 0;
				for(int j = 0; j < numberOfSpeakers; j++) {
					sum += (int)voting[j];
				}
				
				// output
				DecimalFormat douF = new DecimalFormat("#.##");
				for(int j = 0; j < voting.length; j++) {
					int votes = (int)voting[j];
					System.out.print(votes + " (" + douF.format((double) votes/sum) + ")\t");
				}
				System.out.println();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("correct detections: " + correctDetection + " of " + numberOfSpeakers);
		
		long end = System.currentTimeMillis();
		System.out.println("duration: " + (end-start)/1000 + " s");
	}
}
