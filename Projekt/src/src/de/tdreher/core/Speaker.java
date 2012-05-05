package de.tdreher.core;

import java.io.File;

import javax.sound.sampled.*;

public class Speaker {

	private static final int SAMPLE_WIDTH = 640; // 640 = 40ms, 480 = 30ms
	private static final int SLIDING_RATE = 2; // the feed of LPC is SAMPLE_WIDTH/SLIDING_RATE

	public int preprocessing(String filename) {
		int totalFramesRead = 0;
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
				// try to read numBytes bytes from the file.
				while ((numBytesRead = audioInputStream.read(audioBytes)) != -1) {
					// calculate the number of frames actually read.
					numFramesRead = numBytesRead / bytesPerFrame;
					System.out.println("Frames read: " + numFramesRead);
					totalFramesRead += numFramesRead;
					short[] rawData = new short[numFramesRead];
					// cast bits from littleEndian (16bit mono wave)
					for(int i=0;i<totalFramesRead;i++){
						int firstByte = (0x000000FF & ((int)audioBytes[2*i+1]));
						int secondByte = (0x000000FF & ((int)audioBytes[2*i]));
						rawData[i] = (short) (firstByte << 8 | secondByte);
					}
					for(int i = 0; i < numFramesRead-SAMPLE_WIDTH/SLIDING_RATE; i+=SAMPLE_WIDTH/SLIDING_RATE) {
						// TODO hamming window (in this for loop)
						// TODO LPC (in this for loop)
					}
				}
				System.out.println("Total frames read: " + totalFramesRead);
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
}
