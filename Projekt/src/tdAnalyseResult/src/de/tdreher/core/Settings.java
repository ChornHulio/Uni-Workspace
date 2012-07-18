package de.tdreher.core;

import java.io.File;
import java.util.LinkedList;

public class Settings {
	private LinkedList<File> originFiles = new LinkedList<File>();
	private LinkedList<File> predictionFiles = new LinkedList<File>();
	private File outputFile = null;
	private File accuracyFile = null;
		
	public LinkedList<File> getOriginFiles() {
		return originFiles;
	}
	public void addOriginFile(File file) {
		this.originFiles.add(file);
	}
	public LinkedList<File> getPredictionFiles() {
		return predictionFiles;
	}
	public void addPredictionFile(File file) {
		this.predictionFiles.add(file);
	}
	public File getOutputFile() {
		return outputFile;
	}	
	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}
	public File getAccuracyFile() {
		return accuracyFile;
	}
	public void setAccuracyFile(File file) {
		this.accuracyFile  = file;
	}
}
