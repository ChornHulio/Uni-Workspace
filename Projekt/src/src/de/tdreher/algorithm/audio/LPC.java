package de.tdreher.algorithm.audio;

import java.util.ArrayList;

import de.tdreher.algorithm.EnergyCut;
import de.tdreher.algorithm.windows.HammingWindow;
import de.tdreher.algorithm.windows.IWindow;

public class LPC implements IFeatureExtraction {
	
	// default values
	private int sampleWidth = 640; // 640 = 40ms, 480 = 30ms
	private int slidingRate = 2; // the feed of LPC is SAMPLE_WIDTH/SLIDING_RATE
	private IWindow window = new HammingWindow(); // type of window function
	private int coefficents = 12; // size of the feature vector
	private int energyLevel = 90; // percentage of windows to cut
	private ArrayList<double[]> lpc = new ArrayList<double[]>();
	
	public LPC(int sampleWidth,int slidingRate,IWindow window,int coefficents) {
		this.sampleWidth = sampleWidth;
		this.slidingRate = slidingRate;
		this.window = window;
		this.coefficents = coefficents;
	}

	public ArrayList<double[]> process(double[] input){
		// create windows
		int feed = sampleWidth;
		if(slidingRate > 0) {
			feed = sampleWidth/slidingRate; 
		}
		for(int i = 0; i < input.length-sampleWidth; i+=feed) {
			double[] windowData = new double[sampleWidth];
			// copy window
			for(int j = 0; j < sampleWidth; j++) {
				windowData[j] = input[i+j];
			}
			// perform window function
			windowData = window.calc(windowData);
						
			// auto correlation
			double[] r = autocorrelation(windowData, coefficents);
			// lpc
			double[][] a = calcLPC(r,coefficents);
			// cut first element and convert in ArrayList
			lpc.addAll(moveCoefficents(a,coefficents)); 
		}
		return lpc;		
	}
	
	private double[] autocorrelation(double[] frame, int p) {
		double[] r = new double[p+1];
		for(int m = 0; m <= p; m++) {
			r[m] = 0;
			for(int n = 0; n < frame.length - m; n++) {
				r[m] += frame[n] * frame[n + m];
			}
		}
		return r;
	}
	
	private double[][] calcLPC(double[] r, int p) {
		double a[][] = new double[p+1][p+1];
		double E[] = new double[p+1];
		a[0][0] = 0;
		E[0] = r[0];		
		for (int i = 1; i <= p; i++) {
			double k = r[i];
			for (int j = 1; j < i; j++) {
				k -= a[i-1][j] * r[i - j];
			}
			k /= E[i-1];
			
			a[i][i] = k;
			
			for (int j = 1; j < i; j++) {
				a[i][j] = a[i-1][j] - k * a[i-1][i-j];
			}
			
			E[i] = (1.0 - k * k) * E[i-1];			
		}
		return a;
	}
	
	private ArrayList<double[]> moveCoefficents(double[][] a, int p) {
		double[] ret = new double[p];
		for(int i = 0; i < p; i++) {
			ret[i] = a[p][i];
		}
		ArrayList<double[]> al = new ArrayList<double[]>();
		al.add(ret);
		return al;
	}

}
