package de.tdreher.searchstrategies.problems;

import de.tdreher.searchstrategies.heuristics.Heuristic;
import de.tdreher.searchstrategies.heuristics.NumberPuzzleHeuristicA;
import de.tdreher.searchstrategies.strategies.GreedyTreeSearch;
import de.tdreher.searchstrategies.strategies.Strategy;

public class ProblemSolver {
	
	Problem p = null;
	Strategy s = null;
	Heuristic h = null;
	
	/**
	 * Constructor of this class. Defines the problem and its strategy to solve
	 * @param p The problem
	 * @param s The Strategy to solve
	 */
	public ProblemSolver(Problem p, Strategy s) {
		this.p = p;
		this.s = s;
	}
	
	/**
	 * Constructor of this class. Defines the problem and its strategy to solve
	 * @param p The problem
	 * @param s The strategy to solve
	 * @param h The heuristic for the strategy
	 */
	public ProblemSolver(Problem p, Strategy s,	Heuristic h) {
		this.p = p;
		this.s = s;
		this.h = h;
	}

	/**
	 * Run the ProblemSolver and solve the problem
	 * @return The solution as string
	 */
	public String run() {
		s.setStartState(p.getStartState());
		s.setTargetStates(p.getTargetStates());
		s.setActions(p.getActions());
		s.setHeuristic(h); // even if its null
		return s.run();
	}
	
	/**
	 * Overloaded method of run(). Uses a limit for the strategy (e.g., the 
	 * depth-limited-search uses one. 
	 * @param limit
	 * @return The solution as string
	 */
	public String run(int limit) {
		s.setStartState(p.getStartState());
		s.setTargetStates(p.getTargetStates());
		s.setActions(p.getActions());
		s.setLimit(limit);
		s.setHeuristic(h); // even if its null
		return s.run();
	}

}
