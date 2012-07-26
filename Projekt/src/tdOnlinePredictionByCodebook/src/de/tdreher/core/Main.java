package de.tdreher.core;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

import de.tdreher.algorithms.NearestNeighbor;
import de.tdreher.audio.Microphone;
import de.tdreher.audio.WavFile;
import de.tdreher.common.ArgInterpreter;

public class Main {

	private static int windowSize = 512 * 10; // 512 = 32ms
	private static Settings settings = null;
	private static Features features = null;
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

		// init and flush microphone
		Microphone micro = new Microphone(windowSize);
		micro.flushMicrophone();

		File audioFile = new File("online.wav");
		File featureFile = new File("online.mfcc");
		NearestNeighbor nn = new NearestNeighbor(codebooks);
		Runtime runtime = Runtime.getRuntime();
		while (true) {
			try {
				// read data from microphone
				double[] rawData = micro.readLive();
				micro.flushMicrophone();

				// write a wav file
				WavFile wavFile = WavFile.newWavFile(audioFile, 1, windowSize, 16, 16000);
				wavFile.writeFrames(rawData, rawData.length);
				wavFile.close();

				// extract features				
				Process process = runtime
						.exec("java -jar ./tdFeatureExtraction.jar " +
								"-i " + audioFile.getCanonicalPath() + 
								" -o " + featureFile.getCanonicalPath() + 
								" -l 42" +
								" -e 0");
				writeProcessOutput(process);

				// read input files (feature vectors)		
				features = new Features();
				readOneFile(featureFile, true);

				// get nearest neighbor between n feature vectors and the codebooks	
				int[] labels = nn.calc(features, settings.getNumber());
				System.out.println(nn.getWinner());

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

	static void writeProcessOutput(Process process) throws Exception {
		InputStreamReader tempReader = new InputStreamReader(
				new BufferedInputStream(process.getErrorStream()));
		BufferedReader reader = new BufferedReader(tempReader);
		while (true) {
			String line = reader.readLine();
			if (line == null)
				break;
			System.out.println(line);
		}
	}
}