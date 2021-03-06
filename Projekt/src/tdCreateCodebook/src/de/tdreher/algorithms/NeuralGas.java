package de.tdreher.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import de.tdreher.common.SortArray;

public class NeuralGas {
	
	private int n = 0; // number of vectors in the codebook
	private int iterations = 15; // iterations of the algorithm
	private int p = 0; // number of values per vector
	private double[][] w = null; // synaptic weights (dimension n * p)
	private double[][] t = null;
	
	private double[][] data = null;
	
	public NeuralGas(int size, int iterations, int coefficents) {
		this.n = size;
		this.iterations = iterations;
		this.p = coefficents;		
		initW();
	}

	public double[][] calc(ArrayList<double[]> orgData) {
		if(w == null) {
			throw new ExceptionInInitializerError("Instance not initialized");
		}
		copyData(orgData); // copy to data (double[][])
		orgData = null; // delete old data
		
		for(int iteration = 0; iteration < iterations; iteration++) {
			// init params
			double adaption = Math.pow(0.001, ((double) (iteration+1) / iterations));
			double neighbor = n/2 * Math.pow(0.01/n/2, ((double) (iteration+1) / iterations));
			
			// init data
			permutateData();
			
			for(int i = 0; i < data.length; i++) { // go for every data vector
				// get the next vector
				double[] v = data[i];
				
				// calculate distances between v and w_j
				SortArray[] wsa = new SortArray[n];
				for(int j = 0; j < n; j++) {
					double distance = 0.0;
					for(int k = 0; k < p; k++) {
						distance += Math.pow(v[k] - w[j][k] , 2); // euclidean distance
					}
					wsa[j] = new SortArray(distance, j);
				}
				
				// sort distances
				Arrays.sort(wsa);
				
				// adapt w
				for(int k = 0; k < wsa.length; k ++) {
					int index = wsa[k].index;
					for(int l = 0; l < p; l++) {
						w[index][l] += adaption * Math.exp(-k/neighbor) * (v[l] - w[index][l]);
					}
				}			
			}
		}
		return w;
	}
	
	

	private void initW() {
		w = new double[n][p];
		Random rand = new Random();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < p; j++) {
				w[i][j] = 2 * rand.nextDouble() - 1;
			}
		}
		
		t = new double[n][p];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < p; j++) {
				t[i][j] = w[i][j];
			}
		}
	}
	
	private void copyData(ArrayList<double[]> orgData) {
		this.data = new double[orgData.size()][orgData.get(0).length];
		for (int i = 0; i < orgData.size(); i++) {
			this.data[i] = orgData.get(i);
		}
	}
	
	/**
	 * Fisher-Yates Shuffle, permutates the member attribute 'data'
	 */
	private void permutateData() {
		Random rand = new Random();
		for (int i = data.length - 1; i > 0; i--) {
			int j = rand.nextInt(i + 1);
			double[] tmp = data[i];
			data[i] = data[j];
			data[j] = tmp;
		}
	}
}
