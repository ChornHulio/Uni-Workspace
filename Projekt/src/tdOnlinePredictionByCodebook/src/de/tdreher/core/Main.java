package de.tdreher.core;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import de.tdreher.algorithms.NearestNeighbor;
import de.tdreher.audio.WavFile;
import de.tdreher.common.ArgInterpreter;

public class Main {

	private static final int WINDOW_SIZE = 512 * 100;
	private static Settings settings = null;
	private static Features features = new Features();
	private static Features codebooks = new Features();

	public static void main(String[] args) throws Exception {
		// init settings
		settings = ArgInterpreter.interpret(args);
		if (settings == null) {
			return;
		}

		// read input files (codebook vectors)
		for (int i = 0; i < settings.getCodebookFiles().size(); i++) {
			try {
				readOneFile(settings.getCodebookFiles().get(i), false);
			} catch (Exception e) {
				System.err.println(e.getMessage());
				return;
			}
		}

		// check if input files wasn't empty
		if (codebooks.isEmpty()) {
			System.err.println("Codebook files are empty");
			return;
		}

		// clear output file if it is necassary
		clearOutputFile();

		File audioFile = new File("online.wav");
		File featureFile = new File("online.mfcc");
		int counter = 50; // TODO delete
		while (counter-- > 0) {
			try {
				// read data from microphone
				double[] rawData = readLive();

				// write a wav file
				WavFile wavFile = WavFile.newWavFile(audioFile, 1, WINDOW_SIZE,
						16, 16000);
				wavFile.writeFrames(rawData, rawData.length);
				wavFile.close();

				// extract features
				Runtime runtime = Runtime.getRuntime();
				Process process = runtime
						.exec("java -jar ./tdFeatureExtraction.jar -i "
								+ audioFile.getCanonicalPath() + " -o "
								+ featureFile.getCanonicalPath() + " -l 42");
				writeProcessOutput(process);

				// read input files (feature vectors)
				readOneFile(featureFile, true);

				// get nearest neighbor between n feature vectors and the
				// codebooks
				NearestNeighbor nn = new NearestNeighbor(codebooks);
				int[] labels = nn.calc(features, settings.getNumber());

				// write in output file
				writeOutputFile(labels);
			} catch (Exception e) {
				System.err
						.println("error while reading from microphone or executing tdFeatureExtraction");
				System.err.println(e.getMessage());
			}
		}
	}

	private static void readOneFile(File file, boolean featureFile)
			throws Exception {
		FileReader fr = null;
		try {
			fr = new FileReader(file);
			BufferedReader in = new BufferedReader(fr);
			String line = null;
			// read vectors
			while ((line = in.readLine()) != null) {
				line = line.trim();
				String[] strings = line.split(" ");
				// number of coefficents
				int i = 0;
				if (featureFile || !strings[0].contains(":")) {
					settings.setCoefficents(strings.length - 1);
					i = 1;
				} else {
					settings.setCoefficents(strings.length);
				}
				// read features of the current vector
				double[] vector = new double[settings.getCoefficents()];
				for (; i < strings.length; i++) {
					String[] feature = strings[i].split(":");
					vector[i - 1] = Double.parseDouble(feature[1]);
				}
				if (featureFile) {
					features.add(vector);
				} else {
					codebooks.add(vector, Integer.parseInt(strings[0]));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void clearOutputFile() {
		Writer fw = null;
		try {
			fw = new FileWriter(settings.getOutputFile(),
					settings.getAppendFlag());
			fw.write("");
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

	private static void writeOutputFile(int[] labels) {
		Writer fw = null;
		try {
			fw = new FileWriter(settings.getOutputFile(), true);
			String str = "";
			for (int i = 0; i < labels.length; i++) {
				str += labels[i] + System.getProperty("line.separator");
			}
			System.out.println(str);
			fw.write(str);
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

	private static double[] readLive() throws LineUnavailableException {
		AudioFormat format;
		TargetDataLine line;
		DataLine.Info info;

		format = new AudioFormat(16000.0f, 16, 1, true, false); // 16kHz
																// 16bit
																// mono
																// identisch
																// zu den
																// waves
		info = new DataLine.Info(TargetDataLine.class, format);
		line = (TargetDataLine) AudioSystem.getLine(info);

		int bufferSize = WINDOW_SIZE; // hier für ein fenster.
		// es kann notwenig sein den buffer größer zu halten und die
		// fensterfunktion danach zu berechene um die anzahl der zugriffe
		// auf die line zu minimieren.

		line.open(format, bufferSize * 2);
		byte[] data = new byte[bufferSize * 2];
		double[] rawData = new double[bufferSize];

		line.flush();// eventuell altes zeug aus dem puffer werfen // TODO
						// only the first time
		line.start();// hier empfehlen sich schleifen, und multi threading
						// um den eingang permanent auszulesen
		// Read the next chunk of data from the TargetDataLine.
		int numBytesRead = line.read(data, 0, data.length);
		// Save this chunk of data.
		double level = Long.MAX_VALUE >> (64 - 16); // on a 64 bit machine
		for (int i = 0; i < bufferSize; i++) { // siehe offline fall
			int firstByte = (0x000000FF & ((int) data[2 * i + 1]));
			int secondByte = (0x000000FF & ((int) data[2 * i]));
			short result = (short) (firstByte << 8 | secondByte);
			rawData[i] = ((double) result) / level;
		}
		line.stop();
		return rawData;
	}

	static void writeProcessOutput(Process process) throws Exception {
		InputStreamReader tempReader = new InputStreamReader(
				new BufferedInputStream(process.getInputStream()));
		BufferedReader reader = new BufferedReader(tempReader);
		while (true) {
			String line = reader.readLine();
			if (line == null)
				break;
			System.out.println(line);
		}
	}
}