package de.tdreher.algorithms.audio;

import java.util.ArrayList;

public interface IFeatureExtraction {
		
	public ArrayList<double[]> process(double[] input);

}
