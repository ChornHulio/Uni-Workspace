package de.tdreher.algorithm.classification.voting;

import java.util.ArrayList;
import java.util.Arrays;

import de.tdreher.core.SortArray;
import de.tdreher.core.Speaker;

public class VotingWithRanking implements IVoting {
	
	Speaker[] trainedSpeaker = null;
	
	public VotingWithRanking(Speaker[] trainedSpeaker) {
		this.trainedSpeaker = trainedSpeaker;		
	}
	
	public double[] process(Speaker speakerToCheck) throws Exception {
		double[] results = new double[trainedSpeaker.length];
		for(int i = 0; i < results.length; i++) {
			results[i] = 0;
		}
		
		ArrayList<double[]> testData = speakerToCheck.getFeatures();
		for(int i = 0; i < testData.size(); i++) {
			SortArray[] sortArray = new SortArray[trainedSpeaker.length];
			for(int s = 0; s < trainedSpeaker.length; s++) {
				double[][] codebook = trainedSpeaker[s].getCodebook();
				if(codebook == null) {
					return null;
				}
				double distance = Double.MAX_VALUE;
				for(int j = 0; j < codebook.length; j++) {
					double tmpDistance = distance(codebook[j],testData.get(i));
					if(distance > tmpDistance) {
						distance = tmpDistance;
					}
				}
				sortArray[s] = new SortArray(distance, s);
			}
			// sort distances
			Arrays.sort(sortArray);
			// vote
			for(int s = 0; s < trainedSpeaker.length && s < 3; s++) {
				double dist = Math.abs(sortArray[s].distance);
				if(dist != 0) {
					results[sortArray[s].index] += 1/(1-Math.pow(Math.E,-dist));
				} else {
					results[sortArray[s].index] += 100;
				}
			}
			
			
			
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
