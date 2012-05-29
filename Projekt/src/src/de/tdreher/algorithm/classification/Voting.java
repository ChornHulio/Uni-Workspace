package de.tdreher.algorithm.classification;

import java.util.ArrayList;

import de.tdreher.core.Speaker;

public class Voting {
	
	Speaker[] trainedSpeaker = null;
	
	public Voting(Speaker[] trainedSpeaker) {
		this.trainedSpeaker = trainedSpeaker;		
	}
	
	public double[] calc(Speaker speakerToCheck) throws Exception {
		double[] results = new double[trainedSpeaker.length];
		for(int i = 0; i < results.length; i++) {
			results[i] = 0;
		}
		
		ArrayList<double[]> testData = speakerToCheck.getLPC();
		for(int i = 0; i < testData.size(); i++) {
			double distance = Double.MAX_VALUE;
			int speakerNo = 0;
			for(int s = 0; s < trainedSpeaker.length; s++) {
				double[][] codebook = trainedSpeaker[s].getCodebook();
				if(codebook == null) {
					return null;
				}
				for(int j = 0; j < codebook.length; j++) {
					double tmpDistance = distance(codebook[j],testData.get(i));
					if(distance > tmpDistance) {
						distance = tmpDistance;
						speakerNo = s;
					}
				}
			}
			results[speakerNo]++;
		}
		return results;
	}

	private double distance(double[] ds, double[] ds2) throws Exception {
		if(ds.length != ds2.length) {
			throw new Exception("vectors have different lengths");
		}
		double distance = 0.0;
		for(int i = 0; i < ds.length; i++) {
			distance += Math.pow(ds[i]-ds2[i], 2); // euclidean distance
		}
		return distance;
	}

}
