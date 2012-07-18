package de.tdreher.core;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Logger {
	
	static String filename = "ea.csv";

	public static void clearLogFile() {
		Writer fw = null;
		try {
			fw = new FileWriter(filename, false);
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

	public static void log(int generation, Population pop) {
		ArrayList<Individual> individuals = pop.getIndividiuals();
		DecimalFormat douF = new DecimalFormat("##.##");
		
		Writer fw = null;
		try {
			fw = new FileWriter(filename, true);
			for(Individual individual : individuals) {
				String str = "" + generation + ";";
				str += individual.getSampleWidth() + ";";
				str += individual.getSlidingRate() + ";";
				str += individual.getWindow() + ";";
				str += individual.getCoefficents() + ";";
				str += individual.getEnergyLevel() + ";";
				str += individual.getCodebookSize() + ";";
				str += individual.getNgIterations() + ";";
				str += douF.format(individual.getMutationRate()) + ";";				
				str += individual.getFitnessTime() + ";";
				str += douF.format(individual.getFitnessAccuracy()) + ";";
				str += douF.format(individual.getFitness()) + ";";
				str += System.getProperty("line.separator");
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
