package de.tdreher.algorithms;

import java.util.Arrays;

import de.tdreher.common.SortArray;

public class EnergyCut {

	private static double calc(double[] frame) {
		double energy = 0;
		for(int i = 0; i < frame.length; i++) {
			energy += Math.abs(frame[i]);
		}
		energy /= frame.length;
		return energy;
	}

	public static double[] cut(double[] input, int energyLevel, int sampleWidth) {
		// sort input
		int frames = input.length/sampleWidth - 1;
		
		if(frames == 0) {
			System.err.println("Frames: " + frames);
			System.err.println("input.length: " + input.length);
			System.err.println("sampleWidth: " + sampleWidth);
		}
		
		SortArray[] sa = new SortArray[frames];
		for(int i = 0; i < frames; i++) {
			double[] tmp = new double[sampleWidth];
			for(int j = 0; j < sampleWidth; j++) {
				tmp[j] = input[i*sampleWidth+j];
			}
			sa[i] = new SortArray(calc(tmp),i*sampleWidth);
		}
		Arrays.sort(sa);
		
		// calc energy level
		int borderIndex = sa.length - (sa.length * (100 - energyLevel) / 100 );
		double borderValue = sa[borderIndex].value;
		
		// take all frames above this energy level
		int index = 0;
		double[] output = new double[(sa.length - borderIndex) *sampleWidth];
		for(int i = 0; i < frames; i++) {
			double[] tmp = new double[sampleWidth];
			for(int j = 0; j < sampleWidth; j++) {
				tmp[j] = input[i*sampleWidth+j];
			}
			if(calc(tmp) >= borderValue) {
				for(int j = 0; j < sampleWidth; j++) {
					output[index++] = tmp[j];
				}
			}
		}
		return output;
	}

}