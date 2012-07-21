package de.tdreher.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Population {
	
	int size = 0;
	String folder = "";
	ArrayList<Individual> pop = new ArrayList<Individual>();
	
	public Population(int size, String foldername) {
		this.size = size;
		this.folder = foldername;				
		pop.clear();
		for(int i = 0; i < size; i++) {
			pop.add(new Individual(folder)); // individuals with random values
		}
	}

	public void adaptMutationRate(int mutationMin, int mutationMax,
			double learnOverall, double learnProp) {
		Random rand = new Random();
		double adaptForAll = learnProp * learnOverall * rand.nextGaussian();
		for(Individual individual : pop) {
			double currentRate = individual.getMutationRate();
			double adaptForThis = learnProp * learnOverall * rand.nextGaussian();
			double newRate = currentRate * Math.exp(adaptForAll + adaptForThis);
			individual.setMutationRate(newRate);			
		}
	}

	public void mutation() {
		for(int i = 0; i < size; i++) {
			Individual child = duplicate(pop.get(i));
			child.mutate();
			pop.add(child);
		}
	}

	private Individual duplicate(Individual individual) {
		Individual clone = new Individual(folder);
		clone.setSampleWidth(individual.getSampleWidth());
		clone.setSlidingRate(individual.getSlidingRate());
		clone.setWindow(individual.getWindow());
		clone.setCoefficents(individual.getCoefficents());
		clone.setEnergyLevel(individual.getEnergyLevel());
		clone.setCodebookSize(individual.getCodebookSize());
		clone.setNgIterations(individual.getNgIterations());
		clone.setMutationRate(individual.getMutationRate());
		return clone;
	}

	public void evaluation() {
		for(Individual individual : pop) {
			Fitness fitness = individual.process();
			individual.setFitness(fitness);
		}
	}

	public void selection() {
		tournament();
	}

	public ArrayList<Individual> getIndividiuals() {
		return pop;
	}
	
	/**
	 * fittest individuals survive (not very good algorithm)
	 */
	private void truncation() {
		Collections.sort(pop);
		while(pop.size() > this.size && this.size >= 1) {
			pop.remove(1);
		}
	}
	
	private void tournament() {
		int tournamentSize = 4;
		if(size % tournamentSize != 0) {
			System.err.println("Tournament fits not with the population size!");
			truncation();
		} else {
			Random rand = new Random();
			ArrayList<Individual> newpop = new ArrayList<Individual>();
			for(int i = 0; i < size; i++) {
				ArrayList<Individual> tournament = new ArrayList<Individual>();
				for(int j = 0; j < tournamentSize; j++) {
					tournament.add(pop.remove(rand.nextInt(pop.size())));
				}
				Collections.sort(tournament);
				for(int j = 0; j < tournamentSize; j++) {
					System.out.println(tournament.get(j).getFitness());
				}
				newpop.add(tournament.remove(0)); // add the winner
				pop.addAll(tournament); // push the loosers back
			}			
		}
	}

}
