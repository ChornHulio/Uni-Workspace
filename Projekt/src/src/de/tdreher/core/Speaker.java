package de.tdreher.core;

import java.io.File;
import java.util.ArrayList;

import de.tdreher.algorithm.EnergyCut;
import de.tdreher.algorithm.LPC;
import de.tdreher.algorithm.ann.unsupervised.NeuralGas;
import de.tdreher.algorithm.windows.*;
import de.tdreher.audio.WavFile;
import de.tdreher.audio.WavFileException;

public class Speaker {

	private int sampleWidth = 640; // 640 = 40ms, 480 = 30ms
	private int slidingRate = 2; // the feed of LPC is SAMPLE_WIDTH/SLIDING_RATE
	private IWindow window = new HammingWindow(); // type of the window function
	private int p = 12; // number of LPC coefficents
	private int n = 100; // number of vectors in the codebook
	private int energyLevel = 0; // percentage of windows to cut
	
	private ArrayList<double[]> lpc = new ArrayList<double[]>();
	double[][] codebook = null; // codebook with n vectors, each vector has p dimensions
	
	public ArrayList<double[]> load(String filename) {
		return load(filename,-1);
	}

	public ArrayList<double[]> load(String filename, int milliseconds) {
		File fileIn = new File(filename);

		double[] buffer = null;		
		WavFile readWavFile = null;
		try {
			readWavFile = WavFile.openWavFile(fileIn);
			if(readWavFile.getNumChannels() != 1) {
				throw new WavFileException("wrong number of channels in wav file");
			}
			int numFrames = 0;
			if(milliseconds > 0) {
				numFrames = 16*milliseconds;
			} else {
				numFrames = (int) readWavFile.getNumFrames();			
			}
			buffer = new double[numFrames];
			readWavFile.readFrames(buffer, buffer.length);
			readWavFile.close();
		} catch (Exception e) {
			System.err.println("error while reading wav file:");
			e.printStackTrace();
		}
		
		double absolutEnergyLevel = EnergyCut.calcEnergyLevel(energyLevel,buffer);
		
		// create windows
		int feed = sampleWidth/slidingRate; 
		for(int i = 0; i < buffer.length-sampleWidth; i+=feed) {
			double[] windowData = new double[sampleWidth];
			// copy window
			for(int j = 0; j < sampleWidth; j++) {
				windowData[j] = buffer[i+j];
			}
			// perform window function
			windowData = window.calc(windowData);
			// cut windows with low energy
			if(EnergyCut.calc(absolutEnergyLevel,windowData)) {
				continue;
			}
			// perform LPC
			lpc.addAll(LPC.calc(windowData, p));
		}
		
		return lpc;
	}
	
	public ArrayList<double[]> getLPC() {
		return lpc;
	}

	public double[][] createCodebook() {
		if(lpc.isEmpty()) {
			return null;
		}
		NeuralGas ng = new NeuralGas(n,p);
		codebook = ng.calc(lpc);
		return codebook;
	}
	
	public double[][] getCodebook() {
		return codebook;
	}
}
