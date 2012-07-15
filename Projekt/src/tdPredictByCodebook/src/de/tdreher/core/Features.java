package de.tdreher.core;

import java.util.ArrayList;

public class Features {
	
	public static final int MAX_LABELS = 100;
	private ArrayList<ArrayList<double[]>> data = new ArrayList<ArrayList<double[]>>(); // feature vectors
	private int[] labelToIndex = new int[MAX_LABELS];
	
	public Features() {
		for(int i = 0; i < MAX_LABELS; i++) {
			labelToIndex[i] = -1;
		}
	}
	
	public void add(double[] vector) throws Exception {
		add(vector,0);
	}
	
	public void add(double[] vector, int label) throws Exception {
		if(label < 0 || label >= MAX_LABELS) {
			throw new Exception("A label have to be between 0 and " + MAX_LABELS);
		}
		int index = labelToIndex[label];
		if(index >= 0) { // the label already occurs in the data
			ArrayList<double[]> currentData = data.remove(index);
			currentData.add(vector);
			data.add(index,currentData);
		} else { // label is not in data yet
			ArrayList<double[]> currentData = new ArrayList<double[]>();
			currentData.add(vector);
			data.add(currentData);
			labelToIndex[label] = data.size() - 1;
		}
	}

	public boolean isEmpty() {
		if(getCountOfLabels() <= 0) {
			return true;
		}
		return false;
	}

	public int getCountOfLabels() {
		int size = 0;
		for(int i = 0; i < MAX_LABELS; i++) {
			if(labelToIndex[i] >= 0) {
				size++;
			}
		}
		return size;
	}
	
	public int getLabel(int index) throws Exception {
		/* attention: this index is not the index in the data-ArrayList. It is the number of label.
		 * Example: There are the label 2,7,5,1 and 9 (order in data-ArrayList is important). So getLabel(3) return 5.
		 */		
		for(int i = 0; i < MAX_LABELS; i++) {
			if(labelToIndex[i] >= 0) {
				if(index-- == 0) {					
					return i;
				}
			}
		}
		throw new Exception("No such index in the features data");
	}

	public ArrayList<double[]> getData(int label) {
		int index = labelToIndex[label];
		return data.get(index);
	}
	
	public ArrayList<double[]> getAllData() {
		ArrayList<double[]> ret = new ArrayList<double[]>();
		for(int i = 0; i < MAX_LABELS; i++) {
			if(labelToIndex[i] >= 0) {
				ret.addAll(data.get(labelToIndex[i]));
			}
		}
		return ret;
	}
}
