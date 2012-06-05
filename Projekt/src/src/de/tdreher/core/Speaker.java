package de.tdreher.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import de.tdreher.algorithm.EnergyCut;
import de.tdreher.algorithm.ann.unsupervised.NeuralGas;
import de.tdreher.algorithm.audio.*;
import de.tdreher.algorithm.windows.*;
import de.tdreher.audio.WavFile;
import de.tdreher.audio.WavFileException;

public class Speaker {

	// settings for feature extraction
	private int sampleWidth; // 640 = 40ms, 480 = 30ms
	private int slidingRate; // the feed is SAMPLE_WIDTH/SLIDING_RATE
	private IWindow window; // type of window function
	private int coefficents; // size of the feature vector
	private int energyLevel; // percentage of windows to cut
	private IFeatureExtraction featureExtraction; // type of feature extraction (LPC or MFCC)
	private ArrayList<double[]> features = new ArrayList<double[]>();
	
	// settings for training
	private boolean training;
	private int n = 100; // number of vectors in the codebook	
	double[][] codebook = null; // codebook with n vectors, each vector has p dimensions
	
	public Speaker(boolean training, boolean mfcc) {
		this.training = training;
		if(training) {
			slidingRate = 2; // the feed is SAMPLE_WIDTH/SLIDING_RATE
		} else {
			slidingRate = 0;
		}
		if(mfcc) {
			sampleWidth = 512;
			coefficents = 20;
			featureExtraction = new MFCCWrapper(sampleWidth,coefficents);
		} else {
			sampleWidth = 512;
			window = new HammingWindow();
			coefficents = 12;
			featureExtraction = new LPC(sampleWidth,slidingRate,window,coefficents);
		}
		energyLevel = 90;
	}
	
	public ArrayList<double[]> load(String filename) {
		return load(filename,-1);
	}

	/**
	 * read wav files and extract features
	 * @param filename
	 * @param milliseconds
	 * @return features
	 */
	public ArrayList<double[]> load(String filename, int milliseconds) {		
		File fileIn = new File(filename);

		double[] buffer = null;
		double[] audio = null;
		WavFile readWavFile = null;
		try {
			// read
			readWavFile = WavFile.openWavFile(fileIn);
			if(readWavFile.getNumChannels() != 1) {
				throw new WavFileException("wrong number of channels in wav file");
			}
			int bufferSize = (int) readWavFile.getNumFrames();
			buffer = new double[bufferSize];
			readWavFile.readFrames(buffer, buffer.length);

			// copy for training or testing
			int numFrames = 0;
			if(milliseconds > 0) {
				numFrames = 16*milliseconds;
			} else {
				numFrames = bufferSize;		
			}
			readWavFile.close();
			audio = new double[numFrames];
			if(training) {
				// take everthing
				for(int i = 0; i < numFrames; i++) {
					audio[i] = buffer[i]; 
				}
			} else {
				// take random samples of sampleWidth
				Random rand = new Random();
				int i = 0;
				while(i < numFrames - sampleWidth) {
					int start = rand.nextInt(bufferSize - sampleWidth);
					for(int j = start; j < start + sampleWidth; j++) {
						audio[i] = buffer[j];
						i++;
					}					
				}
			}
						
			// energy level
			double[] cuttedAudio = EnergyCut.cut(audio, energyLevel, sampleWidth);
			
			// feature extraction
			features.addAll(featureExtraction.process(cuttedAudio));
		} catch (Exception e) {
			System.err.println("error while reading wav file:");
			e.printStackTrace();
		}
		return features;
	}
	
	public ArrayList<double[]> getFeatures() {
		return features;
	}

	/**
	 * training
	 * @return codebook
	 */
	public double[][] createCodebook() {
		if(features.isEmpty()) {
			return null;
		}
		NeuralGas ng = new NeuralGas(n,coefficents);
		codebook = ng.calc(features);
		return codebook;
	}
	
	public double[][] getCodebook() {
		return codebook;
	}
}
