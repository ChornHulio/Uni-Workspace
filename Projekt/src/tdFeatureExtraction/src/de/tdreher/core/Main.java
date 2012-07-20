package de.tdreher.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Random;

import de.tdreher.algorithms.EnergyCut;
import de.tdreher.audio.WavFile;
import de.tdreher.audio.WavFileException;
import de.tdreher.common.ArgInterpreter;

public class Main {
	
	private static Settings settings = null;
	private static ArrayList<double[]> features = new ArrayList<double[]>();
	
	public static void main(String[] args) {
		// init settings
		settings = ArgInterpreter.interpret(args);
		if(settings == null) {
			return;
		}
		
		// extract features
		for(int i = 0; i < settings.getInputFiles().size(); i++) {
			extractOneFile(settings.getInputFiles().get(i));			
		}
		
		// write in output file
		writeOutputFile();
	}

	private static void extractOneFile(File fileIn) {
		double[] buffer = null;
		double[] audio = null;
		WavFile readWavFile = null;
		int milliseconds = -1; // only for testing purpose
		try {
			// read
			readWavFile = WavFile.openWavFile(fileIn);
			if(readWavFile.getNumChannels() != 1) {
				throw new WavFileException("wrong number of channels in wav file");
			}
			int bufferSize = (int) readWavFile.getNumFrames();
			buffer = new double[bufferSize];
			readWavFile.readFrames(buffer, buffer.length);

			// copy buffer
			int numFrames = 0;
			if(milliseconds < 0) {
				numFrames = bufferSize;					
			} else {
				numFrames = 16*milliseconds;
			}
			readWavFile.close();
			audio = new double[numFrames];
			if(milliseconds < 0) {
				// take everthing
				for(int k = 0; k < numFrames; k++) {
					audio[k] = buffer[k]; 
				}
			} else {
				// take samples from a random place
				Random rand = new Random();
				int k = 0;
				int start = rand.nextInt(bufferSize - settings.getSampleWidth());
				for(int j = start; j < start + settings.getSampleWidth() + numFrames; j++) {
					audio[k] = buffer[j];
					k++;			
				}
			}
						
			// energy level
			double[] cuttedAudio = EnergyCut.cut(audio, settings.getEnergyLevel(), settings.getSampleWidth());
			
			// feature extraction
			features.addAll(settings.getFeatureExtraction().process(cuttedAudio));
		} catch (Exception e) {
			System.err.println("error while reading wav file:");
			e.printStackTrace();
		}
	}
	
	private static void writeOutputFile() {
		Writer fw = null;
		try {
			fw = new FileWriter(settings.getOutputFile(), settings.getAppendFlag());
			for (int i = 0; i < features.size(); i++) {
				String str = "" + settings.getLabel() + " ";
				for (int j = 0; j < features.get(i).length; j++) {
					str += (j + 1) + ":" + features.get(i)[j] + " ";
				}
				str += System.getProperty("line.separator"); // e.g. "\n"
				fw.write(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}		
	}
}
