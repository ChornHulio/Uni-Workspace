package de.tdreher.core;

public class Main {

	public static void main(String[] args) {
		
		// read all training .wav, create LPC values and create weights
		for(int i = 1; i < 2; i++) {
			String str = "/media/Daten/Dokumente/Master/Projekt/Sprachdateien/";
			str = str.concat(Integer.toString(i));
			String str1 = str.concat("/1.wav");
			String str2 = str.concat("/2.wav");
			Speaker speaker = new Speaker();
			speaker.load(str1);
			speaker.load(str2);
			double[][] codebook = speaker.createCodebook();
			// DEBUG
			for(int k = 0; k < 10; k++) {
				for(int j = 0; j < 12; j++) {
					System.out.print(Double.toString(codebook[k][j]).subSequence(0, 3) + "\t");
				}
				System.out.println();
			}
		}
	}
}
