package de.tdreher.core;

import java.io.File;
import java.util.LinkedList;

import de.tdreher.algorithms.audio.IFeatureExtraction;
import de.tdreher.algorithms.audio.*;
import de.tdreher.algorithms.windows.HammingWindow;
import de.tdreher.algorithms.windows.IWindow;

public class Settings {
	// files
	private LinkedList<File> inputFile = new LinkedList<File>();
	private File outputFile = null;
	private boolean append = false; // should the output be appended at the end of the given file or not
	// label
	private int label = -1;
	// feature extraction settings (default are MFCC settings)
	private int sampleWidth = 512; // 640 = 40ms, 480 = 30ms
	private int slidingRate = 2; // the feed is SAMPLE_WIDTH/SLIDING_RATE
	private IWindow window = new HammingWindow(); // type of window function
	private int coefficents = 20; // size of the feature vector
	private int energyLevel = 90; // percentage of windows to cut
	private IFeatureExtraction featureExtraction = null;
	
	
	public LinkedList<File> getInputFiles() {
		return inputFile;
	}
	public void addInputFile(File inputFile) {
		this.inputFile.add(inputFile);
	}
	public File getOutputFile() {
		return outputFile;
	}
	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}
	public int getLabel() {
		return label;
	}
	public void setLabel(int label) {
		this.label = label;
	}
	public int getSampleWidth() {
		return sampleWidth;
	}
	public void setSampleWidth(int sampleWidth) {
		this.sampleWidth = sampleWidth;
	}
	public int getSlidingRate() {
		return slidingRate;
	}
	public void setSlidingRate(int slidingRate) {
		this.slidingRate = slidingRate;
	}
	public IWindow getWindow() {
		return window;
	}
	public void setWindow(IWindow window) {
		this.window = window;
	}
	public int getCoefficents() {
		return coefficents;
	}
	public void setCoefficents(int coefficents) {
		this.coefficents = coefficents;
	}
	public int getEnergyLevel() {
		return energyLevel;
	}
	public void setEnergyLevel(int energyLevel) {
		this.energyLevel = energyLevel;
	}
	public IFeatureExtraction getFeatureExtraction() {
		return featureExtraction;
	}
	/**
	 * Set the method for feature extraction (LPC or MFCC)
	 * @param mfcc if true, MFCC is set; if false, LPC is set
	 */
	public void setFeatureExtraction(boolean mfcc) {
		if(mfcc)
			this.featureExtraction = new MFCCWrapper(sampleWidth, coefficents);
		else
			this.featureExtraction = new LPC(sampleWidth, slidingRate, window, coefficents);
	}
	public boolean getAppendFlag() {
		return append;
	}
	public void setAppendFlag() {
		this.append = true;
	}
}
