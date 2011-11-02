package de.tdreher.searchstrategies;

import de.tdreher.searchstrategies.problems.ProblemSolver;
import de.tdreher.searchstrategies.problems.SokobanProblemA;
import de.tdreher.searchstrategies.strategies.BreadthFirstTreeSearch;

public class Main {

	public static void main(String[] args) {
		ProblemSolver ps = new ProblemSolver(new SokobanProblemA(), new BreadthFirstTreeSearch());
		System.out.println(ps.run());
	}

}
