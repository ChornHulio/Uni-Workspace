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
		return (1.0 - 1.0 / (1 + Math.exp((double) -time/100000.0))) * (accuracy-20);
	}
	
	public long getTime() {
		return this.time;
	}
	
	public double getAccuracy() {
		return this.accuracy;
	}
}
