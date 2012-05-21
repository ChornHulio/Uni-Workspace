package de.tdreher.core;

import de.tdreher.algorithm.classification.NearestNeighbor;

public class Main {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		
		int numberOfSpeakers = 11;
		Speaker speaker[] = new Speaker[numberOfSpeakers];
		
		// read all training waves, create LPC values and create weights
		for(int i = 0; i < numberOfSpeakers; i++) {
			long time = System.currentTimeMillis();
			String str = "/media/Daten/Dokumente/Master/Projekt/Sprachdateien/";
			str = str.concat(Integer.toString(i+1));
			String str1 = str.concat("/1.wav");
			String str2 = str.concat("/2.wav");
			speaker[i] = new Speaker();
			speaker[i].load(str1);
			speaker[i].load(str2);
			
			long time1 = System.currentTimeMillis();
			System.out.println("speaker " + (i+1) + ": LPC created in " + (time1-time) + " ms");
			
			speaker[i].createCodebook();
			long time2 = System.currentTimeMillis();
			System.out.println("speaker " + (i+1) + ": codebook created in " + (time2-time1)/1000 + " s");
		}
		
		// TEST SPEAKERS
		Speaker test[] = new Speaker[numberOfSpeakers];
		
		// read all training waves, create LPC values and create weights
		for(int i = 0; i < numberOfSpeakers; i++) {
			long time = System.currentTimeMillis();
			String str = "/media/Daten/Dokumente/Master/Projekt/Sprachdateien/";
			str = str.concat(Integer.toString(i+1));
			String str1 = str.concat("/3.wav");
			test[i] = new Speaker();
			test[i].load(str1, 5000); // 5 random seconds
			
			long time1 = System.currentTimeMillis();
			System.out.println("test speaker " + (i+1) + ": LPC created in " + (time1-time) + " ms");
		}
		
		// TEST CLASSIFICATION
		int correctDetection = 0;
		NearestNeighbor nn = new NearestNeighbor(speaker);
		for(int i = 0; i < numberOfSpeakers; i++) {
			try {
				int[] classification = nn.calc(test[i]);
				int max = classification[i];
				for(int j = 0; j < numberOfSpeakers; j++) {
					if(max < classification[j]) {
						max = -1;
					}
				}
				if(max > 0) {
					correctDetection++;
				}
				for(int j = 0; j < classification.length; j++) {
					System.out.print(classification[j] + "\t");
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
