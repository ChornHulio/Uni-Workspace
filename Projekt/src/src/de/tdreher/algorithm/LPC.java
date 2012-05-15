package de.tdreher.algorithm;

import java.util.ArrayList;

public class LPC {
	
	public static ArrayList<double[]> calc(short[] frame, int p){
		int[] r = autocorrelation(frame, p);
		double[][] a = calcLPC(r,p);
		return moveCoefficents(a,p); // and vonvert in ArrayList
	}
	
	private static int[] autocorrelation(short[] frame, int p) {
		int[] r = new int[p+1];
		for(int m = 0; m <= p; m++) {
			r[m] = 0;
			for(int n = 0; n < frame.length - m; n++) {
				r[m] += frame[n] * frame[n + m];
			}
		}
		return r;
	}
	
	private static double[][] calcLPC(int[] r, int p) {
		double a[][] = new double[p+1][p+1];
		double E[] = new double[p+1];
		a[0][0] = 0;
		E[0] = r[0];		
		for(int i = 1; i <= p; i++) {
			double k = r[i];
			for (int j = 1; j < i; j++) {
				k -= a[i-1][j] * r[i - j];
			}
			k /= E[i-1];
			
			a[i][i] = k;
			
			for(int j = 1; j < i; j++) {
				a[i][j] = a[i-1][j] - k * a[i-1][i-j];
			}
			
			E[i] = (1 - k * k) * E[i-1];			
		}
		return a;
	}
	
	private static ArrayList<double[]> moveCoefficents(double[][] a, int p) {
		ArrayList<double[]> ret = new ArrayList<double[]>();
		for(int i = 0; i < p; i++) {
			double tmp[] = new double[p];
			for(int j = 0; j < p; j++) {
				tmp[j] = a[i+1][j];
			}
			ret.add(i, tmp);
		}
		return ret;
	}

}
