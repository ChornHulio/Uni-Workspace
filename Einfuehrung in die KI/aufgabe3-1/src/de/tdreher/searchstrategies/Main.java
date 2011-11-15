package de.tdreher.searchstrategies;

import de.tdreher.searchstrategies.problems.NumberPuzzleProblemA;
import de.tdreher.searchstrategies.problems.ProblemSolver;
import de.tdreher.searchstrategies.strategies.IterativeDepthTreeSearch;

public class Main {

	public static void main(String[] args) {
		
		long ms = 0; // milliseconds for stopwatch
		ProblemSolver ps = null; // the solver
		
		// Breadth First - TreeSearch
		/*ps = new ProblemSolver(new NumberPuzzleProblemA(), new BreadthFirstTreeSearch());
		ms = System.currentTimeMillis();
		System.out.println("(A / Breadth First) " + ps.run());
		System.out.println("\t\tLead time: " + (System.currentTimeMillis() - ms));*/
		
		// Iterative Depth - TreeSearch
		ps = new ProblemSolver(new NumberPuzzleProblemA(), new IterativeDepthTreeSearch());
		ms = System.currentTimeMillis();
		System.out.println("(A / Iterative Depth) " + ps.run());
		System.out.println("\t\tLead time: " + (System.currentTimeMillis() - ms));
	}

}
