package de.tdreher.algorithm.classification.voting;

import java.util.ArrayList;

import de.tdreher.core.Speaker;

public class VotingOverK implements IVoting {
	
	private Speaker[] trainedSpeaker = null;
	
	public VotingOverK(Speaker[] trainedSpeaker) {
		this.trainedSpeaker = trainedSpeaker;		
	}
	
	public double[] process(Speaker speakerToCheck) throws Exception {
		// init results
		double[] results = new double[trainedSpeaker.length];		
		for(int i = 0; i < results.length; i++) {
			results[i] = 0;
		}
		
		ArrayList<double[]> testData = speakerToCheck.getFeatures();
		int kWidth = testData.size()/10;
		for(int i = 0; i < testData.size(); i+= kWidth) {
			
			// temporary results
			double[] kResults = new double[trainedSpeaker.length];
			for(int l = 0; l < results.length; l++) {
				kResults[l] = 0;
			}
			
			// classify
			for(int k = i; k < i + kWidth && k < testData.size(); k++) {
				double distance = Double.MAX_VALUE;
				int speakerNo = 0;
				for(int s = 0; s < trainedSpeaker.length; s++) {
					double[][] codebook = trainedSpeaker[s].getCodebook();
					if(codebook == null) {
						return null;
					}
					for(int j = 0; j < codebook.length; j++) {
						double tmpDistance = distance(codebook[j],testData.get(k));
						if(distance > tmpDistance) {
							distance = tmpDistance;
							speakerNo = s;
						}
					}
				}
				kResults[speakerNo]++;
			}
			
			// check temporary results
			int winner = 0;
			for(int l = 1; l < results.length; l++) {
				if(kResults[winner] < kResults[l]) {
					winner = l;
				}
			}
			results[winner]++;
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
