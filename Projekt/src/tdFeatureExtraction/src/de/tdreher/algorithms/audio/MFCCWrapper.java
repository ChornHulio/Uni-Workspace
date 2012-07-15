package de.tdreher.algorithms.audio;

import java.io.IOException;
import java.util.ArrayList;

import comirva.audio.util.MFCC;

public class MFCCWrapper implements IFeatureExtraction {

	// default values
	private int sampleWidth = 512; // 640 = 40ms, 480 = 30ms
	private int coefficents = 20; // size of the feature vector

	public MFCCWrapper(int sampleWidth, int coefficents) {
		this.sampleWidth = sampleWidth;
		this.coefficents = coefficents;
	}

	@Override
	public ArrayList<double[]> process(double[] input) {
		int length = input.length - input.length % (sampleWidth / 2);
		double[] data = new double[length];
		for (int i = 0; i < length; i++) {
			data[i] = input[i];
		}
		MFCC mfcc = new MFCC(16000, sampleWidth, coefficents+1, false);
		try {
			double[][] res = mfcc.process(data);
			return (convertToArrayList(res));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private ArrayList<double[]> convertToArrayList(double[][] input) {
		ArrayList<double[]> output = new ArrayList<double[]>();
		for (int i = 0; i < input.length; i++) {
			output.add(input[i]);
		}
		return output;
	}

}
