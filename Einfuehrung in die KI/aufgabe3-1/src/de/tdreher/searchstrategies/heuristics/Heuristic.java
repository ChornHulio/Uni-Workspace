package de.tdreher.searchstrategies.heuristics;

import java.util.LinkedList;

import de.tdreher.searchstrategies.states.State;

public interface Heuristic {
	
	/**
	 * Calculate h(n). If h(n) == 0 a solution is found
	 * @param currentState The current state
	 * @param targetStates All possible target states
	 * @return The lowest h(n) to one of the target states
	 */
	public int calcHeuristic(State currentState, LinkedList<State> targetStates);

}
