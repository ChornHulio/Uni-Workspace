package de.tdreher.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Microphone {
	
	int windowSize = 51200;
	
	public Microphone(int windowSize) {
		this.windowSize = windowSize;
	}
	
	public void flushMicrophone() throws LineUnavailableException {
		AudioFormat format;
		TargetDataLine line;
		DataLine.Info info;

		// init and open microphone
		format = new AudioFormat(16000.0f, 16, 1, true, false); // 16kHz
																// 16bit
																// mono
																// identisch
																// zu den
																// waves
		info = new DataLine.Info(TargetDataLine.class, format);
		line = (TargetDataLine) AudioSystem.getLine(info);
		line.open(format);
		
		// flush buffer and close the device
		line.flush();
		line.close();
	}
	
	public double[] readLive() throws LineUnavailableException {
		AudioFormat format;
		TargetDataLine line;
		DataLine.Info info;
		byte[] data = new byte[windowSize * 2];
		double[] rawData = new double[windowSize];

		// init and open microphone
		format = new AudioFormat(16000.0f, 16, 1, true, false); // 16kHz
																// 16bit
																// mono
																// identisch
																// zu den
																// waves
		info = new DataLine.Info(TargetDataLine.class, format);
		line = (TargetDataLine) AudioSystem.getLine(info);
		line.open(format, windowSize * 2);

		// read data		
		line.start();
		int numBytesRead = line.read(data, 0, data.length);
		
		// convert and save data
		double level = Long.MAX_VALUE >> (64 - 16); // on a 64 bit machine
		for (int i = 0; i < windowSize && i < numBytesRead*2; i++) {
			int firstByte = (0x000000FF & ((int) data[2 * i + 1]));
			int secondByte = (0x000000FF & ((int) data[2 * i]));
			short result = (short) (firstByte << 8 | secondByte);
			rawData[i] = ((double) result) / level;
		}
		line.stop();
		line.close();
		return rawData;
	}

}
