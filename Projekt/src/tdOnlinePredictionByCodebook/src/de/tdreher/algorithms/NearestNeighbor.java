package de.tdreher.algorithms;

import java.util.ArrayList;

import de.tdreher.core.Features;

public class NearestNeighbor {
	
	int historySize = 5;
	Features codebooks = null;
	ArrayList<int[]> resultHistory = new ArrayList<int[]>();
	ArrayList<Integer> winners = new ArrayList<Integer>();

	public NearestNeighbor(Features codebooks) {
		this.codebooks = codebooks;
	}

	public int[] calc(Features features, int number) throws Exception {
		if(codebooks == null) {
			throw new Exception("codebook not initialized");
		}
		
		ArrayList<double[]> f = features.getAllData();
		int[] results = new int[f.size()/number];		
		for(int i = 0; i < f.size() - number; i+=number) {
			if(number == 1) {
				results[i] = getNearestNeighbor(f.get(i));
			} else {
				// get the nearest neighbors
				int[] tmpResults = new int[number];
				for(int j = 0; j < number; j++) {
					tmpResults[j] = getNearestNeighbor(f.get(i+j));
				}
				// get the label which is most often the nearest neighbor
				int[] winners = new int[Features.MAX_LABELS];
				for(int j = 0; j < winners.length; j++) {
					winners[j] = 0;
				}
				for(int j = 0; j < number; j++) {
					winners[tmpResults[j]]++;
				}
				results[i/number] = 0;
				for(int j = 0; j < winners.length; j++) {
					if(winners[j] > winners[results[i/number]]) {
						results[i/number] = j;
					}
				}				
			}
		}
		if(this.resultHistory.size() >= historySize) {
			this.resultHistory.remove(0);
		}
		this.resultHistory.add(results);
		return results;
	}

	private int getNearestNeighbor(double[] feature) throws Exception {
		double minDis = Double.MAX_VALUE;
		int bestLabel = -1;
		int countOfLabels = codebooks.getCountOfLabels();
		for(int i = 0; i < countOfLabels; i++) {
			int label = codebooks.getLabel(i);
			ArrayList<double[]> codebook = codebooks.getData(label);
			for(int j = 0; j < codebook.size(); j++) {
				double dis = distance(feature,codebook.get(j));
				if(dis < minDis) {
					minDis = dis;
					bestLabel = label;
				}
			}
		}
		return bestLabel;
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

	public int getWinner() {
		// init winners with 0
		winners.clear();
		for(int i = 0; i < Features.MAX_LABELS; i++) {
			winners.add(0);
		}
		// count each winner
		for(int i = 0; i < resultHistory.size(); i++) {
			int[] iResults = resultHistory.get(i);
			for(int j = 0; j < iResults.length; j++) {
				int winner = iResults[j];
				int winnerCount = winners.remove(winner);
				winners.add(winner, winnerCount + i + 1); // newer winners count more
			}
		}		
		// get winner (highest counter)
		int winner = 0;
		for(int i = 0; i < Features.MAX_LABELS; i++) {
			if(winners.get(i) > winners.get(winner)) {
				winner = i;
			}
		}
		return winner;
	}
}
