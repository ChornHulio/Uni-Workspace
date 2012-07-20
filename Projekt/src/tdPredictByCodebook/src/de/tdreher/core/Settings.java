package de.tdreher.core;

import java.io.File;
import java.util.LinkedList;

public class Settings {
	// files
	private LinkedList<File> featureFile = new LinkedList<File>();
	private LinkedList<File> codebookFile = new LinkedList<File>();
	private File outputFile = null;
	private int coefficents = -1; // size of the feature vector
	private int number = 1; // number of feature vectors for one prediction
	private boolean append = false; // should the output be appended at the end of the given file or not
		
	public LinkedList<File> getFeatureFiles() {
		return featureFile;
	}
	public void addFeatureFile(File inputFile) {
		this.featureFile.add(inputFile);
	}
	public LinkedList<File> getCodebookFiles() {
		return codebookFile;
	}
	public void addCodebookFile(File inputFile) {
		this.codebookFile.add(inputFile);
	}
	public File getOutputFile() {
		return outputFile;
	}	
	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getCoefficents() {
		return coefficents;
	}
	public void setCoefficents(int coefficents) throws Exception {
		if(this.coefficents <= 0) // first time: set coefficents
			this.coefficents = coefficents;
		else if(this.coefficents == coefficents) // number of coefficents should be the same, than before
			return;
		else {
			System.err.println("old: " + this.coefficents + " | new: " + coefficents);
			throw new Exception("False number of coefficents in input file");
		}
	}
	public void setAppendFlag() {
		append = true;
	}
	public boolean getAppendFlag() {
		return append;
	}
}
