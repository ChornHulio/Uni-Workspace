package de.tdreher.core;

import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.*;

import de.tdreher.algorithm.LPC;
import de.tdreher.algorithm.ann.unsupervised.NeuralGas;
import de.tdreher.algorithm.windows.*;

public class Speaker {

	private int sampleWidth = 640; // 640 = 40ms, 480 = 30ms
	private int slidingRate = 2; // the feed of LPC is SAMPLE_WIDTH/SLIDING_RATE
	private IWindow window = new HammingWindow(); // type of the window function
	private int p = 12; // number of LPC coefficents
	private int n = 10; // number of vectors in the codebook
	
	private ArrayList<double[]> lpc = new ArrayList<double[]>();
	double[][] codebook = null; // codebook with n vectors, each vector has p dimensions

	public int load(String filename) {
		File fileIn = new File(filename);
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(fileIn);
			int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
			if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
				// some audio formats may have unspecified frame size
				// in that case we may read any amount of bytes
				bytesPerFrame = 1;
			} 
			int numBytes = (int) (audioInputStream.getFrameLength() * bytesPerFrame); // take all
			byte[] audioBytes = new byte[numBytes];
			try {
				int numBytesRead = 0;
				int numFramesRead = 0;
				// try to read numBytes bytes from the file
				while ((numBytesRead = audioInputStream.read(audioBytes)) != -1) {
					// calculate the number of frames actually read
					numFramesRead = numBytesRead / bytesPerFrame;
					try {
						short[] rawData = new short[numFramesRead];
						// cast bits from littleEndian (16bit mono wave)
						for(int i = 0;i < numFramesRead; i++){
							int firstByte = (0x000000FF & ((int)audioBytes[2*i+1]));
							int secondByte = (0x000000FF & ((int)audioBytes[2*i]));
							rawData[i] = (short) (firstByte << 8 | secondByte);
						}
						// create windows
						int feed = sampleWidth/slidingRate; 
						for(int i = 0; i < numFramesRead-sampleWidth; i += feed) {
							short[] windowData = new short[sampleWidth];
							// copy window
							for(int j = 0; j < sampleWidth; j++) {
								windowData[j] = rawData[i+j];
							}
							// perform window function
							window.calc(windowData);
							// perform LPC
							lpc.addAll(LPC.calc(windowData, p));
						}
					} catch (Exception e) {
						System.err.println("error while preprocessing frames:");
						e.printStackTrace();
					}
				}
			} catch (Exception e) { 
				System.err.println("error while reading file:");
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.err.println("error while opening audio stream:");
			e.printStackTrace();
		}
		return 0; // no error		
	}
	
	public double[][] createCodebook() {
		NeuralGas ng = new NeuralGas(n,p);
		codebook = ng.calc(lpc);
		return codebook;
	}
	
	public ArrayList<double[]> getLPC() {
		return lpc;
	}
}
