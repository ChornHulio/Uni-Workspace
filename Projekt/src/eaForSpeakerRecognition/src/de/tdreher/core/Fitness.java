package de.tdreher.core;

public class Fitness {
	
	private long time = 0; // process time in ms
	private double accuracy = 0.0; // accuracy of speaker recognition
	
	public Fitness(long time, double accuracy) {
		this.time = time;
		this.accuracy = accuracy;
	}

	public void setFitness(long time, double accuracy) {
		this.time = time;
		this.accuracy = accuracy;
	}

	public double getFitness() {
		return 0.2 * -1 * time + 0.8 * accuracy; // time is negativ, but not so important than accuracy
	}
	
	public long getTime() {
		return this.time;
	}
	
	public double getAccuracy() {
		return this.accuracy;
	}
}
