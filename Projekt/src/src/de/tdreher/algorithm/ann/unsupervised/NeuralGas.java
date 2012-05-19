package de.tdreher.algorithm.ann.unsupervised;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class NeuralGas {
	
	private final int iterations = 5; // iterations of the algorithm
	
	private int n = 0; // number of vectors in the codebook
	private int p = 0; // number of values per vector
	private double[][] w = null; // synaptic weights (dimension n * p)
	
	private double[][] data = null;
	
	public NeuralGas(int n, int p) {
		this.n = n;
		this.p = p;		
		initW();
	}

	public double[][] calc(ArrayList<double[]> orgData) {
		if(w == null) {
			throw new ExceptionInInitializerError("Instance not initialized");
		}
		copyData(orgData); // copy to data (double[][])
		orgData = null; // delete old data
		
		for(int iteration = 0; iteration < iterations; iteration++) {
			permutateData();
			for(int i = 0; i < data.length; i++) { // go for every data vector
				// init params
				double adaption = 0.1; //Math.pow(0.001, (iteration / data.length));
				double neighbor = 0.1; //n/2 * Math.pow(0.01/n/2, (iteration / data.length));
				
				// get the next vector
				double[] v = data[i];
				
				// calculate distances between v and w_j
				WSortArray[] wsa = new WSortArray[n];
				for(int j = 0; j < n; j++) {
					double distance = 0.0;
					for(int k = 0; k < p; k++) {
						distance += Math.pow(v[k] - w[j][k] , 2); // euclidean distance
					}
					wsa[j] = new WSortArray(distance, j);
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
	
	/**
	 * Class to sort the distances of w_i to v
	 */
	public class WSortArray implements Comparable<WSortArray> {
		 
		  public double distance = 0;
		  public int index = 0;
		  
		  public WSortArray(double d, int i) {
			  this.distance = d;
			  this.index = i;
		  }
		 
		  @Override
		  public int compareTo(WSortArray w) {
		    if(this.distance < w.distance) {
		      return -1;
		    }
		    if(this.distance > w.distance) {
		      return 1;
		    }
		    return 0;
		  }
		}
}
