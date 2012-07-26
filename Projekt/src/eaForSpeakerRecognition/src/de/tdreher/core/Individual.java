package de.tdreher.core;

import java.io.BufferedInputStream;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Individual implements Comparable<Individual> {

	// parameters
	private int sampleWidth = 9; // 2 ^ n
	private int sampleWidthMin = 7;
	private int sampleWidthMax = 16;
	private int slidingRate = 2;
	private int slidingRateMin = 1;
	private int slidingRateMax = 10;
	private String window = "hamming";
	private int coefficents = 20;
	private int coefficentsMin = 1;
	private int coefficentsMax = 38; // smaller than no. of mel-filters in MFCC
	private int energyLevel = 90;
	private int energyLevelMin = 0;
	private int energyLevelMax = 99;
	private int codebookSize = 100;
	private int codebookSizeMin = 1;
	private int codebookSizeMax = 1000;
	private int ngIterations = 15;
	private int ngIterationsMin = 1;
	private int ngIterationsMax = 200;

	// mutation rate
	private double mutationRate = 0.1;

	// fitness
	Fitness fitness = null;

	// foldername
	String folder = "ea";

	public Individual(String folder) {
		// set foldername
		this.folder = folder;

		// init with random values
		Random rand = new Random();
		sampleWidth = rand.nextInt(sampleWidthMax - sampleWidthMin + 1)
				+ sampleWidthMin; // 2 ^ (5 .. 20) -> 128 .. 65536
		slidingRate = rand.nextInt(slidingRateMax - slidingRateMin + 1)
				+ slidingRateMin; // 1 .. 10
		if (rand.nextDouble() > 0.5) {
			window = "hamming";
		} else {
			window = "hann";
		}
		coefficents = rand.nextInt(coefficentsMax - coefficentsMin + 1)
				+ coefficentsMin; // 1 .. 100
		energyLevel = rand.nextInt(energyLevelMax - energyLevelMin + 1)
				+ energyLevelMin; // 0 .. 99
		codebookSize = rand.nextInt(codebookSizeMax - codebookSizeMin + 1)
				+ codebookSizeMin; // 1 .. 1000
		ngIterations = rand.nextInt(ngIterationsMax - ngIterationsMin + 1)
				+ ngIterationsMin; // 1 .. 200
	}

	public void mutate() {
		Random rand = new Random();
		setSampleWidth((int) (this.sampleWidth + mutationRate
				* rand.nextGaussian()));
		setSlidingRate((int) (this.slidingRate + mutationRate
				* rand.nextGaussian()));
		if (mutationRate * rand.nextGaussian() > 0.0) {
			setWindow("hamming");
		} else {
			setWindow("hann");
		}
		setCoefficents((int) (this.coefficents + mutationRate
				* rand.nextGaussian()));
		setEnergyLevel((int) (this.energyLevel + mutationRate
				* rand.nextGaussian()));
		setCodebookSize((int) (this.codebookSize + mutationRate
				* rand.nextGaussian()));
		setNgIterations((int) (this.ngIterations + mutationRate
				* rand.nextGaussian()));
	}

	public int getSampleWidth() {
		return sampleWidth;
	}

	public void setSampleWidth(int sampleWidth) {
		if (sampleWidth > sampleWidthMax)
			this.sampleWidth = sampleWidthMax;
		else if (sampleWidth < sampleWidthMin)
			this.sampleWidth = sampleWidthMin;
		else
			this.sampleWidth = sampleWidth;
	}

	public int getSlidingRate() {
		return slidingRate;
	}

	public void setSlidingRate(int slidingRate) {
		if (slidingRate > slidingRateMax)
			this.slidingRate = slidingRateMax;
		else if (slidingRate < slidingRateMin)
			this.slidingRate = slidingRateMin;
		else
			this.slidingRate = slidingRate;
	}

	public String getWindow() {
		return window;
	}

	public void setWindow(String window) {
		if (window.equalsIgnoreCase("hamming")
				|| window.equalsIgnoreCase("hann"))
			this.window = window;
		else
			this.window = "hamming";
	}

	public int getCoefficents() {
		return coefficents;
	}

	public void setCoefficents(int coefficents) {
		if (coefficents > coefficentsMax)
			this.coefficents = coefficentsMax;
		else if (coefficents < coefficentsMin)
			this.coefficents = coefficentsMin;
		else
			this.coefficents = coefficents;
	}

	public int getEnergyLevel() {
		return energyLevel;
	}

	public void setEnergyLevel(int energyLevel) {
		if (energyLevel > energyLevelMax)
			this.energyLevel = energyLevelMax;
		if (energyLevel < energyLevelMin)
			this.energyLevel = energyLevelMin;
		else
			this.energyLevel = energyLevel;
	}

	public int getCodebookSize() {
		return codebookSize;
	}

	public void setCodebookSize(int codebookSize) {
		if (codebookSize > codebookSizeMax)
			this.codebookSize = codebookSizeMax;
		else if (codebookSize < codebookSizeMin)
			this.codebookSize = codebookSizeMin;
		else
			this.codebookSize = codebookSize;
	}

	public int getNgIterations() {
		return ngIterations;
	}

	public void setNgIterations(int ngIterations) {
		if (ngIterations > ngIterationsMax)
			this.ngIterations = ngIterationsMax;
		else if (ngIterations < ngIterationsMin)
			this.ngIterations = ngIterationsMin;
		else
			this.ngIterations = ngIterations;
	}

	public void setMutationRate(double mutationRate) {
		if (mutationRate == 0.0)
			this.mutationRate = 0.1;
		else
			this.mutationRate = mutationRate;
	}

	public double getMutationRate() {
		return mutationRate;
	}

	public void setFitness(Fitness fitness) {
		this.fitness = fitness;
	}

	public double getFitness() {
		return this.fitness.getFitness();
	}

	public double getFitnessTime() {
		return this.fitness.getTime();
	}

	public double getFitnessAccuracy() {
		return this.fitness.getAccuracy();
	}

	public Fitness process() {
		if (fitness == null) {

			System.out.println("sw: " + sampleWidth + " |sr: " + slidingRate
					+ " |co: " + coefficents + " |e: " + energyLevel + " |cs: "
					+ codebookSize + " |i: " + ngIterations);

			try {
				long startTime = System.currentTimeMillis();
				Runtime runtime = Runtime.getRuntime();
				Process process = null;

				// delete current files
				process = runtime.exec("rm -rf ../test/" + folder);
				writeProcessOutput(process);
				process.getInputStream().close(); 
				process.getOutputStream().close(); 
				process.getErrorStream().close();

				process = runtime.exec("mkdir ../test/" + folder);
				writeProcessOutput(process);
				process.getInputStream().close(); 
				process.getOutputStream().close(); 
				process.getErrorStream().close();

				// feature extraction
				for (int i = 1; i < 11; i++) { // for all speaker
					String processStr = "java -jar tdFeatureExtraction.jar";
					processStr += " -i ../speaker/" + i + "/1.wav";
					processStr += " -i ../speaker/" + i + "/2.wav";
					processStr += " -l " + i;
					processStr += " -o ../test/" + folder + "/mfcc.traindata";
					processStr += " -sw " + (int) Math.pow(2, sampleWidth);
					processStr += " -sr " + slidingRate;
					processStr += " -c " + coefficents;
					processStr += " -w " + window;
					processStr += " -e " + energyLevel;
					processStr += " -a --mfcc";
					process = runtime.exec(processStr);
					writeProcessOutput(process);
					process.getInputStream().close(); 
					process.getOutputStream().close(); 
					process.getErrorStream().close();
				}

				// test features
				for (int i = 1; i < 11; i++) { // for all speaker
					String processStr = "java -jar tdFeatureExtraction.jar";
					processStr += " -i ../speaker/" + i + "/3.wav";
					processStr += " -l " + i;
					processStr += " -o ../test/" + folder + "/mfcc.testdata";
					processStr += " -sw " + (int) Math.pow(2, sampleWidth);
					processStr += " -sr " + slidingRate;
					processStr += " -c " + coefficents;
					processStr += " -w " + window;
					processStr += " -e " + energyLevel;
					processStr += " -a --mfcc";
					process = runtime.exec(processStr);
					writeProcessOutput(process);
					process.getInputStream().close(); 
					process.getOutputStream().close(); 
					process.getErrorStream().close();
				}

				// create codebook
				String processStr = "java -jar tdCreateCodebook.jar";
				processStr += " -i ../test/" + folder + "/mfcc.traindata";
				processStr += " -o ../test/" + folder + "/mfcc.ng";
				processStr += " -s " + codebookSize;
				processStr += " -it " + ngIterations;
				processStr += " -a";
				process = runtime.exec(processStr);
				writeProcessOutput(process);
				process.getInputStream().close(); 
				process.getOutputStream().close(); 
				process.getErrorStream().close();

				// prediction with testdata
				processStr = "java -jar tdPredictByCodebook.jar";
				processStr += " -ff ../test/" + folder + "/mfcc.testdata";
				processStr += " -cf ../test/" + folder + "/mfcc.ng";
				processStr += " -o ../test/" + folder + "/mfcc.res";
				process = runtime.exec(processStr);
				writeProcessOutput(process);
				process.getInputStream().close(); 
				process.getOutputStream().close(); 
				process.getErrorStream().close();

				// analyse
				processStr = "java -jar tdAnalyseResult.jar";
				processStr += " -i ../test/" + folder + "/mfcc.testdata";
				processStr += " -p ../test/" + folder + "/mfcc.res";
				processStr += " -o ../test/" + folder + "/mfcc.analysis";
				processStr += " -a ../test/" + folder + "/mfcc.accuracy";
				process = runtime.exec(processStr);
				writeProcessOutput(process);
				process.getInputStream().close(); 
				process.getOutputStream().close(); 
				process.getErrorStream().close();

				// read accuracy
				double accuracy = readAccuracy("../test/" + folder
						+ "/mfcc.accuracy");

				// stop time
				long endTime = System.currentTimeMillis();

				// create fitness and return it
				return new Fitness(endTime - startTime, accuracy);

			} catch (Exception e) {
				System.err.println("error while processing indivual:");
				e.printStackTrace();
				return new Fitness(Long.MAX_VALUE, Double.MIN_VALUE);
			}
		}
		return fitness;
	}

	@Override
	public int compareTo(Individual other) {
		double f1 = this.getFitness();
		double f2 = other.getFitness();
		if (f1 > f2) {
			return 1;
		}
		if (f1 < f2) {
			return -1;
		}
		return 0;
	}

	private void writeProcessOutput(Process process) throws Exception {		
		InputStreamReader tempReader = new InputStreamReader(
				new BufferedInputStream(process.getErrorStream()));
		BufferedReader reader = new BufferedReader(tempReader);
		while (true) {
			String line = reader.readLine();
			if (line == null)
				break;
			System.out.println(line);
		}
		reader.close();
	}

	private double readAccuracy(String filename) {
		FileReader fr = null;
		try {
			fr = new FileReader(filename);
			BufferedReader in = new BufferedReader(fr);
			String line = in.readLine(); // read first line
			if (line != null) {
				line = line.trim();
				line = line.replace(',', '.');
				return Double.parseDouble(line);
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
		return 0; // error
	}
}
