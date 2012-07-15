package de.tdreher.algorithms;

import java.util.ArrayList;

public class Analysis {
	
	ArrayList<Integer> labels = new ArrayList<Integer>();
	int[][] votingMatrix = null;
	int[] labelToIndex;

	public String process(ArrayList<Integer> originLabels, ArrayList<Integer> predictedLabels) {
		extractLabels(originLabels);
		votingMatrix = new int[labels.size()][labels.size()];
		// init voting matrix
		for(int i = 0; i < labels.size(); i++) {
			for(int j = 0; j < labels.size(); j++) {
				votingMatrix[i][j] = 0;
			}
		}
		for(int i = 0; i < originLabels.size(); i++) {
			int correct = labelToIndex[originLabels.get(i)];
			int predicted = labelToIndex[predictedLabels.get(i)];
			votingMatrix[correct][predicted]++;
		}
		return convertToString();
	}

	private void extractLabels(ArrayList<Integer> originLabels) {
		// extract labels from originLabels
		for(Integer label : originLabels) {
			if(!labels.contains(label)) {
				labels.add(label);
			}
		}
		// sort labels
		for(int i = 1; i < labels.size(); i++) {
			if(labels.get(i-1) > labels.get(i)) {
				Integer label = labels.remove(i-1);
				labels.add(i, label);
			}
		}
		// set labelToIndex
		int max = 0;
		for(Integer label : labels) {
			if(label > max) {
				max = label;
			}
		}
		labelToIndex = new int[max+1];
		for(int i = 0; i <= max; i++) {
			labelToIndex[i] = 0;
		}
		for(int i = 0; i < labels.size(); i++) {
			labelToIndex[labels.get(i)] = i;
		}
	}

	private String convertToString() {
		String str = "";
		for(int i = 0; i < votingMatrix.length; i++) {
			for(int j = 0; j < votingMatrix[i].length; j++) {
				str += votingMatrix[i][j] + "\t";
			}
			str += System.getProperty("line.separator");
		}
		return str;
	}

}
