package de.tdreher.core;

import java.io.File;
import java.util.LinkedList;

public class Settings {
	// files
	private LinkedList<File> inputFile = new LinkedList<File>();
	private File outputFile = null;
	private boolean append = false; // should the output be appended at the end of the given file or not
	
	private int size = 100; // size of codebook
	private int iterations = 15; // number of iterations
	private int coefficents = -1; // coefficents = size of one feature vector (read from input file)
		
	public LinkedList<File> getInputFiles() {
		return inputFile;
	}
	public void addInputFile(File inputFile) {
		this.inputFile.add(inputFile);
	}
	public File getOutputFile() {
		return outputFile;
	}
	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getIterations() {
		return iterations;
	}
	public void setIterations(int iterations) {
		this.iterations = iterations;		
	}
	public int getCoefficents() {
		return coefficents;
	}
	public void setCoefficents(int coefficents) throws Exception {
		if(this.coefficents > 0 && this.coefficents != coefficents) {
			throw new Exception("Different sizes of feature vectors in input file(s)");
		}
		this.coefficents = coefficents;
	}
	public void setAppendFlag() {
		append = true;
	}
	public boolean getAppendFlag() {
		return append;
	}
}
