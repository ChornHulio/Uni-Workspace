package de.tdreher.core;

import java.io.File;
import java.util.LinkedList;

public class Settings {
	// files
	private LinkedList<File> originFiles = new LinkedList<File>();
	private LinkedList<File> predictionFiles = new LinkedList<File>();
	private File outputFile = null;
	private boolean append = false; // should the output be appended at the end of the given file or not
		
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
	public void setAppendFlag() {
		append = true;
	}
	public boolean getAppendFlag() {
		return append;
	}
}
